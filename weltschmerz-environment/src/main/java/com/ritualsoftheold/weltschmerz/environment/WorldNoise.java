package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;
import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleFractal;

import java.util.Dictionary;


public class WorldNoise {

    private ModuleFractal gen;
    private int worldWidth;
    private int worldHeight;
    private ModuleAutoCorrect mod;
    private int samples;
    public static final int DIFFERENCE = 500;

    public WorldNoise(Configuration configuration){
        //Creates basic fractal module
        this.worldHeight = configuration.latitude;
        this.worldWidth = configuration.longitude;
        this.samples = configuration.samples;

        init(configuration.seed, configuration.octaves, configuration.frequency);
        generateNoise();
    }

    private void init(long seed, int octaves, double frequency){
        gen = new ModuleFractal();
        gen.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.GRADIENT);
        gen.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
        gen.setNumOctaves(octaves);
        gen.setFrequency(frequency);
        gen.setType(ModuleFractal.FractalType.FBM);
        gen.setSeed(seed);
        System.out.println("Noise generated");
    }

    private void generateNoise(){
        /*
         * ... route it through an autocorrection module...
         *
         * This module will sample it's source multiple times and attempt to
         * auto-correct the output to the range specified.
         */

        mod = new ModuleAutoCorrect(-DIFFERENCE, DIFFERENCE);
        mod.setSource(gen);// set source (can usually be either another Module or a double value; see specific module for details)
        mod.setSamples(samples); // set how many samples to take
        mod.calculate4D(); // perform the calculations
    }

    public double getNoise(int x, int y){
        float s=x/(float)worldWidth;
        float t=y/(float)worldHeight;
        double nx=Math.cos(s*2*Math.PI)*1.0/(2*Math.PI);
        double ny=Math.cos(t*2*Math.PI)*1.0/(2*Math.PI);
        double nz=Math.sin(s*2*Math.PI)*1.0/(2*Math.PI);
        double nw=Math.sin(t*2*Math.PI)*1.0/(2*Math.PI);
        return (mod.get(nx, ny, nz, nw));
    }

    public double getMax() {
        return DIFFERENCE;
    }

}
