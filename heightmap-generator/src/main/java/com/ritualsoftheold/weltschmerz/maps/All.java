package com.ritualsoftheold.weltschmerz.maps;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.maps.misc.DoubleJSlider;
import com.ritualsoftheold.weltschmerz.maps.world.WorldBiomesCanvas;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class All extends JPanel implements ChangeListener {
    public static void main(String... args) {
        new All();
    }

    private World world;
    private int longitude;
    private int latitude;

    private WorldBiomesCanvas biomesCanvas;

    private Config config;

    //Temperature
    private JSlider minTemperature;
    private JSlider maxTemperature;
    private DoubleJSlider temperatureDecrease;

    //Moisture
    private DoubleJSlider zoom;
    private DoubleJSlider moistureIntensity;
    private DoubleJSlider change;

    //Debug
    private JSlider debug;

    //Precipitation
    private DoubleJSlider circulationIntensity;
    private DoubleJSlider orographicEffect;
    private DoubleJSlider precipitationIntensity;
    private DoubleJSlider iteration;
    private JSlider elevationDelta;

    //Humidity
    private DoubleJSlider transpiration;
    private DoubleJSlider evaporation;

    //Circulation
    private DoubleJSlider exchangeCoefficient;
    private JSlider circulationOctaves;
    private DoubleJSlider temperatureInfluence;
    private JSlider circulationDecline;

    private All() {
        super();
        Weltschmerz weltschmerz = new Weltschmerz();
        this.world = weltschmerz.world;
        config = world.config;

        latitude = config.getInt("map.latitude");
        longitude = config.getInt("map.longitude");

        //Creates frame for heigh map
        JFrame frame = new JFrame("All");

        frame.setPreferredSize(new Dimension(1200, 900));

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.add(initCanvas());
        this.setLayout(new GridLayout(1, 1, 10, 10));
        frame.add(this);

        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel initCanvas() {
        biomesCanvas = new WorldBiomesCanvas(longitude, latitude, world);
        biomesCanvas.updateImage();
        JPanel panel = new JPanel();

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        //Temperature
        minTemperature = new JSlider(-1000, 0, config.getInt("temperature.min_temperature"));
        minTemperature.addChangeListener(this);

        maxTemperature = new JSlider(0, 90, config.getInt("temperature.max_temperature"));
        maxTemperature.addChangeListener(this);

        temperatureDecrease = new DoubleJSlider(0, 1000,
                config.getDouble("temperature.temperature_decrease"), 100);
        temperatureDecrease.addChangeListener(this);

        //Precipitation
        circulationIntensity = new DoubleJSlider(0, 1000,
                config.getDouble("precipitation.circulation_intensity"), 100);
        circulationIntensity.addChangeListener(this);

        orographicEffect = new DoubleJSlider(0, 1000,
                config.getDouble("precipitation.orographic_effect"), 100);
        orographicEffect.addChangeListener(this);

        precipitationIntensity = new DoubleJSlider(0, 1000,
                config.getDouble("precipitation.precipitation_intensity"), 100);
        precipitationIntensity.addChangeListener(this);

        iteration = new DoubleJSlider(0, 1000,
                config.getDouble("precipitation.iteration"), 100);
        iteration.addChangeListener(this);

        elevationDelta = new JSlider(0, 1000,
                config.getInt("precipitation.elevation_delta"));
        elevationDelta.addChangeListener(this);


        //Moisture
        zoom = new DoubleJSlider(0, 1000,
                config.getDouble("moisture.zoom"), 100);
        zoom.addChangeListener(this);

        moistureIntensity = new DoubleJSlider(0, 1000,
                config.getDouble("moisture.moisture_intensity"), 100);
        moistureIntensity.addChangeListener(this);

        change = new DoubleJSlider(0, 1000,
                config.getDouble("moisture.change"), 100);
        change.addChangeListener(this);

        //Circulation
        exchangeCoefficient = new DoubleJSlider(0, 1000,
                config.getDouble("circulation.exchange_coefficient"), 100);
        exchangeCoefficient.addChangeListener(this);

        circulationOctaves = new JSlider(0, 1000,
                config.getInt("circulation.circulation_octaves"));
        circulationOctaves.addChangeListener(this);

        temperatureInfluence = new DoubleJSlider(0, 1000,
                config.getDouble("circulation.temperature_influence"), 100);
        temperatureInfluence.addChangeListener(this);

        circulationDecline = new JSlider(0, 1000,
                config.getInt("circulation.circulation_decline"));
        circulationDecline.addChangeListener(this);

        //Humidity
        evaporation = new DoubleJSlider(0, 1000,
                config.getDouble("humidity.evaporation"), 100);
        evaporation.addChangeListener(this);

        transpiration = new DoubleJSlider(0, 1000,
                config.getDouble("humidity.transpiration"), 100);
        transpiration.addChangeListener(this);

        debug = new JSlider(0, 1000, 10);
        debug.addChangeListener(this);

        //Humidity
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Humidity"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Transpiration"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(transpiration, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Evaporation"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(evaporation, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Transpiration"), gbc);

        //Circulation
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Circulation"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(new JLabel("Exchange coefficient"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        panel.add(exchangeCoefficient, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        panel.add(new JLabel("Circulation octaves"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        panel.add(circulationOctaves, gbc);

        gbc.gridx = 0;
        gbc.gridy = 12;
        panel.add(new JLabel("Temperature influence"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 13;
        panel.add(temperatureInfluence, gbc);

        gbc.gridx = 0;
        gbc.gridy = 14;
        panel.add(new JLabel("Circulation decline"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 15;
        panel.add(circulationDecline, gbc);

        gbc.gridx = 0;
        gbc.gridy = 16;
        panel.add(new JLabel("Elevation delta"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 17;
        panel.add(elevationDelta, gbc);

        //Moisture
        gbc.gridx = 0;
        gbc.gridy = 18;
        panel.add(new JLabel("Moisture"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 19;
        panel.add(new JLabel("Zoom"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 20;
        panel.add(zoom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 21;
        panel.add(new JLabel("Moisture Intensity"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 22;
        panel.add(moistureIntensity, gbc);

        gbc.gridx = 0;
        gbc.gridy = 23;
        panel.add(new JLabel("Change"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 24;
        panel.add(change, gbc);

        gbc.gridx = 0;
        gbc.gridy = 25;
        panel.add(new JLabel("Precipitation"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 26;
        panel.add(new JLabel("Circulation intensity"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 27;
        panel.add(circulationIntensity, gbc);

        gbc.gridx = 0;
        gbc.gridy = 28;
        panel.add(new JLabel("Orographic effect"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 29;
        panel.add(orographicEffect, gbc);

        gbc.gridx = 0;
        gbc.gridy = 30;
        panel.add(new JLabel("Precipitation intensity"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 31;
        panel.add(precipitationIntensity, gbc);

        gbc.gridx = 0;
        gbc.gridy = 32;
        panel.add(new JLabel("Iteration"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 33;
        panel.add(iteration, gbc);

        //Temperature
        gbc.gridx = 0;
        gbc.gridy = 34;
        panel.add(new JLabel("Temperature"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 35;
        panel.add(new JLabel("Min temperature"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 36;
        panel.add(minTemperature, gbc);

        gbc.gridx = 0;
        gbc.gridy = 37;
        panel.add(new JLabel("Max temperature"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 38;
        panel.add(maxTemperature, gbc);

        gbc.gridx = 0;
        gbc.gridy = 39;
        panel.add(new JLabel("Temperature decrease"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 40;
        panel.add(temperatureDecrease, gbc);

        gbc.gridx = 0;
        gbc.gridy = 41;
        panel.add(new JLabel("Debug (increase this to get image)"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 42;
        panel.add(debug, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 43;
        panel.add(biomesCanvas, gbc);
        return panel;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        String string =
                "#Configuration file for Weltschmerz\n" +
                "map{\n" +
                "    #World and image width\n" +
                "    longitude = "+ longitude +"\n" +
                "\n" +
                "    #World and image height\n" +
                "    latitude = "+latitude+"\n" +
                "\n" +
                "    #Seed to generate unique world\n" +
                "    seed = 12316\n" +
                "\n" +
                "    use_earth_image = "+config.getString("map.use_earth_image")+"\n" +
                "\n" +
                "    //Minimal map elvation in negative value\n" +
                "    min_elevation = -500\n" +
                "\n" +
                "    //Maximum map elevation\n" +
                "    max_elevation = 500\n" +
                "}\n" +
                "\n" +
                "noise {\n" +
                "    #Octaves of noise (quatintity of continents)\n" +
                "    octaves = 4\n" +
                "\n" +
                "    #Frequency of noise\n" +
                "    frequency = 7\n" +
                "\n" +
                "    #Noise Samples\n" +
                "    samples = 20\n" +
                "}\n" +
                "\n" +
                "temperature{\n" +
                "    //Maximum world temperature\n" +
                "    max_temperature = "+maxTemperature.getValue()+"\n" +
                "\n" +
                "    //Minimum world temperature\n" +
                "    min_temperature = "+minTemperature.getValue()+"\n" +
                "\n" +
                "    //Temperature decrease with elevation\n" +
                "    temperature_decrease = "+temperatureDecrease.getDouble()+"\n" +
                "}\n" +
                "\n" +
                "moisture{\n" +
                "    //Prevents from multiple moisture zones\n" +
                "    zoom = "+zoom.getDouble()+"\n" +
                "\n" +
                "    //Intesity of moisture\n" +
                "    moisture_intensity = "+moistureIntensity.getDouble()+"\n" +
                "\n" +
                "    //Defines how intense are bigger miosture zones\n" +
                "    change = "+change.getDouble()+"\n" +
                "}\n" +
                "\n" +
                "precipitation{\n" +
                "    //Defines intensity of circulation\n" +
                "    circulation_intensity = "+circulationIntensity.getDouble()+"\n" +
                "\n" +
                "    //Intensity of orographic effect\n" +
                "    orographic_effect = "+orographicEffect.getDouble()+"\n" +
                "\n" +
                "    precipitation_intensity = "+precipitationIntensity.getDouble()+"\n" +
                "\n" +
                "    iteration = "+iteration.getDouble()+"\n" +
                "\n" +
                "    //Elevation influence on precipitation\n" +
                "    elevation_delta = "+elevationDelta.getValue()+"\n" +
                "}\n" +
                "\n" +
                "humidity{\n" +
                "    //Transpiration intensity\n" +
                "    transpiration = "+transpiration.getDouble()+"\n" +
                "\n" +
                "    //Evaporation intensity\n" +
                "    evaporation = "+evaporation.getDouble()+"\n" +
                "}\n" +
                "\n" +
                "circulation{\n" +
                "    exchange_coefficient = "+exchangeCoefficient.getDouble()+"\n" +
                "\n" +
                "    //Range of simulated circulation\n" +
                "    circulation_octaves = "+circulationOctaves.getValue()+"\n" +
                "\n" +
                "    //temeprature influence on circulation\n" +
                "    temperature_influence = "+temperatureInfluence.getDouble()+"\n" +
                "\n" +
                "    circulation_decline = "+circulationDecline.getValue()+"\n" +
                "}\n" +
                "\n" +
                "#Affects level (height) of land generation\n" +
                "biomes{\n" +
                "    TROPICAL_RAINFOREST{\n" +
                "        color = 005430\n" +
                "    }\n" +
                "    TEMPERATE_RAINFOREST{\n" +
                "        color = 00556D\n" +
                "    }\n" +
                "    SAVANNA{\n" +
                "        color = 99A525\n" +
                "    }\n" +
                "    TEMPERATE_SEASONAL_FOREST{\n" +
                "        color = 2C89A1\n" +
                "    }\n" +
                "    BOREAL_FOREST{\n" +
                "        color = 5B8F51\n" +
                "    }\n" +
                "    WOODLAND{\n" +
                "        color = B37C00\n" +
                "    }\n" +
                "    SUBTROPICAL_DESERT{\n" +
                "        color = C67137\n" +
                "    }\n" +
                "    TEMPERATURE_GRASSLAND{\n" +
                "        color = 927D31\n" +
                "    }\n" +
                "    TUNDRA{\n" +
                "        color = 92A7AC\n" +
                "    }\n" +
                "}";

        Config config = ConfigFactory.parseString(string);

        world.changeConfiguration(config);
        biomesCanvas.updateImage();
    }
}
