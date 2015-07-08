package com.cvte.ble.logic;

/**
 * Created by jianhaohong on 10/28/14.
 */
public interface BleDataHandleCallback {
    public void onDataHandle(Object o);

    public void onError(int errorCode, String message);
}
