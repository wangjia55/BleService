package com.cvte.ble.sdk.entity;

import android.content.Context;

import com.cvte.ble.sdk.core.GoogleBle;
import com.cvte.ble.sdk.states.ConnectState;

/**
 * Package : com.cvte.ble.sdk.entity
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类封装了需要sdk连接设备的必要信息
 */
public class BleConnectDevice {

    /**
     * 唯一的标识：因为需要满足同时连接多个设备，所以不同设备之间需要通过tag来区别
     */
    private String singleTag;

    /**
     * 每个设备含有一个独立的GoogleBle对象，这个对象中包含该设备的操作和蓝牙状态
     */
    private GoogleBle googleBle;

    /**
     * 连接蓝牙提供的必要信息
     */
    private BleConnectInfo bleConnectInfo;

    /**
     * 上次的连接状态
     */
    private ConnectState mLastConnectState = ConnectState.Disconnect;


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

    public ConnectState getLastConnectState() {
        return mLastConnectState;
    }

    public void recordLastConnectState() {
        mLastConnectState = getConnectState();
    }

    @Override
    public String toString() {
        return "BleConnectDevice{" +
                "singleTag='" + singleTag + '\'' +
                ", mLastConnectState=" + mLastConnectState +
                '}';
    }
}
