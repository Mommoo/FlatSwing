package com.mommoo.animation.timeInterpolator;

public abstract class TimeInterpolator {
    private double targetDelta;

    public abstract double getFactor(double percent);

    public double getTargetDelta() {
        return targetDelta;
    }

    public void setTargetDelta(double targetDelta) {
        this.targetDelta = targetDelta;
    }

    public double getValue(double percent){
        return targetDelta * getFactor(percent);
    }
}
