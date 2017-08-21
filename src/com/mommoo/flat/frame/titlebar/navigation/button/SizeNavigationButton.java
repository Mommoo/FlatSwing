package com.mommoo.flat.frame.titlebar.navigation.button;

import java.awt.*;

/**
 * Created by mommoo on 2017-07-10.
 */
public class SizeNavigationButton extends NavigationButton {

    SizeNavigationButton() {
        super();
    }

    @Override
    protected void paintShape(int STANDARD_RECT_SIZE, Graphics2D GRAPHICS_2D) {
        final int START_X = (getWidth()  - STANDARD_RECT_SIZE)/2;
        final int START_Y = (getHeight() - STANDARD_RECT_SIZE)/2;
        GRAPHICS_2D.drawRect(START_X, START_Y, STANDARD_RECT_SIZE, STANDARD_RECT_SIZE);
    }
}
