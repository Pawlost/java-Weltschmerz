package com.ritualsoftheold.weltschmerz.maps;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.maps.noise.Noise;
import com.ritualsoftheold.weltschmerz.maps.temperature.Temperature;
import com.ritualsoftheold.weltschmerz.maps.world.Bioms;

public class All {
    public static void main(String... args) {
        Weltschmerz weltschmerz = new Weltschmerz();
        new Bioms(weltschmerz);
        new Temperature(weltschmerz);
        new Noise(weltschmerz);
    }
}
