package io.github.maximgorbatyuk.taskmanager.database;

import android.content.ContentValues;
import android.content.Context;

import io.github.maximgorbatyuk.taskmanager.Essential.Project;
import io.github.maximgorbatyuk.taskmanager.helpers.Constants;
import io.github.maximgorbatyuk.taskmanager.helpers.DateHelper;

/**
 * Created by Maxim on 13.05.2016.
 */
public class Database {

    private DBHelper helper;

    public Database(Context context){
        this.helper = new DBHelper(context);
    }

    static ContentValues getContentValues(Project project){
        DateHelper dateHelper = new DateHelper();
        ContentValues values = new ContentValues();
        values.put(Constants.TITLE_COLUMN,        project.getTitle());
        values.put(Constants.BODY_COLUMN,         project.getBody());
        values.put(Constants.IS_DONE_COLUMN,      project.getIsDone());
        values.put(Constants.DEADLINE_COLUMN,     dateHelper.dateToString( project.getDeadline() ));
        values.put(Constants.CREATED_AT_COLUMN,   dateHelper.dateToString(project.getCreatedAt() ));
        values.put(Constants.COST_COLUMN,         project.getCost());
        values.put(Constants.SPENT_TIME_COLUMN,   project.getMilliseconds());
        return values;
    }

    public void createProject(Project project, IExecuteResult delegate){
        new CreateProject(helper, delegate).execute(project);
    }

    public void destroyProject(int id, IExecuteResult delegate){
        new DestroyProject(helper, delegate).execute(id);
    }

    public void getProject(String[] args, IExecuteResult delegate){
        new ReadProject(helper, delegate).execute(args);
    }

    public void getListOfProjects(IExecuteResult delegate){
        new GetListOfProjects(helper, delegate).execute();
    }

    public void updateProject(Project project, IExecuteResult delegate){
        new UpdateProject(helper, delegate).execute(project);
    }


}
