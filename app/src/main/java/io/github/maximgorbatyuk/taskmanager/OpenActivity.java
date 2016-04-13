package io.github.maximgorbatyuk.taskmanager;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import io.github.maximgorbatyuk.taskmanager.database.GetTask;
import io.github.maximgorbatyuk.taskmanager.database.GetTaskResult;
import io.github.maximgorbatyuk.taskmanager.help.DateHelper;
import io.github.maximgorbatyuk.taskmanager.help.Task;

public class OpenActivity extends AppCompatActivity {

    private int TASK_ID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Intent data = getIntent();
        if (data.hasExtra("id"))
            getTask(data.getStringExtra("id"));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void fillTask(Task task){
        try {
            TASK_ID = task.getId();

            ((TextView) findViewById(R.id.openBody)).setText(
                    task.getBody());
            ((TextView) findViewById(R.id.openDeadline)).setText(
                    new DateHelper().dateToString( task.getDeadline()));
            ((TextView) findViewById(R.id.openCreatedAt)).setText(
                    new DateHelper().dateToString( task.getCreatedAt() ));
            ((TextView) findViewById(R.id.openStatus)).setText(
                    task.getIsDone() ?
                            getString(R.string.open_status) :
                            getString(R.string.open_status_not_done) );
            ((TextView) findViewById(R.id.openId)).setText("" + task.getId());
            ((CollapsingToolbarLayout)findViewById(R.id.toolbar_layout)).setTitle(task.getTitle());

        }
        catch (Exception ex){
            showNotification(ex.getMessage());
        }
    }

    private void getTask(String id){
        new GetTask(this, new GetTaskResult() {
            @Override
            public void processFinish(List<Task> task) {
                if (task.size() > 0) {
                    fillTask(task.get(0));
                }
                else
                    showNotification(getString(R.string.error_smth_goes_wrong));
            }
        }).execute("one", id);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Objects.equals(data.getStringExtra("action"), "update")) {
            if (resultCode == RESULT_OK) {
                showNotification(getString(R.string.update_success));
                getTask("" + TASK_ID);
            }
        }
    }

    private void showNotification(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void onPlayFabClick(View view) {
    }
}
