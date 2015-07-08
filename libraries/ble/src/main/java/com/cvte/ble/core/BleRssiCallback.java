package com.cvte.ble.core;

/**
 * Created by jianhaohong on 2/11/15.
 */
public interface BleRssiCallback {
    public void onBleRssiRead(int rssi);

    public void onBleRssiReadError(int error, String message);
}