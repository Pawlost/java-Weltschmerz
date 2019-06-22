package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.noise.generators.ChunkNoise;
import org.apache.commons.collections4.map.MultiKeyMap;

public class Chunk {
    private  Point position;
    private double elevation;
    private Chunk[] chunkNeighbor;
    private ChunkNoise noise;
    public final String sectorName;

    public Chunk(Point position, double elevation, ChunkNoise noise, String sectorName) {
        this.position = position;
        this.elevation = elevation;
        this.noise = noise;
        chunkNeighbor = new Chunk[4];
        this.sectorName = sectorName;
    }
    public double getNoise(int x, int z) {
        double heightX1 = chunkNeighbor[0].getMin();
        double heightX2 = chunkNeighbor[1].getMin();
        double heightZ1 = chunkNeighbor[2].getMin();
        double heightZ2 = chunkNeighbor[3].getMin();

        if(chunkNeighbor[0].getElevation() < getElevation()){
            heightX1 += (getElevation() - chunkNeighbor[0].getElevation());
        }else if(chunkNeighbor[0].getElevation() > getElevation()) {
            heightX1 += ((chunkNeighbor[0].getElevation() - getElevation()));
        }
        if(chunkNeighbor[1].getElevation() < getElevation()){
            heightX2 += (getElevation() - chunkNeighbor[1].getElevation());
        }else  if(chunkNeighbor[1].getElevation() > getElevation()){
            heightX2 += ((chunkNeighbor[1].getElevation() - getElevation()));
        }

        if(chunkNeighbor[2].getElevation() >  getElevation()){
            heightZ1 += (chunkNeighbor[2].getElevation() - getElevation());
        }else    if(chunkNeighbor[2].getElevation() <  getElevation()){
            heightZ1 += ((getElevation() - chunkNeighbor[2].getElevation()));
        }

        if(chunkNeighbor[3].getElevation() > getElevation()){
            heightZ2 += (chunkNeighbor[3].getElevation() - getElevation()) ;
        }else if(chunkNeighbor[3].getElevation() < getElevation()) {
            heightZ2 += ((getElevation() - chunkNeighbor[3].getElevation()));
        }

        heightX1 = ((heightX1) * x) / (256);
        heightX2 = ((heightX2) * x) / (256);
        heightZ1 = ((heightZ1) * z) / (256);
        heightZ2 = ((heightZ2) * z) / (256);
        double finalHeight = 0;
        if(heightX1 > heightX2){
            finalHeight += heightX1;
        }else if(heightX2 > heightX1){
            finalHeight += heightX1;
        }

        if(heightZ1 > heightZ2){
            finalHeight -= heightZ1;
        }else if(heightZ2 > heightZ1){
            finalHeight -= heightZ2;
        }

        return noise.getNoise(x + position.x * 256, z + position.y * 256) - finalHeight;
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
