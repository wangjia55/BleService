package com.cvte.ble.sdk.entity;

import android.bluetooth.BluetoothDevice;

import java.util.UUID;

/**
 * Package : com.cvte.ble.sdk.core
 * Author : jacob
 * Date : 15-7-10
 * Description : 这个类是用来xxx
 */
public abstract class BleConnectInfo {

    public abstract UUID getWriteCharacteristicUUID();

    public abstract UUID getServiceUUID();

    public abstract UUID getReadCharacteristicUUID();

    public abstract UUID getCharacteristicDescriptorUUID();

    public abstract UUID getNotificationService();

    public abstract boolean shouldConnectDevice(BluetoothDevice bluetoothDevice, byte[] bytes);


}
