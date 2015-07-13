package com.cvte.ble.sdk.listener;

import com.cvte.ble.sdk.entity.BleConnectDevice;
import com.cvte.ble.sdk.entity.BleConnectInfo;
import com.cvte.ble.sdk.states.BluetoothState;
import com.cvte.ble.sdk.states.ConnectState;
import com.cvte.ble.sdk.states.ScanState;

import java.util.Map;

/**
 * Package : com.cvte.ble.sdk.listener
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是蓝牙操作对外提供的接口
 */
public interface BleOperationListener {

    /**
     * 初始化蓝牙
     */
    void init();

    /**
     * 开始扫描
     */
    void startScan();

    /**
     * 停止扫描
     */
    void stopScan();

    /**
     * 清除所有缓存的设备信息，一般用于退出蓝牙操作
     */
    void clearAll();

    /**
     * 获取当前蓝牙的状态
     */
    BluetoothState getBluetoothState();

    /**
     * 获取当前扫描蓝牙的状态
     */
    ScanState getScanState();

    /**
     * 获取当前队列中的设备个数
     */
    int getDeviceSize();

    /**
     *  获取当前队列中的设备信息
     */
    Map<String, BleConnectDevice> getDeviceMap();

    /**
     * 注册蓝牙状态广播
     */
    void registerBleStateReceiver();

    /**
     * 取消注册蓝牙状态广播
     */
    void unregisterBleStateReceiver();

    /**
     * 连接某个指定的蓝牙设备,支持多个设备同时连接
     */
    void connectBleDevice(BleConnectInfo bleConnectInfo);

    /**
     * 取消连接某个蓝牙设备
     */
    void disConnectBleDevice(BleConnectInfo bleConnectInfo);

    /**
     * 获取某个指定设备当前的连接状态
     */
    ConnectState getDeviceState(BleConnectInfo bleConnectInfo);
}
