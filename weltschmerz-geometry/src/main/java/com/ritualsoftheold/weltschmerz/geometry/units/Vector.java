package com.ritualsoftheold.weltschmerz.geometry.units;

public class Vector {
    public final double x;
    public final double y;
    public final Point point;
    public Vector(double x, double y, Point point){
        this.x = x;
        this.y = y;
        this.point = point;
    }

    public Point getIntersection(Vector vector){
        double t = ((vector.point.x *vector.x * vector.y) + (vector.x * point.y) - (vector.x * vector.point.y)
                - (point.x *(vector.x * vector.y)))/(x * vector.x * vector.y - vector.x * y);
        return new Point(point.x + (x * t), point.y + (y * t));
    }
}
