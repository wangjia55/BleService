package com.cvte.ble.exception;

/**
 * Created by jianhaohong on 10/22/14.
 */
public class InitException extends Exception {

    public InitException(String message) {
        super("Google ble init: " + message);
    }
}
