package com.mommoo.flat.textfield;

import com.mommoo.util.FontManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;

abstract class TextFieldProxy {
    private static final ScreenManager SM = ScreenManager.getInstance();
    private static final int TEXT_FONT_SIZE = SM.dip2px(10);
    private static final Color DEFAULT_HINT_COLOR = Color.decode("#999999");


    private final JTextField targetTextField;

    Color originalForegroundColor;
    Color hintColor = DEFAULT_HINT_COLOR;
    String hint = "";

    TextFieldProxy(JTextField textField){
        this.targetTextField = textField;
        this.originalForegroundColor = this.targetTextField.getForeground();
        initTextField();
        addTextWatchListener();
    }

    private void initTextField(){
        this.targetTextField.setBorder(BorderFactory.createEmptyBorder(0, 0,0, 0));
        this.targetTextField.setFont(FontManager.getNanumGothicFont(Font.PLAIN,TEXT_FONT_SIZE));
    }

    private void addTextWatchListener(){
//        this.targetTextField.addCaretListener(e->{
//            if (this.targetTextField.getText().equals("")){
//                setHintText();
//            }
//        });
    }

    void setHint(String hint){
        this.hint = hint;
        setHintText();
    }

    void clear(){
        setText("");
        setHint(this.hint);
    }

    String getHint(){
        return this.hint;
    }

    void setHintColor(Color hintColor){
        this.hintColor = hintColor;
    }

    Color getHintColor(){
        return this.hintColor;
    }

    abstract void setHintText();

    abstract void setText(String text);

    String getText(){
        return this.targetTextField.getText();
    }

    JTextField getTextField(){
        return this.targetTextField;
    }
}
