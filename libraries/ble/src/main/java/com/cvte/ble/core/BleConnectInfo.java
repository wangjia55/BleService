package com.cvte.ble.core;

import android.bluetooth.BluetoothDevice;

import java.util.UUID;

/**
 * Created by jianhaohong on 10/23/14.
 */
public interface BleConnectInfo {

    public UUID getWriteCharacteristicUUID();

    public UUID getServiceUUID();

    public UUID getReadCharacteristicUUID();

    public UUID getCharacteristicDescriptorUUID();

    public UUID getNotificationService();

    public boolean shouldConnectDevice(BluetoothDevice bluetoothDevice, byte[] bytes);


}
