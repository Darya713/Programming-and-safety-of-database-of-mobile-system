package com.butterfly.lab_03;

import android.util.Log;

public class HashTable {
    private String key;
    private String value;
    private int calculatedKey;
    HashTable() {}

    HashTable(String key, String value) {
        this.key = key;
        this.value = value;
        this.calculatedKey = calculateKey(key);
    }

    HashTable(String key, String value, int calculatedKey) {
        this.key = key;
        this.value = value;
        this.calculatedKey = calculatedKey;
    }

    private int calculateKey(String key) {
        return (key.hashCode() % 10);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCalculatedKey() {
        return calculatedKey;
    }

    public void setCalculatedKey(int calculatedKey) {
        this.calculatedKey = calculatedKey;
    }
}