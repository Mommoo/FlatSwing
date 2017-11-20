package com.mommoo.flat.utils;

import java.awt.*;

public class FlatColor {
    private FlatColor(){};

    public static Color darker(Color color, double factor){
        return new Color(Math.max((int)(color.getRed()  * factor), 0),
                Math.max((int)(color.getGreen() * factor), 0),
                Math.max((int)(color.getBlue() * factor), 0),
                color.getAlpha());
    }

    public static Color brighter(Color color, double factor){
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int alpha = color.getAlpha();

        int i = (int)(1.0/(1.0 - factor));
        if ( r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        }
        if ( r > 0 && r < i ) r = i;
        if ( g > 0 && g < i ) g = i;
        if ( b > 0 && b < i ) b = i;

        return new Color(Math.min((int)(r / factor), 255),
                Math.min((int)(g / factor), 255),
                Math.min((int)(b / factor), 255),
                alpha);
    }

    public static Color convertHueToRGB(int hue){

        if(hue <0 ) return null;

        float r = 0.0f;
        float g = 0.0f;
        float b = 0.0f;
        float sub = 4.250f;

        float ratio = hue - 60*(hue/60);
        if(ratio % 60 ==0) ratio += 60;
        if (hue == 0){
            r = 255.0f;
        }else if (hue>0 && hue<=60){
            r = 255.0f;
            g = sub*ratio;
            b = 0.0f;
        }else if (hue>60 && hue<=120){
            r = 255.0f - sub*ratio;
            g = 255.0f;
            b = 0.0f;
        }else if (hue>120 && hue<=180){
            r = 0f;
            g = 255.0f;
            b = sub*ratio;
        }else if (hue>180 && hue <= 240){
            r = 0f;
            g = 255.0f - sub*ratio;
            b = 255.0f;
        }else if (hue>240 && hue <= 300){
            r = sub*ratio;
            g = 0.0f;
            b = 255.0f;
        }else if (hue>300 && hue<=360){
            r = 255.0f;
            g = 0.0f;
            b = 255.0f - sub*ratio;
        }

        return new Color((int)r,(int)g,(int)b);
    }


    /**
     * @param h
     *            0-360
     * @param s
     *            0-100
     * @param b
     *            0-100
     * @return color in hex string
     */
    public static Color convertHSBtoRGB(float h, float s, float b) {

        float R, G, B;

        h /= 360f;
        s /= 100f;
        b /= 100f;

        if (s == 0)
        {
            R = b * 255;
            G = b * 255;
            B = b * 255;
        } else {
            float var_h = h * 6;
            if (var_h == 6)
                var_h = 0; // H must be < 1
            int var_i = (int) Math.floor((double) var_h); // Or ... var_i =
            // floor( var_h )
            float var_1 = b * (1 - s);
            float var_2 = b * (1 - s * (var_h - var_i));
            float var_3 = b * (1 - s * (1 - (var_h - var_i)));

            float var_r;
            float var_g;
            float var_b;
            if (var_i == 0) {
                var_r = b;
                var_g = var_3;
                var_b = var_1;
            } else if (var_i == 1) {
                var_r = var_2;
                var_g = b;
                var_b = var_1;
            } else if (var_i == 2) {
                var_r = var_1;
                var_g = b;
                var_b = var_3;
            } else if (var_i == 3) {
                var_r = var_1;
                var_g = var_2;
                var_b = b;
            } else if (var_i == 4) {
                var_r = var_3;
                var_g = var_1;
                var_b = b;
            } else {
                var_r = b;
                var_g = var_1;
                var_b = var_2;
            }

            R = var_r * 255; // RGB results from 0 to 255
            G = var_g * 255;
            B = var_b * 255;
        }
        return new Color(Math.round(R),Math.round(G),Math.round(B));
    }
}
