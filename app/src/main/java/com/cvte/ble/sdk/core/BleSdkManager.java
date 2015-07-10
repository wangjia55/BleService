package com.cvte.ble.sdk.core;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.cvte.ble.sdk.entity.BleConnectDevice;
import com.cvte.ble.sdk.entity.BleConnectInfo;
import com.cvte.ble.sdk.listener.BleOperationListener;
import com.cvte.ble.sdk.states.BluetoothState;
import com.cvte.ble.sdk.states.ScanState;
import com.cvte.ble.sdk.utils.BleUtils;

/**
 * Package : com.ble.sdk.core
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是整个sdk对外唯一的入口类
 */
public class BleSdkManager implements BleOperationListener {

    private Context mContext;

    private ScanState mScanState = ScanState.ScanStop;

    private BluetoothState mBluetoothState = BluetoothState.Bluetooth_Off;



    private static BleSdkManager sInstance = null;

    private BleSdkManager(Context context) {
        mContext = context;
    }

    public static BleSdkManager newInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BleSdkManager(context);
        }
        return sInstance;
    }

    private BroadcastReceiver mBlueStateBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            switch (blueState) {
                case BluetoothAdapter.STATE_ON:
                    mBluetoothState = BluetoothState.Bluetooth_On;
                    break;
                case BluetoothAdapter.STATE_OFF:
                    mBluetoothState = BluetoothState.Bluetooth_Off;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void registerBleStateReceiver() {
        if (mContext != null) {
            mContext.registerReceiver(mBlueStateBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        }
    }

    @Override
    public void unregisterBleStateReceiver() {
        if (mContext != null) {
            mContext.unregisterReceiver(mBlueStateBroadcastReceiver);
        }
    }


    @Override
    public void startScan() {
        mScanState = ScanState.Scanning;
    }

    @Override
    public void stopScan() {
        mScanState = ScanState.ScanStop;
    }

    @Override
    public boolean isSupportBle() {
        return BleUtils.isBleSupported(mContext);
    }

    @Override
    public void openBluetooth() {

    }

    @Override
    public void closeBluetooth() {
    }

    @Override
    public BluetoothState getBluetoothState() {
        return mBluetoothState;
    }

    @Override
    public ScanState getScanState() {
        return mScanState;
    }

    @Override
    public void connectBleDevice(BleConnectInfo bleConnectInfo) {
        BleConnectDevice bleConnectDevice = new BleConnectDevice(mContext,bleConnectInfo);


    }

    @Override
    public void disConnectBleDevice(BleConnectInfo bleConnectInfo) {

    }


}
