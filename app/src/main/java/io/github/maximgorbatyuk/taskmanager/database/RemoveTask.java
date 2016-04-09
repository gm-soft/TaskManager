package io.github.maximgorbatyuk.taskmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import io.github.maximgorbatyuk.taskmanager.help.Task;

/**
 * Created by Maxim on 09.04.2016.
 */
public class RemoveTask extends AsyncTask<Task, Void, Boolean> {

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

    public RemoveTask(Context context, ExecuteResult delegate){
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
        PRIORITY_COLUMN     = helper.getPrioritytColumn();
    }

    @Override
    protected Boolean doInBackground(Task... params) {
        int count = -1;
        Task task = params[0];
        String args = "_id = " + task.getId();
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            count = db.delete(TABLE_NAME, args, null);
        } catch (Exception ex){
            Log.d(LOG_TAG, "Error while removing task: " + ex.getMessage());
            count = 0;
        }
        finally {
            db.endTransaction();
            helper.close();
        }
        if (count > 0)
            return true;
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        delegate.processFinish(aBoolean);
    }
}
