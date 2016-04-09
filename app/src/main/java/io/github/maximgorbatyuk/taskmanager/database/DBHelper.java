package io.github.maximgorbatyuk.taskmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maxim on 09.04.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private String LOG_TAG = "Task Manager";

    private static final String DATABASE_NAME       = "task_manager_database.db";
    private static final String TABLE_NAME          = "tasks_table";
    private static final String TITLE_COLUMN        = "task_title";
    private static final String BODY_COLUMN         = "task_body";
    private static final String IS_DONE_COLUMN      = "is_done";
    private static final String DEADLINE_COLUMN     = "deadline";
    private static final String CREATED_AT_COLUMN   = "created_at";
    private static final String PRIORITY_COLUMN    = "priority";
    private static final int    DB_VERSION          = 2;

    private static final String DROP_TABLE          = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +
                    "( id INTEGER PRIMARY KEY, " +
                    TITLE_COLUMN        + " TEXT, " +
                    BODY_COLUMN         + " TEXT, " +
                    IS_DONE_COLUMN      + " BOOLEAN, " +
                    PRIORITY_COLUMN    + " INTEGER, " +
                    DEADLINE_COLUMN     + " DATETIME, " +
                    CREATED_AT_COLUMN   + " DATETIME )";

    Context context;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String getTableName()        { return TABLE_NAME; }
    public String getTitleColumn()      { return TITLE_COLUMN; }
    public String getBodyColumn()       { return BODY_COLUMN; }
    public String getIsDoneColumn()     { return IS_DONE_COLUMN; }
    public String getDeadlineColumn()   { return DEADLINE_COLUMN; }
    public String getCreatedAtColumn()  { return CREATED_AT_COLUMN; }
    public String getPrioritytColumn()  { return PRIORITY_COLUMN; }
}
