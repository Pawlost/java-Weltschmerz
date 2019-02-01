package com.ritualsoftheold.weltschmerz.core;

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

    static Shape getRandomShape(int index){
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
             return null;
        }
    }

}
