package com.jacob.ble.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvte.ble.sdk.states.ConnectState;
import com.jacob.ble.R;
import com.jacob.ble.bean.BleDevice;

public class BleStatusView extends LinearLayout implements View.OnClickListener {

    private TextView mTextViewImei;
    private TextView mTextViewImsi;
    private TextView mTextViewName;
    private TextView mTextViewState;
    private BleDevice mBleDevice;
    private OnBleMenuListener mListener;

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

        mTextViewState.setText("已解绑");
        findViewById(R.id.button_bind).setOnClickListener(this);
        findViewById(R.id.button_unbind).setOnClickListener(this);
        findViewById(R.id.button_edit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.button_bind:
                    mListener.onBind(mBleDevice);
                    break;
                case R.id.button_unbind:
                    mTextViewState.setText("已解绑");
                    mListener.unBind(mBleDevice);
                    break;
                case R.id.button_edit:
                    mListener.onEdit(mBleDevice);
                    break;
            }
        }
    }

    public void setBleDevice(BleDevice bleDevice) {
        mBleDevice = bleDevice;
        mTextViewImei.setText(bleDevice.getImei());
        mTextViewImsi.setText(bleDevice.getImsi());
        mTextViewName.setText(bleDevice.getName());
    }

    public void setStatus(String state) {
        mTextViewState.setText(state);
    }

    public void setStatus(ConnectState connectState){
        String state = "";
        switch (connectState) {
            case Connected:
                state = " 已经连接";
                break;
            case Disconnect:
                state = " 已经断开";
                break;
        }
        mTextViewState.setText(state);
    }

    public interface OnBleMenuListener {
        void onBind(BleDevice device);

        void unBind(BleDevice device);

        void onEdit(BleDevice device);
    }

    public void setOnBleMenuListener(OnBleMenuListener listener) {
        mListener = listener;
    }
}
