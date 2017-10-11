package com.mommoo.animation.timeInterpolator;

public class AnticipateInterpolator extends TimeInterpolator{
    private static final float TENSION = 2.0f;

    @Override
    public double getFactor(double percent) {
        return percent * percent * ((TENSION + 1) * percent - TENSION);
    }
}
