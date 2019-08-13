package com.ritualsoftheold.weltschmerz.test;

import com.google.common.collect.HashMultimap;
import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.junit.Before;
import org.junit.Test;
import xerial.larray.LByteArray;
import xerial.larray.japi.LArrayJ;

public class LarrayTest {
    private Weltschmerz weltschmerz;

    @Before
    public void init(){
        weltschmerz = new Weltschmerz();
        weltschmerz.setBlocksID(0, 0, 0);
        MultiKeyMap<Integer, Byte> tree = new MultiKeyMap<>();
        int[][][] bounds = new int[10][10][10];

        tree.put(1, 1, (byte)6);
        tree.put(1, 2,  (byte)6);
        tree.put(2, 1,  (byte)6);
        weltschmerz.setObject(tree, bounds);
    }

    @Test
    public void larrayTest() {
        LByteArray lByteArray = LArrayJ.newLByteArray(262144);
        LByteArray resultArray = weltschmerz.getChunk(0,2,0, lByteArray);
        for(int i = 0; i < 262144; i ++){
            System.out.println(resultArray.apply(i));
        }
    }
}
