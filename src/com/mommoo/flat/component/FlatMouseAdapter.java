package com.mommoo.flat.component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class FlatMouseAdapter extends MouseAdapter{
    private boolean isMouseClicked;
    private boolean isMousePressed;
    private boolean isMouseEntered;
    private boolean isMouseDragging;
    private boolean isMouseMoved;
    private MouseEvent mouseEvent;

    @Override
    public void mouseClicked(MouseEvent e) {
        this.mouseEvent = e;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.mouseEvent = e;
        isMouseClicked = false;
        isMousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.mouseEvent = e;
        isMouseClicked = isMousePressed && isMouseEntered && e.getComponent().isEnabled();
        isMousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.mouseEvent = e;
        isMouseEntered = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.mouseEvent = e;
        isMouseEntered = false;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.mouseEvent = e;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.mouseEvent = e;
        isMouseDragging = true;
        isMouseMoved = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseEvent = e;
        isMouseDragging = false;
        isMouseMoved = true;
    }

    public boolean isMouseClicked() {
        return isMouseClicked;
    }

    public boolean isMouseEntered() {
        return isMouseEntered;
    }

    public boolean isMouseMoved() {
        return isMouseMoved;
    }

    public boolean isMousePressed() {
        return isMousePressed;
    }

    public boolean isMouseDragging() {
        return isMouseDragging;
    }

    public MouseEvent getMouseEvent() {
        return mouseEvent;
    }
}
