package com.mommoo.flat.frame;

import com.sun.prism.j2d.paint.MultipleGradientPaint;
import com.sun.prism.j2d.paint.RadialGradientPaint;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

class ShadowPaintHandler {
    private static final float[] DIST = { 0.0f, 1.0f };
    private static final Color[] COLORS = { new Color(0f,0f,0f,0.8f), new Color(0f,0f,0f,0f) };
//    private static final Color[] COLORS = { new Color(152, 152, 152), new Color(100, 100, 100,0) };
    private int width, height, shadowDip;
    private Drawer TOP_LEFT_DRAWER;
    private Drawer TOP_RIGHT_DRAWER;
    private Drawer BOTTOM_LEFT_DRAWER;
    private Drawer BOTTOM_RIGHT_DRAWER;
    private Drawer TOP_DRAWER;
    private Drawer LEFT_DRAWER;
    private Drawer RIGHT_DRAWER;
    private Drawer BOTTOM_DRAWER;

    void changePaint(int width, int height, int shadowDip){

        if (this.width == width && this.height == height && this.shadowDip == shadowDip){
            return ;
        }

        setData(width, height, shadowDip);
        TOP_LEFT_DRAWER = createTopLeftDrawer();
        TOP_RIGHT_DRAWER = createTopRightDrawer();
        BOTTOM_LEFT_DRAWER = createBottomLeftDrawer();
        BOTTOM_RIGHT_DRAWER = createBottomRightDrawer();
        TOP_DRAWER = createTopDrawer();
        LEFT_DRAWER = createLeftDrawer();
        RIGHT_DRAWER = createRightDrawer();
        BOTTOM_DRAWER = createBottomDrawer();
    }

    private void setData(int width, int height, int shadowDip){
        this.width = width;
        this.height = height;
        this.shadowDip = shadowDip;
    }

    private Drawer createTopLeftDrawer(){
        Shape topLeftCornerShape = new Arc2D.Float(0,0, shadowDip * 2 , shadowDip * 2 ,90, 90, Arc2D.PIE);
        Paint topLeftPaint = new RadialGradientPaint(shadowDip ,shadowDip ,shadowDip , DIST, COLORS, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        return new Drawer(topLeftCornerShape, topLeftPaint);
    }

    private Drawer createTopRightDrawer(){
        Shape topRightCornerShape = new Arc2D.Float(width - (shadowDip * 2),0, shadowDip * 2 , shadowDip * 2 ,90, -90, Arc2D.PIE);
        Paint topRightPaint = new RadialGradientPaint(width - shadowDip, shadowDip, shadowDip, DIST, COLORS, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        return new Drawer(topRightCornerShape, topRightPaint);
    }

    private Drawer createBottomLeftDrawer(){
        Shape bottomLeftShape = new Arc2D.Float(0, height - (shadowDip * 2) , shadowDip * 2 , shadowDip * 2 , 180, 90, Arc2D.PIE);
        Paint bottomLeftPaint = new RadialGradientPaint(shadowDip ,height- shadowDip, shadowDip  , DIST, COLORS, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        return new Drawer(bottomLeftShape, bottomLeftPaint);
    }

    private Drawer createBottomRightDrawer(){
        Shape bottomRightShape = new Arc2D.Float(width - (shadowDip * 2) , height - (shadowDip * 2), shadowDip * 2, shadowDip * 2, 270, 90, Arc2D.PIE);
        Paint bottomRightPaint = new RadialGradientPaint(width - shadowDip, height - shadowDip, shadowDip, DIST, COLORS, MultipleGradientPaint.CycleMethod.NO_CYCLE);;
        return new Drawer(bottomRightShape, bottomRightPaint);
    }

    private Drawer createTopDrawer(){
        Shape topShape = new Rectangle2D.Float(shadowDip, 0, width - (shadowDip * 2), shadowDip/2);
        Paint topPaint = new GradientPaint(shadowDip, shadowDip/2, COLORS[0], shadowDip, 0, COLORS[1]);
        return new Drawer(topShape, topPaint);
    }

    private Drawer createLeftDrawer(){
        Shape leftShape = new Rectangle2D.Float(0, shadowDip, shadowDip, height - ( shadowDip * 2));
        Paint leftPaint = new GradientPaint(shadowDip, shadowDip, COLORS[0], 0, shadowDip, COLORS[1]);
        return new Drawer(leftShape, leftPaint);
    }

    private Drawer createRightDrawer(){
        Shape rightShape = new Rectangle2D.Float(width - shadowDip, shadowDip, shadowDip, height - ( shadowDip * 2));
        Paint rightPaint = new GradientPaint(width - shadowDip, shadowDip, COLORS[0], width, shadowDip, COLORS[1]);
        return new Drawer(rightShape, rightPaint);
    }

    private Drawer createBottomDrawer(){
        Shape bottomShape = new Rectangle2D.Float(shadowDip, height - shadowDip, width - (shadowDip * 2), shadowDip);
        Paint bottomPaint = new GradientPaint(shadowDip, height - shadowDip, COLORS[0], shadowDip, height, COLORS[1]);
        return new Drawer(bottomShape, bottomPaint);
    }

    public Drawer getTopLeftDrawer(){
        return TOP_LEFT_DRAWER;
    }

    public Drawer getTopRightDrawer(){
        return TOP_RIGHT_DRAWER;
    }

    public Drawer getBottomLeftDrawer(){
        return BOTTOM_LEFT_DRAWER;
    }

    public Drawer getBottomRightDrawer(){
        return BOTTOM_RIGHT_DRAWER;
    }

    public Drawer getTopDrawer(){
        return TOP_DRAWER;
    }

    public Drawer getLeftDrawer(){
        return LEFT_DRAWER;
    }

    public Drawer getRightDrawer(){
        return RIGHT_DRAWER;
    }

    public Drawer getBottomDrawer(){
        return BOTTOM_DRAWER;
    }

    public class Drawer{
        private final Shape shape;
        private final Paint paint;

        public Drawer(Shape shape, Paint paint){
            this.shape = shape;
            this.paint = paint;
        }

        public Shape getShape() {
            return shape;
        }

        public Paint getPaint() {
            return paint;
        }
    }
}
