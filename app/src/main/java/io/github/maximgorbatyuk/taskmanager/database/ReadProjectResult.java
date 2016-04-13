package io.github.maximgorbatyuk.taskmanager.database;

import java.util.List;

import io.github.maximgorbatyuk.taskmanager.help.Project;

/**
 * Created by Maxim on 09.04.2016.
 */
public interface ReadProjectResult {
    void processFinish(List<Project> project);
}
