package com.ritualsoftheold.weltschmerz.noise.generators;

import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;
import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.noise.Generation;
import com.ritualsoftheold.weltschmerz.geometry.misc.Shape;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleFractal;


public class WorldNoise extends Generation {

    private ModuleFractal gen;
    private int worldWidth;
    private int worldHeight;
    private double max;
    private double min;
    private ModuleAutoCorrect mod;
    private int samples;
    public static final int MAX_SECTOR_HEIGHT_DIFFERENCE = 100;

    public WorldNoise(Configuration configuration){
        super(configuration.shapes);
        //Creates basic fractal module
        this.worldHeight = configuration.latitude;
        this.worldWidth = configuration.longitude;
        this.samples = configuration.samples;
        this.max = getShape("MOUNTAIN").max * MAX_SECTOR_HEIGHT_DIFFERENCE;
        this.min = getShape("OCEAN").position * MAX_SECTOR_HEIGHT_DIFFERENCE;

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

        mod = new ModuleAutoCorrect(min, max);
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

    public Shape makeLand(Shape shape, Point centroid) {
       if(shape == null) {
            double y = getNoise((int)centroid.x, (int)centroid.y);
            shape = landGeneration(y);
        }

        return shape;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

}
