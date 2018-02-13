package com.example.jojinwoo.learn2caltest.Data;

/**
 * Created by jojinwoo on 2018-02-10.
 */

public enum User {

    UID, LOCATION, DEVICE_NAME, SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_INCH;

    private String value;

    User() {
        this.value = null;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}