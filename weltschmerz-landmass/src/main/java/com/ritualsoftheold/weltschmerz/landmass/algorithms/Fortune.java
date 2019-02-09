package com.ritualsoftheold.weltschmerz.landmass.algorithms;

import com.ritualsoftheold.weltschmerz.landmass.Voronoi;
import com.ritualsoftheold.weltschmerz.landmass.PrecisionMath;
import com.ritualsoftheold.weltschmerz.landmass.events.CircleEvent;
import com.ritualsoftheold.weltschmerz.landmass.events.DataEvent;
import com.ritualsoftheold.weltschmerz.landmass.events.Event;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Vertex;
import com.ritualsoftheold.weltschmerz.landmass.geometry.VoronoiBorder;
import com.ritualsoftheold.weltschmerz.landmass.nodes.DataNode;
import com.ritualsoftheold.weltschmerz.landmass.nodes.Node;

import java.util.*;

public abstract class Fortune {
    public static Voronoi ComputeGraph(Centroid[] vertices){
        PriorityQueue<Event> queue = new PriorityQueue<>();
        for (Centroid v : vertices) {
            DataEvent ev = new DataEvent(v);
            if (!queue.contains(ev)) {
                queue.add(ev);
            }
        }
        return ComputeVoronoiGraph(queue);
    }

    private static Voronoi ComputeVoronoiGraph(PriorityQueue<Event> queue) {
        HashMap<DataNode, CircleEvent> CurrentCircles = new HashMap<>();
        HashSet<Vertex> vertexList = new HashSet<>();
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
            rootNode = VE.process(rootNode, VE.getY(), vertexList, edgeList, CircleCheckList);

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
                Centroid centroid = ((DataEvent) VE).getDatum();
                for (final CircleEvent VCE : CurrentCircles.values()) {
                    double dist = centroid.dist(VCE.center);
                    double offs = VCE.getY() - VCE.center.getY();
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
                if (PrecisionMath.eq(VE.LeftData.getY(), VE.RightData.getY())
                        && VE.LeftData.getX() < VE.RightData.getX()) {
                    Centroid T = VE.LeftData;
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

        HashSet<Border> finalEdges = new HashSet<>();
        for (VoronoiBorder VE : edgeList) {
            if(VE.isPartlyInfinite()) {
                finalEdges.add(VE.toEdge());
            }
        }

        return new Voronoi(finalEdges);
    }

}
