package com.ritualsoftheold.weltschmerz.landmass.events;

import com.ritualsoftheold.weltschmerz.landmass.PrecisionMath;
import com.ritualsoftheold.weltschmerz.landmass.geometry.DataNode;
import com.ritualsoftheold.weltschmerz.landmass.geometry.EdgeNode;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Point;
import com.ritualsoftheold.weltschmerz.landmass.geometry.VoronoiEdge;

import java.util.ArrayList;
import java.util.HashSet;

public class DataEvent extends Event {
    public DataEvent(Point DP) {
        DataPoint = DP;
    }
    private Point DataPoint;

    //Will return the new root (unchanged except in start-up)
    @Override
    public Node process(Node Root, double ys,
                 HashSet<Point> vertList,
                 HashSet<VoronoiEdge> edgeList,
                 ArrayList<DataNode> CircleCheckList)
    {
        if (Root == null) {
            Root = new DataNode(DataPoint);
            CircleCheckList.add((DataNode) Root);
            return Root;
        }

        // 1. Find the node to be replaced
        final Node C = Node.FindDataNode(Root, ys, DataPoint.getX());

        // 2. Create the subtree (ONE Edge, but two VEdgeNodes)
        final VoronoiEdge VE = new VoronoiEdge();
        VE.LeftData = ((DataNode) C).DataPoint;
        VE.RightData = DataPoint;
        VE.VVertexA = Point.UNKNOWN;
        VE.VVertexB = Point.UNKNOWN;
        edgeList.add(VE);

        Node SubRoot;
        if (PrecisionMath.eq(VE.LeftData.getY(), VE.RightData.getY())) {
            DataNode l, r;
            if (VE.LeftData.getX() < VE.RightData.getX()) {
                l = new DataNode(VE.LeftData);
                r = new DataNode(VE.RightData);
                SubRoot = new EdgeNode(VE, false, l, r);
            } else {
                l = new DataNode(VE.RightData);
                r = new DataNode(VE.LeftData);
                SubRoot = new EdgeNode(VE, true, l, r);
            }
            CircleCheckList.add(l);
            CircleCheckList.add(r);
        } else {
            DataNode l = new DataNode(VE.LeftData);
            DataNode rl = new DataNode(VE.RightData);
            DataNode rr = new DataNode(VE.LeftData);
            EdgeNode r = new EdgeNode(VE, true, rl, rr);
            SubRoot = new EdgeNode(VE, false, l, r);
            CircleCheckList.add(l);
            CircleCheckList.add(rl);
            CircleCheckList.add(rr);
        }

        // 3. Apply subtree
        Node parent = C.getParent();
        if (parent == null)
            return SubRoot;
        parent.Replace(C, SubRoot);
        return Root;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DataEvent))
            return false;

        final DataEvent o = (DataEvent) obj;
        return DataPoint.equals(o.DataPoint);
    }

    @Override
    public double getX() {
        return DataPoint.getX();
    }

    @Override
    public double getY() {
        return DataPoint.getY();
    }

    public Point getDatum() {
        return DataPoint;
    }
}
