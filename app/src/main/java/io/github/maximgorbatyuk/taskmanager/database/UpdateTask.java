package io.github.maximgorbatyuk.taskmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import io.github.maximgorbatyuk.taskmanager.help.Task;

/**
 * Created by Maxim on 09.04.2016.
 */
public class UpdateTask extends AsyncTask<Task, Void, Boolean> {

    private String LOG_TAG = "Task Manager";

    private ExecuteResult delegate;
    private DBHelper helper;
    //-------------------
    private static String TABLE_NAME;
    private static String TITLE_COLUMN ;
    private static String BODY_COLUMN ;
    private static String IS_DONE_COLUMN ;
    private static String DEADLINE_COLUMN;
    private static String CREATED_AT_COLUMN ;
    private static String PRIORITY_COLUMN ;

    public UpdateTask(Context context, ExecuteResult delegate){
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
    protected Boolean doInBackground(Task... params) {
        long count = -1;
        Task task = params[0];
        String id = "" + task.getId();
        initializeColumns();
        //
        ContentValues values = new ContentValues();
        values.put(TITLE_COLUMN,        task.getTitle());
        values.put(BODY_COLUMN,         task.getBody());
        values.put(IS_DONE_COLUMN,      task.getIsDone());
        values.put(DEADLINE_COLUMN,     task.getDeadline().toString());
        values.put(CREATED_AT_COLUMN,   task.getCreatedAt().toString());
        values.put(PRIORITY_COLUMN,     task.getPriority());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try{
            count = db.update(TABLE_NAME, values, "id=?", new String[] {id });
            db.setTransactionSuccessful();

        } catch (Exception ex){
            Log.d(LOG_TAG, "Error while task insert: " + ex.getMessage());
            count = 0;
        }
        finally {
            //db.close();
            db.endTransaction();
            helper.close();
        }
        return count > 0;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        delegate.processFinish(aBoolean);
    }
}
