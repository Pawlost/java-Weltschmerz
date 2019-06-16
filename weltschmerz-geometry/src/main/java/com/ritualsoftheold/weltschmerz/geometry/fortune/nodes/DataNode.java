package com.ritualsoftheold.weltschmerz.geometry.fortune.nodes;

import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.geometry.units.Vertex;
import com.ritualsoftheold.weltschmerz.geometry.fortune.events.CircleEvent;

public class DataNode extends Node {

    public Point point;

    public DataNode(Point point) {
        this.point = point;
    }

    public CircleEvent CircleCheckDataNode(double ys) {
        DataNode l = leftDataNode(this);
        DataNode r = rightDataNode(this);
        if (l == null || r == null || l.point == r.point
                || l.point == point || point == r.point)
            return null;
        if (ccw(l.point, point, r.point) <= 0)
            return null;

        Vertex center = circumCircleCenter(l.point, point, r.point);
        CircleEvent VC = new CircleEvent(this, l, r, center);
        if (VC.getY() >= ys)
            return VC;

        return null;
    }

    private static int ccw(Point P0, Point P1, Point P2) {
        double dx1, dx2, dy1, dy2;
        dx1 = P1.x - P0.x;
        dy1 = P1.y - P0.y;
        dx2 = P2.x - P0.x;
        dy2 = P2.y - P0.y;
        if (dx1 * dy2 > dy1 * dx2)
            return +1;
        if (dx1 * dy2 < dy1 * dx2)
            return -1;
        if (dx1 * dx2 < 0 || dy1 * dy2 < 0)
            return -1;
        return 0;
    }
}
