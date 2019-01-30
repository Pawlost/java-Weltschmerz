package com.ritualsoftheold.weltschmerz.maps.experimental;

import javax.swing.*;
import java.awt.*;

import com.ritualsoftheold.weltschmerz.experimental.*;

public class TestHeightMapExperiment {
    public static void main(String... args) {
        int width = 1600;
        int height = 900;
        //Creates frame for heigh map
        JFrame frame = new JFrame("Joise Example 02");
        frame.setPreferredSize(new Dimension(width, height));

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Lithosphere lithospehere = new Lithosphere(300, 1.0f, 5, 3, 2 ,1,1);
        lithospehere.createPlates(5);
        Canvas canvas = new Canvas(300, lithospehere);
        canvas.updateImage();
        frame.add(canvas);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
