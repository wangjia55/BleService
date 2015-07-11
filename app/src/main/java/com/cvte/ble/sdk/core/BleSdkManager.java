package com.cvte.ble.sdk.core;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import com.cvte.ble.sdk.entity.BleConnectDevice;
import com.cvte.ble.sdk.entity.BleConnectInfo;
import com.cvte.ble.sdk.entity.EventBleDevice;
import com.cvte.ble.sdk.listener.BleConnectCallback;
import com.cvte.ble.sdk.listener.BleOperationListener;
import com.cvte.ble.sdk.states.BluetoothState;
import com.cvte.ble.sdk.states.ConnectState;
import com.cvte.ble.sdk.states.ScanState;
import com.cvte.ble.sdk.utils.BleLogUtils;
import com.jacob.ble.ui.BleAlertActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * Package : com.ble.sdk.core
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是整个sdk对外唯一的入口类（使用单例模式）
 */
public class BleSdkManager implements BleOperationListener {
    public static final String TAG = "BleSdkManager";

    private static BleSdkManager sInstance = null;
    private ScanState mScanState = ScanState.ScanStop;
    private BluetoothState mBluetoothState = BluetoothState.Bluetooth_Off;
    private Map<String, BleConnectDevice> mAllDeviceMap = new HashMap<>();

    private Context mContext;
    private static Handler sHandler;
    private static HandlerThread sHandlerThread = new HandlerThread("BleSDKThread");
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;


    private BleSdkManager(Context context) {
        mContext = context;
    }

