package com.cvte.ble.sdk.core;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.cvte.ble.sdk.entity.BleConnectInfo;
import com.cvte.ble.sdk.listener.BleConnectCallback;
import com.cvte.ble.sdk.listener.BleRssiCallback;
import com.cvte.ble.sdk.listener.BleScanCallback;
import com.cvte.ble.sdk.listener.BleWriteCallback;
import com.cvte.ble.sdk.states.ConnectState;
import com.cvte.ble.sdk.utils.BleLogUtils;

/**
 * Created by jianhaohong on 10/28/14.
 */
public class BleManager {
    private GoogleBle mGoogleBle = new GoogleBle();
    private BleConnectCallback mBleConnectCallback;
    private BleConnectInfo mBleConnectInfo;
    private boolean mIsAuto;

    private BleScanCallback mBleScanCallback = new BleScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            connectIfDeviceFound(device, scanRecord);
        }

        @Override
        public void onError(int errorCode, String message) {
            mGoogleBle.stopScan();
            if (mBleConnectCallback != null) {
                mBleConnectCallback.onError(errorCode, message);
            }
        }
    };


    private void connectIfDeviceFound(BluetoothDevice device, byte[] broadcast) {
        BleLogUtils.LOGD("device find ", " " + device.getName());
        if (mBleConnectInfo != null && mBleConnectInfo.shouldConnectDevice(device, broadcast)) {
            mBleConnectCallback.onDeviceFound(device);
            BleLogUtils.LOGD("device connect ", " " + device.getName());
            mGoogleBle.stopScan();
            mGoogleBle.connect(device, mBleConnectInfo, mIsAuto);
        }
    }


    public BleManager(Context context) {
            mGoogleBle.init(context);
    }

    public void dispose() {
        mGoogleBle.dispose();
    }


    public void connectDevice(BleConnectInfo bleConnectInfo, boolean isAuto) {
        if (mGoogleBle.getConnectState() == ConnectState.Disconnect) {
            mIsAuto = isAuto;
            mBleConnectInfo = bleConnectInfo;
            mGoogleBle.startScan(mBleScanCallback);
            BleLogUtils.LOGD("bleManager", "start scan");
        }
    }

    public void setConnectCallback(BleConnectCallback bleConnectCallback) {
        mBleConnectCallback = bleConnectCallback;
        mGoogleBle.setConnectCallback(mBleConnectCallback);
    }

    public void setBleWriteDataCallback(BleWriteCallback bleWriteDataCallback) {
        mGoogleBle.setBleWriteCallback(bleWriteDataCallback);
    }


    public void writeToDevice(byte[] bytes) {
        mGoogleBle.write(bytes);
    }

    public void readRssi() {
        mGoogleBle.readRssi();
    }



    public void disconnect() {
        mGoogleBle.disconnect();
    }


    public ConnectState getCurrentConnectState() {
        return mGoogleBle.getConnectState();
    }

    public void setBleRssiCallback(BleRssiCallback bleRssiCallback) {
        mGoogleBle.setBleRssiCallback(bleRssiCallback);
    }


}
