package com.ritualsoftheold.weltschmerz.environment;

import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleFractal;
import com.typesafe.config.Config;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WorldNoise {
    private ModuleAutoCorrect mod;
    private BufferedImage earth;

    private int longitude;
    private int latitude;
    private int octaves;
    private int samples;
    private double frequency;
    private long seed;
    private int minElevation;
    private int maxElevation;
    private boolean useEarthImage;

    public WorldNoise(Config config){
        this(config, null);
    }

    public WorldNoise(Config config, BufferedImage earth){
        init();
        changeConfiguration(config);
        this.earth = earth;
    }

    private void init() {
        ModuleFractal gen = new ModuleFractal();
        gen.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.GRADIENT);
        gen.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
        gen.setNumOctaves(octaves);
        gen.setFrequency(frequency);
        gen.setType(ModuleFractal.FractalType.FBM);
        gen.setSeed(seed);
        mod = new ModuleAutoCorrect(minElevation, maxElevation);
        mod.setSource(gen);
        mod.setSamples(samples);
        mod.calculate4D();
    }
    public double getNoise(int x, int y){
        if(!useEarthImage) {
            float s = x / (float) longitude;
            float t = y / (float) latitude;
            double nx = Math.cos(s * 2 * Math.PI) * 1.0 / (2 * Math.PI);
            double ny = Math.cos(t * 2 * Math.PI) * 1.0 / (2 * Math.PI);
            double nz = Math.sin(s * 2 * Math.PI) * 1.0 / (2 * Math.PI);
            double nw = Math.sin(t * 2 * Math.PI) * 1.0 / (2 * Math.PI);
            return (mod.get(nx, ny, nz, nw));
        }else{
            return new Color(earth.getRGB(x, y)).getRed();
        }
    }

    public int getMax(){
        return maxElevation;
    }

    public void changeConfiguration(Config config){
        latitude = config.getInt("map.latitude");
        longitude = config.getInt("map.longitude");
        useEarthImage = config.getBoolean("map.use_earth_image");
        minElevation = config.getInt("temperature.max_temperature");
        maxElevation = config.getInt("temperature.min_temperature");
        seed = config.getLong("map.seed");
        octaves = config.getInt("noise.octaves");
        frequency = config.getDouble("noise.frequency");
        samples = config.getInt("noise.samples");
    }
}
