package com.cvte.ble.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.cvte.ble.core.BluetoothState;

/**
 * Created by jianhaohong on 5/18/15.
 */
public class BleUtils {
    public static boolean isBleSupported(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return true;
        } else {
            return false;
        }
    }


    @SuppressLint("NewApi")
    public static BluetoothState getCurrentBluetoothState(Context context) {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            return BluetoothState.Bluetooth_On;
        } else {
            return BluetoothState.Bluetooth_Off;
        }

    }
}
