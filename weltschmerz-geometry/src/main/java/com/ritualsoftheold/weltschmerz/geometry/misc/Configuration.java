package com.ritualsoftheold.weltschmerz.geometry.misc;

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

    //Elevation
    public int elevationDelta = 5;

    //Temperature
    public double maxTemperature = 90;
    public double minTemperature = -20;
    public double temperatureDecrease = 0.3;

    //Moisture
    public double zoom = 6.0;
    public int placement = 13;
    public double moistureIntensity = 1.0;
    public double change = 0.2;

    //Height Maps
    public int moisture = 20;
    public int pressure = 200;
    public int precipitation = 400;
    public int humidity = 10;

    //Precipitation
    public double circulation = 0.5;
    public double orographicEffect = 1.0;
    public double precipitationIntensity = 0.6;
    public double iteration = 1.0;

    //Humidity
    public double traspiration = 0.5;
    public double evaporation = 1.0;

    //Circulation
    public double exchangeCoeficient = 1.5;
    public int circulationOctaves = 7;
    public double temperatureInfluence = 0.5;
    public int circulationDecline = 7;

    //Biomes
    public HashMap<Integer, String> shapes;
}
