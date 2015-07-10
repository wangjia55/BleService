package com.cvte.ble.sdk.entity;

import android.content.Context;

import com.cvte.ble.sdk.core.GoogleBle;
import com.cvte.ble.sdk.states.ConnectState;

/**
 * Package : com.cvte.ble.sdk.entity
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是用来xxx
 */
public class BleConnectDevice {
    private String singleTag;

    private GoogleBle googleBle;

    private BleConnectInfo bleConnectInfo;


    public BleConnectDevice(Context context, BleConnectInfo bleConnectInfo) {
        singleTag = bleConnectInfo.getSingleTag();
        googleBle = new GoogleBle(context);
        this.bleConnectInfo = bleConnectInfo;
    }

    public String getSingleTag() {
        return singleTag;
    }

    public GoogleBle getGoogleBle() {
        return googleBle;
    }

    public BleConnectInfo getBleConnectInfo() {
        return bleConnectInfo;
    }

    public ConnectState getConnectState() {
        return googleBle.getConnectState();
    }
}
