package com.cvte.ble.sdk.entity;

import java.io.Serializable;

/**
 * Package : com.jacob.ble.bean
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是用来发送蓝牙状态信息（通过Event发送给UI层）
 */
public class EventBleDevice implements Serializable {

    public enum BleState {
        DEVICE_FOUND,
        DISCONNECT,
        CONNECTED,
    }

    /**
     * 当前状态
     */
    private BleState bleState;

    /**
     * 指定的蓝牙设备
     */
    private BleConnectInfo bleConnectInfo;

    public EventBleDevice(BleState bleState, BleConnectInfo bleConnectInfo) {
        this.bleState = bleState;
        this.bleConnectInfo = bleConnectInfo;
    }

    public BleState getBleState() {
        return bleState;
    }

    public BleConnectInfo getBleConnectInfo() {
        return bleConnectInfo;
    }
}
