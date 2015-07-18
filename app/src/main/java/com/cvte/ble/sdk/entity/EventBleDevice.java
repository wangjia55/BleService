package com.cvte.ble.sdk.entity;

import java.io.Serializable;

/**
 * Package : com.jacob.ble.bean
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是用来发送蓝牙状态信息（通过Event发送给UI层）
 */
public class EventBleDevice implements Serializable {

    public static final int DEVICE_FOUND = 1;
    public static final int CONNECTED = 2;
    public static final int DISCONNECT = 3;
    public static final int BLUETOOTH_OFF = 4;
    public static final int BLUETOOTH_ON = 5;

    /**
     * 当前状态
     */
    private int bleState;

    /**
     * 指定的蓝牙设备
     */
    private BleConnectInfo bleConnectInfo;

    public EventBleDevice(int bleState, BleConnectInfo bleConnectInfo) {
        this.bleState = bleState;
        this.bleConnectInfo = bleConnectInfo;
    }

    public int getBleState() {
        return bleState;
    }

    public BleConnectInfo getBleConnectInfo() {
        return bleConnectInfo;
    }
}
