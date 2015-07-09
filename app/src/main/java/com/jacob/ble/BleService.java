package com.jacob.ble;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * Package : com.jacob.ble
 * Author : jacob
 * Date : 15-7-9
 * Description : 这个类是用来xxx
 */
public class BleService extends Service {
    public static final String TAG = "BleService";
    private Handler mHandler = new Handler();
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
        LogUtils.LOGE(TAG,"onStartCommand");
        flags = START_REDELIVER_INTENT;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.LOGE(TAG, "----service is running----");
                mHandler.postDelayed(this, 5000);
            }
        }, 5000);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("标题1")
                .setContentTitle("儿童定位宝")
                .setContentText("正在蓝牙守护中")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(false);
        Intent intent1 = new Intent(this,MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent1);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        startForeground(10, builder.build());
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.LOGE(TAG, "onDestroy");
        stopForeground(true);

        Intent service = new Intent(this, BleService.class);
        this.startService(service);
    }
}
