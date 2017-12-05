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

    public ComputableDimension(int size){
        super(size, size);
    }

    public static void main(String[] args){
        double weightW = 340d/300d;
        double weightH = 340d/300d;

        int one = (int)(weightW * 70);
        int two = (int)(weightW * 130);
        int three = (int) (weightW * 100);

        System.out.println(one+","+two+","+three);
        System.out.println(one+two+three);
        System.out.println(weightW * 300d);

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

    public void setSize(int size){
        super.setSize(size, size);
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

    public ComputableDimension setMinimumSize(int size){
        return setMinimumSize(size, size);
    }

    public ComputableDimension setMaximumSize(int width, int height){
        maximumWidth  = width;
        maximumHeight = height;
        return getValidDimension();
    }

    public ComputableDimension setMaximumSize(Dimension dimen){
        return setMaximumSize(dimen.width, dimen.height);
    }

    public ComputableDimension setMaximumSize(int size){
        return setMaximumSize(size, size);
    }

    public ComputableDimension setDimension(double width, double height) {
        super.setSize(width, height);
        getValidDimension();
        return this;
    }

    public ComputableDimension setDimension(Dimension d) {
        super.setSize(d);
        getValidDimension();
        return this;
    }

    public ComputableDimension setDimension(int width, int height) {
        super.setSize(width, height);
        getValidDimension();
        return this;
    }

    public ComputableDimension setDimension(Dimension2D d) {
        super.setSize(d);
        getValidDimension();
        return this;
    }

    public ComputableDimension setDimension(int size){
        super.setSize(size, size);
        getValidDimension();
        return this;
    }

    public ComputableDimension addDimension(int width, int height){
        this.width  += width;
        this.height += height;
        return getValidDimension();
    }

    public ComputableDimension addDimension(Dimension dimen){
        return addDimension(dimen.width, dimen.height);
    }

    public ComputableDimension addDimension(int size){
        return addDimension(size, size);
    }

    public ComputableDimension subDimension(int width, int height){
        this.width  -= width;
        this.height -= height;
        return getValidDimension();
    }

    public ComputableDimension subDimension(Dimension dimen){
        return subDimension(dimen.width, dimen.height);
    }

    public ComputableDimension subDimension(int size){
        return subDimension(size, size);
    }

    public ComputableDimension mulDimension(int width, int height){
        this.width  *= width;
        this.height *= height;
        return getValidDimension();
    }

    public ComputableDimension mulDimension(Dimension dimen){
        return mulDimension(dimen.width, dimen.height);
    }

    public ComputableDimension mulDimension(int size){
        return mulDimension(size, size);
    }

    public ComputableDimension divDimension(int width, int height){
        this.width  /= width;
        this.height /= height;
        return getValidDimension();
    }

//    public ComputableDimension divDimesion

    public ComputableDimension divDimension(Dimension dimen){
        return divDimension(dimen.width, dimen.height);
    }

    public ComputableDimension divDimension(int size){
        return divDimension(size, size);
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
