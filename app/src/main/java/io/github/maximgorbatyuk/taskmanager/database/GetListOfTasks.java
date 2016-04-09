package io.github.maximgorbatyuk.taskmanager.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import io.github.maximgorbatyuk.taskmanager.help.Task;

/**
 * Created by Maxim on 09.04.2016.
 */
public class GetListOfTasks extends AsyncTask<String, Void, List<Task>> {

    private String LOG_TAG = "Task Manager";

    private Context context;
    private GetListOfTasksResult delegate;
    private DBHelper helper;
    //-------------------
    private static String TABLE_NAME;
    private static String TITLE_COLUMN ;
    private static String BODY_COLUMN ;
    private static String IS_DONE_COLUMN ;
    private static String DEADLINE_COLUMN;
    private static String CREATED_AT_COLUMN ;
    private static String PRIORITY_COLUMN ;


    public GetListOfTasks(Context context, GetListOfTasksResult delegate){
        this.context = context;
        this.delegate = delegate;
        helper = new DBHelper(context);
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
        initializeColumns();
        List<Task> list = new ArrayList<>(0);

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, DEADLINE_COLUMN);
        db.beginTransaction();
        try{
            if (cursor.moveToFirst()){
                do {
                    Task task = new Task();
                    task.setId(         cursor.getInt(cursor.getColumnIndex("_id")));
                    task.setTitle(      cursor.getString(cursor.getColumnIndex(TITLE_COLUMN)));
                    task.setBody(       cursor.getString(cursor.getColumnIndex(BODY_COLUMN)));
                    task.setIsDone(     Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(IS_DONE_COLUMN))));
                    task.setDeadline(   Date.valueOf(cursor.getString(cursor.getColumnIndex(DEADLINE_COLUMN))));
                    task.setCreatedAt(  Date.valueOf(cursor.getString(cursor.getColumnIndex(CREATED_AT_COLUMN))));
                    task.setPriority(   Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRIORITY_COLUMN))));
                    list.add(task);
                } while (cursor.moveToNext());
            db.setTransactionSuccessful();
            }
        } catch (Exception ex){
            Log.d(LOG_TAG, ex.getMessage());
        }
        finally {
            cursor.close();
            db.endTransaction();
            db.close();
        }

        return list;
    }

    @Override
    protected void onPostExecute(List<Task> tasks) {
        super.onPostExecute(tasks);
        delegate.processFinish(tasks);
    }
}
