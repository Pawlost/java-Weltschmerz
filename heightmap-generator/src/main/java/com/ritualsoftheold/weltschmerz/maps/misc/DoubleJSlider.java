package com.ritualsoftheold.weltschmerz.maps.misc;

import javax.swing.*;

public class DoubleJSlider extends JSlider {
    private int factor;
    public DoubleJSlider(double min, double max, double value, int factor){
        super((int)min * factor, (int)max * factor, (int)value * factor);
        this.factor = factor;
    }

    public double getDouble(){
        return super.getValue()/factor;
    }
}
