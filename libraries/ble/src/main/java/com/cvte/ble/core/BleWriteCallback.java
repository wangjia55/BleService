package com.cvte.ble.core;

/**
 * Created by jianhaohong on 10/28/14.
 */
public interface BleWriteCallback {
    public void onWriteSuccess(byte[] bytes);

    public void onWriteFail(int errorCode, String reason);
}
