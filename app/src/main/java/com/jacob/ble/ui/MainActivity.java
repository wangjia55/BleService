package com.jacob.ble.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.jacob.ble.BleService;
import com.jacob.ble.R;


public class MainActivity extends FragmentActivity {
    public static final int REQUEST_ADD_DEVICE = 10010;

    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_ble_device);
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
        startActivityForResult(intent, REQUEST_ADD_DEVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_DEVICE) {

            }
        }

    }
}
