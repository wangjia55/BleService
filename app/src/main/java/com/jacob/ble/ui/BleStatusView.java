package com.jacob.ble.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jacob.ble.R;

public class BleStatusView extends LinearLayout implements View.OnClickListener {

    private TextView mTextViewImei;
    private TextView mTextViewImsi;
    private TextView mTextViewName;
    private TextView mTextViewState;

    public BleStatusView(Context context) {
        this(context, null);
    }

    public BleStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BleStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_ble_device_state, this);

        mTextViewImei = (TextView) findViewById(R.id.text_view_imei);
        mTextViewImsi = (TextView) findViewById(R.id.text_view_imsi);
        mTextViewName = (TextView) findViewById(R.id.text_view_name);
        mTextViewState = (TextView) findViewById(R.id.text_view_state);

        findViewById(R.id.button_bind).setOnClickListener(this);
        findViewById(R.id.button_unbind).setOnClickListener(this);
        findViewById(R.id.button_edit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_bind:
                break;
            case R.id.button_unbind:
                break;
            case R.id.button_edit:
                break;
        }
    }
}
