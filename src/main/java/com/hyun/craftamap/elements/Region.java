package com.hyun.craftamap.elements;

import com.hyun.craftamap.elements.base.BaseRegion;
import com.sk89q.worldedit.math.BlockVector2;

import java.util.List;

public class Region extends BaseRegion {
    private final String blockState;

    public Region(int mainKey, int subKey, List<List<BlockVector2>> path, String blockState) {
        super(mainKey, subKey, path);
        this.blockState = blockState;
    }

    public String getBlockState() {
        return blockState;
    }

    @Override
    public String toString() {
        return "Region{" +
                "  mainKey=" + getMainKey() +
                ", subKey=" + getSubKey() +
                '}';
    }
}
