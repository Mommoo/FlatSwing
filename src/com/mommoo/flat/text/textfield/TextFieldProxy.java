package com.mommoo.flat.text.textfield;

import com.mommoo.util.ColorManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

abstract class TextFieldProxy {
    boolean isHintFirst;
    Color hintForeground = ColorManager.getFlatComponentDefaultColor();
    Color originalForegroundColor;
    private JTextField targetTextField;
    String hint = "";

    TextFieldProxy(){ }

    void bind(JTextField textField){
        this.targetTextField = decorateTextField(textField);
        this.originalForegroundColor = this.targetTextField.getForeground();
    }


    private JTextField decorateTextField(JTextField textField){
        textField.setBorder(BorderFactory.createEmptyBorder(0, 0,0, 0));
        textField.setOpaque(false);
        return textField;
    }

    boolean isHintAppeared(){
        boolean isEqualsHintText = getHint().equals(getText());
        boolean isEqualsForegroundColor = getHintForeground() == getTextField().getForeground();
        return isEqualsHintText && isEqualsForegroundColor;
    }

    void setHint(String hint){
        this.hint = hint;
        this.isHintFirst = true;
        setHintText();
    }

    void setText(String text){
        this.isHintFirst = false;
        setNormalText(text);
    }

    void clear(){
        setNormalText("");
        setHint(this.hint);
    }

    String getHint(){
        return this.hint;
    }

    Color getHintForeground(){
        return this.hintForeground;
    }

    void setHintForeground(Color hintColor){
        this.hintForeground = hintColor;
    }

    abstract void setHintText();

    abstract void setNormalText(String text);

    String getText(){
        return this.targetTextField.getText();
    }

    JTextField getTextField(){
        return this.targetTextField;
    }
}
