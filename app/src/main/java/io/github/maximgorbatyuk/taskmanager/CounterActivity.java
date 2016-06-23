package io.github.maximgorbatyuk.taskmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import io.github.maximgorbatyuk.taskmanager.helpers.DateHelper;
import io.github.maximgorbatyuk.taskmanager.helpers.NotificationHelper;

public class CounterActivity extends AppCompatActivity {

    private boolean isTimerActive = false;
    private Date start = null;
    private Timer timer = null;
    private SecondCounter counter = null;
    private long difference = 0;
    private NotificationHelper notificationHelper;
    private DateHelper dateHelper = new DateHelper();
    //private Intent timerService;
    private SharedPreferences preferences;

    private Button startOrStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        Intent intent = getIntent();
        ((TextView) findViewById(R.id.counterDescription)).setText(
                intent.hasExtra("project") ? intent.getStringExtra("project") :
                "No description"
        );

        startOrStopButton = (Button) findViewById(R.id.startOrStop);
        StartOrPauseTimer(startOrStopButton);
        notificationHelper = new NotificationHelper(this, this.getClass());
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //timerService = new Intent(this, TimerService.class);
    }

    public void StartOrPauseTimer(View view) {
        Button button = (Button) findViewById(R.id.startOrStop);
        if (isTimerActive) {

            if (button != null) {
                //button.setCompoundDrawables(getResources().getDrawable (R.drawable.ic_action_play), null, null, null);
            }
            stopTimer();
        }
        else {
            if (button != null) {
                //button.setCompoundDrawables(getResources().getDrawable (R.drawable.ic_action_pause), null, null, null);
            }
            startTimer();

        }
    }

    @Override
    protected void onDestroy() {
        sendDifferenceToParent();
        super.onDestroy();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            sendDifferenceToParent();
        }

        return super.onKeyDown(keyCode, event);
    }

    private void sendDifferenceToParent(){
        if (isTimerActive)
            difference = counter != null ? counter.getDifference() : 0;
        if (timer != null)      timer.cancel();
        if (counter != null)    counter.cancel();

        Intent intent = new Intent();
        //difference = counter.getDifference();
        intent.putExtra("difference", difference + "");
        setResult(RESULT_OK, intent);
    }

    private void startTimer(){
        isTimerActive = true;
        start = new Date();

        counter = new SecondCounter();
        counter.setData(start, difference);
        counter.setActive(true);

        timer = new Timer();
        //counter.run();
        timer.schedule(counter, 500, 1000);
            /*registerReceiver(receiver, new IntentFilter(Constants.BROADCAST_TIMER));
            startService(timerService);*/
        startOrStopButton.setText(getString(R.string.button_pause_timer));
        showNotificaton(getString(R.string.resume));
    }

    private void stopTimer(){
        isTimerActive = false;
        start = null;

        timer.cancel();
        counter.setActive(false);
        counter.cancel();


        difference = counter.getDifference();
        startOrStopButton.setText(getString(R.string.button_start_timer));
            /*unregisterReceiver(receiver);
            stopService(timerService);*/
        counter = null;
        timer = null;
        showNotificaton(getString(R.string.paused));
    }


    public void ResetTimerClick(View view) {
        difference = 0;
        if (isTimerActive)  {
            counter.setData(new Date(), 0);
            showNotificaton(getString(R.string.reset));
        }
        else {
            ( (TextView) findViewById(R.id.timerDisplay) ).setText(new DateHelper().getFormatDifference(difference));
        }
    }

    public void StopTimerClick(View view) {
        stopTimer();
        sendDifferenceToParent();
        finish();
    }

    private void showNotificaton(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /*private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long difference = intent != null ? Long.parseLong(intent.getStringExtra("difference")) : 0;
            if (difference > 0)
                ( (TextView) findViewById(R.id.timerDisplay) ).setText(getFormatDifference(difference));
        }
    };*/





    class SecondCounter extends TimerTask {

        private Date start = null;
        private long difference = 0;
        private long prevDifference = 0;
        private boolean isActive = false;

        public long getDifference(){
            return difference;
        }

        public void setData(Date start, long prevDifference){
            this.start = start;
            this.prevDifference = prevDifference;
        }

        @Override
        public void run() {
            difference = new Date().getTime() - start.getTime();
            difference += prevDifference;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView) findViewById(R.id.timerDisplay)).setText(dateHelper.getFormatDifference(difference));
                    long seconds = dateHelper.TimeCount(difference);
                    if (seconds != 0 && seconds % 10 == 0 && isActive && preferences.getBoolean("notifications", false)) {
                        notificationHelper.showText("Your time is " + seconds + " seconds");
                    }
                    }
                });
        }


        public void setActive(boolean active) {
            isActive = active;
        }
    }



}

