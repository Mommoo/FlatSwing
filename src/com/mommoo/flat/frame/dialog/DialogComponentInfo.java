package com.mommoo.flat.frame.dialog;

import com.mommoo.util.FontManager;

import java.awt.*;

public class DialogComponentInfo {
    private static final Font DEFAULT_FONT = FontManager.getNanumGothicFont(Font.PLAIN,12);
    private String text = "text";
    private Color textColor = Color.BLACK;
    private Color backgroundColor = Color.WHITE;
    private Font textFont = DEFAULT_FONT;

    public DialogComponentInfo setText(String text){
        this.text = text;
        return this;
    }

    public DialogComponentInfo setTextFont(Font textFont){
        this.textFont = textFont;
        return this;
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

    public DialogComponentInfo setTextColor(Color color){
        this.textColor = color;
        return this;
    }

    public DialogComponentInfo setBackgroundColor(Color color){
        this.backgroundColor = color;
        return this;
    }

    public Color getBackgroundColor(){
        return this.backgroundColor;
    }
}
