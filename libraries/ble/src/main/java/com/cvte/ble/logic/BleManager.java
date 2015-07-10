package com.cvte.ble.logic;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.cvte.ble.core.BleConnectCallback;
import com.cvte.ble.core.BleConnectInfo;
import com.cvte.ble.core.BleRssiCallback;
import com.cvte.ble.core.BleScanCallback;
import com.cvte.ble.core.BleWriteCallback;
import com.cvte.ble.core.ConnectState;
import com.cvte.ble.core.GoogleBle;
import com.cvte.ble.core.ScanState;
import com.cvte.ble.utils.LogUtils;

/**
 * Created by jianhaohong on 10/28/14.
 */
public class BleManager {
    private GoogleBle mGoogleBle = new GoogleBle();
    private BleConnectCallback mBleConnectCallback;
    private BleConnectInfo mBleConnectInfo;
    private boolean mIsAuto;
    private ScanType mCurrentScanType;
    private ConnectState mPreviousConnectState = ConnectState.Disconnect;

    private BleScanCallback mBleScanCallback = new BleScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            switch (mCurrentScanType) {
                case DEVICE:
                    connectIfDeviceFound(device, scanRecord);
                    break;
            }
        }

        @Override
        public void onError(int errorCode, String message) {
            mGoogleBle.stopScan();
            switch (mCurrentScanType) {
                case DEVICE:
                    if (mBleConnectCallback != null) {
                        mBleConnectCallback.onError(errorCode, message);
                    }
                    break;
            }
        }
    };


    private void connectIfDeviceFound(BluetoothDevice device, byte[] broadcast) {
        LogUtils.LOGD("device find ", " " + device.getName());
        if (mBleConnectInfo != null && mBleConnectInfo.shouldConnectDevice(device, broadcast)) {
            mBleConnectCallback.onDeviceFound(device);
            LogUtils.LOGD("device connect ", " " + device.getName());
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
        if (mGoogleBle.getScanState() != ScanState.Scanning &&
                mGoogleBle.getConnectState() == ConnectState.Disconnect) {
            mCurrentScanType = ScanType.DEVICE;
            mIsAuto = isAuto;
            mBleConnectInfo = bleConnectInfo;
            mGoogleBle.startScan(mBleScanCallback);
            LogUtils.LOGD("bleManager","start scan");
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


    public void stopScan() {
        mGoogleBle.stopScan();
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

    public ConnectState getPreviousConnectState() {
        return mPreviousConnectState;
    }

    public void setPreviousConnectState(ConnectState previousConnectState) {
        this.mPreviousConnectState = previousConnectState;
    }

    public ScanState getCurrentScanState() {
        return mGoogleBle.getScanState();
    }
}
