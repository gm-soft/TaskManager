package io.github.maximgorbatyuk.taskmanager.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;

import io.github.maximgorbatyuk.taskmanager.MainActivity;
import io.github.maximgorbatyuk.taskmanager.R;
import io.github.maximgorbatyuk.taskmanager.helpers.Constants;

/**
 * Created by Maxim on 23.04.2016.
 */
public class NotificationHelper {

    private Context context;
    private Notification.Builder builder;
    private Resources resources;
    private PendingIntent pendingIntent;

    public NotificationHelper(Context context, Class<?> cls){
        this.context = context;
        builder = new Notification.Builder(context);
        resources  = context.getResources();

        //senderIntent = new Intent(context, MainActivity.class);
        Intent senderIntent = getIntent(context, cls);
        pendingIntent = PendingIntent.getActivity(context, 0, senderIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private Intent getIntent(Context context, Class<?> cls){
        Intent result = new Intent(context, cls);
        return result;
    }

    private void buildAndShow(){


        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_notification)
                .setDefaults(Notification.DEFAULT_ALL);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(Constants.NOTIFY_ID, notification);

    }

    public void showNotification( ){

        //-------------------
        builder.setContentTitle("Hello World!")
                .setContentText("Yeah, this is a notification");
        buildAndShow();
    }

    public void showText(String title, String text){
        builder.setContentTitle(title)
                .setContentText(text);
        buildAndShow();
    }

    public void showText(String text){
        builder.setContentTitle(context.getString(R.string.app_name))
                .setContentText(text);
        buildAndShow();
    }
}
