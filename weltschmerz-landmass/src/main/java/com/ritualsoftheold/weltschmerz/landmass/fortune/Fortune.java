package com.ritualsoftheold.weltschmerz.landmass.fortune;

import com.ritualsoftheold.weltschmerz.landmass.Graph;
import com.ritualsoftheold.weltschmerz.landmass.PrecisionMath;
import com.ritualsoftheold.weltschmerz.landmass.events.CircleEvent;
import com.ritualsoftheold.weltschmerz.landmass.events.DataEvent;
import com.ritualsoftheold.weltschmerz.landmass.events.Event;
import com.ritualsoftheold.weltschmerz.landmass.events.Node;
import com.ritualsoftheold.weltschmerz.landmass.geometry.DataNode;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Edge;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Point;
import com.ritualsoftheold.weltschmerz.landmass.geometry.VoronoiEdge;

import java.util.*;

public abstract class Fortune {


         //Compute the Voronoi diagram for the given set of points.
        public static Graph ComputeVoronoiGraph(Point[] points) {
            // Create a priority queue of data events for each point,
            // and pass that to the main algorithm.  Ignore dupes in the
            // input points.
            PriorityQueue<Event> queue = new PriorityQueue<>();
            for (Point v : points) {
                DataEvent ev = new DataEvent(v);
                if (!queue.contains(ev)) {
                    queue.add(ev);
                }
            }

            return ComputeVoronoiGraph(queue);
        }

        public static Graph FilterVG(Graph graph, double min) {
            // Go through all the edges, and copy the ones which aren't too
            // small to a new edge list.
            final HashSet<Edge> edgeList = new HashSet<>();
            Iterator<Edge> edges = graph.getEdges();
            while (edges.hasNext()) {
                Edge e = edges.next();
                Point da = e.getDatumA();
                Point db = e.getDatumB();
                if (da.dist(db) >= min)
                    edgeList.add(e);
            }

            // Make a new graph of the new edges.
            return new Graph(edgeList);
        }

        private static Graph ComputeVoronoiGraph(PriorityQueue<Event> queue) {
            HashMap<DataNode, CircleEvent> CurrentCircles = new HashMap<DataNode, CircleEvent>();
            HashSet<Point> vertexList = new HashSet<Point>();
            HashSet<VoronoiEdge> edgeList = new HashSet<VoronoiEdge>();

            Node RootNode = null;
            while (queue.size() > 0) {
                Event VE = queue.poll();
                ArrayList<DataNode> CircleCheckList = new ArrayList<DataNode>();
                if (VE instanceof CircleEvent) {
                    CircleEvent cev = (CircleEvent) VE;
                    CurrentCircles.remove(cev.NodeN);
                    if (!cev.Valid)
                        continue;
                }
                RootNode = VE.process(RootNode, VE.getY(), vertexList, edgeList, CircleCheckList);

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
                    Point DP = ((DataEvent) VE).getDatum();
                    for (final CircleEvent VCE : CurrentCircles.values()) {
                        double dist = DP.dist(VCE.Center);
                        double offs = VCE.getY() - VCE.Center.getY();
                        if (PrecisionMath.lt(dist, offs))
                            VCE.Valid = false;
                    }
                }
            }

            for  (VoronoiEdge VE : edgeList) {
                if (VE.Done)
                    continue;
                if (VE.VVertexB == Point.UNKNOWN) {
                    VE.AddVertex(Point.INFINITE);
                    if (PrecisionMath.eq(VE.LeftData.getY(), VE.RightData.getY())
                            && VE.LeftData.getX() < VE.RightData.getX()) {
                        Point T = VE.LeftData;
                        VE.LeftData = VE.RightData;
                        VE.RightData = T;
                    }
                }
            }

            ArrayList<VoronoiEdge> MinuteEdges = new ArrayList<>();
            for (VoronoiEdge VE : edgeList) {
                if (VE.isPartlyInfinite() && VE.VVertexA.equals(VE.VVertexB)) {
                    MinuteEdges.add(VE);
                    // prevent rounding errors from expanding to holes
                    for (VoronoiEdge VE2 : edgeList) {
                        if (VE2.VVertexA.equals(VE.VVertexA))
                            VE2.VVertexA = VE.VVertexA;
                        if (VE2.VVertexB.equals(VE.VVertexA))
                            VE2.VVertexB = VE.VVertexA;
                    }
                }
            }
            for (VoronoiEdge VE : MinuteEdges)
                edgeList.remove(VE);

            HashSet<Edge> finalEdges = new HashSet<>();
            for (VoronoiEdge VE : edgeList) {
                if(VE.isPartlyInfinite()) {
                    finalEdges.add(VE.toEdge());
                }
            }
            return new Graph(finalEdges);
        }
    }
