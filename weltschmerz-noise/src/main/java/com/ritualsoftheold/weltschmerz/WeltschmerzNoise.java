package com.ritualsoftheold.weltschmerz;

import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleFractal;

public class WeltschmerzNoise {

    private ModuleFractal gen;

    public WeltschmerzNoise(long seed, int octaves, double frequency){
        //Creates basic fractal module
        gen = new ModuleFractal();
        gen.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.GRADIENT);
        gen.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
        gen.setNumOctaves(octaves);
        gen.setFrequency(frequency);
        gen.setType(ModuleFractal.FractalType.FBM);
        gen.setSeed(seed);
    }

    public ModuleAutoCorrect generateNoise(){
        /*
         * ... route it through an autocorrection module...
         *
         * This module will sample it's source multiple times and attempt to
         * auto-correct the output to the range specified.
         */

        ModuleAutoCorrect ac = new ModuleAutoCorrect(0, 1);
        ac.setSource(gen); // set source (can usually be either another Module or a double value; see specific module for details)
        ac.setSamples(100000); // set how many samples to take
        ac.calculate2D(); // perform the calculations
        return ac;
    }
}
