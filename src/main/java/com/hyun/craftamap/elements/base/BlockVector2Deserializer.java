package com.hyun.craftamap.elements.base;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sk89q.worldedit.math.BlockVector2;

public class BlockVector2Deserializer implements JsonDeserializer<BlockVector2> {
    private final double scale;
    private final int offsetX;
    private final int offsetZ;

    public enum Orientation {
        NORTH, EAST, SOUTH, WEST
    }

    private final Orientation orientation;

    public BlockVector2Deserializer() {
        this.scale = 1;
        this.offsetX = 0;
        this.offsetZ = 0;
        this.orientation = Orientation.NORTH;
    }

    public BlockVector2Deserializer(double scale, int offsetX, int offsetZ, Orientation orientation) {
        this.scale = scale;
        this.offsetX = offsetX;
        this.offsetZ = offsetZ;
        this.orientation = orientation;
    }

    public BlockVector2 deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        com.google.gson.JsonArray array = json.getAsJsonArray();
        double x = array.get(0).getAsDouble() * scale + offsetX;
        double z = array.get(1).getAsDouble() * scale + offsetZ;

        return switch (orientation) {
            case NORTH -> BlockVector2.at(x, -z);
            case EAST -> BlockVector2.at(z, x);
            case SOUTH -> BlockVector2.at(-x, z);
            case WEST -> BlockVector2.at(-z, -x);
        };
    }
}
