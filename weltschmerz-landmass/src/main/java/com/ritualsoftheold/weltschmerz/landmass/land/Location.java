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
    private Position position;
    private Position currentChunk;

    public Location(int x, int z, long seed) {
        position = new Position(x, z, 1, 1);
        chunkElevation = new double[Constants.DEFAULT_MAX_SECTOR_X/16][Constants.DEFAULT_MAX_SECTOR_Z/16];

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

    public void setChunks(boolean reverse) {
        for (int posX = 0; posX < chunkElevation.length; posX++) {
            for (int posZ = 0; posZ < chunkElevation[posX].length; posZ++) {
                double position = shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE;
                if (position == chunkElevation[posX][posZ] && !shape.key.equals("MOUNTAIN")) {
                    if (reverse) {
                        if (neighbors[2].shape == shape) {
                            position = neighbors[2].getChunkElevation()[0][posZ];
                            if (position > shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position - 1;
                            } else if (position < shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position + 1;
                            }
                        } else {
                            position = neighbors[2].shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE;
                            if (position > shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position - 8;
                            } else if (position < shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position + 8;
                            }
                        }
                    } else {
                        if (neighbors[0].shape == shape) {
                            position = neighbors[0].getChunkElevation()[0][posZ];
                            if (position > shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position - 1;
                            } else if (position < shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position + 1;
                            }
                        } else {
                            position = neighbors[0].shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE;
                            if (position > shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position - 8;
                            } else if (position < shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position + 8;
                            }
                        }
                    }
                    chunkElevation[posX][posZ] = position;
                }
            }
        }

        for (int posX = 0; posX < chunkElevation.length; posX++) {
            for (int posZ = 0; posZ < chunkElevation[posX].length; posZ++) {
                double position = shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE;
                if (position == chunkElevation[posX][posZ] && !shape.key.equals("MOUNTAIN")) {
                    if (reverse) {
                        if (neighbors[3].shape == shape) {
                            position = neighbors[3].getChunkElevation()[posX][0];
                            if (position > shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position - 1;
                            } else if (position < shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position + 1;
                            }
                        } else {
                            position = neighbors[3].shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE;
                            if (position > shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position - 8;
                            } else if (position < shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position + 8;
                            }
                        }
                    } else {
                        if (neighbors[1].shape == shape) {
                            position = neighbors[1].getChunkElevation()[posX][0];
                            if (position > shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position - 1;
                            } else if (position < shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position + 1;
                            }

                        } else {
                            position = neighbors[1].shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE;
                            if (position > shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position - 8;
                            } else if (position < shape.position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE) {
                                position = position + 8;
                            }
                        }
                    }

                    chunkElevation[posX][posZ] = position;
                }
            }
        }
    }

    public Shape getShape() {
        return shape;
    }

    public double[][] getChunkValues(int posX, int posZ) {
        double[][] chunkValues = new double[64][64];
        ChunkNoise noise = new ChunkNoise(seed);
        currentChunk = new Position(Math.abs(posX/Constants.DEFAULT_MAX_SECTOR_X) - position.x, Math.abs(posZ/Constants.DEFAULT_MAX_SECTOR_Z) - position.z);
        noise.generateNoise(getMin(), getMax());
        for (int x = 0; x < 64; x++) {
            for (int z = 0; z < 64; z++) {
                chunkValues[x][z] = noise.getNoise(x + posX * 4, z + posZ * 4);
            }
        }
        return chunkValues;
    }

    public String getKey() {
        return shape.key;
    }

    public Position getPosition(){
        return position;
    }

    public void setTectonicPlate(Plate tectonicPlate) {
        this.tectonicPlate = tectonicPlate;
    }

    public Plate getTectonicPlate() {
        return tectonicPlate;
    }

    public double getY(){
        System.out.println((int)(((shape.min * 0.25) + chunkElevation[currentChunk.x][currentChunk.z])/16)*16);
        return (int)(((shape.min * 0.25) + chunkElevation[currentChunk.x][currentChunk.z])/16)*16;
    }

    public double[][] getChunkElevation() {
        return chunkElevation;
    }

    public double getMin(){
        return shape.min + (((chunkElevation[currentChunk.x][currentChunk.z])%16)*4);
    }
    public double getMax(){
        return shape.max + (((chunkElevation[currentChunk.x][currentChunk.z])%16)*4);
    }
}
