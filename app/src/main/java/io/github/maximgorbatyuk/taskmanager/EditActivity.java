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
import java.util.concurrent.TimeUnit;

import io.github.maximgorbatyuk.taskmanager.database.Database;
import io.github.maximgorbatyuk.taskmanager.database.IExecuteResult;
import io.github.maximgorbatyuk.taskmanager.helpers.DateHelper;
import io.github.maximgorbatyuk.taskmanager.helpers.DatePickerFragment;
import io.github.maximgorbatyuk.taskmanager.helpers.DateTimeInterface;
import io.github.maximgorbatyuk.taskmanager.Essential.Project;
import io.github.maximgorbatyuk.taskmanager.helpers.TimePickerFragment;


public class EditActivity extends AppCompatActivity implements IExecuteResult{

    private EditText editTitle;
    private EditText editBody;
    private TextView textDeadline;
    private Switch switchDone;
    private TextView createdAt;
    private TextView projectId;
    private EditText projectCost;
    private EditText projectHours;
    private TextView millisecondsCount;
    //-
    private Button insertUpdateButton;
    private Button removeTask;
    //--
    private String deadline = "";
    private String ACTION = "";

    private DateHelper dateHelper = new DateHelper();
    private Database database;

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
        projectHours    = (EditText) findViewById(R.id.editExecuteHour);
        millisecondsCount = (TextView) findViewById(R.id.millisecondsCount);

        TextView desc = (TextView) findViewById(R.id.textViewEditDescribe);

        insertUpdateButton = (Button) findViewById(R.id.editUpdateInsert);
        removeTask      = (Button)   findViewById(R.id.buttonRemove);

        database = new Database(this);

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
        textDeadline.   setText(dateHelper.dateToString( project.getDeadline()) );
        switchDone.     setChecked( project.getIsDone() );
        createdAt.      setText(dateHelper.dateToString( project.getCreatedAt() ));
        projectId.      setText(    String.valueOf(project.getId()));
        projectCost.    setText(    String.valueOf(project.getCost()));

        long hours      = TimeUnit.MILLISECONDS.toHours(project.getMilliseconds());
        projectHours.setText(String.valueOf( hours ));

        long remain = project.getMilliseconds() - TimeUnit.HOURS.toMillis(hours);
        millisecondsCount.setText(String.valueOf(remain));
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
                dateHelper.parseDate(textDeadline.getText().toString()) :
                null);

        project.setIsDone(switchDone.isChecked());
        project.setCreatedAt(!createdAt.getText().toString().isEmpty()?
                dateHelper.parseDate(createdAt.getText().toString()) :
                project.getCreatedAt());

        project.setCost( !projectCost.getText().toString().isEmpty() ?
                            Double.parseDouble(projectCost.getText().toString()) :
                            0);

        String ms = millisecondsCount.getText().toString();
        long remain = ms.isEmpty() ? 0 : Long.parseLong(ms);
        long hours  = projectHours.getText().toString().isEmpty() ? 0 : Long.parseLong( projectHours.getText().toString());

        project.setMilliseconds (remain + TimeUnit.HOURS.toMillis( hours ));
        //project.setMilliseconds( project.getMilliseconds() + HoursToMillisecinds( projectHours.getText().toString() ));

        return project;
    }


    private void GetProjectAndFillEdits(String id){
        database.getProject(new String[] {"one", id}, this);
    }

    private void showNotification(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }



    private void insertTaskToDatabase(Project project){
        if (project != null) database.createProject(project, this);
        else showNotification(getString(R.string.error_empty_edits));
    }

    private void updateTaskInDatabase(Project project){
        if (project != null) database.updateProject(project, this);
        else showNotification(getString(R.string.error_empty_edits));
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

    public void plusHourToEdit(View view) {
        int hours = !projectHours.getText().toString().isEmpty() ? Integer.parseInt( projectHours.getText().toString()) : 0;
        projectHours.setText(String.valueOf( hours + 1) );

    }

    public void minusHourToEdit(View view) {
        int hours = !projectHours.getText().toString().isEmpty() ? Integer.parseInt( projectHours.getText().toString()) : 0;
        projectHours.setText(String.valueOf( hours-1 > 0 ? hours - 1 : 0) );
    }

    @Override
    public void onExecute(Boolean result) {
        if (result) {
            if (ACTION == "update")
                createResultIntent("update");
            else
                createResultIntent("create");
        }
        else
            showNotification(getString(R.string.error_not_created_updated));
    }

    @Override
    public void onExecute(List<Project> list) {
        if (list.size() > 0)
            fillEdits(list.get(0));
        else
            showNotification(getString(R.string.error_null_result));
    }
}
