package com.jacob.ble.bean;

/**
 * Package : com.jacob.ble
 * Author : jacob
 * Date : 15-7-13
 * Description : 这个类是用来xxx
 */
public class BleDeviceBean {
    String imei;
    String imsi;

    public BleDeviceBean() {
    }

    public BleDeviceBean(String imei, String imsi) {
        this.imei = imei;
        this.imsi = imsi;
    }

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
}
