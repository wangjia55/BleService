package com.jacob.ble.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.jacob.ble.BleService;
import com.jacob.ble.R;
import com.jacob.ble.bean.BleDevice;
import com.jacob.ble.utils.DataBaseHelper;

import java.util.List;


public class MainActivity extends FragmentActivity {
    public static final int REQUEST_ADD_DEVICE = 10010;

    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_ble_device);
        showBleDeviceState();
    }

    private void showBleDeviceState(){
        mLinearLayout.removeAllViews();
        List<BleDevice> bleDeviceList = DataBaseHelper.getInstance().getAllBleDevice();
        if (bleDeviceList != null && bleDeviceList.size()>0){
            for (BleDevice bleDevice : bleDeviceList){
                BleStatusView bleStatusView = new BleStatusView(this);
                bleStatusView.setBleDevice(bleDevice);
                bleStatusView.setOnBleMenuListener(mBleMenuListener);
                mLinearLayout.addView(bleStatusView);
            }
        }
    }

    public void startService(View view) {
        Intent intent = new Intent(this, BleService.class);
        startService(intent);
    }

    public void stopService(View view) {
        Intent intent = new Intent(this, BleService.class);
        stopService(intent);
    }

    public void addDevice(View view) {
        Intent intent = new Intent(this, AddDeviceActivity.class);
        intent.putExtra(AddDeviceActivity.EXTRA_TYPE,AddDeviceActivity.TYPE_ADD);
        startActivityForResult(intent, REQUEST_ADD_DEVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_DEVICE) {
                showBleDeviceState();
            }
        }

    }

    private BleStatusView.OnBleMenuListener mBleMenuListener = new BleStatusView.OnBleMenuListener() {
        @Override
        public void onBind(BleDevice device) {

        }

        @Override
        public void unBind(BleDevice device) {

        }

        @Override
        public void onEdit(BleDevice device) {
            Intent intent = new Intent(MainActivity.this, AddDeviceActivity.class);
            intent.putExtra(AddDeviceActivity.EXTRA_TYPE,AddDeviceActivity.TYPE_EDIT);
            intent.putExtra(AddDeviceActivity.EXTRA_IMEI,device.getImei());
            startActivityForResult(intent, REQUEST_ADD_DEVICE);
        }
    };
}
