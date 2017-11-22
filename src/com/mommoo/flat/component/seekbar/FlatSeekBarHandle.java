package com.mommoo.flat.component.seekbar;

import com.mommoo.util.ColorManager;

import java.awt.*;
import java.awt.geom.Ellipse2D;

class FlatSeekBarHandle extends Component {
    private Ellipse2D.Double HANDLE_SHAPE = new Ellipse2D.Double();
    private Color seekBarHandleColor = ColorManager.getColorAccent();
    private boolean isGrab;

    @Override
    public void paint(Graphics g) {
        double w = getWidth();
        double h = getHeight();

        Graphics2D graphics2D = (Graphics2D)g;

        if (isGrab){
            drawOuterShape(graphics2D, 0, 0, w);
        }

        drawInnerShape(graphics2D, w/4d, h/4d, w/2d);
    }

    private void drawOuterShape(Graphics2D graphics2D, double x, double y, double size){
        graphics2D.setColor(getSeekBarHandleAlphaColor());
        HANDLE_SHAPE.setFrame(x, y, size, size);
        graphics2D.fill(HANDLE_SHAPE);
    }

    private void drawInnerShape(Graphics2D graphics2D, double x, double y, double size){
        graphics2D.setColor(seekBarHandleColor);
        HANDLE_SHAPE.setFrame(x, y, size, size);
        graphics2D.fill(HANDLE_SHAPE);
    }

    private Color getSeekBarHandleAlphaColor(){
        Color lighterColor = seekBarHandleColor.brighter();
        int r = lighterColor.getRed();
        int g = lighterColor.getGreen();
        int b = lighterColor.getBlue();
        return new Color(r,g,b,160);
    }

    void setSeekBarHandleColor(Color seekBarHandleColor){
        this.seekBarHandleColor = seekBarHandleColor;
    }

    void grab(){
        isGrab = true;
    }

    void unGrab(){
        isGrab = false;
    }
}
