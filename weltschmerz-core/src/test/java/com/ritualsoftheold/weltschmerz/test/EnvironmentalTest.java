package com.ritualsoftheold.weltschmerz.test;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.environment.Precipitation;
import com.ritualsoftheold.weltschmerz.geometry.misc.Utils;
import com.ritualsoftheold.weltschmerz.geometry.units.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import xerial.larray.LByteArray;
import xerial.larray.japi.LArrayJ;

import java.util.ArrayList;

public class EnvironmentalTest {
    private Weltschmerz weltschmerz;

    @Before
    public void init(){
        weltschmerz = new Weltschmerz();
    }

    @Test
    public void pressureTest() {
        double pressure =  weltschmerz.world.getPressure(0, 250);
        System.out.println(pressure);
    }

    @Test
    public void precipitationTest(){
        ArrayList<Double> arrayList = new ArrayList<>();
        for(int x = 0; x < 1000; x++) {
            for(int y =0; y < 500; y++) {
                double precipitation = weltschmerz.world.getPrecipitation(x, y);
                double temperature = weltschmerz.world.getTemperature(x, y) + 10;
                double elevation = weltschmerz.world.getElevation(x, y);
                arrayList.add(precipitation);
                System.out.println("precipitation "+precipitation);
                //System.out.println("temperature "+temperature);
                if(temperature > 0 && Utils.isLand(elevation)) {
                    Assert.assertTrue(precipitation > 0 && (int)precipitation < (temperature * 400) / 50);
                }
            }
        }
        double sum = 0.0;
        for(double lol:arrayList){
            sum += lol;
            System.out.println(lol);
        }
        System.out.println("Summary " + sum);
    }

    @Test
    public void humidityTest(){
        /*ArrayList<Double> arrayList = new ArrayList<>();
        for(int x = 0; x < 1000; x++) {
            for(int y =0; y < 500; y++) {
                double humidity = weltschmerz.world.getHumidity(x, y);
                arrayList.add(humidity);
                System.out.println(humidity);
                Assert.assertTrue(humidity > 0 && humidity < 400);
            }
        }

        double sum = 0.0;
        for(double lol:arrayList){
            sum += lol;
        }

        System.out.println(sum/arrayList.size());*/
    }

    @Test
    public void evapotraspirationTest() {
        ArrayList<Double> arrayList = new ArrayList<>();
        for (int y = 0; y < 500; y++) {
            double evapotraspiration = weltschmerz.world.getEvapotranspiration(y, false);
            arrayList.add(evapotraspiration);
        }
        double sum = 0.0;
        for (double lol : arrayList) {
            sum += lol;
            Assert.assertTrue(lol > 0 && lol < 400);
        }

        System.out.println(sum / arrayList.size());
    }


    @Test
    public void moistureTest() {
        ArrayList<Double> arrayList = new ArrayList<>();
        for (int y = 0; y < 500; y++) {
            double moisture = weltschmerz.world.getMoisture(y);
            arrayList.add(moisture);
        }
        double sum = 0.0;
        for (double lol : arrayList) {
            sum += lol;
            System.out.println(lol);
            Assert.assertTrue(lol >= 0 && lol < 400);
        }

        System.out.println(sum / arrayList.size());
    }

    @Test
    public void orographicEffectTest(){
      /*  for(int x = 0; x < 1000; x++) {
            for (int y = 0; y < 500; y++) {
                double elevation = weltschmerz.world.getElevation(x, y);
                Vector wind = weltschmerz.world.getAirFlow(x, y);
                double effect = Precipitation.getOrotographicEffect(elevation, wind);
                System.out.println(effect);
                Assert.assertTrue(effect >= 0);
            }
        }*/
    }
}
