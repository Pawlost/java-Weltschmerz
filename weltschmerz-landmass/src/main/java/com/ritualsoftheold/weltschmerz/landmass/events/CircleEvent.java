package com.ritualsoftheold.weltschmerz.landmass.events;

import com.ritualsoftheold.weltschmerz.landmass.PrecisionMath;
import com.ritualsoftheold.weltschmerz.landmass.geometry.DataNode;
import com.ritualsoftheold.weltschmerz.landmass.geometry.EdgeNode;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Point;
import com.ritualsoftheold.weltschmerz.landmass.geometry.VoronoiEdge;

import java.util.ArrayList;
import java.util.HashSet;

public class CircleEvent extends Event {

    public DataNode NodeN, NodeL, NodeR;
    public Point Center;
    public boolean Valid;

    public CircleEvent(DataNode n, DataNode l, DataNode r, Point c) {
        NodeN = n;
        NodeL = l;
        NodeR = r;
        Center = c;
        Valid = true;
    }


    @Override
    public double getX() {
        return Center.getX();
    }

    @Override
    public double getY() {
        double dist = NodeN.DataPoint.dist(Center);
        return PrecisionMath.round(Center.getY() + dist);
    }

    @Override
    public Node process(Node Root, double ys,
                        HashSet<Point> vertList,
                        HashSet<VoronoiEdge> edgeList,
                        ArrayList<DataNode> CircleCheckList) {
        final DataNode b = NodeN;
        final DataNode a = Node.LeftDataNode(b);
        final DataNode c = Node.RightDataNode(b);
        if (a == null || b.getParent() == null || c == null
                || !a.DataPoint.equals(NodeL.DataPoint)
                || !c.DataPoint.equals(NodeR.DataPoint)) {
            return Root;
        }

        final EdgeNode eu = (EdgeNode) b.getParent();
        CircleCheckList.add(a);
        CircleCheckList.add(c);

        // 1. Create the new Vertex
        final Point VNew = new Point(Center.getX(), Center.getY());
        vertList.add(VNew);

        // 2. Find out if a or c are in a distand part of the tree (the other
        // is then b's sibling) and assign the new vertex
        EdgeNode eo;
        final Node eleft = eu.getLeft();
        final Node eright = eu.getRight();
        if (eleft == b) {
            // c is sibling
            eo = Node.EdgeToRightDataNode(a);

            // replace eu by eu's Right
            eu.getParent().Replace(eu, eright);
        } else {
            // a is sibling
            eo = Node.EdgeToRightDataNode(b);

            // replace eu by eu's Left
            eu.getParent().Replace(eu, eleft);
        }
        eu.Edge.AddVertex(VNew);
        eo.Edge.AddVertex(VNew);

        // 2. Replace eo by new Edge
        VoronoiEdge VE = new VoronoiEdge();
        VE.LeftData = a.DataPoint;
        VE.RightData = c.DataPoint;
        VE.AddVertex(VNew);
        edgeList.add(VE);

        final EdgeNode VEN = new EdgeNode(VE, false, eo.getLeft(), eo
                .getRight());
        final Node parent = eo.getParent();
        if (parent == null)
            return VEN;
        parent.Replace(eo, VEN);
        return Root;
    }
}
