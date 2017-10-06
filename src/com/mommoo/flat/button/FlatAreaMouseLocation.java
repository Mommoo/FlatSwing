package com.mommoo.flat.button;

import java.awt.*;

class FlatAreaMouseLocation {

    private FlatAreaMouseLocation(){}

    static Point getLocation(Component component){
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Point compLocation  = component.getLocationOnScreen();
        Dimension compDimension = component.getSize();

        int minX = 0;
        int maxX = compDimension.width;
        int minY = 0;
        int maxY = compDimension.height;

        int relativeX = mouseLocation.x - compLocation.x;
        int relativeY = mouseLocation.y - compLocation.y;

        return new Point(Math.max(minX, (Math.min(maxX, relativeX))), Math.max(minY, (Math.min(maxY, relativeY))));
    }
}
