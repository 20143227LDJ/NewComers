package com.example.capstone.pushmessage;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.capstone.MainActivity;
import com.example.capstone.R;

import java.util.Calendar;

//파이어베이스 푸시 알림(FCM)이 프로그램이 죽었을 때, 재부팅 했을 때 등의 상황에서도 서비스가 실행되도록 함
public class UndeadService extends Service {
    public static Intent serviceIntent = null;

    /*
    if app was dead, auto restart the app service by "return START_STICKY"..
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIntent = intent;
        initializeNotification();

        return START_STICKY;
    }

    /*
    if the device reboots or the app is dead, initialize the notification for the app..
    channels was defined by this function..
    this function is if app was dead or device reboot, redefine the channel..
    it will be running the notification message..
     */
    public void initializeNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "10";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText("NewComers 앱이 실행중입니다.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default alarm_channel_id", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(10, builder.build());
        //Notification notification = builder.build();
        startForeground(10, builder.build());
        manager.cancel(10);
        //manager.cancelAll();
        //stopForeground(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        final Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(System.currentTimeMillis());
        calender.add(Calendar.SECOND, 3);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), sender);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        final Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(System.currentTimeMillis());
        calender.add(Calendar.SECOND, 3);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), sender);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
