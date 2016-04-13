package io.github.maximgorbatyuk.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class CounterActivity extends AppCompatActivity {

    private boolean isTimerActive = false;
    private Date start = null;
    private Timer timer = null;
    private SecondCounter counter = null;
    private long millisecondLong = 0;

    private Button startOrStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        startOrStopButton = (Button) findViewById(R.id.startOrStop);
        timer = new Timer();
    }

    public void StartOrStopTimer(View view) {
        if (isTimerActive)
        {
            isTimerActive = false;
            if (timer != null)
                timer.cancel();
            timer = null;
            start = null;
            if (counter != null)
                Toast.makeText(this, "Difference " + counter.getDifference(), Toast.LENGTH_LONG).show();
            counter = null;
            startOrStopButton.setText(getString(R.string.button_start_timer));
        }
        else
        {

            isTimerActive = true;
            start = new Date();
            timer = new Timer();
            counter = new SecondCounter(start);
            timer.schedule(counter, 500, 1000);

            startOrStopButton.setText(getString(R.string.button_stop_timer));

        }
        // here is start or stop timer processor
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        Intent intent = new Intent();
        intent.putExtra("difference", counter != null ? counter.getDifference() : 0);
        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            timer.cancel();
            Intent intent = new Intent();
            intent.putExtra("difference", counter != null ? counter.getDifference() + "" : "0");
            setResult(RESULT_OK, intent);
        }

        return super.onKeyDown(keyCode, event);
    }

    class SecondCounter extends TimerTask {

        private Date start = null;
        private long difference = 0;

        public SecondCounter(Date start){
            this.start = start;
        }

        public long getDifference(){
            return difference;
        }

        @Override
        public void run() {
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            format.setTimeZone(TimeZone.getTimeZone("England"));
            difference = now.getTime() - start.getTime();
            final String time = format.format(new Date(difference));

            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    ( (TextView) findViewById(R.id.timerDisplay) ).setText(time);
                }
            });
        }

    }

}

