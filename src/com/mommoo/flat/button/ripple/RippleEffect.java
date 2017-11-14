package com.mommoo.flat.button.ripple;

import java.awt.*;

public class RippleEffect implements RippleModel {
    private int     rippleHoldDuration   = 250;
    private int     rippleDuration       = 400;
    private float   rippleColorOpacity   = 0.5f;
    private Color   rippleColor          = new Color(241, 241, 240);
    private boolean onEventLaterEffect   = true;
    private boolean rippleDrawOverBorder = true;

    @Override
    public int getRippleHoldDuration() {
        return rippleHoldDuration;
    }

    public RippleEffect setRippleHoldDuration(int rippleHoldDuration) {
        this.rippleHoldDuration = rippleHoldDuration;
        return this;
    }

    @Override
    public int getRippleDuration() {
        return rippleDuration;
    }

    public RippleEffect setRippleDuration(int rippleDuration) {
        this.rippleDuration = rippleDuration;
        return this;
    }

    public float getRippleColorOpacity() {
        return rippleColorOpacity;
    }

    public RippleEffect setRippleColorOpacity(float rippleColorOpacity) {
        this.rippleColorOpacity = rippleColorOpacity;
        return this;
    }

    public Color getRippleColor() {
        return rippleColor;
    }

    public RippleEffect setRippleColor(Color rippleColor) {
        this.rippleColor = rippleColor;
        return this;
    }

    @Override
    public boolean isOnEventLaterEffect() {
        return onEventLaterEffect;
    }

    public RippleEffect setOnEventLaterEffect(boolean onEventLaterEffect) {
        this.onEventLaterEffect = onEventLaterEffect;
        return this;
    }

    public boolean isRippleDrawOverBorder() {
        return rippleDrawOverBorder;
    }

    public RippleEffect setRippleDrawOverBorder(boolean rippleDrawOverBorder) {
        this.rippleDrawOverBorder = rippleDrawOverBorder;
        return this;
    }
}
