package com.jacob.ble.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cvte.ble.sdk.entity.EventBleDevice;
import com.jacob.ble.R;
import com.jacob.ble.bean.BleDevice;
import com.jacob.ble.utils.DataBaseHelper;


/**
 * Package : com.cvte.kidtracker.ui
 * Author : jacob
 * Date : 15-7-8
 * Description : 这个类是当帐号被在其他手机登录后，弹出的提示（单点登录提示窗口）
 */
public class BleAlertActivity extends FragmentActivity {

    TextView mTextViewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_message);
        this.setFinishOnTouchOutside(false);

        mTextViewContent = (TextView) findViewById(R.id.text_view_content);

        findViewById(R.id.button_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setWindowSize();

        showContentFromIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showContentFromIntent(intent);
    }


    private void showContentFromIntent(Intent intent) {
        if (intent != null) {
            EventBleDevice eventBleDevice = (EventBleDevice) intent.getSerializableExtra("eventDevice");
            BleDevice device1 = DataBaseHelper.getInstance().getBleDeviceByImsi(eventBleDevice.getBleConnectInfo().getSingleTag());
            int  state = eventBleDevice.getBleState();
            StringBuilder sb = new StringBuilder(device1.getName() + " ");
            switch (state) {
                case EventBleDevice.CONNECTED:
                    sb.append(" 已连接");
                    break;
                case EventBleDevice.DISCONNECT:
                    sb.append(" 已断开");
                    break;
            }
            mTextViewContent.setText(sb.toString());
        }
    }


    /**
     * 设置窗口大小，特别是针对一个主题是dialog的Activity比较实用
     */
    protected void setWindowSize() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager m = getWindowManager();
        m.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = (int) (dm.widthPixels * 0.86);    //宽度设置为屏幕的0.85
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.6f;      //设置黑暗度
        getWindow().setAttributes(p);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
