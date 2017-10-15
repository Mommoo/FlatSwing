package com.mommoo.flat.text.textarea;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.MouseEvent;

class FlatCaret extends DefaultCaret{
    private static final int BLINK_RATE = 500;

    private Component cacheComp;
    private FontMetrics cacheMetrics;

    private int cursorWidth = 2;

    FlatCaret(){
        setBlinkRate(BLINK_RATE);
        setUpdatePolicy(ALWAYS_UPDATE);
    }

    int getCursorWidth(){
        return cursorWidth;
    }

    void setCursorWidth(int cursorWidth){
        this.cursorWidth = cursorWidth;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D)g;
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
        super.paint(g);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        JTextComponent comp = getComponent();

        if (comp == null || !isVisible()) return;

        if (cacheComp != comp){
            cacheComp = comp;
            cacheMetrics = cacheComp.getFontMetrics(cacheComp.getFont());
        }

        Rectangle r = null;
        try {
            r = comp.modelToView(getDot());
        } catch (Exception e) {
            return;
        }
        // set cursorHeight;
        r.height = cacheMetrics.getHeight();
        g.setColor(comp.getCaretColor());
        g.fillRect(r.x , r.y, cursorWidth, r.height);
    }
}
