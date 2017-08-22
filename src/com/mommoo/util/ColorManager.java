package com.mommoo.util;

import java.awt.*;

/**
 * Created by mommoo on 2017-07-13.
 */
public class ColorManager {
    private ColorManager(){}

    public static Color getTransParentColor(){
        return new Color(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public static Color getFlatComponentDefaultColor(){
        return Color.decode("#eeeeee");
    }

    public static Color getColorAccent(){
        return Color.decode("#E91E63");
    }
}
