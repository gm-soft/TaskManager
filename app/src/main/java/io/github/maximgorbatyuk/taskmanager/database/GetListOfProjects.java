package io.github.maximgorbatyuk.taskmanager.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.github.maximgorbatyuk.taskmanager.Essential.Project;
import io.github.maximgorbatyuk.taskmanager.helpers.Constants;
import io.github.maximgorbatyuk.taskmanager.helpers.DateHelper;

/**
 * Created by Maxim on 13.05.2016.
 */
class GetListOfProjects extends AsyncTask<String, Void, List<Project>> {

    private DBHelper helper;
    private IDatabaseExecute delegate;
    private DateHelper dateHelper;

    GetListOfProjects(DBHelper helper, IDatabaseExecute delegate){
        this.helper = helper;
        this.delegate = delegate;
        dateHelper = new DateHelper();
    }

    @Override
    protected List<Project> doInBackground(String... params) {
        List<Project> list = new ArrayList<>(0);

        SQLiteDatabase db = helper.getReadableDatabase();


        Cursor cursor = null;
        try{
            db.beginTransaction();
            cursor = db.query(Constants.TABLE_NAME, null, null, null, null, null, Constants.DEADLINE_COLUMN);
            if (cursor.moveToFirst()){
                do {
                    Project project = new Project();
                    project.setId(         cursor.getInt(cursor.getColumnIndex("id")));
                    project.setTitle(      cursor.getString(cursor.getColumnIndex(Constants.TITLE_COLUMN)));
                    project.setBody(       cursor.getString(cursor.getColumnIndex(Constants.BODY_COLUMN)));
                    String idDoneStr = cursor.getString(cursor.getColumnIndex(Constants.IS_DONE_COLUMN));
                    project.setIsDone(
                            Integer.parseInt(idDoneStr) == 1   );
                    project.setDeadline(   dateHelper.parseDate(cursor.getString(cursor.getColumnIndex(Constants.DEADLINE_COLUMN))));
                    project.setCreatedAt(  dateHelper.parseDate(cursor.getString(cursor.getColumnIndex(Constants.CREATED_AT_COLUMN))));
                    project.setCost(   Double.parseDouble(cursor.getString(cursor.getColumnIndex(Constants.COST_COLUMN))));
                    project.setMilliseconds( Long.parseLong(cursor.getString(cursor.getColumnIndex(Constants.SPENT_TIME_COLUMN))) );
                    list.add(project);
                } while (cursor.moveToNext());
                db.setTransactionSuccessful();
            }
        } catch (Exception ex){
            Log.d(Constants.LOG_TAG, ex.getMessage());
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
    protected void onPostExecute(List<Project> projects) {
        super.onPostExecute(projects);
        delegate.onUpdatedSuccess(projects);
    }
}
