package com.mommoo.flat.frame;

import com.sun.prism.j2d.paint.MultipleGradientPaint;
import com.sun.prism.j2d.paint.RadialGradientPaint;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;


class ShadowPaintHandler {
    private static final float[] DIST = { 0.0f, 1.0f };
    private static final Color[] COLORS = { new Color(75, 75, 75, 90), new Color(80, 80, 80, 10) };
    private final Arc2D.Float SHADOW_CORNER_SHAPE = new Arc2D.Float();
    private final Rectangle2D.Float SHADOW_SHAPE = new Rectangle2D.Float();
    private int width, height, shadowDip;
    private RadialGradientPaint topLeftShadowGradient;
    private RadialGradientPaint topRightShadowGradient;
    private RadialGradientPaint bottomLeftShadowGradient;
    private RadialGradientPaint bottomRightShadowGradient;
    private GradientPaint topShadowGradient;
    private GradientPaint leftShadowGradient;
    private GradientPaint rightShadowGradient;
    private GradientPaint bottomShadowGradient;

    void changePaint(int width, int height, int shadowDip){

        if (this.width == width && this.height == height && this.shadowDip == shadowDip){
            return ;
        }

        setData(width, height, shadowDip);
        topLeftShadowGradient = createTopLeftShadowGradient();
        topRightShadowGradient = createTopRightShadowGradient();
        bottomLeftShadowGradient = createBottomLeftShadowGradient();
        bottomRightShadowGradient = createBottomRightShadowGradient();
        topShadowGradient = createTopShadowGradient();
        leftShadowGradient = createLeftShadowGradient();
        rightShadowGradient = createRightShadowGradient();
        bottomShadowGradient = createBottomShadowGradient();
    }

    private void setData(int width, int height, int shadowDip){
        this.width = width;
        this.height = height;
        this.shadowDip = shadowDip;
    }


    private RadialGradientPaint createTopLeftShadowGradient(){
        return new RadialGradientPaint(shadowDip ,shadowDip ,shadowDip , DIST, COLORS, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    }

    private RadialGradientPaint createTopRightShadowGradient(){
        return new RadialGradientPaint(width - shadowDip, shadowDip, shadowDip, DIST, COLORS, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    }

    private RadialGradientPaint createBottomLeftShadowGradient(){
        return new RadialGradientPaint(shadowDip ,height- shadowDip ,shadowDip , DIST, COLORS, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    }

    private RadialGradientPaint createBottomRightShadowGradient(){
        return new RadialGradientPaint(width - shadowDip, height - shadowDip, shadowDip, DIST, COLORS, MultipleGradientPaint.CycleMethod.NO_CYCLE);
    }

    private GradientPaint createTopShadowGradient(){
        return new GradientPaint(shadowDip, shadowDip, COLORS[0], shadowDip, 0, COLORS[1]);
    }

    private GradientPaint createLeftShadowGradient(){
        return new GradientPaint(shadowDip, shadowDip, COLORS[0], 0, shadowDip, COLORS[1]);
    }

    private GradientPaint createRightShadowGradient(){
        return new GradientPaint(width - shadowDip, shadowDip, COLORS[0], width, shadowDip, COLORS[1]);
    }

    private GradientPaint createBottomShadowGradient(){
        return new GradientPaint(shadowDip, height - shadowDip, COLORS[0], shadowDip, height, COLORS[1]);
    }

    RadialGradientPaint getTopLeftShadowGradient(){
        return topLeftShadowGradient;
    }

    Shape getTopLeftShadowShape(){
        SHADOW_CORNER_SHAPE.setArc(0,0, shadowDip * 2 , shadowDip * 2 ,90, 90, Arc2D.PIE);
        return SHADOW_CORNER_SHAPE;
    }

    RadialGradientPaint getTopRightShadowGradient(){
        return topRightShadowGradient;
    }

    Shape getTopRightShadowShape(){
        SHADOW_CORNER_SHAPE.setArc(width - (shadowDip * 2),0, shadowDip * 2 , shadowDip * 2 ,90, -90, Arc2D.PIE);
        return SHADOW_CORNER_SHAPE;
    }

    RadialGradientPaint getBottomLeftShadowGradient() {
        return bottomLeftShadowGradient;
    }

    Shape getBottomLeftShadowShape(){
        SHADOW_CORNER_SHAPE.setArc(0, height - (shadowDip * 2) , shadowDip * 2 , shadowDip * 2 , 180, 90, Arc2D.PIE);
        return SHADOW_CORNER_SHAPE;
    }

    RadialGradientPaint getBottomRightShadowGradient() {
        return bottomRightShadowGradient;
    }

    Shape getBottomRightShadowShape(){
        SHADOW_CORNER_SHAPE.setArc(width - (shadowDip * 2) , height - (shadowDip * 2), shadowDip * 2, shadowDip * 2, 270, 90, Arc2D.PIE);
        return SHADOW_CORNER_SHAPE;
    }

    GradientPaint getTopShadowGradient(){
        return topShadowGradient;
    }

    Shape getTopShadowShape(){
        SHADOW_SHAPE.setFrame(shadowDip, 0, width - (shadowDip * 2), shadowDip);
        return SHADOW_SHAPE;
    }

    GradientPaint getLeftShadowGradient() {
        return leftShadowGradient;
    }

    Shape getLeftShadowShape(){
        SHADOW_SHAPE.setFrame(0, shadowDip, shadowDip, height - ( shadowDip * 2));
        return SHADOW_SHAPE;
    }

    GradientPaint getRightShadowGradient() {
        return rightShadowGradient;
    }

    Shape getRightShadowShape(){
        SHADOW_SHAPE.setFrame(width - shadowDip, shadowDip, shadowDip, height - ( shadowDip * 2));
        return SHADOW_SHAPE;
    }

    GradientPaint getBottomShadowGradient() {
        return bottomShadowGradient;
    }

    Shape getBottomShadowShape(){
        SHADOW_SHAPE.setFrame(shadowDip, height - shadowDip, width - (shadowDip * 2), shadowDip);
        return SHADOW_SHAPE;
    }
}
