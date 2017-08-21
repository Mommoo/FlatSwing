package com.mommoo.flat.textfield;

import javax.swing.*;

/**
 * Created by mommoo on 2017-07-14.
 */
public class PasswordTextField extends TextFieldProxy {
    private final JPasswordField passwordField;
    private final char PASS_WORD_ECHO_CHAR;

    PasswordTextField() {
        super(new JPasswordField());
        this.passwordField = (JPasswordField)getTextField();
        this.PASS_WORD_ECHO_CHAR = this.passwordField.getEchoChar();
    }

    @Override
    void setText(String text) {
        passwordField.setForeground(this.originalForegroundColor == null ? passwordField.getForeground() : this.originalForegroundColor);
        passwordField.setEchoChar(PASS_WORD_ECHO_CHAR);
        this.passwordField.setText(text);
    }

    @Override
    void setHintText() {
        this.originalForegroundColor = passwordField.getForeground();
        passwordField.setForeground(hintColor);
        passwordField.setEchoChar((char)0);
        this.passwordField.setText(hint);
    }
}
