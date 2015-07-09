package com.jacob.ble.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Package : com.jacob.ble.bean
 * Author : jacob
 * Date : 15-7-9
 * Description : 这个类是用来xxx
 */
@Table(name = "t_ble_device")
public class BleDevice extends Model {
    @Column(name = "imei")
    String imei;
    @Column(name = "imsi")
    String imsi;
    @Column(name = "name")
    String name;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BleDevice{" +
                "imei='" + imei + '\'' +
                ", imsi='" + imsi + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
