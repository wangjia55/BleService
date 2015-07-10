package com.cvte.ble.sdk.entity;

import android.content.Context;

import com.cvte.ble.sdk.core.BleManager;
import com.cvte.ble.sdk.states.ConnectState;

/**
 * Package : com.cvte.ble.sdk.entity
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是用来xxx
 */
public class BleConnectDevice {
    private String singleTag;

    private BleManager bleManager;

    private BleConnectInfo bleConnectInfo;


    public BleConnectDevice(Context context, BleConnectInfo bleConnectInfo) {
        singleTag = bleConnectInfo.getSingleTag();
        bleManager = new BleManager(context);
        this.bleConnectInfo = bleConnectInfo;
    }

    public String getSingleTag() {
        return singleTag;
    }

    public BleManager getBleManager() {
        return bleManager;
    }

    public BleConnectInfo getBleConnectInfo() {
        return bleConnectInfo;
    }

    public ConnectState getConnectState() {
        return bleManager.getCurrentConnectState();
    }
}
