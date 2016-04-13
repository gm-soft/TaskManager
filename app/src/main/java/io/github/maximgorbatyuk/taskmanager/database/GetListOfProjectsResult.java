package io.github.maximgorbatyuk.taskmanager.database;

import java.util.List;

import io.github.maximgorbatyuk.taskmanager.help.Project;

/**
 * Created by Maxim on 09.04.2016.
 */
public interface GetListOfProjectsResult {
    void processFinish(List<Project> list);
}
