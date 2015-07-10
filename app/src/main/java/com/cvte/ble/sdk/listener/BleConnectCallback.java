package com.cvte.ble.sdk.listener;

import android.bluetooth.BluetoothDevice;

import com.cvte.ble.sdk.entity.BleConnectInfo;

/**
 * Created by jianhaohong on 10/23/14.
 */
public interface BleConnectCallback {

     void onConnectSuccess(BluetoothDevice bluetoothDevice,BleConnectInfo bleConnectInfo);

     void onDeviceFound(BluetoothDevice bluetoothDevice,BleConnectInfo bleConnectInfo);

     void onConnectError(int errorCode, String reason,BleConnectInfo bleConnectInfo);

}
