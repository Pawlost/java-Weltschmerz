package com.ritualsoftheold.weltschmerz.landmass;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ritualsoftheold.weltschmerz.geometry.misc.PrecisionMath;
import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.geometry.fortune.events.CircleEvent;
import com.ritualsoftheold.weltschmerz.geometry.fortune.events.DataEvent;
import com.ritualsoftheold.weltschmerz.geometry.fortune.events.Event;
import com.ritualsoftheold.weltschmerz.geometry.units.Border;
import com.ritualsoftheold.weltschmerz.geometry.units.Vertex;
import com.ritualsoftheold.weltschmerz.geometry.units.VoronoiBorder;
import com.ritualsoftheold.weltschmerz.geometry.fortune.nodes.DataNode;
import com.ritualsoftheold.weltschmerz.geometry.fortune.nodes.Node;

import java.util.*;

public abstract class Fortune {
    public static Graph ComputeGraph(Set<Point> points){
        PriorityQueue<Event> queue = new PriorityQueue<>();
        for (Point p : points) {
            DataEvent ev = new DataEvent(p);
            if (!queue.contains(ev)) {
                queue.add(ev);
            }
        }
        return computeVoronoiGraph(queue);
    }

    private static Graph computeVoronoiGraph(PriorityQueue<Event> queue) {
        HashMap<DataNode, CircleEvent> CurrentCircles = new HashMap<>();
        HashSet<VoronoiBorder> edgeList = new HashSet<>();

        Node rootNode = null;
        while (queue.size() > 0) {
            Event VE = queue.poll();

            ArrayList<DataNode> CircleCheckList = new ArrayList<>();
            if (VE instanceof CircleEvent) {
                CircleEvent cev = (CircleEvent) VE;
                CurrentCircles.remove(cev.NodeN);
                if (!cev.Valid)
                    continue;
            }
            rootNode = VE.process(rootNode, VE.getY(), edgeList, CircleCheckList);

            for (DataNode VD : CircleCheckList) {
                if (CurrentCircles.containsKey(VD)) {
                    CircleEvent cev = CurrentCircles.remove(VD);
                    cev.Valid = false;
                }

                CircleEvent VCE = VD.CircleCheckDataNode(VE.getY());
                if (VCE != null) {
                    queue.add(VCE);
                    CurrentCircles.put(VD, VCE);
                }
            }

            if (VE instanceof DataEvent) {
                Point point = ((DataEvent) VE).getDatum();
                for (final CircleEvent VCE : CurrentCircles.values()) {
                    double dist = point.dist(VCE.center);
                    double offs = VCE.getY() - VCE.center.y;
                    if (PrecisionMath.lt(dist, offs))
                        VCE.Valid = false;
                }
            }
        }

        for  (VoronoiBorder VE : edgeList) {
            if (VE.Done)
                continue;
            if (VE.VVertexB == Vertex.UNKNOWN) {
                VE.AddVertex(Vertex.INFINITE);
                if (PrecisionMath.eq(VE.LeftData.y, VE.RightData.y)
                        && VE.LeftData.x < VE.RightData.x) {
                    Point T = VE.LeftData;
                    VE.LeftData = VE.RightData;
                    VE.RightData = T;
                }
            }
        }

        ArrayList<VoronoiBorder> MinuteEdges = new ArrayList<>();
        for (VoronoiBorder VE : edgeList) {
            if (VE.isPartlyInfinite() && VE.VVertexA.equals(VE.VVertexB)) {
                MinuteEdges.add(VE);
                // prevent rounding errors from expanding to holes
                for (VoronoiBorder VE2 : edgeList) {
                    if (VE2.VVertexA.equals(VE.VVertexA))
                        VE2.VVertexA = VE.VVertexA;
                    if (VE2.VVertexB.equals(VE.VVertexA))
                        VE2.VVertexB = VE.VVertexA;
                }
            }
        }
        for (VoronoiBorder VE : MinuteEdges)
            edgeList.remove(VE);

        Multimap<Point, Border> finalEdges = HashMultimap.create();
        Multimap<Point, Vertex> finalVertices = HashMultimap.create();
        for (VoronoiBorder VE : edgeList) {
            if(VE.isPartlyInfinite()) {
                Border border = VE.toEdge();
                finalEdges.put(border.datumA, border);
                finalEdges.put(border.datumB, border);
                finalVertices.put(border.datumA, border.vertexA);
                finalVertices.put(border.datumA, border.vertexB);
                finalVertices.put(border.datumB, border.vertexA);
                finalVertices.put(border.datumB, border.vertexB);
            }
        }

        return new Graph(finalEdges, finalVertices);
    }

}
