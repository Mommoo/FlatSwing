package com.mommoo.flat.text.textarea;

import java.awt.*;

class ContentsBounds {
    private final int contentsWidth;
    private final int contentsHeight;
    private final int lineCount;

    ContentsBounds(int contentsWidth, int contentsHeight, int lineCount){
        this.contentsWidth = contentsWidth;
        this.contentsHeight = contentsHeight;
        this.lineCount = lineCount;
    }

    public ContentsBounds(Dimension dimension, int lineCount){
        this(dimension.width, dimension.height, lineCount);
    }

    int getContentsWidth(){
        return contentsWidth;
    }

    int getContentsHeight(){
        return contentsHeight;
    }

    Dimension getDimension(){
        return new Dimension(contentsWidth, contentsHeight);
    }

    int getLineCount(){
        return lineCount;
    }

    @Override
    public String toString() {
        return  "contentsWidth  : " + contentsWidth +"\n"+
                "contentsHeight : " + contentsHeight +"\n"+
                "lineCount   : " + lineCount;
    }
}
