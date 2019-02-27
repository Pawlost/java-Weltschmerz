package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.WeltschmerzNoise;
import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.land.Plate;
import com.ritualsoftheold.weltschmerz.landmass.land.Polygon;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestHeightMapWorldTest {

    World world;

    @Before
    public void main() {
        WeltschmerzNoise noise = new WeltschmerzNoise(7987099, 3, 0.01);
        ModuleAutoCorrect module = noise.generateNoise();
        world = new World(3000, 600, 6, 10, 1000, module);
    }

    public void testPlates(){
        for(int i =0; i < 100; i++) {
            main();
            world.generateFirstLand();
            ArrayList<Plate> plates = world.getPlates();
            for (Plate plate : plates) {
                Assert.assertNotNull(plate.getPolygon());
            }
        }
    }

    @Test
    public void testLocation(){
        for(int i =0; i < 100; i++) {
            main();
            world.generateFirstLand();
            Location[] locations = world.getLocations();
            for (Location location : locations) {
                Assert.assertNotNull(location.getPolygon());
            }
        }
    }
}