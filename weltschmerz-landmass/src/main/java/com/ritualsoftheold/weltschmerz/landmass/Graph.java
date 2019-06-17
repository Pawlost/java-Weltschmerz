package com.ritualsoftheold.weltschmerz.landmass;

import com.google.common.collect.Multimap;
import com.ritualsoftheold.weltschmerz.geometry.units.Border;
import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.geometry.units.Polygon;
import com.ritualsoftheold.weltschmerz.geometry.units.Vertex;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;

import java.util.*;

public class Graph {
    private Multimap<Point, Border> allBorders;
    private Multimap<Point, Vertex> allVertices;

    Graph(Multimap<Point, Border> map, Multimap<Point, Vertex> points) {
        allBorders = map;
        allVertices = points;
    }

    public void getVoronoiArea(HashMap<Point, Location> locations) {
        for (Location location : new ArrayList<>(locations.values())) {
            Set<Border> borders = new HashSet<>(allBorders.get(location.position.centroid));
            location.position.addBorder(borders);
            location.position.createPolygon();
        }
    }

    public HashMap<Point, Location> smoothLocation(HashMap<Point, Location> world, int smooth) {
        HashMap<Point, Location> locationHashMap = new HashMap<>();
        for (Location location : world.values()) {
            Collection<Vertex> vertices = allVertices.get(location.position.centroid);
            double centerX = 0;
            double centerY = 0;

            for (Vertex vertex : vertices) {
                centerX += vertex.x;
                centerY += vertex.y;
            }

            centerX /= vertices.size();
            centerY /= vertices.size();

            if (centerX > -1 || centerY > -1) {
                Point centroid = new Point(location.position.centroid.x + centerX / (2 * smooth), location.position.centroid.y + centerY / (2 * smooth));
                Point center = new Point(((location.position.centroid.x + centerX)/16)*16, ((location.position.centroid.y + centerY)/16)*16);
                Polygon polygon = new Polygon(centroid, center);
                location = new Location(polygon, location.seed);
                locationHashMap.put(centroid, location);
            }
        }
        world.clear();
        return locationHashMap;
    }
}
