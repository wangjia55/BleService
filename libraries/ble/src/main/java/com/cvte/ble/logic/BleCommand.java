package com.cvte.ble.logic;

import java.util.Arrays;

/**
 * Created by jianhaohong on 5/20/15.
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
