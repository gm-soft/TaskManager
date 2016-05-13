package io.github.maximgorbatyuk.taskmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import io.github.maximgorbatyuk.taskmanager.Essential.Project;
import io.github.maximgorbatyuk.taskmanager.helpers.Constants;
import io.github.maximgorbatyuk.taskmanager.helpers.DateHelper;

/**
 * Created by Maxim on 13.05.2016.
 */
class UpdateProject extends AsyncTask<Project, Void, Boolean> {

    private IExecuteResult delegate;
    private DBHelper helper;
    private DateHelper dateHelper = new DateHelper();
    //-------------------

    public UpdateProject(DBHelper helper, IExecuteResult delegate){
        this.delegate = delegate;
        this.helper = helper;
    }

    @Override
    protected Boolean doInBackground(Project... params) {
        long count = -1;
        Project project = params[0];
        String id = "" + project.getId();
        //
        ContentValues values = Database.getContentValues(project);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            db.beginTransaction();
            count = db.update(Constants.TABLE_NAME, values, "id=?", new String[] {id });
            db.setTransactionSuccessful();

        } catch (Exception ex){
            Log.d(Constants.LOG_TAG, "Error while project insert: " + ex.getMessage());
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
        delegate.onExecute(aBoolean);
    }
}
