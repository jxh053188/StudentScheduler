package com.example.studentscheduler.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.studentscheduler.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class TermEndReceiver extends BroadcastReceiver {
    static int termEndNotificationId = 30000;
    String channel_id="TermEnd";
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context,channel_id);
        Notification n= new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentText(context.getResources().getString(R.string.term_end_text))
                .setContentTitle(context.getResources().getString(R.string.term_end_title)).build();
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(termEndNotificationId++,n);
    }

    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getResources().getString(R.string.term_end_channel);
            String description = context.getString(R.string.term_end_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}