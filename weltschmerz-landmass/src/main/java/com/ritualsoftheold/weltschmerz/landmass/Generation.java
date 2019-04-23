package com.ritualsoftheold.weltschmerz.landmass;

import java.util.ArrayList;
import java.util.Arrays;
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

    public Shape landGeneration(ArrayList<Double> elevation) {
        int[] level = new int[6];
        Arrays.fill(level, 0);

        for (double e : elevation) {
            if (e < shapes.get("OCEAN").max) {
                level[0] += 1;
            } else if (e > shapes.get("SEA").min && e < shapes.get("SEA").max) {
                level[1] += 1;
            } else if (e > shapes.get("SHORELINE").min && e < shapes.get("SHORELINE").max) {
                level[2] += 1;
            } else if (e > shapes.get("PLAIN").min && e < shapes.get("PLAIN").max) {
                level[3] += 1;
            } else if (e > shapes.get("HILL").min && e < shapes.get("HILL").max) {
                level[4] += 1;
            } else if (e > shapes.get("MOUNTAIN").min && e < shapes.get("MOUNTAIN").max) {
                level[5] += 1;
            }
        }
        int max = 0;
        int index = 0;
        for (int i = 0; i < level.length; i++) {
            if (level[i] > max) {
                max = level[i];
                index = i;
            }
        }

        String key = getKey(index);
        return shapes.get(key);
    }

    public Shape getShape(String key){
        return shapes.get(key);
    }
}