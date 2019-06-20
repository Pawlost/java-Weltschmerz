package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.geometry.units.Border;
import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.geometry.misc.Shape;
import com.ritualsoftheold.weltschmerz.geometry.units.Polygon;
import com.ritualsoftheold.weltschmerz.geometry.units.Vector;
import com.ritualsoftheold.weltschmerz.noise.generators.ChunkNoise;


import java.util.ArrayList;

public class Location {

    public final long seed;
    private double centerChunkElevation;
    public static final int MAX_SECTOR_HEIGHT_DIFFERENCE = 128;
    private Plate tectonicPlate;
    private Shape shape;
    private ArrayList<Location> neighbors;
    public final Polygon position;
    public static final float VOLATILITY = 2;

    public Location(Point point, long seed) {
        this(new Polygon(point, null), seed);
    }

    public Location(Polygon polygon, long seed) {
        this.position = polygon;
        this.seed = seed;
        neighbors = new ArrayList<>();
    }


    public ArrayList<Location> getNeighbors() {
        return neighbors;
    }

    public void add(Location neighbor) {
        this.neighbors.add(neighbor);
    }

    public boolean setShape(Shape shape) {
        this.shape = shape;
        centerChunkElevation = shape.position * MAX_SECTOR_HEIGHT_DIFFERENCE;
        return shape.key.equals("MOUNTAIN") || shape.key.equals("OCEAN");
    }

    public void setElevation() {
        for (Location neighbor : neighbors) {
            double dist = position.center.dist(neighbor.position.center);
            centerChunkElevation += (neighbor.centerChunkElevation * ((dist * VOLATILITY) - dist)) / (dist * VOLATILITY);
        }
        centerChunkElevation = centerChunkElevation/(neighbors.size()-1);
    }

    public Shape getShape() {
        return shape;
    }

    public Chunk setChunk(int posX, int posZ) {
        double chunkElevation = 0;
        Point chunk = new Point(posX, posZ);
        for (Location neighbor : neighbors) {
            double wholeDist = position.center.dist(neighbor.position.center);
            double localChunkElevation = 0;

            Border mainBorder = position.getBorders().get(neighbor.position.centroid);
            Vector vector = new Vector(position.center.x - neighbor.position.center.x,
                    position.center.y - neighbor.position.center.y, position.center);

            Point intersection = vector.getIntersection(mainBorder.vector);
            java.awt.Polygon polygon = new java.awt.Polygon();
            java.awt.Polygon neighborPolygon = new java.awt.Polygon();

            neighborPolygon.addPoint((int) position.center.x, (int) position.center.y);
            neighborPolygon.addPoint((int) mainBorder.vertexA.x, (int) mainBorder.vertexA.y);
            neighborPolygon.addPoint((int) neighbor.position.center.x, (int) neighbor.position.center.y);
            neighborPolygon.addPoint((int) mainBorder.vertexB.x, (int) mainBorder.vertexB.y);


            polygon.addPoint((int) position.center.x, (int) position.center.y);
            for (Location polygonNeigbor : neighbors) {
                if (polygonNeigbor.centerChunkElevation > centerChunkElevation) {
                    Border border = position.getBorders().get(neighbor.position.centroid);
                    polygon.addPoint((int) polygonNeigbor.position.center.x, (int) polygonNeigbor.position.center.y);
                    polygon.addPoint((int) border.vertexA.x, (int) border.vertexA.y);
                    polygon.addPoint((int) border.vertexB.x, (int) border.vertexB.y);

                }
            }
            int newChunkElevation = ((int) (centerChunkElevation) / 16) * 16;
            int newChunkElevationNeighbor = ((int) (neighbor.centerChunkElevation) / 16) * 16;
           // System.out.println(newChunkElevation);
            double outerChunkElevation = 0;
            double localIntersection = 0;

            if (newChunkElevation > newChunkElevationNeighbor) {
                localChunkElevation = (newChunkElevation * (wholeDist - chunk.dist(position.center))) / wholeDist;
            } else  if (newChunkElevation < newChunkElevationNeighbor) {
                localChunkElevation = (newChunkElevationNeighbor * (wholeDist - chunk.dist(neighbor.position.center))) / wholeDist;
            }else if(polygon.contains((int) chunk.x, (int)chunk.y)){
                localChunkElevation = (newChunkElevation * (wholeDist - intersection.dist(position.center))) / wholeDist;
            }

            if (outerChunkElevation > localChunkElevation) {
                localChunkElevation = outerChunkElevation;
            }

            if (localIntersection < localChunkElevation && chunkElevation < localChunkElevation) {
                chunkElevation = localChunkElevation;
            }
        }
        ChunkNoise noise = new ChunkNoise(seed, shape.key, chunkElevation);
        return new Chunk(new Point(chunk.x, chunk.y), chunkElevation, noise, shape.key, centerChunkElevation, position.center);
    }

    public String getName() {
        return shape.key;
    }

    public void setTectonicPlate(Plate tectonicPlate) {
        this.tectonicPlate = tectonicPlate;
    }

    public Plate getTectonicPlate() {
        return tectonicPlate;
    }

    public double getCenterChunkElevation() {
        return centerChunkElevation;
    }
}