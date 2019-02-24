package com.ritualsoftheold.weltschmerz.core;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public abstract class Generation {

    static final int SHAPES = 4;

    public static Shape getPrevious(Shape shape){
        switch (shape){
            case SHORELINE:
                return Shape.SEA;
            case PLAINS:
                return Shape.SHORELINE;
            case HILLS:
                return  Shape.PLAINS;
            case MOUNTAINS:
                return Shape.HILLS;
                default:
                    return null;
        }
    }

    public static Shape getNext(Shape shape){
        switch (shape){
            case SEA:
                return Shape.SHORELINE;
            case SHORELINE:
                return Shape.PLAINS;
            case PLAINS:
                return Shape.HILLS;
            case HILLS:
                return  Shape.MOUNTAINS;
            case MOUNTAINS:
                return Shape.MOUNTAINS;
            default:
                return null;
        }
    }

    public static int getNumShapes(Shape shape){
        int num;
        for (num = 1; shape != Shape.SEA; num++){
            shape = getPrevious(Objects.requireNonNull(shape));
        }
        return num;
    }

    static Shape getShape(int index){
        switch (index){
            case 1:
                return Shape.SHORELINE;
            case 2:
                return Shape.PLAINS;
            case 3:
                return Shape.HILLS;
            case 4:
                return Shape.MOUNTAINS;
         default:
             return Shape.SEA;
        }
    }

    public static boolean landGeneration(ArrayList<Double> elevation){
        int ocean = 0;
        int land = 0;

        for(double e:elevation) {
            if (e < 0.5) {
                ocean++;
            }else{
                land++;
            }
        }
        if (ocean > land) {
            return false;
        }else{
            return true;
        }
    }

    public static Color getColor(Shape shape){
        switch (shape){
            case SEA:
                return Color.CYAN;
            case SHORELINE:
                return Color.YELLOW;
            case PLAINS:
                return Color.GREEN;
            case HILLS:
                return Color.RED;
            case MOUNTAINS:
                return Color.WHITE;
            default:
                return Color.BLUE;
        }
    }

}
