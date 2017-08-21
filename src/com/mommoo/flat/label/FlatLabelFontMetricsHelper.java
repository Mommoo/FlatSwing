package com.mommoo.flat.label;

import java.awt.*;

class FlatLabelFontMetricsHelper extends FontMetrics{
    private FontMetrics target;
    private int lineHeight;

    FlatLabelFontMetricsHelper(FontMetrics target, int lineHeight) {
        super(target.getFont());
        this.target = target;
        this.lineHeight = lineHeight;
    }

    @Override
    public int getHeight() {
        if(lineHeight == 0) return super.getHeight();
        return lineHeight;
    }

    @Override
    public int bytesWidth(byte[] data, int off, int len) {
        return target.bytesWidth(data, off, len);
    }

    @Override
    public int charWidth(char ch) {
        return target.charWidth(ch);
    }

    @Override
    public int stringWidth(String str) {
        return target.stringWidth(str);
    }

    @Override
    public int charWidth(int codePoint) {
        return target.charWidth(codePoint);
    }
}
