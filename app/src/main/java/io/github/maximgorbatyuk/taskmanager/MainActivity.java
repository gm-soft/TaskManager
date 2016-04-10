package io.github.maximgorbatyuk.taskmanager;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import io.github.maximgorbatyuk.taskmanager.database.GetListOfTasks;
import io.github.maximgorbatyuk.taskmanager.database.GetListOfTasksResult;
import io.github.maximgorbatyuk.taskmanager.help.Task;
import io.github.maximgorbatyuk.taskmanager.help.TaskAdapter;

public class MainActivity extends AppCompatActivity {

    private boolean SHOW_ALL = true;
    private ListView listView;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        listView = (ListView) findViewById(R.id.listTasks);
        textView = (TextView) findViewById(R.id.textView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //createNewTask();
                    //showSnackbar(view, "Snackbar");
                    //showNotification("Toast");
                    createNewTask();
                }
            });
        }
        // Берется список задач из базы по созданию активити
        getTasksList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            createIntent(SettingsActivity.class);
            return true;
        }
        if (id == R.id.action_about){
            createIntent(AboutActivity.class);
            return true;
        }
        if (id == R.id.action_show_all){
            showNotification("Show all");
            return true;
        }
        if (id == R.id.action_show_uncomplicted){
            showNotification("Show uncompleted");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showNotification(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private void showSnackbar(View view, String text){
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }

    private void createIntent(Class activityClass){
        Intent intent = new Intent(this, activityClass);
        startActivityForResult(intent, 0);
    }

    private void getTasksList(){
        new GetListOfTasks(this, new GetListOfTasksResult() {
            @Override
            public void processFinish(List<Task> list) {
                fillListByTasks(list);
            }
        }).execute();
    }

    private void fillListByTasks(List<Task> source){
        if (source.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            listView.setAdapter(new TaskAdapter(this, source));
        }
        else{
            listView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }

    }

    private void createNewTask(){
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("action", "create");
        startActivityForResult(intent, 0);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            String action = data.getStringExtra("action");
            if (resultCode == RESULT_OK) {
                if (Objects.equals(action, "create"))
                    showNotification(getString(R.string.insert_success));
                if (Objects.equals(action, "update"))
                    showNotification(getString(R.string.update_success));

                getTasksList();

            }
            if (resultCode == RESULT_CANCELED) {
                showNotification(getString(R.string.error_not_created_updated));
            }
        }
    }
}
