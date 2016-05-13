package io.github.maximgorbatyuk.taskmanager.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import io.github.maximgorbatyuk.taskmanager.helpers.Constants;

public class TimerService extends Service {

    private Date start;
    private Timer timer = null;
    private Counter counter = null;
    private boolean isActive = false;
    private long difference = 0;
    private Intent intent;
    private Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        handler.removeCallbacks(sendDifferenceUpdate);

        this.intent = new Intent(Constants.BROADCAST_TIMER);
        handler.postDelayed(sendDifferenceUpdate, 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        start = intent.hasExtra("start") ? new Date(Long.parseLong(intent.getStringExtra("start"))) : new Date();
        startTimer();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //putDifferenceToBroadcast();
        stopTimer();
        sendDifferenceUpdate = null;
        stopSelf();
    }

    private Runnable sendDifferenceUpdate = new Runnable() {
        @Override
        public void run() {
            putDifferenceToBroadcast();
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public boolean  isActive() { return isActive; }
    public void     setActive(boolean active) { isActive = active; }


    private void startTimer(){
        timer = new Timer();
        counter = new Counter();
        timer.schedule(counter, 1000, 1000);
        isActive = true;
    }

    private void stopTimer(){
        if (timer   != null) timer.cancel();
        if (counter != null) counter.cancel();
        counter = null;
        timer = null;
        isActive = false;
    }

    private void putDifferenceToBroadcast(){
        intent.putExtra("difference", difference + "");
        sendBroadcast(intent);
        difference = 0;
    }

    class Counter extends TimerTask {
        public Counter(){
        }
        @Override
        public void run() {
            Date now = new Date();
            difference = now.getTime() - start.getTime();
        }
    }

}

