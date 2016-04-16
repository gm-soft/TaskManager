package io.github.maximgorbatyuk.taskmanager;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.maximgorbatyuk.taskmanager.database.ExecuteResult;
import io.github.maximgorbatyuk.taskmanager.database.ReadProject;
import io.github.maximgorbatyuk.taskmanager.database.ReadProjectResult;
import io.github.maximgorbatyuk.taskmanager.database.UpdateProject;
import io.github.maximgorbatyuk.taskmanager.help.DateHelper;
import io.github.maximgorbatyuk.taskmanager.help.Project;

public class OpenActivity extends AppCompatActivity {

    private String PROJECT_ID = "";
    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Intent data = getIntent();
        if (data.hasExtra("id")) {
            PROJECT_ID = data.getStringExtra("id");
            getProject(PROJECT_ID);
        }
    }

    private void fillProject(Project project){
        try {
            //PROJECT_ID = project.getId();

            ((TextView) findViewById(R.id.openBody)).setText(
                    project.getBody());
            ((TextView) findViewById(R.id.openDeadline)).setText(
                    new DateHelper().dateToString( project.getDeadline()));
            ((TextView) findViewById(R.id.openCreatedAt)).setText(
                    new DateHelper().dateToString( project.getCreatedAt() ));
            ((TextView) findViewById(R.id.openStatus)).setText(
                    project.getIsDone() ?
                            getString(R.string.open_status) :
                            getString(R.string.open_status_not_done) );
            ((TextView) findViewById(R.id.openId)).setText("" + project.getId());
            ((CollapsingToolbarLayout)findViewById(R.id.toolbar_layout)).setTitle(project.getTitle());


            ((TextView) findViewById(R.id.openSpentTime)).setText(
                    new DateHelper().getFormatDifference(project.getMilliseconds()));

            ((TextView) findViewById(R.id.openCosts)).setText(
                    project.getCost() + "");
        }
        catch (Exception ex){
            showNotification(ex.getMessage());
        }
    }

    private void getProject(String id){
        new ReadProject(this, new ReadProjectResult() {
            @Override
            public void processFinish(List<Project> projects) {
                if (projects.size() > 0) {
                    project = projects.get(0);
                    fillProject(project);
                    fillStatistic(project);
                }
                else
                    showNotification(getString(R.string.error_smth_goes_wrong));
            }
        }).execute("one", id);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            if (data.hasExtra("difference")) {
                long difference = Long.parseLong(data.getStringExtra("difference"));
                if (difference > 0)
                    addDifferenceToProject(difference);
                //showNotification("Difference is " + difference);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showNotification(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void onPlayFabClick(View view) {
        Intent intent = new Intent(this, CounterActivity.class);
        String shortBody = project.getBody().length() > 30 ? project.getBody().substring(0, 30) + "..." : project.getBody();
        intent.putExtra("project", project.getTitle() + "\n" + shortBody);
        startActivityForResult(intent, 2);
    }

    private void addDifferenceToProject(long difference){
        if (project != null){
            project.setMilliseconds( project.getMilliseconds() + difference);
            new UpdateProject(this, new ExecuteResult() {
                @Override
                public void processFinish(Boolean result) {
                    showNotification(getString(result ? R.string.update_success : R.string.error_not_created_updated));
                    if (result && !PROJECT_ID.isEmpty()) getProject(PROJECT_ID);
                }
            }).execute(project);
        }
    }

    private void fillStatistic(Project project){
        //((TextView) findViewById(R.id.openBody))
        long hours      = TimeUnit.MILLISECONDS.toHours(project.getMilliseconds());
        double cost     = project.getCost() / hours;
        long spentHours = TimeUnit.MILLISECONDS.toHours(new Date().getTime() - project.getCreatedAt().getTime());
        long spentDays  = TimeUnit.MILLISECONDS.toDays(new Date().getTime() - project.getCreatedAt().getTime());
        double usefulTimePercent = 0;
        if (hours > 0)
            usefulTimePercent = spentHours / hours;

        //------------------------------
        ((TextView) findViewById(R.id.statHourCost)).setText(cost + "");
        ((TextView) findViewById(R.id.statKpd))     .setText(usefulTimePercent + "");
        ((TextView) findViewById(R.id.statDaysCount)).setText(spentDays + "");
        ((TextView) findViewById(R.id.statHoursCount)).setText(spentHours + "");
    }
}
