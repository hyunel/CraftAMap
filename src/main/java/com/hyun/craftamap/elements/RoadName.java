package com.hyun.craftamap.elements;

import com.hyun.craftamap.elements.base.BaseElement;
import com.sk89q.worldedit.math.BlockVector2;

import java.util.List;

public class RoadName extends BaseElement {
    private final int shieldType;
    private final int rank;
    private final String name;
    private final List<BlockVector2> path;

    public RoadName(int mainKey, int subKey, int shieldType, int rank, String name, List<BlockVector2> path) {
        super(mainKey, subKey);
        this.shieldType = shieldType;
        this.rank = rank;
        this.name = name;
        this.path = path;
    }

    public int getShieldType() {
        return shieldType;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public List<BlockVector2> getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "RoadName{" +
                "mainKey=" + getMainKey() +
                ", subKey=" + getSubKey() +
                ", name='" + name + '\'' +
                '}';
    }
}
