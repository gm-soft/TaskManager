package io.github.maximgorbatyuk.taskmanager.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import io.github.maximgorbatyuk.taskmanager.Essential.Project;
import io.github.maximgorbatyuk.taskmanager.helpers.Constants;

/**
 * Created by Maxim on 13.05.2016.
 */
class CreateProject extends AsyncTask<Project, Void, Boolean> {

    private DBHelper helper;
    private IExecuteResult delegate;

    CreateProject(DBHelper helper, IExecuteResult delegate){
        this.helper = helper;
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Project... params) {
        long count = 0;
        Project project = params[0];
        //
        ContentValues values = Database.getContentValues(project);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            db.beginTransaction();
            count = db.insert(Constants.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
            //db.endTransaction();
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
