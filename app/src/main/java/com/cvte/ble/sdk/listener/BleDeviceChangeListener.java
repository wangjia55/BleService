package com.cvte.ble.sdk.listener;

import com.cvte.ble.sdk.entity.BleConnectDevice;

import java.util.Map;

/**
 * Package : com.cvte.ble.sdk.listener
 * Author : jacob
 * Date : 15-7-13
 * Description : 这个类是用来xxx
 */
public interface BleDeviceChangeListener {
    void onDeviceSizeChange(Map<String, BleConnectDevice> allDeviceMap);
}
