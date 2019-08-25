package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.environment.*;

import com.ritualsoftheold.weltschmerz.misc.misc.Constants;
import com.ritualsoftheold.weltschmerz.misc.misc.Random;
import com.ritualsoftheold.weltschmerz.misc.misc.Utils;
import com.ritualsoftheold.weltschmerz.misc.units.Vector;
import com.typesafe.config.Config;
import squidpony.squidmath.XoRoRNG;

import java.awt.image.BufferedImage;

public class Weltschmerz {

    public final Config config;
    private Equator equator;
    private Precipitation precipitation;
    private WorldNoise noise;
    private Circulation circulation;
    private final Biom[][] bioms;
    private static final String EARTH_FILE = "earth.png";

    private Random random;

    //Generate map image
    public static void main(String[] args) {
        Weltschmerz weltschmerz = new Weltschmerz();

        Config configuration = weltschmerz.config;

        int latitude = configuration.getInt("map.latitude");
        int longitude = configuration.getInt("map.longitude");

        BufferedImage image = new BufferedImage(longitude, latitude, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < longitude; x++) {
            for (int y = 0; y < latitude; y++) {
                Biom biom = weltschmerz.bioms[x][y];
                image.setRGB(x, y, biom.color.getRGB());
            }
        }
        MapIO.saveImage(image);
    }

    public Weltschmerz() {
        this.config = MapIO.loadMapConfig();
        System.out.println("Preparation");
        BufferedImage earth = MapIO.loadMap(EARTH_FILE);
        this.noise = new WorldNoise(config, earth);
        this.equator = new Equator(config);
        this.circulation = new Circulation(equator, noise, config);
        this.precipitation = new Precipitation(equator, noise, config);
        this.random = new Random(config.getLong("map.seed"));
        bioms = MapIO.loadBiomMap(config);
        System.out.println("Preparation done");
    }

    public Config getConfiguration() {
        return config;
    }

    public Biom getBiom(int posX, int posY) {
        double elevation = noise.getNoise(posX, posY);
        double temperature = equator.getTemperature(posY, elevation);
        Vector airFlow = circulation.getAirFlow(posX, posY);
        double precipitation = this.precipitation.getPrecipitation(posX, posY, elevation, temperature, airFlow);

        Biom biom = null;
        int y = (int) (precipitation * (1000 / Constants.MAXIMUM_PRECIPITATION));
        if (temperature >= 0 && temperature < 40) {
            int x = (int) ((temperature * 20) + Constants.MAXIMUM_TEMPERATURE_DIFFERENCE);
            biom = bioms[x][y];
        } else if (temperature > -10) {
            int x = (int) (Constants.MAXIMUM_TEMPERATURE_DIFFERENCE / Math.max(Math.abs(temperature), 1));
            biom = bioms[x][y];
        }

        if (biom == null || !Utils.isLand(elevation)) {
            biom = selectDefault(temperature, elevation);
        }

        return biom;
    }

    private static Biom selectDefault(double temperature, double elevation) {
        if (elevation <= 0) {
            if (elevation < Constants.OCEAN_DEPTH) {
                return new Biom("OCEAN", Integer.parseInt("000066", 16));
            } else {
                return new Biom("SEA", Integer.parseInt("0099FF", 16));
            }
        } else {
            if (temperature <= 0) {
                return new Biom("ICELAND", Integer.parseInt("FFFFFF", 16));
            } else {
                return new Biom("DESERT", Integer.parseInt("FFCC00", 16));
            }
        }
    }

    public double getPressure(int posX, int posY) {
        return circulation.calculateDensity(posX, posY);
    }

    public double getMoisture(int posY) {
        return precipitation.getMoisture(posY);
    }

    public double getElevation(int posX, int posY) {
        return noise.getNoise(posX, posY);
    }

    public void changeConfiguration(Config config) {
        this.equator.changeConfiguration(config);
        this.noise.changeConfiguration(config);
        this.circulation.changeConfiguration(config);
        this.precipitation.changeConfiguration(config);
    }

    public Random getRandom(){
        return random;
    }
}
