package com.cvte.ble.sdk.listener;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * Created by jianhaohong on 10/28/14.
 */
public interface BleDeviceFoundCallback {
    public void onManyDeviceFound(List<BluetoothDevice> bluetoothDevices);

    public void onOneDeviceFound(BluetoothDevice bluetoothDevices);

    public void onDeviceConnected(BluetoothDevice bluetoothDevice);

    public void onDeviceNoFound();

    public void onError(int errorCode, String message);
}
