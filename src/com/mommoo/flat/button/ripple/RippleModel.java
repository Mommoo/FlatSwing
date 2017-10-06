package com.mommoo.flat.button.ripple;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Consumer;

public interface RippleModel {
    public boolean isOnEventLaterEffect();
    public int getRippleDuration();
    public int getRippleHoldDuration();
    public Color getRippleColor();
    public float getRippleColorOpacity();
}
