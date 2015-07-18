package com.jacob.ble;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.cvte.ble.sdk.core.BleSdkManager;
import com.cvte.ble.sdk.entity.BleConnectDevice;
import com.cvte.ble.sdk.listener.BleDeviceChangeListener;
import com.google.gson.Gson;
import com.jacob.ble.bean.BleDeviceBean;
import com.jacob.ble.ui.MainActivity;
import com.jacob.ble.ui.TrackerConnectInfo;
import com.jacob.ble.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private BleSdkManager mBleSdkManager = BleSdkManager.newInstance(this);

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
        if (!isRunning) {
            mBleSdkManager.init();
            mBleSdkManager.registerBleStateReceiver();
            mBleSdkManager.setOnDeviceSizeChangeListener(sizeChangeListener);
            startForeground(10, getNotification());
            isRunning = true;

            mBackgroundService = Executors.newSingleThreadScheduledExecutor();
            mBackgroundService.scheduleWithFixedDelay(new TimerRunnable(), 0, 20, TimeUnit.SECONDS);

            reloadBleService();
        }

        return START_STICKY;
    }

    private BleDeviceChangeListener sizeChangeListener = new BleDeviceChangeListener() {
        @Override
        public void onDeviceSizeChange(Map<String, BleConnectDevice> allDeviceMap) {

            JSONArray jsonArray = new JSONArray();
            Set<String> keySet = allDeviceMap.keySet();
            for (String tag : keySet) {
                BleConnectDevice connectDevice = allDeviceMap.get(tag);
                BleDeviceBean bleDevice = new BleDeviceBean();
                bleDevice.setImei(connectDevice.getBleConnectInfo().getVerifyCommand());
                bleDevice.setImsi(connectDevice.getBleConnectInfo().getSingleTag());
                jsonArray.put(new Gson().toJson(bleDevice));
            }
            mBleSdkManager.setCacheDeviceInfo(jsonArray.toString());
        }
    };

    private void reloadBleService() {
        String bleInfo = mBleSdkManager.reloadCacheDeviceInfo();
        LogUtils.LOGE("bleinfo:", bleInfo);
        if ("".equals(bleInfo)) {
            return;
        }

        int size = mBleSdkManager.getDeviceSize();
        List<BleDeviceBean> bleDeviceList = new ArrayList<>();
        if (size == 0) {
            try {
                Gson gson = new Gson();
                JSONArray jsonArray = new JSONArray(bleInfo);
                int length = jsonArray.length();
                for (int i = 0; i < length; i++) {
                    BleDeviceBean bleDevice = gson.fromJson(jsonArray.getString(i), BleDeviceBean.class);
                    bleDeviceList.add(bleDevice);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            for (BleDeviceBean bleDevice : bleDeviceList) {
                TrackerConnectInfo connectInfo = new TrackerConnectInfo(bleDevice.getImsi(), bleDevice.getImei());
                BleSdkManager.newInstance(getApplicationContext()).connectBleDevice(connectInfo);
            }
        }


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
        mBleSdkManager.unregisterBleStateReceiver();
        Intent service = new Intent(this, BleService.class);
        this.startService(service);
    }

    private class TimerRunnable implements Runnable {
        @Override
        public void run() {
            LogUtils.LOGE(TAG, "---service is alive ---");
        }
    }
}
