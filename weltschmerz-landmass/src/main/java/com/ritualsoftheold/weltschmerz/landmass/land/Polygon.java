package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Point;

public class Polygon{
    public final Point centroid;

    public Polygon(Point center) {
        this.centroid = center;
    }

    public boolean contains(Point point) {
        return false;
    }

    public java.awt.Polygon getSwingPolygon() {
        return null;
    }
}
