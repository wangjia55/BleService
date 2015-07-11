package com.jacob.ble.utils;

import com.activeandroid.query.Select;
import com.jacob.ble.bean.BleDevice;

import java.util.List;

/**
 * Package : com.jacob.ble.utils
 * Author : jacob
 * Date : 15-7-9
 * Description : 这个类是用来xxx
 */
public class DataBaseHelper {
    private static DataBaseHelper sInstance;

    private DataBaseHelper() {
    }

    public static DataBaseHelper getInstance() {
        if (sInstance == null) {
            sInstance = new DataBaseHelper();
        }
        return sInstance;
    }


    public List<BleDevice> getAllBleDevice(){
        return new Select().from(BleDevice.class).execute();
    }

    public BleDevice getBleDeviceByImsi(String imsi) {
        return new Select().from(BleDevice.class).where("imsi = ?",imsi).executeSingle();
    }

}
