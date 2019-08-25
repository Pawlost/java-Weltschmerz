package com.ritualsoftheold.weltschmerz.misc.misc;

import squidpony.squidmath.XoRoRNG;

public class Random {
    private XoRoRNG random;
    public Random(long seed){
        random = new XoRoRNG(seed);
    }

    public boolean getBoolean(){
        return random.nextBoolean();
    }
}
