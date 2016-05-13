package io.github.maximgorbatyuk.taskmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import io.github.maximgorbatyuk.taskmanager.helpers.Constants;

/**
 * Created by Maxim on 09.04.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DROP_TABLE          = "DROP TABLE IF EXISTS " + Constants.TABLE_NAME;
    private static final String CREATE_TABLE =
            "CREATE TABLE " + Constants.TABLE_NAME +
                    "( id INTEGER PRIMARY KEY, " +
                    Constants.TITLE_COLUMN        + " TEXT, " +
                    Constants.BODY_COLUMN         + " TEXT, " +
                    Constants.IS_DONE_COLUMN      + " BOOLEAN, " +
                    Constants.COST_COLUMN         + " REAL, " +
                    Constants.DEADLINE_COLUMN     + " TEXT, " +
                    Constants.SPENT_TIME_COLUMN   + " TEXT, " +
                    Constants.CREATED_AT_COLUMN   + " TEXT )";

    Context context;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context){
        super(context, Constants.DATABASE_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
