package com.mommoo.flat.frame.colorpicker;

import com.mommoo.flat.component.FlatMouseAdapter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import static com.mommoo.flat.utils.FlatColor.convertHSBtoRGB;

public class HSB_View extends JPanel {
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    private static final BasicStroke STROKE = new BasicStroke(1.5f);
    private BufferedImage cacheImage;

    private HSB hsb = new HSB(0, 0, 0);

    private boolean needToDraw;
    private boolean isAutoPoint;
    private boolean once;

    private float mouseX, mouseY;

    private Consumer<HSB> onHSBChangeListener = hsb -> { };

    public HSB_View() {
        setBorder(new LineBorder(Color.BLACK, 1));
        setOpaque(false);
        setCursor(HAND_CURSOR);
        MouseAdapter mouseEventHandler = new HSB_RectMouseEvent();
        addMouseListener(mouseEventHandler);
        addMouseMotionListener(mouseEventHandler);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        final Graphics2D G_2D = (Graphics2D) g;
        final int SIZE = Math.min(getWidth(), getHeight());
        final int RADIUS = Math.max(2, SIZE / 30);

        boolean isNeedToReDraw = needToDraw || cacheImage == null || cacheImage.getWidth() != SIZE;

        drawHsbRect(isNeedToReDraw, G_2D, SIZE);
        needToDraw = false;

        G_2D.setStroke(STROKE);

        float mouseX = Math.min(SIZE, Math.max(0, this.mouseX));
        float mouseY = Math.min(SIZE, Math.max(0, this.mouseY));

        if (isAutoPoint){
            mouseX = (hsb.getSaturation() *  SIZE ) / 100f;
            mouseY = ((100 - hsb.getBrightness()) * SIZE)/100f;
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            isAutoPoint = false;
        }

        /* Decide mouse circle pointer color */
        G_2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        G_2D.setColor(hsb.getBrightness() <= 40 ? Color.WHITE : Color.BLACK);

        /* Draw mouse area and Draw mouse point using circle*/
        G_2D.drawOval(Math.round(mouseX - RADIUS), Math.round(mouseY - RADIUS), RADIUS * 2, RADIUS * 2);


        /* Data changed by user manipulation are setting*/
        int saturation = Math.min((int) (mouseX * 100 / SIZE), 100);
        int brightness = Math.max(100 - (int) (mouseY * 100 / SIZE), 0);
        hsb = new HSB(hsb.getHue(), saturation, brightness);

        if (!once){
            once = true;
        } else {
            onHSBChangeListener.accept(hsb);
        }
    }

    public void setOnHSBChangeListener(Consumer<HSB> onHSBChangeListener) {
        this.onHSBChangeListener = onHSBChangeListener;
    }

    private void drawHsbRect(boolean reDraw, Graphics2D graphics2D, int size) {

        if (reDraw) {
            cacheImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = (Graphics2D) cacheImage.getGraphics();

            Rectangle2D.Double RECT = new Rectangle.Double();
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    float s = col * 100f / size;
                    float b = 100f - (row * 100f / size);
                    graphics.setColor(convertHSBtoRGB(hsb.getHue(), s, b));
                    RECT.setRect(col, row, 1, 1);
                    graphics.fill(RECT);
                }
            }

            graphics.dispose();
        }

        graphics2D.drawImage(cacheImage, 0, 0, null);
    }

    public void setHue(int hue) {
        if (hsb.getHue() != hue){
            this.hsb = this.hsb.changeHue(hue);
            needToDraw = true;
            repaint();
        }

    }

    public void setHSB(HSB hsb){
        this.hsb = hsb;
        needToDraw = true;
        isAutoPoint = true;
        repaint();
    }


    @Override
    public String toString() {
        return "HSB_View";
    }

    private class HSB_RectMouseEvent extends FlatMouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if (isMousePressed()) {
                mouseX = e.getX();
                mouseY = e.getY();
                repaint();
            }
        }
    }
}
