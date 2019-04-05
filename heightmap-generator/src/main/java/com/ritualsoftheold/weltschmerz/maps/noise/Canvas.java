package com.ritualsoftheold.weltschmerz.maps.noise;

import com.ritualsoftheold.weltschmerz.noise.WeltschmerzNoise;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

//Jframe canvas to show noise
public class Canvas extends JPanel {

  private static final float SCALE = 1.0f;
  private BufferedImage image;

  public Canvas(int width, int height) {
    this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  public void updateImage(WeltschmerzNoise noise) {
    int width = this.image.getWidth();
    int height = this.image.getHeight();

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        float c = (float) noise.getNoise(x, y);
        this.image.setRGB(x, y, new Color(c, c, c).getRGB());
      }
    }

    this.repaint();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.drawImage(this.image, null, null);
    g2.dispose();
  }

}
