package com.mommoo.flat.frame.titlebar.navigation.button;

import java.awt.*;

/**
 * Created by mommoo on 2017-07-10.
 */
public class MiniMumNavigationButton extends NavigationButton {

    MiniMumNavigationButton() {
        super();
    }

    @Override
    protected void paintShape(int STANDARD_RECT_SIZE, Graphics2D GRAPHICS_2D) {
        final int START_X = (getWidth() - STANDARD_RECT_SIZE)/2;
        final int START_Y = ((getHeight() - STANDARD_RECT_SIZE)/2) + STANDARD_RECT_SIZE;
        GRAPHICS_2D.drawLine(START_X,START_Y,START_X+STANDARD_RECT_SIZE,START_Y);
    }
}
