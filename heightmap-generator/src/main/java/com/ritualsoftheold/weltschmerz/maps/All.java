package com.ritualsoftheold.weltschmerz.maps;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.maps.misc.DoubleJSlider;
import com.ritualsoftheold.weltschmerz.maps.world.WorldBiomesCanvas;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class All extends JPanel implements MouseListener, ActionListener {

    public static void main(String... args) {
        new All();
    }

    public static final int THREADS = 8;

    private NumberFormatter integerFormatter;
    private NumberFormatter doubleFormatter;

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

    //Temperature
    private JFormattedTextField minTemperatureLabel;
    private JFormattedTextField maxTemperatureLabel;
    private JFormattedTextField temperatureDecreaseLabel;

    //Moisture
    private JFormattedTextField zoomLabel;
    private JFormattedTextField moistureIntensityLabel;
    private JFormattedTextField changeLabel;

    //Debug
    private JFormattedTextField debugLabel;

    //Precipitation
    private JFormattedTextField circulationIntensityLabel;
    private JFormattedTextField orographicEffectLabel;
    private JFormattedTextField precipitationIntensityLabel;
    private JFormattedTextField iterationLabel;
    private JFormattedTextField elevationDeltaLabel;

    //Humidity
    private JFormattedTextField transpirationLabel;
    private JFormattedTextField evaporationLabel;

    //Circulation
    private JFormattedTextField exchangeCoefficientLabel;
    private JFormattedTextField circulationOctavesLabel;
    private JFormattedTextField temperatureInfluenceLabel;
    private JFormattedTextField circulationDeclineLabel;

    private All() {
        super();
        Weltschmerz weltschmerz = new Weltschmerz();
        this.world = weltschmerz.world;
        config = world.config;

        latitude = config.getInt("map.latitude");
        longitude = config.getInt("map.longitude");

        integerFormatter = new NumberFormatter();
        integerFormatter.setValueClass(Integer.class);
        integerFormatter.setAllowsInvalid(false);

        doubleFormatter = new NumberFormatter();
        doubleFormatter.setValueClass(Double.class);
        doubleFormatter.setAllowsInvalid(false);

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
        minTemperature = new JSlider(-100, 0, config.getInt("temperature.min_temperature"));
        minTemperature.addMouseListener(this);

        minTemperatureLabel = new JFormattedTextField(integerFormatter);
        minTemperatureLabel.setValue(config.getInt("temperature.min_temperature"));
        minTemperatureLabel.addActionListener(this);
        minTemperatureLabel.setPreferredSize(new Dimension(50, 20));

        maxTemperature = new JSlider(0, 40, config.getInt("temperature.max_temperature"));
        maxTemperature.addMouseListener(this);

        maxTemperatureLabel = new JFormattedTextField(integerFormatter);
        maxTemperatureLabel.setValue(config.getInt("temperature.max_temperature"));
        maxTemperatureLabel.addActionListener(this);
        maxTemperatureLabel.setPreferredSize(new Dimension(50, 20));

        temperatureDecrease = new DoubleJSlider(0, 100,
                config.getDouble("temperature.temperature_decrease"), 10);
        temperatureDecrease.addMouseListener(this);

        temperatureDecreaseLabel = new JFormattedTextField(doubleFormatter);
        temperatureDecreaseLabel.setValue(config.getDouble("temperature.temperature_decrease"));
        temperatureDecreaseLabel.addActionListener(this);
        temperatureDecreaseLabel.setPreferredSize(new Dimension(50, 20));


        //Precipitation
        circulationIntensity = new DoubleJSlider(0, 100,
                config.getDouble("precipitation.circulation_intensity"), 10);
        circulationIntensity.addMouseListener(this);

        circulationIntensityLabel = new JFormattedTextField(doubleFormatter);
        circulationIntensityLabel.setValue(config.getDouble("precipitation.circulation_intensity"));
        circulationIntensityLabel.addActionListener(this);
        circulationIntensityLabel.setPreferredSize(new Dimension(50, 20));

        orographicEffect = new DoubleJSlider(0, 100,
                config.getDouble("precipitation.orographic_effect"), 10);
        orographicEffect.addMouseListener(this);

        orographicEffectLabel = new JFormattedTextField(doubleFormatter);
        orographicEffectLabel.setValue(config.getDouble("precipitation.orographic_effect"));
        orographicEffectLabel.addActionListener(this);
        orographicEffectLabel.setPreferredSize(new Dimension(50, 20));

        precipitationIntensity = new DoubleJSlider(0, 100,
                config.getDouble("precipitation.precipitation_intensity"), 10);
        precipitationIntensity.addMouseListener(this);

        precipitationIntensityLabel = new JFormattedTextField(doubleFormatter);
        precipitationIntensityLabel.setValue(config.getDouble("precipitation.precipitation_intensity"));
        precipitationIntensityLabel.addActionListener(this);
        precipitationIntensityLabel.setPreferredSize(new Dimension(50, 20));

        iteration = new DoubleJSlider(0, Math.max(longitude, latitude)*10,
                config.getDouble("precipitation.iteration"), 10);
        iteration.addMouseListener(this);

        iterationLabel = new JFormattedTextField(doubleFormatter);
        iterationLabel.setValue(config.getDouble("precipitation.iteration"));
        iterationLabel.addActionListener(this);
        iterationLabel.setPreferredSize(new Dimension(50, 20));

        elevationDelta = new JSlider(0, Math.max(longitude, latitude),
                config.getInt("precipitation.elevation_delta"));
        elevationDelta.addMouseListener(this);

        elevationDeltaLabel = new JFormattedTextField(integerFormatter);
        elevationDeltaLabel.setValue(config.getInt("precipitation.elevation_delta"));
        elevationDeltaLabel.addActionListener(this);
        elevationDeltaLabel.setPreferredSize(new Dimension(50, 20));

        //Moisture
        zoom = new DoubleJSlider(0, 100,
                config.getDouble("moisture.zoom"), 100);
        zoom.addMouseListener(this);

        zoomLabel = new JFormattedTextField(doubleFormatter);
        zoomLabel.setValue(config.getDouble("moisture.zoom"));
        zoomLabel.addActionListener(this);
        zoomLabel.setPreferredSize(new Dimension(50, 20));

        moistureIntensity = new DoubleJSlider(0, 100,
                config.getDouble("moisture.moisture_intensity"), 10);
        moistureIntensity.addMouseListener(this);

        moistureIntensityLabel = new JFormattedTextField(doubleFormatter);
        moistureIntensityLabel.setValue(config.getDouble("moisture.moisture_intensity"));
        moistureIntensityLabel.addActionListener(this);
        moistureIntensityLabel.setPreferredSize(new Dimension(50, 20));

        change = new DoubleJSlider(0, 100,
                config.getDouble("moisture.change"), 10);
        change.addMouseListener(this);

        changeLabel = new JFormattedTextField(doubleFormatter);
        changeLabel.setValue(config.getDouble("moisture.change"));
        changeLabel.addActionListener(this);
        changeLabel.setPreferredSize(new Dimension(50, 20));

        //Circulation
        exchangeCoefficient = new DoubleJSlider(0, 100,
                config.getDouble("circulation.exchange_coefficient"), 10);
        exchangeCoefficient.addMouseListener(this);

        exchangeCoefficientLabel = new JFormattedTextField(doubleFormatter);
        exchangeCoefficientLabel.setValue(config.getDouble("circulation.exchange_coefficient"));
        exchangeCoefficientLabel.addActionListener(this);
        exchangeCoefficientLabel.setPreferredSize(new Dimension(50, 20));

        circulationOctaves = new JSlider(0, 100,
                config.getInt("circulation.circulation_octaves"));
        circulationOctaves.addMouseListener(this);

        circulationOctavesLabel = new JFormattedTextField(integerFormatter);
        circulationOctavesLabel.setValue(config.getInt("circulation.circulation_octaves"));
        circulationOctavesLabel.addActionListener(this);
        circulationOctavesLabel.setPreferredSize(new Dimension(50, 20));

        temperatureInfluence = new DoubleJSlider(0, 100,
                config.getDouble("circulation.temperature_influence"), 10);
        temperatureInfluence.addMouseListener(this);

        temperatureInfluenceLabel = new JFormattedTextField(doubleFormatter);
        temperatureInfluenceLabel.setValue(config.getDouble("circulation.temperature_influence"));
        temperatureInfluenceLabel.addActionListener(this);
        temperatureInfluenceLabel.setPreferredSize(new Dimension(50, 20));

        circulationDecline = new JSlider(0, 100,
                config.getInt("circulation.circulation_decline"));
        circulationDecline.addMouseListener(this);

        circulationDeclineLabel = new JFormattedTextField(integerFormatter);
        circulationDeclineLabel.setValue(config.getInt("circulation.circulation_decline"));
        circulationDeclineLabel.addActionListener(this);
        circulationDeclineLabel.setPreferredSize(new Dimension(50, 20));

        //Humidity
        evaporation = new DoubleJSlider(0, 100,
                config.getDouble("humidity.evaporation"), 10);
        evaporation.addMouseListener(this);

        evaporationLabel = new JFormattedTextField(doubleFormatter);
        evaporationLabel.setValue(config.getDouble("humidity.evaporation"));
        evaporationLabel.addActionListener(this);
        evaporationLabel.setPreferredSize(new Dimension(50, 20));

        transpiration = new DoubleJSlider(0, 100,
                config.getDouble("humidity.transpiration"), 10);
        transpiration.addMouseListener(this);

        transpirationLabel = new JFormattedTextField(doubleFormatter);
        transpirationLabel.setValue(config.getDouble("humidity.transpiration"));
        transpirationLabel.addActionListener(this);
        transpirationLabel.setPreferredSize(new Dimension(50, 20));

        debug = new JSlider(0, 1000, 10);
        debug.addMouseListener(this);

        //Humidity
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Humidity"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Transpiration"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(transpiration, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(transpirationLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Evaporation"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(evaporation, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(evaporationLabel, gbc);

        //Circulation
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Circulation"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Exchange coefficient"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 8;
        panel.add(exchangeCoefficient, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(exchangeCoefficientLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Circulation octaves"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 10;
        panel.add(circulationOctaves, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        panel.add(circulationOctavesLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Temperature influence"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 12;
        panel.add(temperatureInfluence, gbc);

        gbc.gridx = 0;
        gbc.gridy = 12;
        panel.add(temperatureInfluenceLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Circulation decline"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 14;
        panel.add(circulationDecline, gbc);

        gbc.gridx = 0;
        gbc.gridy = 14;
        panel.add(circulationDeclineLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Elevation delta"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 16;
        panel.add(elevationDelta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 16;
        panel.add(elevationDeltaLabel, gbc);

        //Moisture
        gbc.gridx = 0;
        gbc.gridy = 17;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Moisture"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 18;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Zoom"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 19;
        panel.add(zoom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 19;
        panel.add(zoomLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 20;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Moisture Intensity"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 21;
        panel.add(moistureIntensity, gbc);

        gbc.gridx = 0;
        gbc.gridy = 21;
        panel.add(moistureIntensityLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 22;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Change"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 23;
        panel.add(change, gbc);

        gbc.gridx = 0;
        gbc.gridy = 23;
        panel.add(changeLabel, gbc);


        gbc.gridx = 0;
        gbc.gridy = 24;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Precipitation"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 25;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Circulation intensity"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 26;
        panel.add(circulationIntensity, gbc);

        gbc.gridx = 0;
        gbc.gridy = 26;
        panel.add(circulationIntensityLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 27;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Orographic effect"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 28;
        panel.add(orographicEffect, gbc);

        gbc.gridx = 0;
        gbc.gridy = 28;
        panel.add(orographicEffectLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 29;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Precipitation intensity"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 30;
        panel.add(precipitationIntensity, gbc);

        gbc.gridx = 0;
        gbc.gridy = 30;
        panel.add(precipitationIntensityLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 31;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Iteration"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 33;
        panel.add(iteration, gbc);

        gbc.gridx = 0;
        gbc.gridy = 33;
        panel.add(iterationLabel, gbc);

        //Temperature
        gbc.gridx = 0;
        gbc.gridy = 33;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Temperature"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 34;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Min temperature"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 35;
        panel.add(minTemperature, gbc);

        gbc.gridx = 0;
        gbc.gridy = 35;
        panel.add(minTemperatureLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 36;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Max temperature"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 37;
        panel.add(maxTemperature, gbc);

        gbc.gridx = 0;
        gbc.gridy = 37;
        panel.add(maxTemperatureLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 38;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Temperature decrease"), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1;
        gbc.gridy = 39;
        panel.add(temperatureDecrease, gbc);

        gbc.gridx = 0;
        gbc.gridy = 39;
        panel.add(temperatureDecreaseLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 40;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Debug (increase this to get image)"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 42;
        panel.add(debug, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridheight = 43;
        panel.add(biomesCanvas, gbc);
        return panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
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
                        "    seed = "+config.getLong("map.seed")+"\n" +
                        "\n" +
                        "    use_earth_image = "+config.getString("map.use_earth_image")+"\n" +
                        "\n" +
                        "    //Minimal map elvation in negative value\n" +
                        "    min_elevation = "+config.getInt("map.min_elevation")+"\n" +
                        "\n" +
                        "    //Maximum map elevation\n" +
                        "    max_elevation = "+config.getInt("map.max_elevation")+"\n" +
                        "}\n" +
                        "\n" +
                        "noise {\n" +
                        "    #Octaves of noise (quatintity of continents)\n" +
                        "    octaves = "+config.getInt("noise.octaves")+"\n" +
                        "\n" +
                        "    #Frequency of noise\n" +
                        "    frequency = "+config.getInt("noise.frequency")+"\n" +
                        "\n" +
                        "    #Noise Samples\n" +
                        "    samples = "+config.getInt("noise.samples")+"\n" +
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
                        "}\n";



        //Temperature
        minTemperatureLabel.setValue(minTemperature.getValue());
        maxTemperatureLabel.setValue(maxTemperature.getValue());
        temperatureDecreaseLabel.setValue(temperatureDecrease.getDouble());

        //Moisture
        zoomLabel.setValue(zoom.getDouble());
        moistureIntensityLabel.setValue(moistureIntensity.getDouble());
        changeLabel.setValue(change.getValue());

        //Precipitation
        circulationIntensityLabel.setValue(circulationIntensity.getDouble());
        orographicEffectLabel.setValue(orographicEffect.getDouble());
        precipitationIntensityLabel.setValue(precipitationIntensity.getDouble());
        iterationLabel.setValue(iteration.getDouble());
        elevationDeltaLabel.setValue(elevationDelta.getValue());

        //Humidity
        transpirationLabel.setValue(transpiration.getDouble());
        evaporationLabel.setValue(evaporation.getDouble());

        //Circulation
        exchangeCoefficientLabel.setValue(exchangeCoefficient.getDouble());
        circulationOctavesLabel.setValue(circulationOctaves.getValue());
        temperatureInfluenceLabel.setValue(temperatureInfluence.getDouble());
        circulationDeclineLabel.setValue(circulationDecline.getValue());

        Config config = ConfigFactory.parseString(string);

        world.changeConfiguration(config);
        biomesCanvas.updateImage();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
                        "    seed = "+config.getLong("map.seed")+"\n" +
                        "\n" +
                        "    use_earth_image = "+config.getString("map.use_earth_image")+"\n" +
                        "\n" +
                        "    //Minimal map elvation in negative value\n" +
                        "    min_elevation = "+config.getInt("map.min_elevation")+"\n" +
                        "\n" +
                        "    //Maximum map elevation\n" +
                        "    max_elevation = "+config.getInt("map.max_elevation")+"\n" +
                        "}\n" +
                        "\n" +
                        "noise {\n" +
                        "    #Octaves of noise (quatintity of continents)\n" +
                        "    octaves = "+config.getInt("noise.octaves")+"\n" +
                        "\n" +
                        "    #Frequency of noise\n" +
                        "    frequency = "+config.getInt("noise.frequency")+"\n" +
                        "\n" +
                        "    #Noise Samples\n" +
                        "    samples = "+config.getInt("noise.samples")+"\n" +
                        "}\n" +
                        "\n" +
                        "temperature{\n" +
                        "    //Maximum world temperature\n" +
                        "    max_temperature = "+maxTemperatureLabel.getValue()+"\n" +
                        "\n" +
                        "    //Minimum world temperature\n" +
                        "    min_temperature = "+minTemperatureLabel.getValue()+"\n" +
                        "\n" +
                        "    //Temperature decrease with elevation\n" +
                        "    temperature_decrease = "+temperatureDecreaseLabel.getValue()+"\n" +
                        "}\n" +
                        "\n" +
                        "moisture{\n" +
                        "    //Prevents from multiple moisture zones\n" +
                        "    zoom = "+zoomLabel.getValue()+"\n" +
                        "\n" +
                        "    //Intesity of moisture\n" +
                        "    moisture_intensity = "+moistureIntensityLabel.getValue()+"\n" +
                        "\n" +
                        "    //Defines how intense are bigger miosture zones\n" +
                        "    change = "+changeLabel.getValue()+"\n" +
                        "}\n" +
                        "\n" +
                        "precipitation{\n" +
                        "    //Defines intensity of circulation\n" +
                        "    circulation_intensity = "+circulationIntensityLabel.getValue()+"\n" +
                        "\n" +
                        "    //Intensity of orographic effect\n" +
                        "    orographic_effect = "+orographicEffectLabel.getValue()+"\n" +
                        "\n" +
                        "    precipitation_intensity = "+precipitationIntensityLabel.getValue()+"\n" +
                        "\n" +
                        "    iteration = "+iterationLabel.getValue()+"\n" +
                        "\n" +
                        "    //Elevation influence on precipitation\n" +
                        "    elevation_delta = "+elevationDeltaLabel.getValue()+"\n" +
                        "}\n" +
                        "\n" +
                        "humidity{\n" +
                        "    //Transpiration intensity\n" +
                        "    transpiration = "+transpirationLabel.getValue()+"\n" +
                        "\n" +
                        "    //Evaporation intensity\n" +
                        "    evaporation = "+evaporationLabel.getValue()+"\n" +
                        "}\n" +
                        "\n" +
                        "circulation{\n" +
                        "    exchange_coefficient = "+exchangeCoefficientLabel.getValue()+"\n" +
                        "\n" +
                        "    //Range of simulated circulation\n" +
                        "    circulation_octaves = "+circulationOctavesLabel.getValue()+"\n" +
                        "\n" +
                        "    //temeprature influence on circulation\n" +
                        "    temperature_influence = "+temperatureInfluenceLabel.getValue()+"\n" +
                        "\n" +
                        "    circulation_decline = "+circulationDeclineLabel.getValue()+"\n" +
                        "}\n";

        //Temperature
        minTemperature.setValue((Integer) minTemperatureLabel.getValue());
        maxTemperature.setValue((Integer) maxTemperatureLabel.getValue());
        temperatureDecrease.setValue((Double) temperatureDecreaseLabel.getValue());

        //Moisture
        zoom.setValue((Double) zoomLabel.getValue());
        moistureIntensity.setValue((Double)moistureIntensityLabel.getValue());
        change.setValue((Double)changeLabel.getValue());

        //Precipitation
        circulationIntensity.setValue((Double)circulationIntensityLabel.getValue());
        orographicEffect.setValue((Double)orographicEffectLabel.getValue());
        precipitationIntensity.setValue((Double)precipitationIntensityLabel.getValue());
        iteration.setValue((Double)iterationLabel.getValue());
        elevationDelta.setValue((Integer) elevationDeltaLabel.getValue());

        //Humidity
        transpiration.setValue((Double)transpirationLabel.getValue());
        evaporation.setValue((Double)evaporationLabel.getValue());

        //Circulation
        exchangeCoefficient.setValue((Double)exchangeCoefficientLabel.getValue());
        circulationOctaves.setValue((Integer)circulationOctavesLabel.getValue());
        temperatureInfluence.setValue((Double)temperatureInfluenceLabel.getValue());
        circulationDecline.setValue((Integer)circulationDeclineLabel.getValue());

        Config config = ConfigFactory.parseString(string);

        world.changeConfiguration(config);
        biomesCanvas.updateImage();
    }
}
