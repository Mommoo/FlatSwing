package com.mommoo.flat.select;

import com.mommoo.flat.utils.FlatColor;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoverColorChangeListener extends MouseAdapter {
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    private Color originalBackgroundColor;
    private Color focusGainColor;
    private Color pressedColor;

    private void initColor(Component component){
        originalBackgroundColor = component.getBackground();
        focusGainColor = FlatColor.darker(originalBackgroundColor, 0.9);
        pressedColor = FlatColor.darker(focusGainColor, 0.9);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        e.getComponent().setBackground(pressedColor);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        if (originalBackgroundColor == null) {
            initColor(e.getComponent());
        }
        e.getComponent().setBackground(focusGainColor);
        e.getComponent().setCursor(HAND_CURSOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        e.getComponent().setBackground(originalBackgroundColor);
        e.getComponent().setCursor(DEFAULT_CURSOR);
    }
}
