package com.mommoo.flat.frame.titlebar.navigation.button;

import java.awt.*;

/**
 * Created by mommoo on 2017-07-10.
 */
public class ExitNavigationButton extends NavigationButton {

    ExitNavigationButton() {
        super();
    }

    @Override
    protected void paintShape(int STANDARD_RECT_SIZE, Graphics2D GRAPHICS_2D) {
        final int LEFT_TOP_X = (getWidth() - STANDARD_RECT_SIZE)/2;
        final int LEFT_TOP_Y = (getHeight() - STANDARD_RECT_SIZE)/2;
        GRAPHICS_2D.drawLine(LEFT_TOP_X,LEFT_TOP_Y,LEFT_TOP_X+STANDARD_RECT_SIZE,LEFT_TOP_Y+STANDARD_RECT_SIZE);

        final int RIGHT_TOP_X = LEFT_TOP_X + STANDARD_RECT_SIZE;
        final int RIGHT_TOP_Y = LEFT_TOP_Y;
        GRAPHICS_2D.drawLine(RIGHT_TOP_X,RIGHT_TOP_Y, LEFT_TOP_X, LEFT_TOP_Y+STANDARD_RECT_SIZE);
    }
}
