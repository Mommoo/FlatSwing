package com.mommoo.util;

import java.awt.*;

/**
 * Created by mommoo on 2017-07-13.
 */
public class ColorManager {
    private ColorManager(){}

    private static Color DEFAULT_COLOR = Color.decode("#999999");
    private static Color COLOR_ACCENT = Color.decode("#E91E63");

    public static Color getTransParentColor(){
        return new Color(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public static Color getFlatComponentDefaultColor(){
        return DEFAULT_COLOR;
    }

    public static Color getColorAccent(){
        return COLOR_ACCENT;
    }
}
