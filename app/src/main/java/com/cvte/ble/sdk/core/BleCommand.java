package com.cvte.ble.sdk.core;

import java.util.Arrays;

/**
 * Package : com.ble.sdk.core
 * Author : jacob
 * Date : 15-7-10
 * Description : 蓝牙命令工具，将字符串命令转成byte字节写入设备
 */
public class BleCommand {
    public static byte[] getVerifyCommand(String imei) {
        return imei.getBytes();
    }

    public static byte[] getShutDownCommand(String imei) {
        byte[] bytes = imei.getBytes();
        byte[] closeCommand = Arrays.copyOf(bytes, bytes.length + 1);
        closeCommand[bytes.length] = 0x04;
        return closeCommand;
    }
}
