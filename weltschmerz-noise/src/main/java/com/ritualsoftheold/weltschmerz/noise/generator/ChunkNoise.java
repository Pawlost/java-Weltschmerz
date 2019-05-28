package com.ritualsoftheold.weltschmerz.noise.generator;

import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleFractal;

public class ChunkNoise {

    private ModuleFractal gen;
    private double max;
    private double min;
    private static final int OCTAVES = 3;
    private static final float FREQUENCY = 0.1f;
    private static final int SAMPLES = 1000;
    private  ModuleAutoCorrect mod;

    public ChunkNoise(long seed, double min, double max){
        this.min = min;
        this.max = max;

        gen = new ModuleFractal();
        gen.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.GRADIENT);
        gen.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
        gen.setNumOctaves(OCTAVES);
        gen.setFrequency(FREQUENCY);
        gen.setType(ModuleFractal.FractalType.FBM);
        gen.setSeed(seed);

       generateNoise();
    }

    private void generateNoise(){
        mod = new ModuleAutoCorrect(min, max);
        mod.setSource(gen);// set source (can usually be either another Module or a double value; see specific module for details)
        mod.setSamples(SAMPLES); // set how many samples to take
        mod.calculate2D(); // perform the calculations
    }

    public double getNoise(int x, int z){
        return mod.get(x, z);
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }
}
