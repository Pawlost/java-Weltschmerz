package com.ritualsoftheold.weltschmerz.geometry.misc;

import java.util.HashMap;

public class Configuration {
    public int longitude = 4000;
    public int latitude = 4000;
    public long seed = 7987099;
    public int octaves = 3;
    public double frequency = 2;
    public int samples = 15;
    public int volcanoes = 0;
    public int tectonicPlates = 20;
    public int detail = 25000;
    public int maxTemperature = 50;
    public int minTemperature = -80;
    //Over 1000 meters
    public double temperatureDecrease = 0.0098;
    public float seaLevelAirPressure = 1013.25f;
    public HashMap<String, Shape> shapes;
}
