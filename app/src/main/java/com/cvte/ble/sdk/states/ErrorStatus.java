package com.cvte.ble.sdk.states;

/**
 * Package : com.cvte.ble.sdk.listener
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是整个蓝牙操作过程的中的异常状态码
 */
public class ErrorStatus {
    public static final int CONNECT_STATE_FAIL = 10001;
    public static final int ALREADY_SCAN = 10002;
    public static final int CONNECT_TIME_OUT = 10004;
    public static final int STATE_DISCONNECT = 10005;
    public static final int GATT_NULL = 10006;
    public static final int BLUETOOTH_NO_OPEN = 10007;
    public static final int ALREADY_CONNECTING = 10008;
    public static final int ALREADY_CONNECTED = 10009;
    public static final int GATT_FAIL = 10010;
    public static final int GATT_ERROR = 10011;
}
