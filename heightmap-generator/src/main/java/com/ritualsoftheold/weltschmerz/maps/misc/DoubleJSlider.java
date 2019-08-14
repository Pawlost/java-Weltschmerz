package com.ritualsoftheold.weltschmerz.maps.misc;

import javax.swing.*;

public class DoubleJSlider extends JSlider {
    private double factor;
    public DoubleJSlider(double min, double max, double value, double factor){
        super((int)(min * factor), (int)(max * factor), (int)(value * factor));
        this.factor = factor;
    }


    public void setValue(double n) {
        super.setValue((int)(n*factor));
    }

    public double getDouble(){
        return (super.getValue()/factor);
    }
}
