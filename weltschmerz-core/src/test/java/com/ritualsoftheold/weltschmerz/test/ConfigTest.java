package com.ritualsoftheold.weltschmerz.test;

import com.ritualsoftheold.weltschmerz.core.ConfigParser;
import com.ritualsoftheold.weltschmerz.core.MapIO;
import com.ritualsoftheold.weltschmerz.environment.BiomDefinition;
import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;

import com.ritualsoftheold.weltschmerz.geometry.misc.Utils;
import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class ConfigTest {

    private Configuration config;

    @Before
    public void init(){
       config = MapIO.loadMapConfig();
    }


    @Test
    public void testPolygon() throws IOException {
    }
}
