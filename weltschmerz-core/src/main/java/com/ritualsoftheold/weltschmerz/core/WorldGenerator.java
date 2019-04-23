package com.ritualsoftheold.weltschmerz.core;

import java.util.ArrayList;

public class WorldGenerator {
    private ArrayList<Integer> ids;
    private World world;
    public WorldGenerator(World world, ArrayList<Integer> ids){
        this.ids = ids;
        this.world = world;
    }
}
