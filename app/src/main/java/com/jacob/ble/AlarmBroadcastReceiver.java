package com.jacob.ble;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cvte.ble.sdk.core.Bootstrap;
import com.jacob.ble.utils.LogUtils;

/**
 * Package : com.jacob.ble
 * Author : jacob
 * Date : 15-7-13
 * Description : 这个类是用来xxx
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = AlarmBroadcastReceiver.class.getSimpleName();
    public static final String ACTION_CUSTOM_ALARM = "action.jacob.bleservice";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AlarmBroadcastReceiver.ACTION_CUSTOM_ALARM)) {
            LogUtils.LOGE(LOG_TAG,"onReceive---");
            Bootstrap.startAlwaysOnService(context);
        }
    }
}
