package com.ritualsoftheold.weltschmerz.landmass.nodes;

import com.ritualsoftheold.weltschmerz.landmass.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Vertex;

public abstract class Node {

    public Node() {
        _Left = null;
        _Right = null;
    }


    public Node(Node left, Node right) {
        setLeft(left);
        setRight(right);
    }


    public Node getLeft() {
        return _Left;
    }


    private void setLeft(Node value) {
        _Left = value;
        value._Parent = this;
    }


    public Node getRight() {
        return _Right;
    }


    private void setRight(Node value) {
        _Right = value;
        value._Parent = this;
    }


    public Node getParent() {
        return _Parent;
    }


    public void Replace(Node ChildOld, Node ChildNew) {
        if (_Left == ChildOld)
            setLeft(ChildNew);
        else if (_Right == ChildOld)
            setRight(ChildNew);
        else
            throw new RuntimeException("Child not found in Node.Replace!");
        ChildOld._Parent = null;
    }

    public static DataNode leftDataNode(DataNode current) {
        Node C = current;

        // 1. Up
        do {
            if (C._Parent == null)
                return null;
            if (C._Parent._Left == C) {
                C = C._Parent;
                continue;
            } else {
                C = C._Parent;
                break;
            }
        } while (true);

        // 2. One Left
        C = C._Left;

        // 3. Down
        while (C._Right != null)
            C = C._Right;

        return (DataNode) C;
    }


    public static DataNode rightDataNode(DataNode Current) {
        Node C = Current;

        // 1. Up
        do {
            if (C._Parent == null)
                return null;
            if (C._Parent._Right == C) {
                C = C._Parent;
                continue;
            } else {
                C = C._Parent;
                break;
            }
        } while (true);

        // 2. One Right
        C = C._Right;

        // 3. Down
        while (C._Left != null)
            C = C._Left;

        return (DataNode) C;
    }


    public static BorderNode edgeToRightDataNode(DataNode Current) {
        Node C = Current;

        // 1. Up
        do {
            if (C._Parent == null)
                throw new RuntimeException("No Left Leaf found!");
            if (C._Parent._Right == C) {
                C = C._Parent;
                continue;
            } else {
                C = C._Parent;
                break;
            }
        } while (true);
        return (BorderNode) C;
    }


    public static DataNode findDataNode(Node Root, double ys, double x) {
        Node C = Root;
        do {
            if (C instanceof DataNode)
                return (DataNode) C;
            if (((BorderNode) C).Cut(ys, x) < 0)
                C = C._Left;
            else
                C = C._Right;
        } while (true);
    }


    public static Vertex circumCircleCenter(Centroid left, Centroid middle, Centroid right) {
        if (left == middle || middle == right || left == right)
            throw new IllegalArgumentException("Need three different points!");
        double tx = (left.getX() + right.getX()) / 2;
        double ty = (left.getY() + right.getY()) / 2;

        double vx = (middle.getX() + right.getX()) / 2;
        double vy = (middle.getY() + right.getY()) / 2;

        double ux, uy, wx, wy;

        if (left.getX() == right.getX()) {
            ux = 1;
            uy = 0;
        } else {
            ux = (right.getY() - left.getY()) / (left.getX() - right.getX());
            uy = 1;
        }

        if (middle.getX() == right.getX()) {
            wx = -1;
            wy = 0;
        } else {
            wx = (middle.getY() - right.getY()) / (middle.getX() - right.getX());
            wy = -1;
        }

        final double alpha = (wy * (vx - tx) - wx * (vy - ty))
                / (ux * wy - wx * uy);

        return new Vertex(tx + alpha * ux, ty + alpha * uy, null);
    }

    private Node _Parent = null;

    private Node _Left = null, _Right = null;
}
