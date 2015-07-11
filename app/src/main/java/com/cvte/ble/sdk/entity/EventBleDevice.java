package com.cvte.ble.sdk.entity;

import java.io.Serializable;

/**
 * Package : com.jacob.ble.bean
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是用来发送蓝牙状态信息（通过Event发送给UI层）
 */
public class EventBleDevice implements Serializable{

    public  enum BleState {
        DEVICE_FOUND,
        DISCONNECT,
        CONNECTED,
    }

    private BleState bleState;
    private String imsi;

    public EventBleDevice(BleState bleState, String imsi) {
        this.bleState = bleState;
        this.imsi = imsi;
    }

    public BleState getBleState() {
        return bleState;
    }

    public String getImsi() {
        return imsi;
    }

}
