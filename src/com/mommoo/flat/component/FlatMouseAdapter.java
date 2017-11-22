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

    protected final boolean isMouseClicked() {
        return isMouseClicked;
    }

    protected final boolean isMouseEntered() {
        return isMouseEntered;
    }

    protected final boolean isMouseMoved() {
        return isMouseMoved;
    }

    protected final boolean isMousePressed() {
        return isMousePressed;
    }

    protected final boolean isMouseDragging() {
        return isMouseDragging;
    }

    protected final MouseEvent getMouseEvent() {
        return mouseEvent;
    }
}
