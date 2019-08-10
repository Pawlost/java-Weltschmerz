package com.ritualsoftheold.weltschmerz.maps;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.misc.misc.Configuration;
import com.ritualsoftheold.weltschmerz.maps.circulation.WorldCirculationCanvas;
import com.ritualsoftheold.weltschmerz.maps.humidity.WorldHumidityCanvas;
import com.ritualsoftheold.weltschmerz.maps.moisture.WorldMoistureCanvas;
import com.ritualsoftheold.weltschmerz.maps.noise.WorldNoiseCanvas;
import com.ritualsoftheold.weltschmerz.maps.precipitation.WorldPrecipitationCanvas;
import com.ritualsoftheold.weltschmerz.maps.pressure.WorldPressureCanvas;
import com.ritualsoftheold.weltschmerz.maps.temperature.WorldTemperatureCanvas;
import com.ritualsoftheold.weltschmerz.maps.world.WorldBiomesCanvas;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class All extends JPanel implements ActionListener {
    public static void main(String... args) {
        new All();
    }

    private World world;
    private int width;
    private int height;
    private NumberFormatter integerFormatter;
    private NumberFormatter doubleFormatter;

    //Temperature
    private JFormattedTextField minTemperature;
    private JSlider maxTemperature;
    private JFormattedTextField temperatureDecrease;

    //Elevation
    private JFormattedTextField elevationDelta;

    //Moisture
    private JFormattedTextField zoom;
    private JFormattedTextField placement;
    private JFormattedTextField moistureIntensity;
    private JFormattedTextField change;

    //Debug
    private JFormattedTextField moisture;
    private JFormattedTextField pressure;
    private JFormattedTextField precipitation;
    private JFormattedTextField humidity;

    //Precipitation
    private JFormattedTextField circulation;
    private JFormattedTextField orographicEffect;
    private JFormattedTextField precipitationIntensity;
    private JFormattedTextField iteration;

    //Humidity
    private JFormattedTextField traspiration;
    private JFormattedTextField evaporation;

    //Circulation
    private JFormattedTextField exchangeCoeficient;
    private JFormattedTextField circulationOctaves;
    private JFormattedTextField temperatureInfluence;
    private JFormattedTextField circulationDecline;

    private All() {
        super();
        Weltschmerz weltschmerz = new Weltschmerz();
        this.world = weltschmerz.world;

        //Creates frame for heigh map
        JFrame frame = new JFrame("All");

        frame.setPreferredSize(new Dimension(1200, 900));

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.add(init());
        this.setLayout(new GridLayout(1, 1, 10, 10));
        frame.add(this);

        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JTabbedPane init() {

        width = world.conf.longitude;
        height = world.conf.latitude;

        integerFormatter = new NumberFormatter();
        integerFormatter.setValueClass(Integer.class);

        doubleFormatter = new NumberFormatter();
        doubleFormatter.setValueClass(Double.class);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Biomes", null, initBiomesCanvas(width, height, world));
        tabbedPane.addTab("Temperature", null, initTemperatureCanvas(width, height, world));
        tabbedPane.addTab("Humidity", null, initHumidityCanvas(width, height, world));
        tabbedPane.addTab("Precipitation", null, initPrecipitationCanvas(width, height, world));
        tabbedPane.addTab("Moisture", null, initMoistureCanvas(width, height, world));
        tabbedPane.addTab("Circulation", null, initCirculationCanvas(width, height, world));
        tabbedPane.addTab("Pressure", null, initPressureCanvas(width, height, world));
        tabbedPane.addTab("Elevation", null, initNoiseCanvas(width, height, world));

        return tabbedPane;
    }

    private JComponent initBiomesCanvas(int width, int height, World world) {
        WorldBiomesCanvas biomesCanvas = new WorldBiomesCanvas(width, height, world);
        JPanel panel = new JPanel();

        JButton generate = new JButton("Generate");
        generate.addActionListener(biomesCanvas);
        generate.setActionCommand("generate");

        JButton save = new JButton("Save");
        save.addActionListener(this);
        save.setActionCommand("save");

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        panel.add(save, gbc);
        panel.add(generate, gbc);
        panel.add(biomesCanvas, gbc);
        return panel;
    }

    private JComponent initTemperatureCanvas(int width, int height, World world) {
        WorldTemperatureCanvas temperatureCanvas = new WorldTemperatureCanvas(width, height, world);
        JPanel panel = new JPanel();

        JButton generate = new JButton("Generate");
        generate.addActionListener(temperatureCanvas);
        generate.setActionCommand("generate");

        JButton save = new JButton("Save");
        save.addActionListener(this);
        save.setActionCommand("save");

        NumberFormatter temperature = new NumberFormatter();
        temperature.setValueClass(Integer.class);
        temperature.setMaximum(0);

        minTemperature = new JFormattedTextField(temperature);
        minTemperature.setValue(world.conf.minTemperature);
        minTemperature.setSize(200, 10);

        maxTemperature = new JSlider(0, 90, (int) world.conf.maxTemperature);

        temperatureDecrease = new JFormattedTextField(doubleFormatter);
        temperatureDecrease.setValue(world.conf.temperatureDecrease);
        temperatureDecrease.setSize(200, 10);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Min temperature"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(minTemperature, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Max temperature"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(maxTemperature, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Temperature decrease"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(temperatureDecrease, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(save, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(generate, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 6;
        panel.add(temperatureCanvas, gbc);

        return panel;
    }

    private JComponent initPrecipitationCanvas(int width, int height, World world) {
        WorldPrecipitationCanvas precipitationCanvas = new WorldPrecipitationCanvas(width, height, world);
        JPanel panel = new JPanel();

        JButton generate = new JButton("Generate");
        generate.addActionListener(precipitationCanvas);
        generate.setActionCommand("generate");

        JButton save = new JButton("Save");
        save.addActionListener(this);
        save.setActionCommand("save");

        circulation = new JFormattedTextField(doubleFormatter);
        circulation.setValue(world.conf.circulation);
        circulation.setSize(100, 10);

        orographicEffect = new JFormattedTextField(doubleFormatter);
        orographicEffect.setValue(world.conf.orographicEffect);
        orographicEffect.setSize(100, 10);

        precipitationIntensity = new JFormattedTextField(doubleFormatter);
        precipitationIntensity.setValue(world.conf.precipitationIntensity);
        precipitationIntensity.setSize(100, 10);

        iteration = new JFormattedTextField(doubleFormatter);
        iteration.setValue(world.conf.iteration);
        iteration.setSize(100, 10);

        precipitation = new JFormattedTextField(integerFormatter);
        precipitation.setValue(world.conf.precipitation);
        precipitation.setSize(100, 10);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Precipitation circulation"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(circulation, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Orographic effect"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(orographicEffect, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Precipitation intensity"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(precipitationIntensity, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Iteration"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(iteration, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Debug (increase this to get image)"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(precipitation, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(save, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(generate, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 8;
        panel.add(precipitationCanvas, gbc);

        return panel;
    }

    private JComponent initNoiseCanvas(int width, int height, World world) {
        WorldNoiseCanvas noiseCanvas = new WorldNoiseCanvas(width, height, world);
        JPanel panel = new JPanel();
        JButton generate = new JButton("Generate");
        generate.addActionListener(noiseCanvas);
        generate.setActionCommand("generate");

        JButton save = new JButton("Save");
        save.addActionListener(this);
        save.setActionCommand("save");

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(save, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(generate, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        panel.add(noiseCanvas, gbc);

        return panel;
    }

    private JComponent initMoistureCanvas(int width, int height, World world) {
        WorldMoistureCanvas moistureCanvas = new WorldMoistureCanvas(width, height, world);
        JPanel panel = new JPanel();

        JButton generate = new JButton("Generate");
        generate.addActionListener(moistureCanvas);
        generate.setActionCommand("generate");

        JButton save = new JButton("Save");
        save.addActionListener(this);
        save.setActionCommand("save");

        placement = new JFormattedTextField(integerFormatter);
        placement.setValue(world.conf.placement);

        zoom = new JFormattedTextField(doubleFormatter);
        zoom.setValue(world.conf.zoom);

        moistureIntensity = new JFormattedTextField(doubleFormatter);
        moistureIntensity.setValue(world.conf.moistureIntensity);

        change = new JFormattedTextField(doubleFormatter);
        change.setValue(world.conf.change);

        moisture = new JFormattedTextField(integerFormatter);
        moisture.setValue(world.conf.moisture);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Moisture placement"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(placement, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Zoom"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(zoom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Moisture Intensity"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(moistureIntensity, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Change"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(change, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Debug (increase this to get image)"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(moisture, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(save, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(generate, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 8;
        panel.add(moistureCanvas, gbc);
        return panel;
    }

    private JComponent initCirculationCanvas(int width, int height, World world) {
        WorldCirculationCanvas circulationCanvas = new WorldCirculationCanvas(width, height, world);
        JPanel panel = new JPanel();

        JButton generate = new JButton("Generate");
        generate.addActionListener(circulationCanvas);
        generate.setActionCommand("generate");

        JButton save = new JButton("Save");
        save.addActionListener(this);
        save.setActionCommand("save");

        elevationDelta = new JFormattedTextField(integerFormatter);
        elevationDelta.setValue(world.conf.elevationDelta);
        elevationDelta.setSize(100, 10);

        exchangeCoeficient = new JFormattedTextField(doubleFormatter);
        exchangeCoeficient.setValue(world.conf.exchangeCoeficient);

        circulationOctaves = new JFormattedTextField(integerFormatter);
        circulationOctaves.setValue(world.conf.circulationOctaves);

        temperatureInfluence = new JFormattedTextField(doubleFormatter);
        temperatureInfluence.setValue(world.conf.temperatureInfluence);

        circulationDecline = new JFormattedTextField(integerFormatter);
        circulationDecline.setValue(world.conf.circulationDecline);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Exchange coeficient"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(exchangeCoeficient, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Circulation octaves"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(circulationOctaves, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Temperature influence"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(temperatureInfluence, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Circulation decline"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(circulationDecline, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Elevation delta"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(elevationDelta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(save, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(generate, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 8;
        panel.add(circulationCanvas, gbc);

        return panel;
    }

    private JComponent initPressureCanvas(int width, int height, World world) {
        WorldPressureCanvas pressureCanvas = new WorldPressureCanvas(width, height, world);
        JPanel panel = new JPanel();

        JButton generate = new JButton("Generate");
        generate.addActionListener(pressureCanvas);
        generate.setActionCommand("generate");

        JButton save = new JButton("Save");
        save.addActionListener(this);
        save.setActionCommand("save");

        pressure = new JFormattedTextField(integerFormatter);
        pressure.setValue(world.conf.pressure);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Debug (increase this to get image)"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(pressure, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(save, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(generate, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        panel.add(pressureCanvas, gbc);

        return panel;
    }

    private JComponent initHumidityCanvas(int width, int height, World world) {
        WorldHumidityCanvas humidityCanvas = new WorldHumidityCanvas(width, height, world);
        JPanel panel = new JPanel();

        JButton generate = new JButton("Generate");
        generate.addActionListener(humidityCanvas);
        generate.setActionCommand("generate");

        JButton save = new JButton("Save");
        save.addActionListener(this);
        save.setActionCommand("save");

        humidity = new JFormattedTextField(integerFormatter);
        humidity.setValue(world.conf.humidity);

        evaporation = new JFormattedTextField(doubleFormatter);
        evaporation.setValue(world.conf.evaporation);

        traspiration = new JFormattedTextField(doubleFormatter);
        traspiration.setValue(world.conf.traspiration);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Transpiration"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(traspiration, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Evaporation"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(evaporation, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Debug (increase this to get image)"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(humidity, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(save, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(generate, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 6;
        panel.add(humidityCanvas, gbc);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Configuration configuration = new Configuration();
        configuration.latitude = height;
        configuration.longitude = width;

        //Temperature
        configuration.minTemperature = Integer.parseInt(minTemperature.getText());
        configuration.maxTemperature = maxTemperature.getValue();
        configuration.temperatureDecrease = (Double) temperatureDecrease.getValue();

        //Elevation
        configuration.elevationDelta = Integer.parseInt(elevationDelta.getText());

        //Moisture
        configuration.zoom = (Double) zoom.getValue();
        configuration.placement = Integer.parseInt(placement.getText());
        configuration.moistureIntensity = (Double) moistureIntensity.getValue();
        configuration.change = (Double) change.getValue();

        //Height Maps
        configuration.moisture = Integer.parseInt(moisture.getText());
        configuration.pressure = Integer.parseInt(pressure.getText());
        configuration.precipitation = Integer.parseInt(precipitation.getText());
        configuration.humidity = Integer.parseInt(precipitation.getText());

        //Precipitation
        configuration.circulation = (Double) circulation.getValue();
        configuration.orographicEffect = (Double) orographicEffect.getValue();
        configuration.precipitationIntensity = (Double) precipitationIntensity.getValue();
        configuration.iteration =(Double) iteration.getValue();

        //Humidity
        configuration.traspiration = (Double) traspiration.getValue();
        configuration.evaporation = (Double) evaporation.getValue();
        configuration.humidity = Integer.parseInt(humidity.getText());

        //Circulation
        configuration.exchangeCoeficient = (Double) exchangeCoeficient.getValue();
        configuration.circulationOctaves = Integer.parseInt(circulationOctaves.getText());
        configuration.temperatureInfluence = (Double) temperatureInfluence.getValue();
        configuration.circulationDecline = Integer.parseInt(circulationDecline.getText());

        world.changeConfiguration(configuration);
    }
}
