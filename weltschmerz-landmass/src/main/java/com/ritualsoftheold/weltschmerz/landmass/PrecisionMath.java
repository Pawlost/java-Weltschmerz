package com.ritualsoftheold.weltschmerz.landmass;

public class PrecisionMath {
    public static double sigmoid(long seed, int modulo) {
        seed = seed % modulo;
        return 1/(1+Math.pow(Math.E,-(double) seed));
    }
}