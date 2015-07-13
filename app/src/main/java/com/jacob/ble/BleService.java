package com.jacob.ble;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.cvte.ble.sdk.core.BleSdkManager;
import com.jacob.ble.ui.MainActivity;
import com.jacob.ble.utils.LogUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Package : com.jacob.ble
 * Author : jacob
 * Date : 15-7-9
 * Description : 这个类是用来xxx
 */
public class BleService extends Service {
    public static boolean isRunning = false;
    public static final String TAG = "BleService";
    private ScheduledExecutorService mBackgroundService;
    private BleSdkManager bleSdkManager = BleSdkManager.newInstance(this);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.LOGE(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.LOGE(TAG, "onStartCommand");
        if (!isRunning){
            bleSdkManager.init();
            bleSdkManager.registerBleStateReceiver();
            startForeground(10, getNotification());
            isRunning = true;

            mBackgroundService = Executors.newSingleThreadScheduledExecutor();
            mBackgroundService.scheduleWithFixedDelay(new TimerRunnable(), 0, 15, TimeUnit.SECONDS);
        }

        return START_STICKY;
    }


    private Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("儿童定位宝")
                .setContentText("正在蓝牙守护中")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(false);
        Intent intent1 = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent1);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        return builder.build();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.LOGE(TAG, "onDestroy");
        stopForeground(true);
        isRunning = false;
        bleSdkManager.unregisterBleStateReceiver();
//        Intent service = new Intent(this, BleService.class);
//        this.startService(service);
    }

    private class TimerRunnable implements Runnable {
        @Override
        public void run() {
            LogUtils.LOGE(TAG,"---service is alive ---");
        }
    }
}
