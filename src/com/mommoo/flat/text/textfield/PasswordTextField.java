package com.mommoo.flat.text.textfield;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mommoo on 2017-07-14.
 */
public class PasswordTextField extends TextFieldProxy {
    private final JPasswordField passwordField;
    private final char PASS_WORD_ECHO_CHAR;

    PasswordTextField() {
        bind(new JPasswordField(){
            private boolean once;

            @Override
            public void paint(Graphics g) {
                if (!once){
                    once = true;
                    setProperForegroundColor();
                }
                super.paint(g);
            }

            private void setProperForegroundColor(){
                if (isHintFirst){
                    setForeground(hintForeground);
                } else {
                    setForeground(originalForegroundColor);
                }
            }
        });
        this.passwordField = (JPasswordField)getTextField();
        this.PASS_WORD_ECHO_CHAR = this.passwordField.getEchoChar();
    }

    @Override
    void setNormalText(String text) {
        passwordField.setForeground(this.originalForegroundColor == null ? passwordField.getForeground() : this.originalForegroundColor);
        passwordField.setEchoChar(PASS_WORD_ECHO_CHAR);
        this.passwordField.setText(text);
    }

    @Override
    void setHintText() {
        this.originalForegroundColor = passwordField.getForeground();
        passwordField.setForeground(hintForeground);
        passwordField.setEchoChar((char)0);
        this.passwordField.setText(getHint());
    }
}
