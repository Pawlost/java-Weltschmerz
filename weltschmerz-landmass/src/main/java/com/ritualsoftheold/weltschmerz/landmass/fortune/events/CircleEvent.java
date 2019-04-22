package com.ritualsoftheold.weltschmerz.landmass.fortune.events;

import com.ritualsoftheold.weltschmerz.landmass.fortune.nodes.DataNode;
import com.ritualsoftheold.weltschmerz.landmass.PrecisionMath;
import com.ritualsoftheold.weltschmerz.landmass.fortune.nodes.BorderNode;
import com.ritualsoftheold.weltschmerz.landmass.fortune.nodes.Node;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Vertex;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.VoronoiBorder;

import java.util.ArrayList;
import java.util.HashSet;

public class CircleEvent extends Event {

    public DataNode NodeN, NodeL, NodeR;
    public Vertex center;
    public boolean Valid;

    public CircleEvent(DataNode n, DataNode l, DataNode r, Vertex c) {
        NodeN = n;
        NodeL = l;
        NodeR = r;
        center = c;
        Valid = true;
    }


    @Override
    public double getX() {
        return center.getX();
    }

    @Override
    public double getY() {
        double dist = NodeN.point.dist(center);
        return PrecisionMath.round(center.getY() + dist);
    }

    @Override
    public Node process(Node Root, double ys,
                        HashSet<VoronoiBorder> edgeList,
                        ArrayList<DataNode> CircleCheckList) {
        DataNode b = NodeN;
        DataNode a = Node.leftDataNode(b);
        DataNode c = Node.rightDataNode(b);
        if (a == null || b.getParent() == null || c == null
                || !a.point.equals(NodeL.point)
                || !c.point.equals(NodeR.point)) {
            return Root;
        }

        BorderNode eu = (BorderNode) b.getParent();
        CircleCheckList.add(a);
        CircleCheckList.add(c);

        // 1. Create the new Vertex
        Vertex VNew = new Vertex(center.getX(), center.getY());

        // 2. Find out if a or c are in a distand part of the tree (the other
        // is then b's sibling) and assign the new vertex
        BorderNode eo;
        Node eleft = eu.getLeft();
        Node eright = eu.getRight();
        if (eleft == b) {
            // c is sibling
            eo = Node.edgeToRightDataNode(a);

            // replace eu by eu's Right
            eu.getParent().Replace(eu, eright);
        } else {
            // a is sibling
            eo = Node.edgeToRightDataNode(b);

            // replace eu by eu's Left
            eu.getParent().Replace(eu, eleft);
        }
        eu.Edge.AddVertex(VNew);
        eo.Edge.AddVertex(VNew);

        // 2. Replace eo by new Edge
        VoronoiBorder VE = new VoronoiBorder();
        VE.LeftData = a.point;
        VE.RightData = c.point;
        VE.AddVertex(VNew);
        edgeList.add(VE);

        BorderNode VEN = new BorderNode(VE, false, eo.getLeft(), eo.getRight());
        Node parent = eo.getParent();
        if (parent == null)
            return VEN;
        parent.Replace(eo, VEN);
        return Root;
    }
}
