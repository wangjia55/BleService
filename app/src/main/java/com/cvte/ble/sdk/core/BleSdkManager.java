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
import android.os.Looper;
import android.os.Message;

import com.cvte.ble.sdk.entity.BleConnectDevice;
import com.cvte.ble.sdk.entity.BleConnectInfo;
import com.cvte.ble.sdk.entity.EventBleDevice;
import com.cvte.ble.sdk.listener.BleConnectCallback;
import com.cvte.ble.sdk.listener.BleDeviceChangeListener;
import com.cvte.ble.sdk.listener.BleOperationListener;
import com.cvte.ble.sdk.states.BluetoothState;
import com.cvte.ble.sdk.states.ConnectState;
import com.cvte.ble.sdk.states.ScanState;
import com.cvte.ble.sdk.utils.BleLogUtils;
import com.jacob.ble.ui.BleAlertActivity;
import com.jacob.ble.utils.LogUtils;

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
    public static final int MSG_DISCONNECT_AND_RESEARCH = 100;

    private boolean isConnectFinish = true;
    private static BleSdkManager sInstance = null;
    private ScanState mScanState = ScanState.ScanStop;
    private BluetoothState mBluetoothState = BluetoothState.Bluetooth_Off;
    private static Map<String, BleConnectDevice> mAllDeviceMap = new HashMap<>();

    private static Context mContext;
    private static BleSdkHandler sHandler;
    private static HandlerThread sHandlerThread = new HandlerThread("BleSDKThread");
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BleDeviceChangeListener mDeviceSizeChangeListener;


    private BleSdkManager(Context context) {
        mContext = context;
    }

    public static BleSdkManager newInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BleSdkManager(context);
            sHandlerThread.start();
            sHandler = new BleSdkHandler(sHandlerThread.getLooper());
        }
        return sInstance;
    }

    private static class BleSdkHandler extends Handler {
        public BleSdkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_DISCONNECT_AND_RESEARCH:
                    String tag = (String) msg.obj;
                    BleConnectDevice bleConnectDevice = mAllDeviceMap.get(tag);
                    if (bleConnectDevice != null && (bleConnectDevice.getConnectState() == ConnectState.Disconnect)) {
                        //给出提示
                        EventBleDevice eventBleDevice = new EventBleDevice(EventBleDevice.DISCONNECT,
                                bleConnectDevice.getBleConnectInfo());
                        EventBus.getDefault().post(eventBleDevice);

                        //清除当前设备中的记录的连接信息
                        startAlertActivity(eventBleDevice);
                    }
                    break;
            }
        }
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
                    mScanState = ScanState.ScanStop;
                    mBluetoothState = BluetoothState.Bluetooth_Off;
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 注册蓝牙状态广播
     */
    @Override
    public void registerBleStateReceiver() {
        if (mContext != null) {
            mContext.registerReceiver(mBlueStateBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        }
    }

    /**
     * 取消注册蓝牙状态广播
     */
    @Override
    public void unregisterBleStateReceiver() {
        if (mContext != null) {
            mContext.unregisterReceiver(mBlueStateBroadcastReceiver);
        }
    }

    /**
     * 初始化蓝牙,得到当前蓝牙的状态
     */
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

    /**
     * 开始扫描
     */
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

    /**
     * 判断是否需要扫描蓝牙，比如：当前的设备已经全部处于连接上的状态时，是不需要扫描蓝牙的
     */
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
        BleLogUtils.LOGE(TAG, "map size:" + size + "--- hasConnect : " + hasConnect);
        return size > 0 && !hasConnect;
    }

    /**
     * 停止扫描
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void stopScan() {
        BleLogUtils.LOGE(TAG, "stopScan");
        mScanState = ScanState.ScanStop;
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.stopLeScan(mBleScanCallback);
        }
    }

    /**
     * 连接某个指定的蓝牙设备,支持多个设备同时连接
     */
    @Override
    public void connectBleDevice(BleConnectInfo bleConnectInfo) {
        BleLogUtils.LOGE(TAG, "connectBleDevice");
        BleConnectDevice bleConnectDevice = mAllDeviceMap.get(bleConnectInfo.getSingleTag());
        if (bleConnectDevice != null) {
            return;
        } else if (!"".equals(bleConnectInfo.getSingleTag())) {
            bleConnectDevice = new BleConnectDevice(mContext, bleConnectInfo);
            mAllDeviceMap.put(bleConnectDevice.getSingleTag(), bleConnectDevice);
            sHandler.removeCallbacks(mScanDeviceRunnable);
            sHandler.post(mScanDeviceRunnable);
        }
        if (mDeviceSizeChangeListener != null) {
            mDeviceSizeChangeListener.onDeviceSizeChange(mAllDeviceMap);
        }
    }

    /**
     * 取消连接某个蓝牙设备
     */
    @Override
    public void disConnectBleDevice(BleConnectInfo bleConnectInfo) {
        BleLogUtils.LOGE(TAG, "disConnectBleDevice");
        BleConnectDevice bleConnectDevice = mAllDeviceMap.get(bleConnectInfo.getSingleTag());
        mAllDeviceMap.remove(bleConnectInfo.getSingleTag());
        if (bleConnectDevice != null) {
            bleConnectDevice.getGoogleBle().dispose();
        }
        if (mDeviceSizeChangeListener != null) {
            mDeviceSizeChangeListener.onDeviceSizeChange(mAllDeviceMap);
        }
    }

    /**
     * 获取某个指定设备当前的连接状态
     */
    @Override
    public ConnectState getDeviceState(BleConnectInfo bleConnectInfo) {
        if (bleConnectInfo == null) return ConnectState.Disconnect;

        BleConnectDevice bleConnectDevice = mAllDeviceMap.get(bleConnectInfo.getSingleTag());
        if (bleConnectDevice != null) {
            return bleConnectDevice.getConnectState();
        }
        return ConnectState.Disconnect;
    }

    /**
     * 蓝牙扫描设备的回调，在这里为了保证蓝牙连接的稳定性，要求蓝牙是串行连接，即：一个连接成功后再次连接另外一个
     */
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
            //设置连接状态的值
            isConnectFinish = true;
            //重新进行扫描
            sHandler.removeCallbacks(mScanDeviceRunnable);
            sHandler.post(mScanDeviceRunnable);


            //连接成功后，记录BleConnectDevice中的一些数据（是否连接／连接失败次数）
            BleConnectDevice bleConnectDevice = mAllDeviceMap.get(bleConnectInfo.getSingleTag());
            if (bleConnectDevice != null) {
                bleConnectDevice.getGoogleBle().write(BleCommand.getVerifyCommand(bleConnectInfo.getVerifyCommand()));

                EventBleDevice eventBleDevice = new EventBleDevice(EventBleDevice.CONNECTED, bleConnectInfo);
                EventBus.getDefault().post(eventBleDevice);

                LogUtils.LOGE("wangjia:", bleConnectDevice.toString());
                if (bleConnectDevice.getLastConnectState() == ConnectState.Disconnect) {
                    startAlertActivity(eventBleDevice);
                }

                bleConnectDevice.recordLastConnectState();
            }
        }

        @Override
        public void onDeviceFound(BleConnectInfo bleConnectInfo, BluetoothDevice bluetoothDevice) {
            BleLogUtils.LOGE(TAG, "onDeviceFound--:" + bleConnectInfo.getSingleTag());
            isConnectFinish = false;
            stopScan();
            EventBleDevice eventBleDevice = new EventBleDevice(EventBleDevice.DEVICE_FOUND, bleConnectInfo);
            EventBus.getDefault().post(eventBleDevice);
        }

        @Override
        public void onConnectError(BleConnectInfo bleConnectInfo, int errorCode, String reason) {
            //重新进行扫描
            isConnectFinish = true;
            sHandler.removeCallbacks(mScanDeviceRunnable);
            sHandler.post(mScanDeviceRunnable);

            BleLogUtils.LOGE(TAG, "onConnectError--:" + bleConnectInfo.getSingleTag() + "--reason:" + reason);
            BleConnectDevice bleConnectDevice = mAllDeviceMap.get(bleConnectInfo.getSingleTag());
            LogUtils.LOGE("bleConnectDevice==null:", "" + (bleConnectDevice == null));
            if (bleConnectDevice != null) {
                //延迟一定时间再次检查这个设备是否已经连接，如果没有连接给出断开提示
                Message message = sHandler.obtainMessage();
                message.what = MSG_DISCONNECT_AND_RESEARCH;
                message.obj = bleConnectInfo.getSingleTag();
                sHandler.sendMessageDelayed(message, BleSdkConfig.BLE_ALERT_DALEY_WHEN_DISCONNECT);

                LogUtils.LOGE("wangjia:", bleConnectDevice.toString());
                if (bleConnectDevice.getLastConnectState() != ConnectState.Connected) {
                    //给出提示
                    EventBleDevice eventBleDevice = new EventBleDevice(EventBleDevice.DISCONNECT, bleConnectInfo);
                    EventBus.getDefault().post(eventBleDevice);

                    //清除当前设备中的记录的连接信息
                    startAlertActivity(eventBleDevice);
                }

                bleConnectDevice.recordLastConnectState();
            }
        }
    };


    private static void startAlertActivity(EventBleDevice eventBleDevice) {
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
            if (isConnectFinish) {
                stopScan();
                startScan();
                sHandler.postDelayed(mScanDeviceRunnable, BleSdkConfig.SCAN_TIME);
            }
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
    public int getDeviceSize() {
        if (mAllDeviceMap == null) {
            return 0;
        }
        return mAllDeviceMap.size();
    }

    @Override
    public Map<String, BleConnectDevice> getDeviceMap() {
        return mAllDeviceMap;
    }

    @Override
    public void clearAll() {
        stopScan();
        sHandler.removeCallbacks(mScanDeviceRunnable);
        sHandler.removeMessages(MSG_DISCONNECT_AND_RESEARCH);
        Set<String> keySet = mAllDeviceMap.keySet();
        for (String tag : keySet) {
            BleConnectDevice bleConnectDevice = mAllDeviceMap.get(tag);
            if (bleConnectDevice != null) {
                bleConnectDevice.getGoogleBle().dispose();
            }
        }
        mAllDeviceMap.clear();
    }


    public void setOnDeviceSizeChangeListener(BleDeviceChangeListener listener) {
        this.mDeviceSizeChangeListener = listener;
    }

}
