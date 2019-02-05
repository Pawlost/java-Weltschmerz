package com.ritualsoftheold.weltschmerz.landmass.geometry;

import com.ritualsoftheold.weltschmerz.landmass.events.CircleEvent;
import com.ritualsoftheold.weltschmerz.landmass.events.Node;

public class DataNode extends Node {

    public DataNode(Point DP) {
        DataPoint = DP;
    }
    public Point DataPoint;

    public CircleEvent CircleCheckDataNode(double ys) {
        final DataNode l = Node.LeftDataNode(this);
        final DataNode r = Node.RightDataNode(this);
        if (l == null || r == null || l.DataPoint == r.DataPoint
                || l.DataPoint == DataPoint || DataPoint == r.DataPoint)
            return null;
        if (ccw(l.DataPoint, DataPoint, r.DataPoint) <= 0)
            return null;

        final Point Center = CircumCircleCenter(l.DataPoint,
                DataPoint, r.DataPoint);
        final CircleEvent VC = new CircleEvent(this, l, r, Center);
        if (VC.getY() >= ys)
            return VC;

        return null;
    }

    private static int ccw(Point P0, Point P1, Point P2) {
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
