package com.ritualsoftheold.weltschmerz.noise;

import java.util.HashMap;

public abstract class Generation {
    private HashMap<String, Shape> shapes;

    public Generation(HashMap<String, Shape> shapes) {
        this.shapes = shapes;
    }

    private static String getKey(int index) {
        switch (index) {
            case 0:
                return "OCEAN";
            case 1:
                return "SEA";
            case 2:
                return "SHORELINE";
            case 3:
                return "PLAIN";
            case 4:
                return "HILL";
            default:
                return "MOUNTAIN";
        }
    }

    public Shape landGeneration(double e) {
        if (e < shapes.get("OCEAN").max) {
            String key = getKey(0);
            return shapes.get(key);
        } else if (e > shapes.get("SEA").min && e < shapes.get("SEA").max) {
            String key = getKey(1);
            return shapes.get(key);
        } else if (e > shapes.get("SHORELINE").min && e < shapes.get("SHORELINE").max) {
            String key = getKey(2);
            return shapes.get(key);
        } else if (e > shapes.get("PLAIN").min && e < shapes.get("PLAIN").max) {
            String key = getKey(3);
            return shapes.get(key);
        } else if (e > shapes.get("HILL").min && e < shapes.get("HILL").max) {
            String key = getKey(4);
            return shapes.get(key);
        } else if (e > shapes.get("MOUNTAIN").min) {
            String key = getKey(5);
            return shapes.get(key);
        }
        return null;
    }

    public Shape getShape(String key){
        return shapes.get(key);
    }
}