package com.jacob.ble.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jacob.ble.R;
import com.jacob.ble.bean.BleDevice;
import com.jacob.ble.utils.DataBaseHelper;

public class AddDeviceActivity extends Activity implements View.OnClickListener {
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_IMSI = "imsi";

    public static final int TYPE_ADD = 100;
    public static final int TYPE_EDIT = 101;
    int type = TYPE_ADD;

    EditText mEditTextImei;
    EditText mEditTextImsi;
    EditText mEditTextName;

    BleDevice mBleDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        type = getIntent().getIntExtra(EXTRA_TYPE, type);
        String imsi = getIntent().getStringExtra(EXTRA_IMSI);
        findViewById(R.id.button_add_device).setOnClickListener(this);
        mEditTextImei = (EditText) findViewById(R.id.edit_text_imei);
        mEditTextImsi = (EditText) findViewById(R.id.edit_text_imsi);
        mEditTextName = (EditText) findViewById(R.id.edit_text_name);

        if (type == TYPE_EDIT) {
            mBleDevice = DataBaseHelper.getInstance().getBleDeviceByImsi(imsi);
            mEditTextImei.setText(mBleDevice.getImei());
            mEditTextImsi.setText(mBleDevice.getImsi());
            mEditTextName.setText(mBleDevice.getName());
        } else {
            mBleDevice = new BleDevice();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_device:
                String imei = mEditTextImei.getText().toString();
                String imsi = mEditTextImsi.getText().toString();
                String name = mEditTextName.getText().toString();

                if ("".equals(imei)) {
                    Toast.makeText(this, "请输入imei", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("".equals(imsi)) {
                    Toast.makeText(this, "请输入imsi", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("".equals(name)) {
                    Toast.makeText(this, "请输入name", Toast.LENGTH_SHORT).show();
                    return;
                }
                mBleDevice.setImei(imei);
                mBleDevice.setImsi(imsi);
                mBleDevice.setName(name);
                mBleDevice.save();

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
