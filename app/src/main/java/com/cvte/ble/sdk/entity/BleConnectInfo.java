package com.cvte.ble.sdk.entity;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;
import java.util.UUID;

/**
 * Package : com.cvte.ble.sdk.core
 * Date : 15-7-10
 * Description : 这个类是连接蓝牙设备的信息类（需要提供蓝牙设备的必要信息，如UUID 等）
 */
public abstract class BleConnectInfo  implements Serializable{
    public abstract String getSingleTag();

    public abstract UUID getWriteCharacteristicUUID();

    public abstract UUID getServiceUUID();

    public abstract UUID getReadCharacteristicUUID();

    public abstract UUID getCharacteristicDescriptorUUID();

    public abstract UUID getNotificationService();

    public abstract boolean shouldConnectDevice(BluetoothDevice bluetoothDevice, byte[] bytes);


}
