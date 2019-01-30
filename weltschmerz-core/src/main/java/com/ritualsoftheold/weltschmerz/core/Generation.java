package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.WeltschmerzNoise;

import java.lang.reflect.Type;
import java.util.Random;

public abstract class Generation {

    static final int SHAPES = 5;

    public static Shape getNext(Shape shape, boolean back){
        switch (shape){
            case SEA:
                return  back ?  Shape.SEA:Shape.SHORELINE;
            case SHORELINE:
                return  back ?  Shape.SEA:Shape.PLAINS;
            case PLAINS:
                return  back ?  Shape.SHORELINE:Shape.HILLS;
            case HILLS:
                return  back ?  Shape.PLAINS:Shape.MOUNTAINS;
            case MOUNTAINS:
                return  back ?  Shape.HILLS:Shape.MOUNTAINS;
                default:
                    return null;
        }
    }

    static Shape getRandomShape(int index){
        switch (index){
            case 1:
                return Shape.SEA;
            case 2:
                return Shape.SHORELINE;
            case 3:
                return Shape.PLAINS;
            case 4:
                return Shape.HILLS;
            case 5:
                return Shape.MOUNTAINS;
         default:
             return null;
        }
    }

}
