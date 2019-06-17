package com.ritualsoftheold.weltschmerz.landmass;

import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Zone extends ArrayList<Location> {
    private HashMap<Point, Location> world;
    private final int PRELOAD = 80;
    private Location[][][][] elevation;

    public Zone (HashMap<Point, Location> world){
        this.world = world;
        elevation = new Location[3][3][][];
    }

    public Location updatePlayerPosition(double x, double z){
        for(Location location:world.values()){
            if(location.position.getBounds().x < x && location.position.getBounds().y < z &&
                    location.position.getBounds().width > x && location.position.getBounds().height > z){
                return location;
            }
        }
        return null;
    }

  /*  public void updatePlayerPosition(double x, double z){
          ArrayList<Point> points = new ArrayList<>(world.keySet());
        Collections.sort(points);
        if((x/16)%PRELOAD == 0 && (z/16)%PRELOAD == 0) {
            ArrayList<Point> points = new ArrayList<>(world.keySet());
            Collections.sort(points);

            for (int x = 0; x < elevation.length; x++) {
                for (int z = 0; z < elevation.length; z++) {

                }
            }
        }else if(elevation[1][1].length == 0){
            for(Location[][][] zone: elevation) {
                for(Location[][] innerZone : zone){
                    innerZone = new Location[PRELOAD][PRELOAD];
                    for(int i = 0; i < 9; i++){
                        switch (i){
                            case 0:
                                if(x - PRELOAD > 0 && z - PRELOAD > 0){
                                }
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            case 6:
                                break;
                            case 7:
                                break;
                            case 8:
                                break;
                        }
                    }
                }
            }
        }
    }*/

    public Location getChunkLocation (int x, int z){
        return elevation[2][2][x][z];
    }
}
