package io.github.maximgorbatyuk.taskmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {


    Thread welcomeThread = new Thread() {
        @Override
        public void run() {
            try {
                super.run();
                sleep(1000);  //Delay of 10 seconds
            } catch (Exception e) {

            } finally {

                Intent i = new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        welcomeThread.start();
    }
}
