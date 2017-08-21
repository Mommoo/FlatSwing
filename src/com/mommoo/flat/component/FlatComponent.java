package com.mommoo.flat.component;

import javax.swing.*;
import java.awt.*;

/**
 *  This class represent view rect customized com.mommoo.flat design.
 *  To background color change animation, override setBackground method.
 */

public abstract class FlatComponent extends JButton {
    private Color backgroundColor;

    public FlatComponent() {
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
    }

    @Override
    public void setBackground(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(this.backgroundColor);
        g.fillRect(0,0, getWidth(), getHeight());
        super.paint(g);
    }
}
