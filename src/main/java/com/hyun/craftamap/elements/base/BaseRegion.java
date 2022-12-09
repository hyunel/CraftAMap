package com.hyun.craftamap.elements.base;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.world.World;

import java.lang.reflect.Type;
import java.util.List;

public class BaseRegion extends BaseElement implements JsonDeserializer<BlockVector2> {
    protected final List<List<BlockVector2>> path;
    public BaseRegion(int mainKey, int subKey, List<List<BlockVector2>> path) {
        super(mainKey, subKey);
        this.path = path;
    }

    public List<List<BlockVector2>> getPath() {
        return path;
    }

    public List<Polygonal2DRegion> getRegions(World world, int minY, int maxY) {
        return path.stream().map(p -> new Polygonal2DRegion(world, p, minY, maxY)).toList();
    }

    @Override
    public BlockVector2 deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }
}
