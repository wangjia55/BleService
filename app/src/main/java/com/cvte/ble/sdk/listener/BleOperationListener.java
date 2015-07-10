package com.cvte.ble.sdk.listener;

import com.cvte.ble.sdk.entity.BleConnectDevice;

/**
 * Package : com.cvte.ble.sdk.listener
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是用来xxx
 */
public interface BleOperationListener {

    void startScan();

    void stopScan();

    boolean isSupportBle();

    void openBluetooth();

    void closeBluetooth();

    void registerBleStateReceiver() ;

    void unregisterBleStateReceiver() ;

    void connectBleDevice(BleConnectDevice connectDevice);

    void disConnectBleDevice(BleConnectDevice connectDevice);
}
