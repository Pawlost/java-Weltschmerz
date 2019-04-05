package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.noise.Shape;
import java.util.HashMap;

public class Configuration {
    public int width = 600;
    public int height = 600;
    public long detail = 15000;
    public int smooth = 1;
    public long seed = 7987099;
    public int octaves = 3;
    public double frequency = 2;
    public int volcanoes = 0;
    public int tectonicPlates = 20;
    public int tectonicMovement = 3;
    public int islandSize = 20;
    public HashMap<String, Shape> shapes;
}
