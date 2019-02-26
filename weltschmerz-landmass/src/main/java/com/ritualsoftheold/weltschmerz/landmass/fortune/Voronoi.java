package com.ritualsoftheold.weltschmerz.landmass.fortune;

import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.land.Area;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Vertex;

import java.util.ArrayList;
import java.util.HashSet;

public class Voronoi {
    private HashSet<Border> voronoiBorders;
    private HashSet<Vertex> voroniVertices;

    public Voronoi(HashSet<Border> e) {
        voroniVertices = null;
        voronoiBorders = e;
        listVertices();
    }

    public Border[] getBorderArray() {
        Border[] vEdges = new Border[voronoiBorders.size()];
        voronoiBorders.toArray(vEdges);
        return vEdges;
    }

    private void listVertices() {
        voroniVertices = new HashSet<>();
        for (Border VE : voronoiBorders) {
            voroniVertices.add(VE.getVertexA());
            voroniVertices.add(VE.getVertexB());
        }
    }

    public void getVoronoiArea(ArrayList<Area> areas, int width, int height) {
        for (Area area : areas) {
            for (Border border : getBorderArray()) {
                if(border.getVertexA().getX() < width && border.getVertexA().getY() < height &&
                        border.getVertexB().getX() < width && border.getVertexB().getY() < height &&
                        border.getVertexA().getX() > 0 && border.getVertexA().getY() > 0 &&
                        border.getVertexB().getX() > 0 && border.getVertexB().getY() > 0) {
                    if (border.getDatumA() == area.getCentroid() || border.getDatumB() == area.getCentroid()) {
                        area.add(border);
                    }
                }else{
                    voronoiBorders.remove(border);
                }
            }
        }

        ArrayList<Area> cloneAreas = new ArrayList<>(areas);
        for (Area area : cloneAreas) {
            if (area.getBorders().length < 2) {
                areas.remove(area);
            }
        }

        for (Area area : areas) {
            area.circularize();
        }

    }
}
