package com.ritualsoftheold.weltschmerz.geometry.misc;

import com.ritualsoftheold.weltschmerz.geometry.units.Vector;

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

    public static double toUnsignedRange(double value){
        return (value * 0.5) + 0.5;
    }

    public static Vector rotation(double angle){
        double sine = Math.sin(Math.toRadians(angle)), cosine = Math.cos(Math.toRadians(angle));
        return new Vector(cosine, -sine, sine, cosine);
    }

    public static double mix(double x, double y, double a) {
        return x * (1 - a) + y * a;
    }

    public static double nthRoot(double n, double base) {
        return Math.pow(Math.E, Math.log(base)/n);
    }
}