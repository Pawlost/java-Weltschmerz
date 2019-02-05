package com.ritualsoftheold.weltschmerz.landmass.events;

import com.ritualsoftheold.weltschmerz.landmass.geometry.DataNode;
import com.ritualsoftheold.weltschmerz.landmass.geometry.EdgeNode;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Point;

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

    public static DataNode LeftDataNode(DataNode Current) {
        Node C = Current;

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


    public static DataNode RightDataNode(DataNode Current) {
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


    public static EdgeNode EdgeToRightDataNode(DataNode Current) {
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
        return (EdgeNode) C;
    }


    public static DataNode FindDataNode(Node Root, double ys, double x) {
        Node C = Root;
        do {
            if (C instanceof DataNode)
                return (DataNode) C;
            if (((EdgeNode) C).Cut(ys, x) < 0)
                C = C._Left;
            else
                C = C._Right;
        } while (true);
    }


    public static Point CircumCircleCenter(Point A, Point B, Point C) {
        if (A == B || B == C || A == C)
            throw new IllegalArgumentException("Need three different points!");
        final double tx = (A.getX() + C.getX()) / 2;
        final double ty = (A.getY() + C.getY()) / 2;

        final double vx = (B.getX() + C.getX()) / 2;
        final double vy = (B.getY() + C.getY()) / 2;

        double ux, uy, wx, wy;

        if (A.getX() == C.getX()) {
            ux = 1;
            uy = 0;
        } else {
            ux = (C.getY() - A.getY()) / (A.getX() - C.getX());
            uy = 1;
        }

        if (B.getX() == C.getX()) {
            wx = -1;
            wy = 0;
        } else {
            wx = (B.getY() - C.getY()) / (B.getX() - C.getX());
            wy = -1;
        }

        final double alpha = (wy * (vx - tx) - wx * (vy - ty))
                / (ux * wy - wx * uy);

        return new Point(tx + alpha * ux, ty + alpha * uy);
    }

    private Node _Parent = null;

    private Node _Left = null, _Right = null;
}
