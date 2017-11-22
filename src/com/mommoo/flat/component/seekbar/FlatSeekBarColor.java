package com.mommoo.flat.component.seekbar;

import com.mommoo.util.ColorManager;

import java.awt.*;

class FlatSeekBarColor {
    private Color backgroundSeekBarColor = Color.GRAY;
    private Color frontSeekBarColor = ColorManager.getColorAccent();
    private Color seekBarHandleColor = ColorManager.getColorAccent().darker();

    Color getBackgroundSeekBarColor() {
        return backgroundSeekBarColor;
    }

    void setBackgroundSeekBarColor(Color backgroundSeekBarColor) {
        this.backgroundSeekBarColor = backgroundSeekBarColor;
    }

    Color getFrontSeekBarColor() {
        return frontSeekBarColor;
    }

    void setFrontSeekBarColor(Color frontSeekBarColor) {
        this.frontSeekBarColor = frontSeekBarColor;
    }

    Color getSeekBarHandleColor() {
        return seekBarHandleColor;
    }

    void setSeekBarHandleColor(Color seekBarHandleColor) {
        this.seekBarHandleColor = seekBarHandleColor;
    }
}
