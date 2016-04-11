package io.github.maximgorbatyuk.taskmanager.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.github.maximgorbatyuk.taskmanager.help.DateHelper;
import io.github.maximgorbatyuk.taskmanager.help.Task;

/**
 * Created by Maxim on 09.04.2016.
 */
public class GetTask extends AsyncTask<String, Void, List<Task>> {
    private String LOG_TAG = "Task Manager";

    private GetTaskResult delegate;
    private DBHelper helper;
    //-------------------
    private static String TABLE_NAME;
    private static String TITLE_COLUMN ;
    private static String BODY_COLUMN ;
    private static String IS_DONE_COLUMN ;
    private static String DEADLINE_COLUMN;
    private static String CREATED_AT_COLUMN ;
    private static String PRIORITY_COLUMN ;

    public GetTask(Context context, GetTaskResult delegate){
        this.delegate = delegate;
        this.helper = new DBHelper(context);
    }

    private void initializeColumns(){
        TABLE_NAME          = helper.getTableName();
        TITLE_COLUMN        = helper.getTitleColumn();
        BODY_COLUMN         = helper.getBodyColumn();
        IS_DONE_COLUMN      = helper.getIsDoneColumn();
        DEADLINE_COLUMN     = helper.getDeadlineColumn();
        CREATED_AT_COLUMN   = helper.getCreatedAtColumn() ;
        PRIORITY_COLUMN    = helper.getPrioritytColumn();
    }

    @Override
    protected List<Task> doInBackground(String... params) {
        List<Task> list = new ArrayList<>(0);
        initializeColumns();
        String query = null;
        if (params[0] == "one")
            query = "SELECT * FROM " + TABLE_NAME + " WHERE id=" + params[1] + " LIMIT 1";
        if (params[0] == "done")
            query = "SELECT * FROM " + TABLE_NAME + " WHERE " + IS_DONE_COLUMN + "=true";
        if (params[0] == "done")
            query = "SELECT * FROM " + TABLE_NAME + " WHERE " + IS_DONE_COLUMN + "=false";

        Log.d(LOG_TAG, "Query in GetTask: " + query);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        db.beginTransaction();
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()){
                do {
                    Task task = new Task();
                    task.setId(         cursor.getInt(cursor.getColumnIndex("id")));
                    task.setTitle(      cursor.getString(cursor.getColumnIndex(TITLE_COLUMN)));
                    task.setBody(       cursor.getString(cursor.getColumnIndex(BODY_COLUMN)));
                    String idDoneStr = cursor.getString(cursor.getColumnIndex(IS_DONE_COLUMN));
                    task.setIsDone(  Integer.parseInt(idDoneStr) == 1   );
                    task.setDeadline(   new DateHelper().parseDate(cursor.getString(cursor.getColumnIndex(DEADLINE_COLUMN))));

                    String date = cursor.getString(cursor.getColumnIndex(CREATED_AT_COLUMN));
                    task.setCreatedAt(  new DateHelper().parseDate(date));
                    task.setPriority(   Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRIORITY_COLUMN))));
                    list.add(task);
                } while (cursor.moveToNext());
                db.setTransactionSuccessful();
            }
        } catch (Exception ex){
            Log.d(LOG_TAG, "Error while getting task(s): " + ex.getMessage());
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
    protected void onPostExecute(List<Task> task) {
        super.onPostExecute(task);
        delegate.processFinish(task);
    }


}
