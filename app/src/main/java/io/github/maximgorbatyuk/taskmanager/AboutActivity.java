package io.github.maximgorbatyuk.taskmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView t2 = (TextView) findViewById(R.id.textView2);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void viewSourceCode(View view) {
        startIntent( "https://github.com/maximgorbatyuk/TaskManager" );
    }

    public void viewFacebook(View view) {
        startIntent( "mailto:maximgorbatyuk191093@gmail.com?subject=ProjectManager bugreport&body=Hi,I found this bug:\n" );
    }

    private void startIntent(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
