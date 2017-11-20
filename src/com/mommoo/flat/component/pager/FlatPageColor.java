package com.mommoo.flat.component.pager;

import com.mommoo.util.ColorManager;

import java.awt.*;

class FlatPageColor {
    private static final FlatPageColor DEFAULT_PAGER_COLOR = new FlatPageColor(ColorManager.getColorAccent(), Color.BLACK);
    private final Color focusInColor;
    private final Color focusOutColor;

    FlatPageColor(Color focusInColor, Color focusOutColor){
        this.focusInColor = focusInColor;
        this.focusOutColor = focusOutColor;
    }

    static FlatPageColor getDefaultFlatPagerColor(){
        return DEFAULT_PAGER_COLOR;
    }

    Color getFocusInColor() {
        return focusInColor;
    }

    Color getFocusOutColor() {
        return focusOutColor;
    }
}
