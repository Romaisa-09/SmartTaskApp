package com.example.taskapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;               // ← import Build
import android.util.Log;              // ← import Log

import androidx.core.app.NotificationCompat;

public class DeadlineReceiver extends BroadcastReceiver {
    private static final String TAG = "DeadlineReceiver";
    private static final String CHANNEL_ID = "deadline_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        int taskId    = intent.getIntExtra("taskId", 0);
        String title  = intent.getStringExtra("taskTitle");
        if (title == null) title = "You have a task due";

        Intent tapIntent = new Intent(context, MainActivity.class);
        tapIntent.putExtra("openTaskId", taskId);
        PendingIntent pi = PendingIntent.getActivity(
                context, taskId, tapIntent, PendingIntent.FLAG_IMMUTABLE
        );

        NotificationManager nm =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(new NotificationChannel(
                    CHANNEL_ID, "Deadlines", NotificationManager.IMPORTANCE_HIGH
            ));
        }

        nm.notify(taskId,
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_add)
                        .setContentTitle("Task Due Soon: " + title)        // ← show title
                        .setContentText("Your deadline is approaching.")
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .build()
        );
    }

}
