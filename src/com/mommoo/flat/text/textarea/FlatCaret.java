package com.mommoo.flat.text.textarea;

import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import java.awt.*;

class FlatCaret extends DefaultCaret{
    private static final int BLINK_RATE = 500;

    private Component cacheComp;
    private FontMetrics cacheMetrics;

    private int cursorWidth = 2;

    FlatCaret(){
        setBlinkRate(BLINK_RATE);
    }

    int getCursorWidth(){
        return cursorWidth;
    }

    void setCursorWidth(int cursorWidth){
        this.cursorWidth = cursorWidth;
    }

    @Override
    public void paint(Graphics g) {
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
        g.fillRect(r.x -( cursorWidth - 1), r.y, cursorWidth, r.height);
    }
}
