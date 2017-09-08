package com.mommoo.util;
import java.awt.*;

public class ComputableDimension extends Dimension{
    public ComputableDimension() {
        super();
    }

    public ComputableDimension(Dimension d) {
        super(d);
    }

    public ComputableDimension(int width, int height) {
        super(width, height);
    }

    public ComputableDimension addDimension(Dimension dimen){
        this.width  += dimen.width;
        this.height += dimen.height;
        return this;
    }

    public ComputableDimension addDimension(int width, int height){
        this.width  += width;
        this.height += height;
        return this;
    }

    public ComputableDimension subDimension(Dimension dimen){
        this.width  -= dimen.width;
        this.height -= dimen.height;
        return this;
    }

    public ComputableDimension subDimension(int width, int height){
        this.width  -= width;
        this.height -= height;
        return this;
    }

    public ComputableDimension mulDimension(Dimension dimen){
        this.width  *= dimen.width;
        this.height *= dimen.height;
        return this;
    }

    public ComputableDimension mulDimension(int width, int height){
        this.width  *= width;
        this.height *= height;
        return this;
    }

    public ComputableDimension divDimension(Dimension dimen){
        this.width  /= dimen.width;
        this.height /= dimen.height;
        return this;
    }

    public ComputableDimension divDimension(int width, int height){
        this.width  /= width;
        this.height /= height;
        return this;
    }

    public ComputableDimension setZeroDimension(){
        this.width  = 0;
        this.height = 0;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Dimension)) return false;

        Dimension targetDimen = (Dimension) obj;

        return this.width == targetDimen.width && this.height == targetDimen.height;
    }
}
