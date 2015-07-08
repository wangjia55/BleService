package com.cvte.ble.core;

import android.bluetooth.BluetoothDevice;

/**
 * Created by jianhaohong on 10/23/14.
 */
public interface BleConnectCallback {

    public void onConnectSuccess(BluetoothDevice bluetoothDevice);

    public void onDeviceFound(BluetoothDevice bluetoothDevice);

    public void onError(int errorCode, String reason);

}
