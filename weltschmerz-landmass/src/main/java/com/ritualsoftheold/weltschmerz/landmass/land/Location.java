package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.geometry.units.Border;
import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.geometry.units.Polygon;
import com.ritualsoftheold.weltschmerz.geometry.misc.Shape;
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
    private int posX;
    private int posZ;
    public static final float VOLATILITY = 16;
    private ChunkNoise noise;

    public Location(Point point, long seed) {
        this.position = new Polygon(point, null);
        this.seed = seed;
        neighbors = new ArrayList<>();
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
            double newCenterChunkElevation = (neighbor.centerChunkElevation * ((dist*VOLATILITY) - dist))/(dist*VOLATILITY);
            if (newCenterChunkElevation > centerChunkElevation) {
                centerChunkElevation = newCenterChunkElevation;
            }
        }
    }

    public Shape getShape() {
        return shape;
    }

    public double setChunk(int posX, int posZ) {
        Point chunk = new Point(posX, posZ);
        double chunkElevation = 0;
        for (Location neighbor : neighbors) {
            Border border = position.getBorders().get(neighbor.position.centroid);
            Vector vector = new Vector(position.center.x - neighbor.position.center.x, position.center.y - neighbor.position.center.y,
                    position.center);
            Point point = border.vector.getIntersection(vector);
            double wholeDist = position.center.dist(neighbor.position.center);
            double minChunkElevation;
            double minLocalChunkElevation = (centerChunkElevation * (wholeDist - point.dist(position.center))) / wholeDist;
            double minOuterChunkELevation = (neighbor.centerChunkElevation * (wholeDist - point.dist(neighbor.position.center))) / wholeDist;

            if (minLocalChunkElevation > minOuterChunkELevation) {
                minChunkElevation = minLocalChunkElevation;
            } else {
                minChunkElevation = minOuterChunkELevation;
            }

            if (neighbor.centerChunkElevation > centerChunkElevation && minChunkElevation < centerChunkElevation) {
                minChunkElevation = centerChunkElevation;
            }

            double localChunkElevation = (centerChunkElevation * (wholeDist - chunk.dist(position.center))) / wholeDist;
            double outerChunkElevation = (neighbor.centerChunkElevation * (wholeDist - chunk.dist(neighbor.position.center))) / wholeDist;

            double newChunkElevation;
            if (localChunkElevation > outerChunkElevation) {
                newChunkElevation = localChunkElevation;
            } else {
                newChunkElevation = outerChunkElevation;
            }

            if (minChunkElevation < newChunkElevation && chunkElevation < newChunkElevation) {
                chunkElevation = newChunkElevation;
            } else if (minChunkElevation > chunkElevation) {
                chunkElevation = minChunkElevation;
            }
        }

        System.out.println(((int) (chunkElevation / 16)) * 16);

        return ((int) (chunkElevation / 16)) * 16;
    }

    public void generateNoise(){
        noise.generateNoise();
    }

    public double getNoise(int x, int z) {
        double heightX1;
        double heightX2;
        double heightZ1;
        double heightZ2;

        if (posX > 0) {
         //   heightX1 = chunkElevation[posX - 1][posZ];
        } else {
        /*   int index = neighbors[0].chunkElevation.length - 1;
            neighbors[0].setChunk(index, posZ);
            heightX1 = neighbors[0].getMin();

         */
        }
/*
        if (posX < chunkElevation.length - 1) {
            heightX2 = chunkElevation[posX + 1][posZ];
        } else {
            /*neighbors[2].setChunk(0, posZ);
            heightX2 = neighbors[2].getMin();


        }

        if (posZ > 0) {
            heightZ1 = chunkElevation[posX][posZ - 1];
        } else {
            int index = neighbors[1].chunkElevation[posX].length - 1;
            neighbors[1].setChunk(posX, index);
            heightZ1 = neighbors[1].getMin();
        }

        if (posZ < chunkElevation[posZ].length - 1) {
            heightZ2 = chunkElevation[posX][posZ + 1];
        } else {
            neighbors[3].setChunk(posX, 0);
            heightZ2 = neighbors[3].getMin();
        }

        heightX1 = ((heightX1 * x) / ((256 * CHUNK_IN_SECTOR_X) + (256 + Constants.VOLATILITY)));
        heightZ1 = ((heightZ1 * z) / ((256 * CHUNK_IN_SECTOR_Z) + (256 + Constants.VOLATILITY)));
        heightX2 = ((heightX2 * x) / ((256 * CHUNK_IN_SECTOR_X) + (256 + Constants.VOLATILITY)));
        heightZ2 = ((heightZ2 * z) / ((256 * CHUNK_IN_SECTOR_Z) + (256 + Constants.VOLATILITY)));

        double finalHeight = 0;
        if (heightX1 > heightX2) {
            finalHeight += heightX1;
        } else if (heightX2 > heightX1) {
            finalHeight += heightX2;
        }

        if (heightZ1 > heightZ2) {
            finalHeight += heightZ1;
        } else if (heightZ2 > heightZ1) {
            finalHeight += heightZ2;
        }

        }
        */
        double finalHeight = 0;
        return noise.getNoise(x + posX * 16, z + posZ * 16) - finalHeight + (VOLATILITY * 4);

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

    public double getMin() {
        return noise.getMin();
    }

    public double getMax() {
        return noise.getMax();
    }

    public double getCenterChunkElevation(){
        return centerChunkElevation;
    }
}