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
}
