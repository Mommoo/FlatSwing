package com.mommoo.flat.frame.dialog;

import com.mommoo.util.FontManager;

import java.awt.*;

public class DialogComponentInfo {
    private String text = "text";
    private Color textColor = Color.BLACK;
    private Color backgroundColor = Color.WHITE;
    private Font textFont = FontManager.getNanumGothicFont(Font.PLAIN,12);

    public void setText(String text){
        this.text = text;
    }

    public void setTextFont(Font textFont){
        this.textFont = textFont;
    }

    public String getText(){
        return text;
    }

    public Font getTextFont(){
        return textFont;
    }

    public Color getTextColor(){
        return textColor;
    }

    public void setTextColor(Color color){
        this.textColor = color;
    }

    public void setBackgroundColor(Color color){
        this.backgroundColor = color;
    }

    public Color getBackgroundColor(){
        return this.backgroundColor;
    }
}
