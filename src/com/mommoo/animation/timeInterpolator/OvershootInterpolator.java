package com.mommoo.animation.timeInterpolator;

public class OvershootInterpolator extends TimeInterpolator{
    private static final float TENSION = 2.0f;
    @Override
    public double getFactor(double percent) {
        percent -= 1.0f;
        return percent * percent * ((TENSION + 1) * percent + TENSION) + 1.0f;
    }
}
