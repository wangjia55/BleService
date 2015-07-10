package com.cvte.ble.sdk.listener;

/**
 * Created by jianhaohong on 10/28/14.
 */
public interface BleWriteCallback {
     void onWriteSuccess(byte[] bytes);

     void onWriteFail(int errorCode, String reason);
}
