package com.mommoo.flat.list;

import java.awt.*;
import java.awt.event.MouseEvent;

class MouseEventFactory{
    static MouseEvent createMouseEvent(Component source, int id, AWTEvent event){
        MouseEvent mouseEvent = (MouseEvent) event;
        Point point = MouseInfo.getPointerInfo().getLocation();
        return new MouseEvent(source,
                id,
                System.currentTimeMillis(),
                mouseEvent.getModifiers(),
                point.x,
                point.y,
                mouseEvent.getClickCount(),
                mouseEvent.isPopupTrigger(),
                mouseEvent.getButton());
    }
}
