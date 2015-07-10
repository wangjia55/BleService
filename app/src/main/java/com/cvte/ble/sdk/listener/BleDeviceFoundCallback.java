package com.cvte.ble.sdk.listener;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * Created by jianhaohong on 10/28/14.
 */
public interface BleDeviceFoundCallback {
     void onManyDeviceFound(List<BluetoothDevice> bluetoothDevices);

     void onOneDeviceFound(BluetoothDevice bluetoothDevices);

     void onDeviceConnected(BluetoothDevice bluetoothDevice);

     void onDeviceNoFound();

     void onError(int errorCode, String message);
}
