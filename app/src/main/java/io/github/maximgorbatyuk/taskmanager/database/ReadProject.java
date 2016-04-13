package io.github.maximgorbatyuk.taskmanager.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.github.maximgorbatyuk.taskmanager.help.Constants;
import io.github.maximgorbatyuk.taskmanager.help.DateHelper;
import io.github.maximgorbatyuk.taskmanager.help.Project;

/**
 * Created by Maxim on 09.04.2016.
 */
public class ReadProject extends AsyncTask<String, Void, List<Project>> {

    private ReadProjectResult delegate;
    private DBHelper helper;
    //-------------------


    public ReadProject(Context context, ReadProjectResult delegate){
        this.delegate = delegate;
        this.helper = new DBHelper(context);
    }

    @Override
    protected List<Project> doInBackground(String... params) {
        List<Project> list = new ArrayList<>(0);
        String query = null;
        if (params[0] == "one")
            query = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE id=" + params[1] + " LIMIT 1";
        if (params[0] == "done")
            query = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.IS_DONE_COLUMN + "=true";
        if (params[0] == "done")
            query = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.IS_DONE_COLUMN + "=false";

        Log.d(Constants.LOG_TAG, "Query in ReadProject: " + query);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        db.beginTransaction();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()){
                do {
                    Project project = new Project();
                    project.setId(
                            cursor.getInt(cursor.getColumnIndex("id")));
                    project.setTitle(
                            cursor.getString(cursor.getColumnIndex(Constants.TITLE_COLUMN)));
                    project.setBody(
                            cursor.getString(cursor.getColumnIndex(Constants.BODY_COLUMN)));
                    String idDoneStr = cursor.getString(cursor.getColumnIndex(Constants.IS_DONE_COLUMN));
                    project.setIsDone(
                            Integer.parseInt(idDoneStr) == 1   );
                    project.setDeadline(
                            new DateHelper().parseDate(cursor.getString(cursor.getColumnIndex(Constants.DEADLINE_COLUMN))));

                    String date = cursor.getString(cursor.getColumnIndex(Constants.CREATED_AT_COLUMN));
                    project.setCreatedAt(
                            new DateHelper().parseDate(date));
                    project.setCost(
                            Double.parseDouble(cursor.getString(cursor.getColumnIndex(Constants.COST_COLUMN))));
                    project.setMilliseconds(
                            Long.parseLong(cursor.getString(cursor.getColumnIndex(Constants.SPENT_TIME_COLUMN))));

                    list.add(project);
                } while (cursor.moveToNext());
                db.setTransactionSuccessful();
            }
        } catch (Exception ex){
            Log.d(Constants.LOG_TAG, "Error while getting task(s): " + ex.getMessage());
        }
        finally {
            if (cursor != null)
                cursor.close();
            db.endTransaction();
            db.close();
        }

        return list;
    }

    @Override
    protected void onPostExecute(List<Project> project) {
        super.onPostExecute(project);
        delegate.processFinish(project);
    }


}
