package io.github.maximgorbatyuk.taskmanager.helpers;

/**
 * Created by Maxim on 13.04.2016.
 */
public class Constants {
    public static final String LOG_TAG = "Project Manager";
    public static final String BROADCAST_TIMER = "io.github.maximgorbatyuk.PROJECT_EXECUTION_TIMER";

    public static final String DATABASE_NAME       = "project_manager_database.db";
    public static final String TABLE_NAME          = "project_table";
    public static final String TITLE_COLUMN        = "project_title";
    public static final String BODY_COLUMN         = "project_body";
    public static final String IS_DONE_COLUMN      = "is_done";
    public static final String DEADLINE_COLUMN     = "deadline";
    public static final String CREATED_AT_COLUMN   = "created_at";
    public static final String COST_COLUMN = "price";
    public static final String SPENT_TIME_COLUMN   = "spent_time";
    public static final int    DB_VERSION          = 4;
    //------------
    public static final int    NOTIFY_ID          = 04;
}
