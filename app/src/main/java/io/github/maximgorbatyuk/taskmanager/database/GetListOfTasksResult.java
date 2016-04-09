package io.github.maximgorbatyuk.taskmanager.database;

import java.util.List;

import io.github.maximgorbatyuk.taskmanager.help.Task;

/**
 * Created by Maxim on 09.04.2016.
 */
public interface GetListOfTasksResult {
    void processFinish(List<Task> list);
}
