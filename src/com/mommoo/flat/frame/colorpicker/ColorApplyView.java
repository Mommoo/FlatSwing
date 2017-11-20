package com.mommoo.flat.frame.colorpicker;

import com.mommoo.flat.border.FlatEmptyBorder;
import com.mommoo.flat.button.FlatButton;
import com.mommoo.flat.frame.FlatDialog;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.text.textarea.alignment.FlatHorizontalAlignment;
import com.mommoo.flat.text.textarea.alignment.FlatVerticalAlignment;
import com.mommoo.flat.text.textfield.FlatTextField;
import com.mommoo.flat.text.textfield.format.FlatTextFormat;
import com.mommoo.util.ComputableDimension;
import com.mommoo.util.FontManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

class ColorApplyView extends JPanel {
    private static final ScreenManager SCREEN = ScreenManager.getInstance();
    private static final Font BUTTON_FONT = FontManager.getNanumGothicFont(Font.PLAIN, SCREEN.dip2px(6));
    private static final Font FONT = FontManager.getNanumGothicFont(Font.PLAIN, SCREEN.dip2px(10));

    private Consumer<HSB> onColorApplyListener = hsb -> {};

    ColorApplyView() {
        setOpaque(false);
        setLayout(new LinearLayout(Orientation.VERTICAL, 0));
        add(new GuideView("apply"), new LinearConstraints(LinearSpace.MATCH_PARENT));
        add(new ApplyPanel(), new LinearConstraints(1, LinearSpace.MATCH_PARENT));
    }

    void setUiColor(Color uiColor){
        ((FlatTextField)getApplyPanel().getComponent(1)).setFocusGainedColor(uiColor);
        getApplyPanel().getComponent(2).setBackground(uiColor);
    }

    void setOnColorApplyListener(Consumer<HSB> onColorApplyListener){
        this.onColorApplyListener = onColorApplyListener;
    }

    private Container getApplyPanel(){
        return (Container)getComponent(1);
    }

    private class ApplyPanel extends JPanel{

        private ApplyPanel(){
            setOpaque(false);
            setLayout(new LinearLayout(0));
            add(createPrefixLabel(),    new LinearConstraints(LinearSpace.MATCH_PARENT));
            add(createColorTextField(), new LinearConstraints(1, LinearSpace.MATCH_PARENT));
            add(createApplyButton(),    new LinearConstraints(LinearSpace.MATCH_PARENT));
        }

        private Component createPrefixLabel(){
            FlatLabel prefixLabel = new FlatLabel("#");
            prefixLabel.setFont(FONT);
            prefixLabel.setVerticalAlignment(FlatVerticalAlignment.CENTER);
            prefixLabel.setHorizontalAlignment(FlatHorizontalAlignment.CENTER);
            return prefixLabel;
        }

        private Component createColorTextField(){
            FlatTextField colorTextField = new FlatTextField(false);
            colorTextField.setFont(FONT);
            colorTextField.setFormat(FlatTextFormat.HEX_COLOR);
            colorTextField.setOpaque(false);
            colorTextField.setHint("ex) 68ceef");
            colorTextField.setHorizontalTextFieldAlignment(JTextField.CENTER);
            return colorTextField;
        }

        private Component createApplyButton(){
            FlatButton applyButton = new FlatButton("APPLY");
            applyButton.setFont(BUTTON_FONT);
            applyButton.setPreferredSize(new ComputableDimension(applyButton.getPreferredSize()).addDimension(SCREEN.dip2px(10)));
            applyButton.setOnClickListener(comp-> {

                String hexCodeText = ((FlatTextField)getComponent(1)).getText();
                if (hexCodeText.length() != 6){
                    new FlatDialog
                            .Builder()
                            .setTitle("Format Error")
                            .setLocationCenterTo(SwingUtilities.getWindowAncestor(applyButton))
                            .setContent("You have to keep the six digits")
                            .build()
                            .show();

                    return;
                }

                onColorApplyListener.accept(new HSB(Color.decode("#"+hexCodeText)));
            });
            return applyButton;
        }
    }
}
