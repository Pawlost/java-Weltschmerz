package com.ritualsoftheold.weltschmerz.landmass.land;

import com.google.common.collect.Multimap;
import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.noise.generators.ChunkNoise;
import org.apache.commons.collections4.map.MultiKeyMap;

public class Chunk {
    private  Point position;
    private double elevation;
    private Chunk[] chunkNeighbor;
    private ChunkNoise noise;
    public final String sectorName;
    private double centerElevation;
    private Point center;

    public Chunk(Point position, double elevation, ChunkNoise noise, String sectorName, double centerElevation, Point center){
        this.position = position;
        this.elevation = elevation;
        this.noise = noise;
        chunkNeighbor = new Chunk[4];
        this.sectorName = sectorName;
        this.center = center;
        this.centerElevation = centerElevation;
    }
    public double getNoise(int x, int z) {
        double heightX1 = ((chunkNeighbor[0].getMin()) * x) / (256);
        double heightX2 = ((chunkNeighbor[1].getMin()) * x) / (256);
        double heightZ1 = ((chunkNeighbor[2].getMin()) * z) / (256);
        double heightZ2 = ((chunkNeighbor[3].getMin()) * z) / (256);


      /*  double heightX1 = ((chunkNeighbor[0].getMin()) * x) / ((chunkNeighbor[0].position.dist(position)*Location.VOLATILITY)+512);
        double heightX2 = ((chunkNeighbor[1].getMin()) * x) / ((chunkNeighbor[1].position.dist(position)*Location.VOLATILITY)+512);
        double heightZ1 = ((chunkNeighbor[2].getMin()) * z) / ((chunkNeighbor[2].position.dist(position)*Location.VOLATILITY)+512);
        double heightZ2 = ((chunkNeighbor[3].getMin()) * z) / ((chunkNeighbor[3].position.dist(position)*Location.VOLATILITY)+512);
*/
        double finalHeight = 0;
        if(chunkNeighbor[2].centerElevation < centerElevation || chunkNeighbor[3].centerElevation < centerElevation) {
            if (heightZ1 > heightZ2) {
                finalHeight -= heightZ1;
            } else if (heightZ2 > heightZ1) {
                finalHeight -= heightZ2;
            }
        }else{
            if (heightZ1 > heightZ2) {
                finalHeight += heightZ1;
            } else if (heightZ2 > heightZ1) {
                finalHeight += heightZ2;
            }
        }

        if(chunkNeighbor[0].centerElevation < centerElevation || chunkNeighbor[1].centerElevation < centerElevation) {
            if (heightX1 > heightX2) {
                finalHeight += heightX1;
            } else if (heightX2 > heightX1) {
                finalHeight += heightX2;
            }
        }else{
            if (heightX1 > heightX2) {
                finalHeight -= heightX1;
            } else if (heightX2 > heightX1) {
                finalHeight -= heightX2;
            }
        }

        if(finalHeight < 0){
            finalHeight = 0;
        }
        return noise.getNoise(x + position.x * 16, z + position.y * 16) - finalHeight;
    }


    public void findNeighbors(MultiKeyMap<Integer, Chunk> chunks) {
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    chunkNeighbor[0] = chunks.get((int)position.x - 1, (int)position.y);
                    break;
                case 1:
                    chunkNeighbor[1] = chunks.get((int)position.x + 1, (int)position.y);
                    break;
                case 2:
                    chunkNeighbor[2] = chunks.get((int)position.x, (int)position.y - 1);
                    break;
                case 3:
                    chunkNeighbor[3] = chunks.get((int)position.x, (int)position.y + 1);
                    break;
            }
        }
    }

    public int getElevation() {
        return ((int) (elevation / 16)) * 16;
    }

    public double getMin() {
        return noise.getMin();
    }

    public double getMax() {
        return noise.getMax();
    }
}
