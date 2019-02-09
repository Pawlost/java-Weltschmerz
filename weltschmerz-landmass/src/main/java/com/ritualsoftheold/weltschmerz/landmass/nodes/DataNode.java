package com.ritualsoftheold.weltschmerz.landmass.nodes;

import com.ritualsoftheold.weltschmerz.landmass.events.CircleEvent;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Vertex;

public class DataNode extends Node {

    public Centroid point;

    public DataNode(Centroid point) {
        this.point = point;
    }

    public CircleEvent CircleCheckDataNode(double ys) {
        DataNode l = Node.leftDataNode(this);
        DataNode r = Node.rightDataNode(this);
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

    private static int ccw(Centroid P0, Centroid P1, Centroid P2) {
        double dx1, dx2, dy1, dy2;
        dx1 = P1.getX() - P0.getX();
        dy1 = P1.getY() - P0.getY();
        dx2 = P2.getX() - P0.getX();
        dy2 = P2.getY() - P0.getY();
        if (dx1 * dy2 > dy1 * dx2)
            return +1;
        if (dx1 * dy2 < dy1 * dx2)
            return -1;
        if (dx1 * dx2 < 0 || dy1 * dy2 < 0)
            return -1;
        return 0;
    }
}
