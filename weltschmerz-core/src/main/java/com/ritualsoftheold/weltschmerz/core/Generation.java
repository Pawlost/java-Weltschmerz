package com.ritualsoftheold.weltschmerz.core;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Generation {

    static final int SHAPES = 4;

    public static Shape getPrevious(Shape shape){
        switch (shape){
            case SHORELINE:
                return Shape.SEA;
            case PLAIN:
                return Shape.SHORELINE;
            case HILL:
                return  Shape.PLAIN;
            case MOUNTAIN:
                return Shape.HILL;
                default:
                    return null;
        }
    }

    public static Shape getNext(Shape shape){
        switch (shape){
            case SEA:
                return Shape.SHORELINE;
            case SHORELINE:
                return Shape.PLAIN;
            case PLAIN:
                return Shape.HILL;
            case HILL:
                return  Shape.MOUNTAIN;
            case MOUNTAIN:
                return Shape.MOUNTAIN;
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
                return Shape.PLAIN;
            case 3:
                return Shape.HILL;
            case 4:
                return Shape.MOUNTAIN;
         default:
             return Shape.SEA;
        }
    }

    public static Shape landGeneration(ArrayList<Double> elevation){
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
            return Shape.OCEAN;
        }else{
            return Shape.PLAIN;
        }
    }

}
