package io.github.maximgorbatyuk.taskmanager.database;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import io.github.maximgorbatyuk.taskmanager.Essential.Project;
import io.github.maximgorbatyuk.taskmanager.helpers.Constants;

/**
 * Created by Maxim on 13.05.2016.
 */
 class DestroyProject extends AsyncTask<Integer, Void, Boolean> {

    private DBHelper helper;
    private IExecuteResult delegate;

    DestroyProject(DBHelper helper, IExecuteResult delegate){
        this.helper = helper;
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        int count = -1;
        String args = "id = " + params[0];
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            db.beginTransaction();
            count = db.delete(Constants.TABLE_NAME, args, null);
            db.setTransactionSuccessful();
        } catch (Exception ex){
            Log.d(Constants.LOG_TAG, "Error while removing project: " + ex.getMessage());
            count = 0;
        }
        finally {
            db.endTransaction();
            db.close();
            helper.close();
        }
        return  count > 0;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        delegate.onExecute(aBoolean);
    }
}
