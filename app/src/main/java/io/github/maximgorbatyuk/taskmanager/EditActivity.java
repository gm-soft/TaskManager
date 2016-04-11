package io.github.maximgorbatyuk.taskmanager;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import io.github.maximgorbatyuk.taskmanager.database.ExecuteResult;
import io.github.maximgorbatyuk.taskmanager.database.GetTask;
import io.github.maximgorbatyuk.taskmanager.database.GetTaskResult;
import io.github.maximgorbatyuk.taskmanager.database.InsertTask;
import io.github.maximgorbatyuk.taskmanager.database.UpdateTask;
import io.github.maximgorbatyuk.taskmanager.help.DateHelper;
import io.github.maximgorbatyuk.taskmanager.help.DatePickerFragment;
import io.github.maximgorbatyuk.taskmanager.help.DateTimeInterface;
import io.github.maximgorbatyuk.taskmanager.help.Task;
import io.github.maximgorbatyuk.taskmanager.help.TimePickerFragment;


public class EditActivity extends AppCompatActivity {

    private int DEADLINE_DATE = 0;
    private int DEADLINE_TIME = 0;

    private EditText editTitle;
    private EditText editBody;
    private TextView textDeadline;
    private Switch switchDone;
    private TextView createdAt;
    private TextView taskId;
    //-
    private Button insertUpdateButton;
    private Button removeTask;
    //--
    private String deadline = "";
    private String ACTION = "";

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ACTION = getIntent().getStringExtra("action");

        editTitle       = (EditText) findViewById(R.id.editTaskTitle);
        editBody        = (EditText) findViewById(R.id.editTaskBody);
        textDeadline    = (TextView) findViewById(R.id.textViewDeadline);
        switchDone      = (Switch)   findViewById(R.id.switch1);
        createdAt       = (TextView) findViewById(R.id.editCreatedAt);
        taskId          = (TextView) findViewById(R.id.editID);

        TextView desc = (TextView) findViewById(R.id.textViewEditDescribe);

        insertUpdateButton = (Button) findViewById(R.id.editUpdateInsert);
        removeTask      = (Button)   findViewById(R.id.buttonRemove);
        if (Objects.equals(ACTION, "create")){
            insertUpdateButton.setText(getString(R.string.button_create_task));
            insertUpdateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertTaskToDatabase(constructTask());
                }
            });


            desc.setText(getString(R.string.create_task));
            createdAt.setVisibility(View.INVISIBLE);
            switchDone.setVisibility(View.INVISIBLE);
        }
        if (Objects.equals(ACTION, "update")){
            insertUpdateButton.setText(getString(R.string.button_update_task));
            insertUpdateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateTaskInDatabase(constructTask());
                }
            });
            desc.setText(getString(R.string.edit_activity_update_task));
            createdAt.setVisibility(View.VISIBLE);
            switchDone.setVisibility(View.VISIBLE);
            GetTaskAndFillEdits(getIntent().getStringExtra("id"));
        }
        //------------------------

    }

    public void showDeadlineDialog(View v){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setDelegate(new DateTimeInterface() {
            @Override
            public void getDateTimeString(String result) {
                deadline += result;
                showTimePicker();
            }
        });
        fragment.show(getSupportFragmentManager(), "datePicker");


    }

    private void showTimePicker(){
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setDelegate(new DateTimeInterface() {
            @Override
            public void getDateTimeString(String result) {
                deadline += " " + result;
                textDeadline.setText(deadline);
                deadline = "";
            }
        });
        fragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void fillEdits(Task task){
        editTitle.setText( task.getTitle() );
        editBody.setText( task.getBody() );
        textDeadline.setText( new DateHelper().dateToString( task.getDeadline()) );
        switchDone.setChecked( task.getIsDone() );
        createdAt.setText( new DateHelper().dateToString( task.getCreatedAt() ));
        taskId.setText(task.getId() + "");
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Task constructTask(){
        if (Objects.equals(ACTION, "create") && editBody.getText().toString().isEmpty()){
            showNotification(getString(R.string.error_empty_edits));
            return null;
        }
        Task task = new Task();
        /*
        editTitle.setText( task.getTitle() );
        editBody.setText( task.getBody() );
        textDeadline.setText( task.getDeadline().toString() );
        switchDone.setChecked( task.getIsDone() );
        createdAt.setText( task.getCreatedAt().toString() );
        taskId.setText( task.getId() );
        */
        int id = (taskId.getText().toString().isEmpty() || Objects.equals(ACTION, "create")) ?
                -1 :
                Integer.parseInt(taskId.getText().toString());
        task.setId(id);
        task.setTitle   (!editTitle.getText().toString().isEmpty() ?
                editTitle.getText().toString() :
                getString(R.string.fill_no_title));

        task.setBody    (editBody.getText().toString());

        task.setDeadline( !textDeadline.getText().toString().isEmpty() ?
                new DateHelper().parseDate(textDeadline.getText().toString()) :
                null);

        task.setIsDone  (switchDone.isChecked());
        task.setCreatedAt(!createdAt.getText().toString().isEmpty()?
                new DateHelper().parseDate(createdAt.getText().toString()) :
                task.getCreatedAt());

        return task;
    }

    private void GetTaskAndFillEdits(String id){
        new GetTask(this, new GetTaskResult() {
            @Override
            public void processFinish(List<Task> task) {
                if (task.size() > 0)
                    try {
                        fillEdits(task.get(0));
                    } catch (Exception ex){
                        showNotification(ex.getMessage());
                    }
                else
                    showNotification(getString(R.string.error_null_result));
            }
        }).execute("one", id);
    }

    private void showNotification(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }



    private void insertTaskToDatabase(Task task){
        if (task != null) {
            new InsertTask(getApplicationContext(), new ExecuteResult() {
                @Override
                public void processFinish(Boolean result) {
                    if (result)
                        createResultIntent("create");
                    else
                        showNotification(getString(R.string.error_not_created_updated));
                }
            }).execute(task);
        }
        else
            showNotification(getString(R.string.error_empty_edits));
    }

    private void updateTaskInDatabase(Task task){
        if (task != null) {
            new UpdateTask(getApplicationContext(), new ExecuteResult() {
                @Override
                public void processFinish(Boolean result) {
                    if (result)
                        createResultIntent("update");
                    else
                        showNotification(getString(R.string.error_not_created_updated));
                }
            }).execute(task);
        }
        else
            showNotification(getString(R.string.error_empty_edits));
    }

    private void createResultIntent(String action){
        Intent intent = new Intent();
        intent.putExtra("action", action);

        setResult(RESULT_OK, intent);
        finish();
    }

    public void clearDeadline(View view) {
        textDeadline.setText("");
    }
}
