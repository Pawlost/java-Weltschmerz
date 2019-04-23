package com.ritualsoftheold.weltschmerz.noise;

import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleFractal;

public class ChunkNoise {

    private ModuleFractal gen;
    private double max;
    private double min;
    private static final int OCTAVES = 8;
    private static final int FREQUENCY = 1000;
    private static final int SAMPLES = 8;
    private  ModuleAutoCorrect mod;

    public ChunkNoise(Location location){
        this.max = location.getShape().max;
        this.min = location.getShape().min;

       init(location.getSeed());
       generateNoise();
    }

    private void init(long seed){
        gen = new ModuleFractal();
        gen.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.GRADIENT);
        gen.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
        gen.setNumOctaves(OCTAVES);
        gen.setFrequency(FREQUENCY);
        gen.setType(ModuleFractal.FractalType.FBM);
        gen.setSeed(seed);
    }

    private void generateNoise(){
        /*
         * ... route it through an autocorrection module...
         *
         * This module will sample it's source multiple times and attempt to
         * auto-correct the output to the range specified.
         */

        mod = new ModuleAutoCorrect(min, max);
        mod.setSource(gen);// set source (can usually be either another Module or a double value; see specific module for details)
        mod.setSamples(SAMPLES); // set how many samples to take
        mod.calculate2D(); // perform the calculations
    }

    public double getNoise(int x, int y){
        return mod.get(x, y);
    }
}
