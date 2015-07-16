package com.jacob.ble.ui;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cvte.ble.sdk.core.BleSdkManager;
import com.cvte.ble.sdk.core.Bootstrap;
import com.cvte.ble.sdk.entity.EventBleDevice;
import com.cvte.ble.sdk.states.BluetoothState;
import com.cvte.ble.sdk.utils.BleUtils;
import com.jacob.ble.R;
import com.jacob.ble.bean.BleDevice;
import com.jacob.ble.utils.DataBaseHelper;

import java.util.List;

import de.greenrobot.event.EventBus;


public class MainActivity extends FragmentActivity {
    public static final int REQUEST_ADD_DEVICE = 10010;
    private static final int BLUETOOTH_OPEN_REQUEST = 0x1008;

    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_ble_device);
        showBleDeviceState();
        EventBus.getDefault().register(this);
    }

    private void showBleDeviceState() {
        mLinearLayout.removeAllViews();
        BleSdkManager bleSdkManager = BleSdkManager.newInstance(getApplicationContext());
        List<BleDevice> bleDeviceList = DataBaseHelper.getInstance().getAllBleDevice();
        if (bleDeviceList != null && bleDeviceList.size() > 0) {
            for (BleDevice bleDevice : bleDeviceList) {
                BleStatusView bleStatusView = new BleStatusView(this);
                bleStatusView.setBleDevice(bleDevice);
                bleStatusView.setTag(bleDevice.getImsi());
                bleStatusView.setOnBleMenuListener(mBleMenuListener);
                bleStatusView.setStatus(bleSdkManager.getDeviceState(new TrackerConnectInfo(bleDevice.getImsi(), bleDevice.getImsi())));
                mLinearLayout.addView(bleStatusView);
            }
        }
    }

    private BleStatusView getBleStatusView(String imsi) {
        int count = mLinearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mLinearLayout.getChildAt(i);
            if (view.getTag().equals(imsi)) {
                return (BleStatusView) view;
            }
        }
        return null;
    }

    public void startService(View view) {
        Bootstrap.startAlwaysOnService(this);
    }

    public void stopService(View view) {
        Bootstrap.stopAlwaysOnService(this);
    }

    public void addDevice(View view) {
        Intent intent = new Intent(this, AddDeviceActivity.class);
        intent.putExtra(AddDeviceActivity.EXTRA_TYPE, AddDeviceActivity.TYPE_ADD);
        startActivityForResult(intent, REQUEST_ADD_DEVICE);
    }

    public void clearData(View view) {
        getSharedPreferences("BLE_SERVICE", MODE_PRIVATE).edit().putString("ble", "").commit();
        BleSdkManager.newInstance(this).clearAll();
        int count = mLinearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            BleStatusView statusView = (BleStatusView) mLinearLayout.getChildAt(i);
            statusView.setStatus("已断开");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        BluetoothState bluetoothState = BleUtils.getCurrentBluetoothState(this);
        if (bluetoothState == BluetoothState.Bluetooth_Off) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BLUETOOTH_OPEN_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_DEVICE) {
            if (resultCode == RESULT_OK) {
                showBleDeviceState();
            }
        }

        if (requestCode == BLUETOOTH_OPEN_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "同意开启蓝牙", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "拒绝开启蓝牙", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onEventMainThread(EventBleDevice device) {
        String imsi = device.getBleConnectInfo().getSingleTag();
        String state = "";
        switch (device.getBleState()) {
            case EventBleDevice.CONNECTED:
                state = "已连接";
                break;
            case EventBleDevice.DISCONNECT:
                state = "断开";
                break;
            case EventBleDevice.DEVICE_FOUND:
                state = "发现设备";
                break;
        }

        BleStatusView statusView = getBleStatusView(imsi);
        if (statusView != null) {
            statusView.setStatus(state);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private BleStatusView.OnBleMenuListener mBleMenuListener = new BleStatusView.OnBleMenuListener() {
        @Override
        public void onBind(BleDevice device) {
            TrackerConnectInfo connectInfo = new TrackerConnectInfo(device.getImsi(), device.getImei());
            BleSdkManager.newInstance(getApplicationContext()).connectBleDevice(connectInfo);
        }

        @Override
        public void unBind(BleDevice device) {
            TrackerConnectInfo connectInfo = new TrackerConnectInfo(device.getImsi(), device.getImei());
            BleSdkManager.newInstance(getApplicationContext()).disConnectBleDevice(connectInfo);
        }

        @Override
        public void onEdit(BleDevice device) {
            Intent intent = new Intent(MainActivity.this, AddDeviceActivity.class);
            intent.putExtra(AddDeviceActivity.EXTRA_TYPE, AddDeviceActivity.TYPE_EDIT);
            intent.putExtra(AddDeviceActivity.EXTRA_IMSI, device.getImsi());
            startActivityForResult(intent, REQUEST_ADD_DEVICE);
        }
    };
}
