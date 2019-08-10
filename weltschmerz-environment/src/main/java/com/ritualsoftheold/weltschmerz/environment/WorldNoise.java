package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleFractal;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WorldNoise {
    private ModuleAutoCorrect mod;
    private BufferedImage earth;
    private Configuration conf;

    public WorldNoise(Configuration configuration){
        this(configuration, null);
    }

    public WorldNoise(Configuration configuration, BufferedImage earth){
        conf = configuration;
        init();
        this.earth = earth;
    }

    private void init(){
        ModuleFractal gen = new ModuleFractal();
        gen.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.GRADIENT);
        gen.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
        gen.setNumOctaves(conf.octaves);
        gen.setFrequency(conf.frequency);
        gen.setType(ModuleFractal.FractalType.FBM);
        gen.setSeed(conf.seed);
        mod = new ModuleAutoCorrect(conf.minElevation, conf.maxElevation);
        mod.setSource(gen);// set source (can usually be either another Module or a double value; see specific module for details)
        mod.setSamples(conf.samples); // set how many samples to take
        mod.calculate4D(); // perform the calculations
    }
    public double getNoise(int x, int y){
        if(!conf.useEarthImage) {
            float s = x / (float) conf.longitude;
            float t = y / (float) conf.latitude;
            double nx = Math.cos(s * 2 * Math.PI) * 1.0 / (2 * Math.PI);
            double ny = Math.cos(t * 2 * Math.PI) * 1.0 / (2 * Math.PI);
            double nz = Math.sin(s * 2 * Math.PI) * 1.0 / (2 * Math.PI);
            double nw = Math.sin(t * 2 * Math.PI) * 1.0 / (2 * Math.PI);
            return (mod.get(nx, ny, nz, nw));
        }else{
            return new Color(earth.getRGB(x, y)).getRed();
        }
    }

    public void changeConfiguration(Configuration configuration){
        //Creates basic fractal module
        conf = configuration;
    }
}
