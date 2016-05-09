package io.github.maximgorbatyuk.taskmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import io.github.maximgorbatyuk.taskmanager.help.Constants;
import io.github.maximgorbatyuk.taskmanager.help.DateHelper;
import io.github.maximgorbatyuk.taskmanager.help.Project;

/**
 * Created by Maxim on 09.04.2016.
 */
public class UpdateProject extends AsyncTask<Project, Void, Boolean> {

    private ExecuteResult delegate;
    private DBHelper helper;
    private DateHelper dateHelper = new DateHelper();
    //-------------------

    public UpdateProject(Context context, ExecuteResult delegate){
        this.delegate = delegate;
        this.helper = new DBHelper(context);
    }

    @Override
    protected Boolean doInBackground(Project... params) {
        long count = -1;
        Project project = params[0];
        String id = "" + project.getId();
        //
        ContentValues values = new ContentValues();
        values.put(Constants.TITLE_COLUMN,        project.getTitle());
        values.put(Constants.BODY_COLUMN,         project.getBody());
        values.put(Constants.IS_DONE_COLUMN,      project.getIsDone());
        values.put(Constants.DEADLINE_COLUMN,     dateHelper.dateToString( project.getDeadline() ));
        values.put(Constants.CREATED_AT_COLUMN,   dateHelper.dateToString(project.getCreatedAt() ));
        values.put(Constants.COST_COLUMN,         project.getCost());
        values.put(Constants.SPENT_TIME_COLUMN,   project.getMilliseconds());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try{
            count = db.update(Constants.TABLE_NAME, values, "id=?", new String[] {id });
            db.setTransactionSuccessful();

        } catch (Exception ex){
            Log.d(Constants.LOG_TAG, "Error while project insert: " + ex.getMessage());
            count = 0;
        }
        finally {
            //db.close();
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
}
