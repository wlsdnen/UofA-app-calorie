package com.example.jojinwoo.learn2caltest.Data;

/**
 * Created by jojinwoo on 2018-02-12.
 */

public enum Demographic {

    GENDER,
    AGE,
    WEIGHT_KG,
    WEIGHT_LBS,
    HEIGHT_CM,
    HEIGHT_FT,
    HEIGHT_IN;

    private String value;

    Demographic() {
        this.value = null;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
