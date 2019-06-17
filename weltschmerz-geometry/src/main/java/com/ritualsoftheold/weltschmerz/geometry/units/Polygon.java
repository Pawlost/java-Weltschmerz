package com.ritualsoftheold.weltschmerz.geometry.units;

import com.google.common.collect.HashMultimap;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Polygon {
    public final Point centroid;
    public final Point center;
    private java.awt.Polygon polygon;
    private HashMultimap<Vertex, Border> borders;
    Set<Point> neighborPoints;


    public Polygon(Point centroid, Point center) {
        this.centroid = centroid;
        this.center = center;
        polygon = new java.awt.Polygon();
        borders = HashMultimap.create();
        neighborPoints = new HashSet<>();
    }

    public boolean contains(double x, double y) {
        return polygon.contains(x, y);
    }

    public java.awt.Polygon getSwingPolygon() {
        return polygon;
    }

    public void addBorder(Set<Border> borders) {
        for (Border border : borders) {
            if (border.getDatumA() == centroid || border.getDatumB() == centroid) {
                this.borders.put(border.getVertexA(), border);
                this.borders.put(border.getVertexB(), border);
            }
        }
    }

    public Border[] getBorders() {
        Border[] borders = new Border[this.borders.size()];
        this.borders.values().toArray(borders);
        return borders;
    }

    public Rectangle getBounds() {
        return polygon.getBounds();
    }

    public void createPolygon() {
        Vertex vertex = null;
        Vertex previousVertex;
        HashSet<Border> removeBorders = new HashSet<>(borders.values());
        for (Vertex key : borders.keySet()) {
            if (borders.containsKey(key)) {
                Border border = (Border) borders.get(key).toArray()[0];
                polygon.addPoint((int) Math.round(border.getVertexB().x), (int) Math.round(border.getVertexB().y));
                polygon.addPoint((int) Math.round(border.getVertexA().x), (int) Math.round(border.getVertexA().y));
                addPoints(border.getDatumA(), border.getDatumB());
                removeBorders.remove(border);
                vertex = border.getVertexA();
                break;
            }
        }

        while (!removeBorders.isEmpty()) {
            previousVertex = vertex;
            for (Border anotherBorder : borders.get(vertex)) {
                if (removeBorders.contains(anotherBorder)) {
                    if (anotherBorder.getVertexA() != vertex && borders.containsKey(vertex)) {
                        removeBorders.remove(anotherBorder);
                        vertex = anotherBorder.getVertexA();
                        polygon.addPoint((int) Math.round(vertex.x), (int) Math.round(vertex.y));
                        addPoints(anotherBorder.getDatumA(), anotherBorder.getDatumB());
                        break;
                    } else if (anotherBorder.getVertexB() != vertex) {
                        removeBorders.remove(anotherBorder);
                        vertex = anotherBorder.getVertexB();
                        polygon.addPoint((int) Math.round(vertex.x), (int) Math.round(vertex.y));
                        addPoints(anotherBorder.getDatumA(), anotherBorder.getDatumB());
                        break;
                    }
                }
            }

            if (vertex == previousVertex) {
                for (Border border : new ArrayList<>(removeBorders)) {
                    if (border.getVertexA() == vertex) {
                        vertex = border.getVertexB();
                    } else if (border.getVertexB() == vertex) {
                        vertex = border.getVertexA();
                    } else {
                        removeBorders.remove(border);
                    }
                }
            }
        }
    }

    private void addPoints(Point a, Point b){
        if(a != centroid){
            neighborPoints.add(a);
        }

        if (b != centroid){
            neighborPoints.add(b);
        }
    }

    public Set<Point> getNeighborPoints() {
        return neighborPoints;
    }
}
