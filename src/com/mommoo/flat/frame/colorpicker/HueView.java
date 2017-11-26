package com.mommoo.flat.frame.colorpicker;

import com.mommoo.flat.component.FlatMouseAdapter;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.utils.FlatColor;
import com.mommoo.util.ComputableDimension;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.function.IntConsumer;

public class HueView extends JPanel {
    private static final ScreenManager SCREEN = ScreenManager.getInstance();
    private static final int ARROW_SIZE = SCREEN.dip2px(7);
    private double mouseY = ARROW_SIZE/2;
    private int previousHue = -1;

    private IntConsumer onHSBChangeListener = hue -> {};
    private boolean once;

    private int hue;
    private boolean isAutoMouseY;

    public HueView(){
        setOpaque(false);
        setLayout(new LinearLayout(SCREEN.dip2px(3)));
        add(new HueViewArrowView(false),  new LinearConstraints(LinearSpace.MATCH_PARENT));
        add(new HueRectView(),               new LinearConstraints(1, LinearSpace.MATCH_PARENT));
        add(new HueViewArrowView(true), new LinearConstraints(LinearSpace.MATCH_PARENT));
    }

    public static void main(String[] args){
        FlatFrame frame = new FlatFrame();
        frame.setSize(500,500);
        frame.setLocationOnScreenCenter();
        frame.getContainer().setLayout(new FlowLayout());
        HueView hueView = new HueView();
        hueView.setHue(100);
        frame.getContainer().add(hueView).setPreferredSize(new Dimension(100,400));
        frame.show();
    }

    public void setOnHSBChangeListener(IntConsumer onHSBChangeListener){
        this.onHSBChangeListener = onHSBChangeListener;
    }

    public void setHue(int hue){
        this.hue = hue;
        this.isAutoMouseY = true;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (isAutoMouseY) {
            isAutoMouseY = false;
            mouseY  = (((360 - hue) * (getHeight() - ARROW_SIZE))/360) + ARROW_SIZE/2;
        }
        super.paint(g);
        int hue = (int)(360 - 360 * (((mouseY - (ARROW_SIZE/2)) / (getHeight() - ARROW_SIZE))));

        if (hue >= 360 ) hue = 0;
        hue = Math.max(0, hue);
        if (previousHue != hue){
            if (!once){
                once = true;
            }else {
                onHSBChangeListener.accept(hue);
            }
        }
        previousHue = hue;
    }

    private class HueEventView extends JPanel{
        private HueEventView(){
            setOpaque(false);
            HueViewMouseEventListener hueViewMouseEventListener = new HueViewMouseEventListener();
            addMouseListener(hueViewMouseEventListener);
            addMouseMotionListener(hueViewMouseEventListener);
        }
    }

    private class HueViewArrowView extends HueEventView{
        private final boolean left;

        private HueViewArrowView(boolean left){
            this.left = left;
            setPreferredSize(new ComputableDimension(ARROW_SIZE));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            float size = Math.min(getWidth(), getHeight());
            Shape shape = createGuideShape(size, left ? -1 : 1);
            Graphics2D graphics2D = ((Graphics2D)g);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.translate(0, mouseY - size/2);
            graphics2D.fill(shape);
        }

        /**
         * @param RECT_SIZE : Standard of guide shape size
         * @param direction : if direction +1 : right direction and -1 : left direction
         */
        private Path2D.Float createGuideShape(final float RECT_SIZE,int direction){
            final Path2D.Float PATH = new Path2D.Float();
            final float TRIANGLE_HEIGHT = (float)Math.sqrt(3)*RECT_SIZE/2;
            float startX = direction == -1 ? RECT_SIZE : 0;
            PATH.moveTo(startX,0);
            PATH.lineTo(startX,0 + RECT_SIZE);
            PATH.lineTo(startX + direction*(RECT_SIZE - TRIANGLE_HEIGHT), RECT_SIZE);
            PATH.lineTo(startX + direction*(RECT_SIZE),RECT_SIZE/2);
            PATH.lineTo(startX + direction*(RECT_SIZE - TRIANGLE_HEIGHT), 0);
            PATH.closePath();
            return PATH;
        }
    }

    private class HueRectView extends HueEventView{
        private BufferedImage cacheImage;
        private boolean isHorizontal;

        private HueRectView(){
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            if (cacheImage == null || cacheImage.getWidth() != getWidth() || cacheImage.getHeight() != getHeight()){
                cacheImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                final int size = (isHorizontal ? getWidth()  : getHeight()) - ARROW_SIZE;

                final Graphics2D graphics2D = cacheImage.createGraphics();
                final Rectangle2D.Float RECT = new Rectangle2D.Float();
                final float colorRectHeight = getHeight()/360.0f;

                graphics2D.setStroke(new BasicStroke(colorRectHeight));

                for (int i = 0 ; i <= size ; i++){
                    int hue = 360 * i / size;

                    graphics2D.setColor(FlatColor.convertHueToRGB(hue));
                    if (isHorizontal){
                        RECT.setRect(size - i,0,1,getHeight());
                    } else {
                        RECT.setRect(0,size - i,getWidth(),1);
                    }

                    graphics2D.fill(RECT);
                }
            }

            g.drawImage(cacheImage,0 ,ARROW_SIZE/2, null);
            g.setColor(Color.BLACK);
            g.drawRect(0,ARROW_SIZE/2, cacheImage.getWidth() - 1, getHeight() - ARROW_SIZE);
        }
    }

    private class HueViewMouseEventListener extends FlatMouseAdapter{

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            mouseY = Math.min(getHeight() - ARROW_SIZE/2, Math.max(ARROW_SIZE/2, e.getY()));
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if(isMousePressed()){
                mouseY = Math.min(getHeight() - ARROW_SIZE/2, Math.max(ARROW_SIZE/2, e.getY()));
                repaint();
            }
        }
    }

}
