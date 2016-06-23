package io.github.maximgorbatyuk.taskmanager.database;

import java.util.List;

import io.github.maximgorbatyuk.taskmanager.Essential.Project;

/**
 * Created by Maxim on 09.04.2016.
 */
public interface IDatabaseExecute {
    void onUpdatedSuccess(Boolean result);
    void onUpdatedSuccess(List<Project> list);
}
