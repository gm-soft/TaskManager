package io.github.maximgorbatyuk.taskmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import io.github.maximgorbatyuk.taskmanager.help.Constants;

/**
 * Created by Maxim on 09.04.2016.
 */
public class DestroyProject extends AsyncTask<Integer, Void, Boolean> {


    private ExecuteResult delegate;
    private DBHelper helper;
    //-------------------


    public DestroyProject(Context context, ExecuteResult delegate){
        this.delegate = delegate;
        this.helper = new DBHelper(context);
    }


    @Override
    protected Boolean doInBackground(Integer... params) {
        int count = -1;

        String args = "id = " + params[0];
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
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
        delegate.processFinish(aBoolean);
    }
}
