package com.ritualsoftheold.weltschmerz.test;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import org.junit.Before;
import org.junit.Test;
import xerial.larray.LByteArray;
import xerial.larray.japi.LArrayJ;

public class LarrayTest {
    private Weltschmerz weltschmerz;

    @Before
    public void init(){
        weltschmerz = new Weltschmerz();
        weltschmerz.setMaterialID(1, 2);
    }

    @Test
    public void larrayTest() {
        LByteArray lByteArray = LArrayJ.newLByteArray(262144);
        LByteArray resultArray = weltschmerz.getChunk(0,48,0, lByteArray);
        for(int i = 0; i < 262144; i ++){
            System.out.println(resultArray.apply(i));
        }
    }
}
