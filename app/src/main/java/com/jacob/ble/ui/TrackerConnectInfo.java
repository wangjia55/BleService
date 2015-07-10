package com.jacob.ble.ui;

import android.bluetooth.BluetoothDevice;

import com.cvte.ble.sdk.entity.BleConnectInfo;

import java.util.Arrays;
import java.util.UUID;

/**
 * Package : com.jacob.ble.ui
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是用来xxx
 */
public class TrackerConnectInfo extends BleConnectInfo {

    private String imbt;

    public TrackerConnectInfo(String imbt){
        this.imbt = imbt;
    }

    @Override
    public String getSingleTag() {
        return imbt;
    }


    @Override
    public UUID getWriteCharacteristicUUID() {
        return UUID.fromString("00002A1A-0000-1000-8000-00805F9B34FB");
    }

    @Override
    public UUID getServiceUUID() {
        return UUID.fromString("0000110F-0000-1000-8000-00805F9B34FB");
    }

    @Override
    public UUID getReadCharacteristicUUID() {
        return UUID.fromString("00002A19-0000-1000-8000-00805F9B34FB");
    }

    @Override
    public UUID getCharacteristicDescriptorUUID() {
        return null;
    }

    @Override
    public UUID getNotificationService() {
        return null;
    }

    @Override
    public boolean shouldConnectDevice(BluetoothDevice bluetoothDevice, byte[] bytes) {

        int startIndex = 9;
        byte[] imbtBytes = Arrays.copyOfRange(bytes, startIndex, startIndex + imbt.length());
        String scanImbt = new String(imbtBytes);
        if (imbt.equals(scanImbt)) {
            return true;
        } else {
            return false;
        }
    }


}
