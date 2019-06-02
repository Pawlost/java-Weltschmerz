package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.landmass.Constants;
import com.ritualsoftheold.weltschmerz.noise.Shape;
import com.ritualsoftheold.weltschmerz.noise.generator.ChunkNoise;

public class Location {

    private long seed;
    private Plate tectonicPlate;
    private Shape shape;
    private Location[] neighbors;
    private double[][] chunkElevation;
    public final Position position;
    private Position currentChunk;
    private static final int CHUNK_IN_SECTOR_X = Constants.DEFAULT_MAX_SECTOR_X/16;
    private static final int CHUNK_IN_SECTOR_Z = Constants.DEFAULT_MAX_SECTOR_Z/16;
    private static final float VOLATILITY = 3;
    private ChunkNoise noise;

    public Location(int x, int z, long seed) {
        position = new Position(x, z, 1, 1);
        chunkElevation = new double[CHUNK_IN_SECTOR_X][CHUNK_IN_SECTOR_Z];
        this.seed = seed;
        neighbors = new Location[4];
    }

    public Location[] getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Location neighbor, int position){
        neighbors[position] = neighbor;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
        for (int posX = 0; posX < chunkElevation.length; posX++) {
            for (int posZ = 0; posZ < chunkElevation[posX].length; posZ++) {
                chunkElevation[posX][posZ] = shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE;
            }
        }
    }

    public void setElevation(boolean reverse) {
        if (!shape.key.equals("MOUNTAIN") && !shape.key.equals("OCEAN")) {
            if (!reverse) {
                for (int posX = 0; posX < chunkElevation.length; posX++) {
                    for (int posZ = 0; posZ < chunkElevation[posX].length; posZ++) {
                        double position = shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE;
                        if (position == chunkElevation[posX][posZ]) {
                            if (posX == 0) {
                                position = neighbors[0].chunkElevation[chunkElevation.length - 1][posZ];
                            } else {
                                position = chunkElevation[posX - 1][posZ];
                            }

                            if (position + chunkElevation[posX][posZ] > chunkElevation[posX][posZ] + VOLATILITY) {
                                position = position - VOLATILITY;
                            } else if (position + chunkElevation[posX][posZ] < chunkElevation[posX][posZ] - VOLATILITY) {
                                position = position + VOLATILITY;
                            }
                            chunkElevation[posX][posZ] = position;
                        }
                    }
                }

                for (int posX = 0; posX < chunkElevation.length; posX++) {
                    for (int posZ = 0; posZ < chunkElevation[posX].length; posZ++) {
                        double position = shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE;
                        if (position == chunkElevation[posX][posZ]) {
                            if (posZ == 0) {
                                position = neighbors[1].chunkElevation[posX][chunkElevation[posX].length - 1];
                            } else {
                                position = chunkElevation[posX][posZ - 1];
                            }

                            if (position + chunkElevation[posX][posZ] > chunkElevation[posX][posZ] + VOLATILITY) {
                                position = position - VOLATILITY;
                            } else if (position + chunkElevation[posX][posZ] < chunkElevation[posX][posZ] - VOLATILITY) {
                                position = position + VOLATILITY;
                            }
                            chunkElevation[posX][posZ] = position;
                        }
                    }
                }
            } else {
                for (int posX = chunkElevation.length - 1; posX >= 0; posX--) {
                    for (int posZ = chunkElevation[posX].length - 1; posZ >= 0; posZ--) {
                        double position = shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE;
                        if (position == chunkElevation[posX][posZ]) {
                            if (posZ == chunkElevation[posX].length - 1) {
                                position = neighbors[3].chunkElevation[posX][0];
                            } else {
                                position = chunkElevation[posX][posZ + 1];
                            }

                            if (position + chunkElevation[posX][posZ] > chunkElevation[posX][posZ] + VOLATILITY) {
                                position = position - VOLATILITY;
                            } else if (position + chunkElevation[posX][posZ] < chunkElevation[posX][posZ] - VOLATILITY) {
                                position = position + VOLATILITY;
                            }
                            chunkElevation[posX][posZ] = position;

                        }
                    }
                }

                for (int posX = chunkElevation.length - 1; posX >= 0; posX--) {
                    for (int posZ = chunkElevation[posX].length - 1; posZ >= 0; posZ--) {
                        double position = shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE;
                        if (position == chunkElevation[posX][posZ]) {
                            if (posX == chunkElevation.length - 1) {
                                position = neighbors[2].chunkElevation[0][posZ];
                            } else {
                                position = chunkElevation[posX + 1][posZ];
                            }

                            if (position + chunkElevation[posX][posZ] > chunkElevation[posX][posZ] + VOLATILITY) {
                                position = position - VOLATILITY;
                            } else if (position + chunkElevation[posX][posZ] < chunkElevation[posX][posZ] - VOLATILITY) {
                                position = position + VOLATILITY;
                            }
                            chunkElevation[posX][posZ] = position;
                        }
                    }
                }
            }
        }
    }

    public Shape getShape() {
        return shape;
    }

    public float setChunk(int posX, int posZ) {
        noise = new ChunkNoise(seed);

        if(posX > 0) {
            posX = posX % Constants.DEFAULT_MAX_SECTOR_X;
        }else{
            posX = ((Constants.DEFAULT_MAX_SECTOR_X * Math.abs(posX/Constants.DEFAULT_MAX_SECTOR_X)) + posX)% Constants.DEFAULT_MAX_SECTOR_X;
        }
        if(posX > 0) {
            posZ = posZ % Constants.DEFAULT_MAX_SECTOR_Z;
        }else{
            posZ = ((Constants.DEFAULT_MAX_SECTOR_Z * Math.abs(posZ/Constants.DEFAULT_MAX_SECTOR_X)) + posZ)% Constants.DEFAULT_MAX_SECTOR_Z;
        }

        currentChunk = new Position(Math.abs(posX/16) % CHUNK_IN_SECTOR_X, Math.abs(posZ/16) % CHUNK_IN_SECTOR_Z);
        noise.generateNoise(getMin(), getMax());
        return (((int)(chunkElevation[currentChunk.x][currentChunk.z])/16)*16);
    }

    public double getNoise(int x, int z){
        return noise.getNoise(x + currentChunk.x * 16 * 4, z + currentChunk.z * 16 *4);
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

    public double getMin(){
        double chunkElevationValue = chunkElevation[currentChunk.x][currentChunk.z];
        if(chunkElevationValue < 0){
            return Math.abs(shape.min) + ((16-(Math.abs(chunkElevationValue)%16))*4);
        }else{
            return Math.abs(shape.min) + ((Math.abs((chunkElevationValue)%16))*4);
        }
    }
    public double getMax(){
        double chunkElevationValue = chunkElevation[currentChunk.x][currentChunk.z];
        if(chunkElevationValue < 0){
            return Math.abs(shape.max) + ((16-(Math.abs(chunkElevationValue)%16))*4);
        }else{
            return Math.abs(shape.max) + ((Math.abs((chunkElevationValue)%16))*4);
        }
    }
}
