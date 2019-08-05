package com.ritualsoftheold.weltschmerz.test;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import org.junit.Before;
import org.junit.Test;
import xerial.larray.LByteArray;
import xerial.larray.japi.LArrayJ;

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
}
