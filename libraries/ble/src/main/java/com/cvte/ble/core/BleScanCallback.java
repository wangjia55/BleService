package com.cvte.ble.core;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

/**
 * Created by jianhaohong on 10/22/14.
 */
@SuppressLint("NewApi")
public interface BleScanCallback extends BluetoothAdapter.LeScanCallback {
    @Override
    void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord);

    void onError(int errorCode, String message);
}
