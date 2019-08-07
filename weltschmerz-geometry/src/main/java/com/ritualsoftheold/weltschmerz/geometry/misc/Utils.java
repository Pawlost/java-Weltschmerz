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
        double sine = Math.sin(Math.toRadians(angle));
        double cosine = Math.cos(Math.toRadians(angle));
        return new Vector(cosine, -sine, sine, cosine);
    }

    public static double mix(double x, double y, double a) {
        return x * (1 - a) + y * a;
    }

    public static Vector clamp(Vector vector, double min, double max) {
        double x = Math.max(Math.min(vector.x, max), min);
        double y = Math.max(Math.min(vector.y, max), min);
        double z = Math.max(Math.min(vector.z, max), min);
        double w = Math.max(Math.min(vector.w, max), min);
        return new Vector(x, y, z, w);
    }

    public static double nthRoot(double n, double base) {
        return Math.pow(Math.E, Math.log(base) / n);
    }

    public static boolean isLand(double elevation) {
        return elevation > 0;
    }

    public static Vector normalize(Vector value) {
        double diviseur = Math.pow(value.x, 2.0) + Math.pow(value.y, 2.0) + Math.pow(value.z, 2.0) + Math.pow(value.w, 2.0);
        return new Vector(value.x / diviseur, value.y / diviseur, value.z / diviseur, value.w / diviseur);
    }

    public static double lenght(Vector value) {
        return Math.sqrt(Math.pow(value.x, 2) + Math.pow(value.y, 2) + Math.pow(value.z, 2) + Math.pow(value.w, 2));
    }
}