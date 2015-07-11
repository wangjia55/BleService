package com.cvte.ble.sdk.listener;

/**
 * Package : com.cvte.ble.sdk.listener
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是蓝牙信号强度的接口
 */
public interface BleRssiCallback {
     void onBleRssiRead(int rssi);

     void onBleRssiReadError(int error, String message);
}
