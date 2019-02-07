package com.ritualsoftheold.weltschmerz.landmass;

public class PrecisionMath {
    private static double precision = 10000000000.0;

    public static double round(double val) {
        return Math.rint(val * precision) / precision;
    }

    public static boolean eq(double a, double b) {
        return Math.abs(a - b) * precision < 1.0;
    }

    public static boolean lt(double a, double b) {
        return (b - a) * precision > 1.0;
    }
}