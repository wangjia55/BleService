package com.cvte.ble.sdk.listener;

/**
 * Created by jianhaohong on 2/11/15.
 */
public interface BleRssiCallback {
     void onBleRssiRead(int rssi);

     void onBleRssiReadError(int error, String message);
}
