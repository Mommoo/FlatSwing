package com.mommoo.util;
import java.awt.*;
import java.awt.geom.Dimension2D;

public class ComputableDimension extends Dimension{

    private int minimumWidth  = - Integer.MAX_VALUE;
    private int minimumHeight = - Integer.MAX_VALUE;
    private int maximumWidth  = + Integer.MAX_VALUE;
    private int maximumHeight = + Integer.MAX_VALUE;

    public ComputableDimension() {
        super();
    }

    public ComputableDimension(Dimension d) {
        super(d);
    }

    public ComputableDimension(int width, int height) {
        super(width, height);
    }

    public static void main(String[] args){
        System.out.println(new ComputableDimension(100,100)
                .setMinimumSize(110,110)
                .setMaximumSize(150,150)
                .addDimension(100,100));
    }

    @Override
    public void setSize(double width, double height) {
        super.setSize(width, height);
        getValidDimension();
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        getValidDimension();
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        getValidDimension();
    }

    @Override
    public void setSize(Dimension2D d) {
        super.setSize(d);
        getValidDimension();
    }

    public ComputableDimension setMinimumSize(int width, int height){
        minimumWidth = width;
        minimumHeight = height;
        return getValidDimension();
    }

    public ComputableDimension setMinimumSize(Dimension dimen){
        return setMinimumSize(dimen.width ,dimen.height);
    }

    public ComputableDimension setMaximumSize(int width, int height){
        maximumWidth  = width;
        maximumHeight = height;
        return getValidDimension();
    }

    public ComputableDimension setMaximumSize(Dimension dimen){
        return setMaximumSize(dimen.width, dimen.height);
    }

    public ComputableDimension addDimension(Dimension dimen){
        this.width  += dimen.width;
        this.height += dimen.height;
        return getValidDimension();
    }

    public ComputableDimension addDimension(int width, int height){
        this.width  += width;
        this.height += height;
        return getValidDimension();
    }

    public ComputableDimension subDimension(Dimension dimen){
        this.width  -= dimen.width;
        this.height -= dimen.height;
        return getValidDimension();
    }

    public ComputableDimension subDimension(int width, int height){
        this.width  -= width;
        this.height -= height;
        return getValidDimension();
    }

    public ComputableDimension mulDimension(Dimension dimen){
        this.width  *= dimen.width;
        this.height *= dimen.height;
        return getValidDimension();
    }

    public ComputableDimension mulDimension(int width, int height){
        this.width  *= width;
        this.height *= height;
        return getValidDimension();
    }

    public ComputableDimension divDimension(Dimension dimen){
        this.width  /= dimen.width;
        this.height /= dimen.height;
        return getValidDimension();
    }

    public ComputableDimension divDimension(int width, int height){
        this.width  /= width;
        this.height /= height;
        return getValidDimension();
    }

    public ComputableDimension setZeroDimension(){
        this.width  = 0;
        this.height = 0;
        return getValidDimension();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Dimension)) return false;

        Dimension targetDimen = (Dimension) obj;

        return this.width == targetDimen.width && this.height == targetDimen.height;
    }

    private ComputableDimension getValidDimension(){
        int validWidth = Math.max(minimumWidth, Math.min(width, maximumWidth));
        int validHeight = Math.max(minimumHeight, Math.min(height, maximumHeight));
        super.setSize(validWidth, validHeight);
        return this;
    }
}
