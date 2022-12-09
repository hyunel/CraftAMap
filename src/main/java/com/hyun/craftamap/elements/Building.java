package com.hyun.craftamap.elements;

import com.hyun.craftamap.elements.base.BaseRegion;
import com.sk89q.worldedit.math.BlockVector2;

import java.util.List;

public class Building extends BaseRegion {

    private final int height;
    private final int altitude;

    public Building(int mainKey, int subKey, List<List<BlockVector2>> path, int height, int altitude) {
        super(mainKey, subKey, path);
        this.height = height;
        this.altitude = altitude;
    }

    public int getAltitude() {
        return altitude;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Building{" +
                "mainKey=" + getMainKey() +
                ", subKey=" + getSubKey() +
                ", height=" + height +
                ", altitude=" + altitude +
                '}';
    }
}
