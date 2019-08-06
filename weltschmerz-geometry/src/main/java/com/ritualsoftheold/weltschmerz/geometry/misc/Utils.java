package com.ritualsoftheold.weltschmerz.geometry.misc;

import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.geometry.units.Vector;

import java.awt.image.BufferedImage;
import java.nio.channels.Pipe;

public class Utils {

    public static double toUnsignedRange(double value) {
        return (value * 0.5) + 0.5;
    }

    public static Vector rotation(double angle) {
        double sine = Math.sin(Math.toRadians(angle)), cosine = Math.cos(Math.toRadians(angle));
        return new Vector(cosine, -sine, sine, cosine);
    }

    public static double mix(double x, double y, double a) {
        return x * (1 - a) + y * a;
    }

    public static double nthRoot(double n, double base) {
        return Math.pow(Math.E, Math.log(base) / n);
    }

    public static  boolean isLand(double elevation){
        return elevation > 0;
    }
}