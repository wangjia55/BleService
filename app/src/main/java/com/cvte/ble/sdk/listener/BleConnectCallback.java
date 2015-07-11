package com.cvte.ble.sdk.listener;

import android.bluetooth.BluetoothDevice;

import com.cvte.ble.sdk.entity.BleConnectInfo;

/**
 * Package : com.cvte.ble.sdk.entity
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是蓝牙连接状态的回调接口
 */
public interface BleConnectCallback {

    /**
     * 蓝牙连接成功
     *
     * @param bleConnectInfo 连接上的某个指定蓝牙设备
     */
    void onConnectSuccess(BleConnectInfo bleConnectInfo, BluetoothDevice bluetoothDevice);

    /**
     * 蓝牙链接失败
     *
     * @param bleConnectInfo 连接上的某个指定蓝牙设备
     */
    void onConnectError(BleConnectInfo bleConnectInfo, int errorCode, String reason);

    /**
     * 发现需要连接的指定的蓝牙设备
     *
     * @param bleConnectInfo 连接上的某个指定蓝牙设备
     */
    void onDeviceFound(BleConnectInfo bleConnectInfo, BluetoothDevice bluetoothDevice);

}