    public static BleSdkManager newInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BleSdkManager(context);
            sHandlerThread.start();
            sHandler = new Handler(sHandlerThread.getLooper());
        }
        return sInstance;
    }

    private BroadcastReceiver mBlueStateBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            switch (blueState) {
                case BluetoothAdapter.STATE_ON:
                    BleLogUtils.LOGE(TAG, "Bluetooth_On");
                    mBluetoothState = BluetoothState.Bluetooth_On;
                    break;
                case BluetoothAdapter.STATE_OFF:
                    BleLogUtils.LOGE(TAG, "Bluetooth_Off");
                    mScanState = ScanState.ScanStop;
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


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void init() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();
            if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
                mBluetoothState = BluetoothState.Bluetooth_On;
            } else {
                mBluetoothState = BluetoothState.Bluetooth_Off;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void startScan() {
        if (isNeedStartScan()) {
            BleLogUtils.LOGE(TAG, "startScan");
            mScanState = ScanState.Scanning;
            if (mBluetoothAdapter != null &&
                    mBluetoothState == BluetoothState.Bluetooth_On) {
                mBluetoothAdapter.startLeScan(mBleScanCallback);
            }
        }
    }

    private boolean isNeedStartScan() {
        int size = mAllDeviceMap.size();
        boolean hasConnect = true;
        Set<String> keySet = mAllDeviceMap.keySet();
        for (String tag : keySet) {
            if (mAllDeviceMap.get(tag).getGoogleBle().getConnectState() == ConnectState.Disconnect) {
                hasConnect = false;
                break;
            }
        }
        BleLogUtils.LOGE(TAG, "map size:" + size + "/**/" + hasConnect);
        return size > 0 && !hasConnect;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void stopScan() {
        BleLogUtils.LOGE(TAG, "stopScan");
        mScanState = ScanState.ScanStop;
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.stopLeScan(mBleScanCallback);
        }
    }

    @Override
    public void connectBleDevice(BleConnectInfo bleConnectInfo) {
        BleLogUtils.LOGE(TAG, "connectBleDevice");
        BleConnectDevice bleConnectDevice = new BleConnectDevice(mContext, bleConnectInfo);
        mAllDeviceMap.put(bleConnectDevice.getSingleTag(), bleConnectDevice);
        sHandler.removeCallbacks(mScanDeviceRunnable);
        sHandler.post(mScanDeviceRunnable);
    }

    @Override
    public void disConnectBleDevice(BleConnectInfo bleConnectInfo) {
        BleLogUtils.LOGE(TAG, "disConnectBleDevice");
        BleConnectDevice bleConnectDevice = mAllDeviceMap.get(bleConnectInfo.getSingleTag());
        if (bleConnectDevice != null) {
            bleConnectDevice.getGoogleBle().dispose();
        }
        mAllDeviceMap.remove(bleConnectInfo.getSingleTag());
    }

    @Override
    public ConnectState getDeviceState(BleConnectInfo bleConnectInfo) {
        if (bleConnectInfo == null) return ConnectState.Disconnect;

        BleConnectDevice bleConnectDevice = mAllDeviceMap.get(bleConnectInfo.getSingleTag());
        if (bleConnectDevice != null) {
            return bleConnectDevice.getConnectState();
        }
        return ConnectState.Disconnect;
    }

    private BluetoothAdapter.LeScanCallback mBleScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            BleLogUtils.LOGE(TAG, "onLeScan:" + device.getName());
            Set<String> keySet = mAllDeviceMap.keySet();
            for (String tag : keySet) {
                BleConnectDevice bleConnectDevice = mAllDeviceMap.get(tag);
                if (bleConnectDevice.getConnectState() == ConnectState.Connecting) {
                    return;
                }

                if (bleConnectDevice.getConnectState() == ConnectState.Disconnect
                        && bleConnectDevice.getBleConnectInfo().shouldConnectDevice(device, scanRecord)) {
                    BleLogUtils.LOGE(TAG, "found device:" + bleConnectDevice.getSingleTag());
                    mBleConnectCallBack.onDeviceFound(bleConnectDevice.getBleConnectInfo(), device);
                    bleConnectDevice.getGoogleBle().connect(device, bleConnectDevice.getBleConnectInfo(),
                            mBleConnectCallBack, false);
                    return;
                }
            }
        }

    };

    /**
     * 蓝牙连接状态的回调函数，当接收到回调信息后直接通过EventBus发出信息
     * UI层只需要接收消息即可
     */
    private BleConnectCallback mBleConnectCallBack = new BleConnectCallback() {
        @Override
        public void onConnectSuccess(BleConnectInfo bleConnectInfo, BluetoothDevice bluetoothDevice) {
            BleLogUtils.LOGE(TAG, "onConnectSuccess--:" + bleConnectInfo.getSingleTag());
            BleConnectDevice bleConnectDevice = mAllDeviceMap.get(bleConnectInfo.getSingleTag());
            if (bleConnectDevice != null) {
                bleConnectDevice.getGoogleBle().write(BleCommand.getVerifyCommand(bleConnectInfo.getVerifyCommand()));
            }
            EventBleDevice eventBleDevice = new EventBleDevice(EventBleDevice.BleState.CONNECTED, bleConnectInfo);
            EventBus.getDefault().post(eventBleDevice);
            startAlertActivity(eventBleDevice);
        }

        @Override
        public void onDeviceFound(BleConnectInfo bleConnectInfo, BluetoothDevice bluetoothDevice) {
            BleLogUtils.LOGE(TAG, "onDeviceFound--:" + bleConnectInfo.getSingleTag());
            EventBleDevice eventBleDevice = new EventBleDevice(EventBleDevice.BleState.DEVICE_FOUND, bleConnectInfo);
            EventBus.getDefault().post(eventBleDevice);
        }

        @Override
        public void onConnectError(BleConnectInfo bleConnectInfo, int errorCode, String reason) {
            BleLogUtils.LOGE(TAG, "onConnectError--:" + bleConnectInfo.getSingleTag() + "//" + reason);
            EventBleDevice eventBleDevice = new EventBleDevice(EventBleDevice.BleState.DISCONNECT, bleConnectInfo);
            EventBus.getDefault().post(eventBleDevice);
            startAlertActivity(eventBleDevice);
        }

    };


    private void startAlertActivity(EventBleDevice eventBleDevice) {
        Intent intent = new Intent(mContext, BleAlertActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putSerializable("eventDevice", eventBleDevice);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    private Runnable mScanDeviceRunnable = new Runnable() {
        @Override
        public void run() {
            stopScan();
            startScan();
            sHandler.postDelayed(mScanDeviceRunnable, 10 * 1000);
        }
    };

    @Override
    public BluetoothState getBluetoothState() {
        return mBluetoothState;
    }

    @Override
    public ScanState getScanState() {
        return mScanState;
    }

    @Override
    public void clearAll() {
        Set<String> keySet = mAllDeviceMap.keySet();
        for (String tag : keySet) {
            mAllDeviceMap.get(tag).getGoogleBle().dispose();
        }
        mAllDeviceMap.clear();
    }

}
