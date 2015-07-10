package com.cvte.ble.sdk.listener;

import com.cvte.ble.sdk.entity.BleConnectInfo;
import com.cvte.ble.sdk.states.BluetoothState;
import com.cvte.ble.sdk.states.ScanState;

/**
 * Package : com.cvte.ble.sdk.listener
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是用来xxx
 */
public interface BleOperationListener {

    void init();

    void startScan();

    void stopScan();

    BluetoothState getBluetoothState();

    ScanState getScanState();

    void registerBleStateReceiver() ;

    void unregisterBleStateReceiver() ;

    void connectBleDevice(BleConnectInfo bleConnectInfo);

    void disConnectBleDevice(BleConnectInfo bleConnectInfo);
}
