package com.mommoo.flat.text.textfield;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mommoo on 2017-07-14.
 */
public class NormalTextField extends TextFieldProxy {

    NormalTextField() {
        bind(new JTextField(){
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
    }

    @Override
    void setNormalText(String text) {
        getTextField().setForeground(this.originalForegroundColor == null ? getTextField().getForeground() : this.originalForegroundColor);
        getTextField().setText(text);
    }

    @Override
    void setHintText() {
        getTextField().setText(hint);
        this.originalForegroundColor = getTextField().getForeground();
        getTextField().setForeground(hintForeground);
    }
}
