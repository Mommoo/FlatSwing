package com.mommoo.flat.text.textfield;

import com.mommoo.flat.text.textfield.format.FlatTextFormat;
import com.mommoo.flat.text.textfield.format.FormattedDocument;
import com.mommoo.util.ColorManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

abstract class TextFieldProxy {
    boolean isHintFirst;
    Color hintForeground = ColorManager.getFlatComponentDefaultColor();
    Color originalForegroundColor;
    private JTextField targetTextField;
    private String hint = "";

    private FormattedDocument formattedDocument = new FormattedDocument();

    TextFieldProxy(){ }

    void bind(JTextField textField){
        this.targetTextField = decorateTextField(textField);
        this.originalForegroundColor = this.targetTextField.getForeground();
    }


    private JTextField decorateTextField(JTextField textField){
        textField.setBorder(BorderFactory.createEmptyBorder(0, 0,0, 0));
        textField.setOpaque(false);
        textField.setDocument(formattedDocument);
        return textField;
    }


    boolean isHintAppeared(){
        boolean isEqualsHintText = getHint().equals(getText());
        boolean isEqualsForegroundColor = getHintForeground() == getTextField().getForeground();
        return isEqualsHintText && isEqualsForegroundColor;
    }

    void setHint(String hint){
        formattedDocument.setHintStatus(true);
        this.hint = hint;
        this.isHintFirst = true;
        setHintText();
        formattedDocument.setHintStatus(false);
    }

    void setText(String text){
        formattedDocument.setHintStatus(false);
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

    List<FlatTextFormat> getFormatList(){
        return ((FormattedDocument)this.targetTextField.getDocument()).getFormatList();
    }

    void setFormat(FlatTextFormat... formats){
        ((FormattedDocument)this.targetTextField.getDocument()).setFormat(formats);
    }
}
