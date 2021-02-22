package main.utils;

import javafx.scene.paint.Color;

import java.util.Random;

public class MyColors {
    static public Color getRandomColor(){
        Random rnd = new Random();
        double red = (double)rnd.nextInt(256) / 256;
        double green = (double)rnd.nextInt(256) / 256;
        double blue = (double)rnd.nextInt(256) / 256;
        return Color.color(red, green, blue);
    }
    static public Color getEvenlyDistributedColor(int k, int partsAmount){
        int color = (int)(((double)(k+1)/partsAmount) * (0xffffff - 100));
        String colorCode = String.format("#%06x", color);
        System.out.println(colorCode);
        return Color.web(colorCode, 1);
    }
    static public Color getEvenlyDistributedColorHSB(int k, int partsAmount){
        double interval = 360d / (partsAmount);
        return Color.hsb((double)(k+1) * interval, 1, 1);
    }
}
