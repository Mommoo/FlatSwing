package com.mommoo.animation.timeInterpolator;

public class AccelerateInterpolator extends TimeInterpolator{
    @Override
    public double getFactor(double percent) {
        return Math.pow(percent, 3);
    }
}
