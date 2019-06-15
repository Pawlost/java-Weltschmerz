package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.landmass.Constants;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Point;
import com.ritualsoftheold.weltschmerz.noise.Shape;
import com.ritualsoftheold.weltschmerz.noise.generators.ChunkNoise;

public class Location {

    private long seed;
    private Plate tectonicPlate;
    private Shape shape;
    private Location[] neighbors;
    private double[][] chunkElevation;
    public final Polygon position;
    private int posX;
    private int posZ;
    private static final int CHUNK_IN_SECTOR_X = Constants.DEFAULT_MAX_SECTOR_X / 16;
    private static final int CHUNK_IN_SECTOR_Z = Constants.DEFAULT_MAX_SECTOR_Z / 16;
    private ChunkNoise noise;

    public Location(Polygon point, long seed) {
        this.position = point;
        chunkElevation = new double[CHUNK_IN_SECTOR_X][CHUNK_IN_SECTOR_Z];
        this.seed = seed;
        neighbors = new Location[4];
    }

    public Location[] getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Location neighbor, int position) {
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

                            if (position + chunkElevation[posX][posZ] > chunkElevation[posX][posZ] + Constants.VOLATILITY) {
                                position = position - Constants.VOLATILITY;
                            } else if (position + chunkElevation[posX][posZ] < chunkElevation[posX][posZ] - Constants.VOLATILITY) {
                                position = position + Constants.VOLATILITY;
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

                            if (position + chunkElevation[posX][posZ] > chunkElevation[posX][posZ] + Constants.VOLATILITY) {
                                position = position - Constants.VOLATILITY;
                            } else if (position + chunkElevation[posX][posZ] < chunkElevation[posX][posZ] - Constants.VOLATILITY) {
                                position = position + Constants.VOLATILITY;
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

                            if (position + chunkElevation[posX][posZ] > chunkElevation[posX][posZ] + Constants.VOLATILITY) {
                                position = position - Constants.VOLATILITY;
                            } else if (position + chunkElevation[posX][posZ] < chunkElevation[posX][posZ] - Constants.VOLATILITY) {
                                position = position + Constants.VOLATILITY;
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

                            if (position + chunkElevation[posX][posZ] > chunkElevation[posX][posZ] + Constants.VOLATILITY) {
                                position = position - Constants.VOLATILITY;
                            } else if (position + chunkElevation[posX][posZ] < chunkElevation[posX][posZ] - Constants.VOLATILITY) {
                                position = position + Constants.VOLATILITY;
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
        if (posX > 0) {
            posX = posX % Constants.DEFAULT_MAX_SECTOR_X;
        } else {
            posX = ((Constants.DEFAULT_MAX_SECTOR_X * Math.abs(posX / Constants.DEFAULT_MAX_SECTOR_X)) + posX) % Constants.DEFAULT_MAX_SECTOR_X;
        }
        if (posZ > 0) {
            posZ = posZ % Constants.DEFAULT_MAX_SECTOR_Z;
        } else {
            posZ = ((Constants.DEFAULT_MAX_SECTOR_Z * Math.abs(posZ / Constants.DEFAULT_MAX_SECTOR_X)) + posZ) % Constants.DEFAULT_MAX_SECTOR_Z;
        }

        this.posX = Math.abs(posX / 16) % CHUNK_IN_SECTOR_X;
        this.posZ = Math.abs(posZ / 16) % CHUNK_IN_SECTOR_Z;
        noise = new ChunkNoise(seed, shape.key, chunkElevation[this.posX][this.posZ]);
        return (((int) (chunkElevation[this.posX][this.posZ]) / 16) * 16);
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
            heightX1 = chunkElevation[posX - 1][posZ];
        } else {
            int index = neighbors[0].chunkElevation.length - 1;
            neighbors[0].setChunk(index, posZ);
            heightX1 = neighbors[0].getMin();
        }

        if (posX < chunkElevation.length - 1) {
            heightX2 = chunkElevation[posX + 1][posZ];
        } else {
            neighbors[2].setChunk(0, posZ);
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

        return noise.getNoise(x + posX * 16, z + posZ * 16) - finalHeight + (Constants.VOLATILITY * 4);
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
}