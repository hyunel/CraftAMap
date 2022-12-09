package com.hyun.craftamap.elements;

import java.util.Arrays;

public class Elements {
    Building[] buildings;
    Road[] roads;
    Region[] regions;
    RoadName[] roadnames;
    POI[] pois;

    public Elements(Building[] buildings, Road[] roads, Region[] regions) {
        this.buildings = buildings;
        this.roads = roads;
        this.regions = regions;
    }

    public Building[] getBuildings() {
        return buildings;
    }

    public Road[] getRoads() {
        return roads;
    }

    public Region[] getRegions() {
        return regions;
    }

    public POI[] getPois() {
        return pois;
    }

    public RoadName[] getRoadnames() {
        return roadnames;
    }

    @Override
    public String toString() {
        return "Elements{" + "\n" +
                "  buildings=" + Arrays.toString(buildings) + ",\n" +
                "  roads=" + Arrays.toString(roads) + ",\n" +
                "  regions=" + Arrays.toString(regions) + ",\n" +
                "  pois=" + Arrays.toString(pois) + ",\n" +
                "  roadnames=" + Arrays.toString(roadnames) + "\n" +
                '}';
    }
}
