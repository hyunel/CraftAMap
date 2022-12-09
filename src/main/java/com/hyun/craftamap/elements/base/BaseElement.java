package com.hyun.craftamap.elements.base;

public class BaseElement {
    protected final int mainkey;
    protected final int subkey;

    public BaseElement(int mainKey, int subKey) {
        this.mainkey = mainKey;
        this.subkey = subKey;
    }

    public int getMainKey() {
        return mainkey;
    }

    public int getSubKey() {
        return subkey;
    }
}
