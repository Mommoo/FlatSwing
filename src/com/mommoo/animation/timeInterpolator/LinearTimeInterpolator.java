package com.mommoo.animation.timeInterpolator;

public class LinearTimeInterpolator extends TimeInterpolator{
    @Override
    public double getFactor(double percent) {
        return percent;
    }
}
