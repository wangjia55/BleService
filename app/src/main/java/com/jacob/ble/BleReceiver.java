package com.jacob.ble;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Package : com.jacob.ble
 * Author : jacob
 * Date : 15-7-9
 * Description : 这个类是用来xxx
 */
public class BleReceiver extends BroadcastReceiver {
    public static final String TAG = "BleReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            LogUtils.LOGE(TAG, "手机开机了....");
            startBleService(context);
        }

        if (Intent.ACTION_USER_PRESENT.equals(action)) {
            LogUtils.LOGE(TAG, "手机解锁了....");
            startBleService(context);
        }

        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            LogUtils.LOGE(TAG, "蓝牙状态发生变化了....");
            startBleService(context);
        }

    }

    private void startBleService(Context context) {
        Intent intent = new Intent(context, BleService.class);
        context.startService(intent);
    }
}
