package com.ritualsoftheold.weltschmerz.landmass.events;

import com.ritualsoftheold.weltschmerz.landmass.Area;
import com.ritualsoftheold.weltschmerz.landmass.PrecisionMath;
import com.ritualsoftheold.weltschmerz.landmass.nodes.DataNode;
import com.ritualsoftheold.weltschmerz.landmass.nodes.Node;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Vertex;
import com.ritualsoftheold.weltschmerz.landmass.geometry.VoronoiBorder;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Event implements Comparable<Event>  {

    public abstract double getX();
    public abstract double getY();


    public abstract Node process(Node Root, double ys,
                                 HashSet<Vertex> vertList,
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
