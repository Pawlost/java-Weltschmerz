package com.ritualsoftheold.weltschmerz.noise.generators;

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

    public ChunkNoise(long seed){
        gen = new ModuleFractal();
        gen.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.GRADIENT);
        gen.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
        gen.setNumOctaves(OCTAVES);
        gen.setFrequency(FREQUENCY);
        gen.setType(ModuleFractal.FractalType.FBM);
        gen.setSeed(seed);
    }

    public void generateNoise(String level, double chunkElevationValue) {
        switch (level) {
            case "MOUNTAIN":
                min = 0;
                max = 15;
                break;
            default:
                min = 0;
                max = 4;
                break;
        }


        if(chunkElevationValue < 0){
            min = Math.abs(min) + ((16-(Math.abs(chunkElevationValue)%16))*4);
        }else{
            min = Math.abs(min) + ((Math.abs((chunkElevationValue)%16))*4);
        }

        if(chunkElevationValue < 0){
            max = Math.abs(max) + ((16-(Math.abs(chunkElevationValue)%16))*4);
        }else{
            max = Math.abs(max) + ((Math.abs((chunkElevationValue)%16))*4);
        }

        mod = new ModuleAutoCorrect(min, max);
        mod.setSource(gen);// set source (can usually be either another Module or a double value; see specific module for details)
        mod.setSamples(SAMPLES); // set how many samples to take
        mod.calculate2D(); // perform the calculations
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getNoise(int x, int z){
        return mod.get(x, z);
    }
}
