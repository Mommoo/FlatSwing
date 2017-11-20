package com.mommoo.flat.frame.colorpicker;

import com.mommoo.flat.utils.FlatColor;

import java.awt.*;

class HSB {
    private final int hue;
    private final int saturation;
    private final int brightness;

    HSB(int hue, int saturation, int brightness){
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    HSB(Color color){
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.hue = (int)(hsb[0] * 360f);
        this.saturation = (int)(hsb[1] * 100f);
        this.brightness = (int)(hsb[2] * 100f);
    }

    int getHue() {
        return hue;
    }

    int getSaturation() {
        return saturation;
    }

    int getBrightness() {
        return brightness;
    }

    HSB changeHue(int hue){
        return new HSB(hue, getSaturation(), getBrightness());
    }

    Color getColor(){
        return FlatColor.convertHSBtoRGB(getHue(), getSaturation(), getBrightness());
    }

    @Override
    public String toString() {
        return getHue()+"/"+getSaturation()+"/"+getBrightness();
    }
}
