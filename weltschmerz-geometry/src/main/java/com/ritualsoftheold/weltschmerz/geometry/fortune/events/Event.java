package com.ritualsoftheold.weltschmerz.geometry.fortune.events;

import com.ritualsoftheold.weltschmerz.geometry.misc.PrecisionMath;
import com.ritualsoftheold.weltschmerz.geometry.units.VoronoiBorder;
import com.ritualsoftheold.weltschmerz.geometry.fortune.nodes.DataNode;
import com.ritualsoftheold.weltschmerz.geometry.fortune.nodes.Node;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Event implements Comparable<Event>  {

    public abstract double getX();
    public abstract double getY();


    public abstract Node process(Node Root, double ys,
                                 HashSet<VoronoiBorder> edgeList,
                                 ArrayList<DataNode> CircleCheckList);

    public int compareTo(Event ev) {
        if (!PrecisionMath.eq(getY(), ev.getY())) {
            if (getY() < ev.getY())
                return -1;
            else if (getY() > ev.getY())
                return 1;
        } else {
            if (getX() < ev.getX())
                return -1;
            else if (getX() > ev.getX())
                return 1;
        }
        return 0;
    }
}
