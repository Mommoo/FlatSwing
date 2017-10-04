package com.mommoo.flat.text.textarea;

public class StringViewModel{
    private final int stringWidth;
    private final int stringHeight;
    private final int lineCount;

    public StringViewModel(int stringWidth, int stringHeight, int lineCount){
        this.stringWidth = stringWidth;
        this.stringHeight = stringHeight;
        this.lineCount = lineCount;
    }

    public int getStringWidth(){
        return stringWidth;
    }

    public int getStringHeight(){
        return stringHeight;
    }

    public int getLineCount(){
        return lineCount;
    }

    @Override
    public String toString() {
        return  "stringWidth  : " + stringWidth +"\n"+
                "stringHeight : " + stringHeight+"\n"+
                "lineCount   : " + lineCount;
    }
}
