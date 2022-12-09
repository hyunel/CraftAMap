package com.hyun.craftamap.elements;

import com.hyun.craftamap.elements.base.BaseElement;
import com.sk89q.worldedit.math.BlockVector2;

public class POI extends BaseElement {
    private final int rank;
    private final int z;
    private final String name;
    private final BlockVector2 pos;

    public POI(int mainKey, int subKey, int rank, int z, String name, BlockVector2 pos) {
        super(mainKey, subKey);
        this.rank = rank;
        this.z = z;
        this.name = name;
        this.pos = pos;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public BlockVector2 getPos() {
        return pos;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "POI{" +
                "mainKey=" + getMainKey() +
                ", subKey=" + getSubKey() +
                ", name='" + name + '\'' +
                ", pos=" + pos +
                '}';
    }
}
