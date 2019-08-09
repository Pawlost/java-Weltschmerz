package com.ritualsoftheold.weltschmerz.misc.misc;

import java.util.HashMap;

public class Configuration {
    //Map
    public int longitude = 4000;
    public int latitude = 4000;
    public long seed = 7987099;

    //Noise
    public int octaves = 3;
    public double frequency = 2;
    public int samples = 15;

    //Temperature
    public double maxTemperature = 90;
    public double minTemperature = -30;
    public double temperatureDecrease = 0.3;

    //Moisture
    public double zoom = 6.0;
    public double placement = 0;
    public double moistureIntensity = 10.0;
    public double change = 0.5;

    //Height Maps
    public int moisture = 10;
    public int pressure = 200;
    public int precipitation = 400;
    public int humidity = 10;

    //Precipitation
    public double circulation = 0.5;
    public double orographicEffect = 1.0;
    public double precipitationIntensity = 1.4;
    public double iteration = 1.0;

    //Humidity
    public double traspiration = 0.5;
    public double evaporation = 1.0;

    //Circulation
    public double exchangeCoeficient = 1.5;
    public int circulationOctaves = 7;
    public double temperatureInfluence = 0.5;
    public int circulationDecline = 7;
    public int elevationDelta = 5;

    //Biomes
    public HashMap<Integer, String> shapes;
}
