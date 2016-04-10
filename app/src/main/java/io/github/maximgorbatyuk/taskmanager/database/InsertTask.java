package io.github.maximgorbatyuk.taskmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.maximgorbatyuk.taskmanager.help.Task;

/**
 * Created by Maxim on 09.04.2016.
 */
public class InsertTask extends AsyncTask<Task, Void, Boolean> {

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

    public InsertTask(Context context, ExecuteResult delegate){
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
        long count = 0;
        Task task = params[0];
        initializeColumns();
        //
        ContentValues values = new ContentValues();
        values.put(TITLE_COLUMN,        task.getTitle());
        values.put(BODY_COLUMN,         task.getBody());
        values.put(IS_DONE_COLUMN,      task.getIsDone());
        values.put(DEADLINE_COLUMN,     dateToString(task.getDeadline()));
        values.put(CREATED_AT_COLUMN,   dateToString(task.getCreatedAt()));
        values.put(PRIORITY_COLUMN,     task.getPriority());
        SQLiteDatabase db = helper.getWritableDatabase();

        try{
            db.beginTransaction();

            count = db.insert(TABLE_NAME, null, values);

            db.setTransactionSuccessful();
            //db.endTransaction();
        } catch (Exception ex){
            Log.d(LOG_TAG, "Error while task insert: " + ex.getMessage());
            count = 0;
        }
        finally {
            db.endTransaction();
            db.close();
            helper.close();

        }
        return count > 0;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        delegate.processFinish(aBoolean);
    }

    @Nullable
    private String dateToString(Date date){
        try {
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
            return format.format(date);

        } catch (Exception ex){
            return null;
        }
    }
}
