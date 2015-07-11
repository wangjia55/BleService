package com.cvte.ble.sdk.listener;

/**
 * Package : com.cvte.ble.sdk.listener
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是向蓝牙设备写数据的接口
 */
public interface BleWriteCallback {
    void onWriteSuccess(byte[] bytes);

    void onWriteFail(int errorCode, String reason);
}
