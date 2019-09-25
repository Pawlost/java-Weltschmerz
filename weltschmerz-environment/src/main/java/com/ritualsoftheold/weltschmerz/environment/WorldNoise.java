package com.ritualsoftheold.weltschmerz.environment;

import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleFractal;
import com.typesafe.config.Config;

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
    private double[] image;

    public WorldNoise(Config config, boolean useImage) {
        this(config, null, useImage);
    }

    public WorldNoise(Config config, BufferedImage earth, boolean useImage) {
        this.earth = earth;
        changeConfiguration(config);
        init();
        fillImage(useImage);
    }

    private void init() {
        ModuleFractal gen = new ModuleFractal();
        gen.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.GRADIENT);
        gen.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
        gen.setNumOctaves(octaves);
        gen.setFrequency(frequency);
        gen.setType(ModuleFractal.FractalType.FBM);
        gen.setSeed(seed);
        mod = new ModuleAutoCorrect(-minElevation, maxElevation);
        mod.setSource(gen);
        mod.setSamples(samples);
        mod.calculate4D();
    }

    public double getNoise(int x, int y) {
        if (image != null) {
            return image[(y * longitude) + x];
        } else {
            float s = x / (float) longitude;
            float t = y / (float) latitude;
            double nx = Math.cos(s * 2 * Math.PI) * 1.0 / (2 * Math.PI);
            double ny = Math.cos(t * 2 * Math.PI) * 1.0 / (2 * Math.PI);
            double nz = Math.sin(s * 2 * Math.PI) * 1.0 / (2 * Math.PI);
            double nw = Math.sin(t * 2 * Math.PI) * 1.0 / (2 * Math.PI);
            return mod.get(nx, ny, nz, nw);
        }
    }

    public int getMax() {
        return maxElevation;
    }

    public void changeConfiguration(Config config) {
        latitude = config.getInt("map.latitude");
        longitude = config.getInt("map.longitude");
        minElevation = config.getInt("map.min_elevation");
        maxElevation = config.getInt("map.max_elevation");
        seed = config.getLong("map.seed");
        octaves = config.getInt("noise.octaves");
        frequency = config.getDouble("noise.frequency");
        samples = config.getInt("noise.samples");
    }

    public void fillImage(boolean useImage) {
        if (useImage) {
            image = new double[longitude * latitude];
            if (earth != null) {
                int[] earthImage = new int[earth.getWidth() * earth.getHeight()];
                earth.getRGB(0, 0, earth.getWidth(), earth.getHeight(), earthImage, 0, earth.getWidth());
                for (int y = 0; y < earth.getHeight(); y++) {
                    for (int x = 0; x < earth.getWidth(); x++) {
                        image[(y * longitude) + x] = earthImage[(y * earth.getWidth()) + x] & 0xFF;
                    }
                }
            } else {
                for (int y = 0; y < latitude; y++) {
                    for (int x = 0; x < longitude; x++) {
                        float s = x / (float) longitude;
                        float t = y / (float) latitude;
                        double nx = Math.cos(s * 2 * Math.PI) * 1.0 / (2 * Math.PI);
                        double ny = Math.cos(t * 2 * Math.PI) * 1.0 / (2 * Math.PI);
                        double nz = Math.sin(s * 2 * Math.PI) * 1.0 / (2 * Math.PI);
                        double nw = Math.sin(t * 2 * Math.PI) * 1.0 / (2 * Math.PI);
                        image[(y * longitude) + x] = mod.get(nx, ny, nz, nw);
                    }
                }
            }
        }
    }
}
