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
import io.github.maximgorbatyuk.taskmanager.database.ReadProject;
import io.github.maximgorbatyuk.taskmanager.database.ReadProjectResult;
import io.github.maximgorbatyuk.taskmanager.database.CreateProject;
import io.github.maximgorbatyuk.taskmanager.database.UpdateProject;
import io.github.maximgorbatyuk.taskmanager.help.DateHelper;
import io.github.maximgorbatyuk.taskmanager.help.DatePickerFragment;
import io.github.maximgorbatyuk.taskmanager.help.DateTimeInterface;
import io.github.maximgorbatyuk.taskmanager.help.Project;
import io.github.maximgorbatyuk.taskmanager.help.TimePickerFragment;


public class EditActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editBody;
    private TextView textDeadline;
    private Switch switchDone;
    private TextView createdAt;
    private TextView projectId;
    private EditText projectCost;
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
        projectId       = (TextView) findViewById(R.id.editID);
        projectCost     = (EditText) findViewById(R.id.editProjectCost);

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
            GetProjectAndFillEdits(getIntent().getStringExtra("id"));
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

    private void fillEdits(Project project){
        editTitle.      setText(    project.getTitle() );
        editBody.       setText(    project.getBody() );
        textDeadline.   setText(new DateHelper().dateToString( project.getDeadline()) );
        switchDone.     setChecked( project.getIsDone() );
        createdAt.      setText(new DateHelper().dateToString( project.getCreatedAt() ));
        projectId.      setText(    "" + project.getId());
        projectCost.    setText(    "" + project.getCost());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Project constructTask(){
        if (Objects.equals(ACTION, "create") && editBody.getText().toString().isEmpty()){
            showNotification(getString(R.string.error_empty_edits));
            return null;
        }
        Project project = new Project();
        /*
        editTitle.setText( project.getTitle() );
        editBody.setText( project.getBody() );
        textDeadline.setText( project.getDeadline().toString() );
        switchDone.setChecked( project.getIsDone() );
        createdAt.setText( project.getCreatedAt().toString() );
        projectId.setText( project.getId() );
        */
        int id = (projectId.getText().toString().isEmpty() || Objects.equals(ACTION, "create")) ?
                -1 :
                Integer.parseInt(projectId.getText().toString());
        project.setId(id);
        project.setTitle(!editTitle.getText().toString().isEmpty() ?
                editTitle.getText().toString() :
                getString(R.string.fill_no_title));

        project.setBody(editBody.getText().toString());

        project.setDeadline( !textDeadline.getText().toString().isEmpty() ?
                new DateHelper().parseDate(textDeadline.getText().toString()) :
                null);

        project.setIsDone(switchDone.isChecked());
        project.setCreatedAt(!createdAt.getText().toString().isEmpty()?
                new DateHelper().parseDate(createdAt.getText().toString()) :
                project.getCreatedAt());

        project.setCost( !projectCost.getText().toString().isEmpty() ?
                            Double.parseDouble(projectCost.getText().toString()) :
                            0);

        return project;
    }

    private void GetProjectAndFillEdits(String id){
        new ReadProject(this, new ReadProjectResult() {
            @Override
            public void processFinish(List<Project> project) {
                if (project.size() > 0)
                    try {
                        fillEdits(project.get(0));
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



    private void insertTaskToDatabase(Project project){
        if (project != null) {
            new CreateProject(getApplicationContext(), new ExecuteResult() {
                @Override
                public void processFinish(Boolean result) {
                    if (result)
                        createResultIntent("create");
                    else
                        showNotification(getString(R.string.error_not_created_updated));
                }
            }).execute(project);
        }
        else
            showNotification(getString(R.string.error_empty_edits));
    }

    private void updateTaskInDatabase(Project project){
        if (project != null) {
            new UpdateProject(getApplicationContext(), new ExecuteResult() {
                @Override
                public void processFinish(Boolean result) {
                    if (result)
                        createResultIntent("update");
                    else
                        showNotification(getString(R.string.error_not_created_updated));
                }
            }).execute(project);
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
