package io.github.maximgorbatyuk.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import io.github.maximgorbatyuk.taskmanager.help.DateHelper;

public class CounterActivity extends AppCompatActivity {

    private boolean isTimerActive = false;
    private Date start = null;
    private Timer timer = null;
    private SecondCounter counter = null;
    private long difference = 0;
    //private Intent timerService;

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
        timer = new Timer();
        StartOrPauseTimer(startOrStopButton);
        //timerService = new Intent(this, TimerService.class);
    }

    public void StartOrPauseTimer(View view) {
        if (isTimerActive)
            stopTimer();
        else
            startTimer();
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
        if (isTimerActive){
            difference = counter != null ? counter.getDifference() : 0;
        }
        Intent intent = new Intent();
        //difference = counter.getDifference();
        intent.putExtra("difference", difference + "");
        setResult(RESULT_OK, intent);
    }

    private void startTimer(){
        isTimerActive = true;
        start = new Date();
        timer = new Timer();
        counter = new SecondCounter(start, difference);
        timer.schedule(counter, 500, 1000);
            /*registerReceiver(receiver, new IntentFilter(Constants.BROADCAST_TIMER));
            startService(timerService);*/
        startOrStopButton.setText(getString(R.string.button_pause_timer));
    }

    private void stopTimer(){
        isTimerActive = false;
        if (timer != null)
            timer.cancel();
        timer = null;
        start = null;
        if (counter != null)
            //Toast.makeText(this, "Difference " + counter.getDifference(), Toast.LENGTH_LONG).show();
            difference = counter.getDifference();
        startOrStopButton.setText(getString(R.string.button_start_timer));
            /*unregisterReceiver(receiver);
            stopService(timerService);*/
    }


    public void ResetTimerClick(View view) {
        difference = 0;
        if (isTimerActive)
            counter.setStartDate(new Date());
        else
            ( (TextView) findViewById(R.id.timerDisplay) ).setText(new DateHelper().getFormatDifference(difference));
    }

    public void StopTimerClick(View view) {
        stopTimer();
        sendDifferenceToParent();
        finish();
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

        public SecondCounter(Date start){  this.start = start; }
        public SecondCounter(Date start, long prevDifference){
            this.start = start;
            this.prevDifference = prevDifference;
        }
        public long getDifference(){
            return difference;
        }

        public void setStartDate(Date start){
            this.start = start;
        }

        @Override
        public void run() {
            difference = new Date().getTime() - start.getTime();
            difference += prevDifference;
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    ( (TextView) findViewById(R.id.timerDisplay) ).setText(new DateHelper().getFormatDifference(difference));
                }
            });
        }



    }



}

