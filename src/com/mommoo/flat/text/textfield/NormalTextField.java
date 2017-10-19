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

            private void setProperForegroundColor() {
                if (isHintFirst) {
                    setForeground(hintForeground);
                } else {
                    setForeground(originalForegroundColor);
                }
            }
        });
    }

    @Override
    void setNormalText(String text) {
        getTextField().setText(text);
        getTextField().setForeground(this.originalForegroundColor == null ? getTextField().getForeground() : this.originalForegroundColor);
    }

    @Override
    void setHintText() {
        getTextField().setText(getHint());
        this.originalForegroundColor = getTextField().getForeground();
        getTextField().setForeground(hintForeground);
    }
}
