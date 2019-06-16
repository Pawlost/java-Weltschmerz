package com.ritualsoftheold.weltschmerz.geometry.units;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ritualsoftheold.weltschmerz.geometry.units.Point;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Polygon {
    public final Point centroid;
    private java.awt.Polygon polygon;
    private HashMultimap<Vertex, Border> borders;

    public Polygon(Point center) {
        this.centroid = center;
        polygon = new java.awt.Polygon();
        borders = HashMultimap.create();
    }

    public boolean contains(Point point) {
        return polygon.contains(point.x, point.y);
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

    public void createPolygon() {
        Vertex vertex = null;
        Vertex previousVertex;
        HashSet<Border> removeBorders = new HashSet<>(borders.values());
        for (Vertex key : borders.keySet()) {
            if (borders.containsKey(key)) {
                Border border =  (Border) borders.get(key).toArray()[0];
                polygon.addPoint((int)Math.round(border.getVertexB().x), (int)Math.round(border.getVertexB().y));
                polygon.addPoint((int)Math.round(border.getVertexA().x), (int)Math.round(border.getVertexA().y));
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
                        break;
                    } else if (anotherBorder.getVertexB() != vertex) {
                        removeBorders.remove(anotherBorder);
                        vertex = anotherBorder.getVertexB();
                        polygon.addPoint((int) Math.round(vertex.x), (int) Math.round(vertex.y));
                        break;
                    }
                }
            }

            if (vertex == previousVertex) {
                for (Border border : removeBorders) {
                    if (border.getVertexA() == vertex) {
                        vertex = border.getVertexB();
                    } else if (border.getVertexB() == vertex) {
                        vertex = border.getVertexA();
                    }
                }
            }

            if (vertex == previousVertex) {
                removeBorders.clear();
            }
        }
    }
}
