package com.mommoo.flat.text.textarea;

import java.awt.*;

class StringViewModel{
    private final int stringWidth;
    private final int stringHeight;
    private final int lineCount;

    StringViewModel(int stringWidth, int stringHeight, int lineCount){
        this.stringWidth = stringWidth;
        this.stringHeight = stringHeight;
        this.lineCount = lineCount;
    }

    public StringViewModel(Dimension dimension, int lineCount){
        this(dimension.width, dimension.height, lineCount);
    }

    int getStringWidth(){
        return stringWidth;
    }

    int getStringHeight(){
        return stringHeight;
    }

    int getLineCount(){
        return lineCount;
    }

    @Override
    public String toString() {
        return  "stringWidth  : " + stringWidth +"\n"+
                "stringHeight : " + stringHeight+"\n"+
                "lineCount   : " + lineCount;
    }
}
