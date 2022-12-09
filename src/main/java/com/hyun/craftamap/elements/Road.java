package com.hyun.craftamap.elements;

import com.hyun.craftamap.elements.base.BaseElement;
import com.sk89q.worldedit.math.BlockVector2;

import java.util.List;

public class Road extends BaseElement {
    private final List<BlockVector2> path;
    private final int weight;

    public Road(int mainKey, int subKey, List<BlockVector2> path, int weight) {
        super(mainKey, subKey);
        this.path = path;
        this.weight = weight;
    }

    public List<BlockVector2> getPath() {
        return path;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Road{" +
                "mainKey=" + getMainKey() +
                ", subKey=" + getSubKey() +
                ", weight=" + weight +
                '}';
    }

}
