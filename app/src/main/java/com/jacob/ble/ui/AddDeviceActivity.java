package com.jacob.ble.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jacob.ble.R;

public class AddDeviceActivity extends Activity implements View.OnClickListener {

    EditText mEditTextImei;
    EditText mEditTextImsi;
    EditText mEditTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        findViewById(R.id.button_add_device).setOnClickListener(this);

        mEditTextImei = (EditText) findViewById(R.id.edit_text_imei);
        mEditTextImsi = (EditText) findViewById(R.id.edit_text_imsi);
        mEditTextName = (EditText) findViewById(R.id.edit_text_name);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_device:
                break;
        }
    }
}
