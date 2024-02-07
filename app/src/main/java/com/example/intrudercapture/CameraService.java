package com.example.intrudercapture;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

public class CameraService extends Service {
    private static final String ID_FOREGROUND = "foreground";
    private NotificationManager notificationManager;
    private PhoneUnlockedReceiver receiver;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return Service.START_STICKY;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        startForeground(1000, getNotification());
        this.receiver = new PhoneUnlockedReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        registerReceiver(this.receiver, intentFilter);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(this.receiver);
        } catch (Exception unused) {
        }
    }

    private Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getPackageName() + "_" + CameraService.ID_FOREGROUND);
        builder.setOnlyAlertOnce(true);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationHelper.Priority.LOW.getBelow24());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Hidden camera detector.");
        builder.setContentText(String.format("Intruder service .", getString(R.string.app_name)));
        builder.setOngoing(true);
        builder.setShowWhen(false);
        builder.setGroup(ID_FOREGROUND);
        builder.setGroupSummary(false);
        if (Build.VERSION.SDK_INT >= 31) {
            builder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE));
        } else {
            builder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
        }
        if (Build.VERSION.SDK_INT >= 26) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(getPackageName() + "_" + CameraService.ID_FOREGROUND, "Background Service", NotificationHelper.Priority.LOW.getAboveAnd24());
            if (CameraService.ID_FOREGROUND.equals(ID_FOREGROUND)) {
                notificationChannel.setShowBadge(false);
            }
            NotificationManager notificationManager = this.notificationManager;
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
            builder.setChannelId(getPackageName() + "_" + CameraService.ID_FOREGROUND);
        }
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle(builder);
        inboxStyle.addLine(String.format("Press to open %s.", getString(R.string.app_name)));
        inboxStyle.addLine("Long press to hide this notification.");
        return inboxStyle.build();
    }
}
