package com.mommoo.flat.frame.colorpicker;

import com.mommoo.flat.button.FlatButton;
import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.layout.linear.Alignment;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.util.FontManager;
import com.mommoo.util.ScreenManager;

import java.awt.*;

class ColorConfirmView extends FlatPanel {
    private static final ScreenManager SCREEN = ScreenManager.getInstance();
    private static final Font BUTTON_FONT = FontManager.getNanumGothicBoldFont(SCREEN.dip2px(8));

    private Runnable onConfirmListener = () -> {};
    private Runnable onCancelListener  = () -> {};

    ColorConfirmView(){
        setLayout(new LinearLayout(SCREEN.dip2px(6), Alignment.CENTER));
        add(createConfirmButton(), new LinearConstraints(1, LinearSpace.MATCH_PARENT),"confirmButton");
        add(createCancelButton() , new LinearConstraints(1, LinearSpace.MATCH_PARENT),"cancelButton");
    }

    ColorConfirmView setOnConfirmListener(Runnable onConfirmListener){
        this.onConfirmListener = onConfirmListener;
        return this;
    }

    ColorConfirmView setOnCancelListener(Runnable onCancelListener){
        this.onCancelListener = onCancelListener;
        return this;
    }

    void setUiColor(Color uiColor){
        getComponent(0).setBackground(uiColor);
        getComponent(1).setBackground(uiColor);
    }

    private FlatButton createCommonButton(String buttonText){
        FlatButton commonButton = new FlatButton(buttonText.toUpperCase());
        commonButton.setFont(BUTTON_FONT);
        commonButton.setPreferredSize(new Dimension(SCREEN.dip2px(50), SCREEN.dip2px(30)));
        return commonButton;
    }

    private Component createConfirmButton(){
        FlatButton confirmButton = createCommonButton("confirm");
        confirmButton.setOnClickListener(comp -> onConfirmListener.run());
        return confirmButton;
    }

    private Component createCancelButton(){
        FlatButton cancelButton = createCommonButton("cancel");
        cancelButton.setOnClickListener(comp -> onCancelListener.run());
        return cancelButton;
    }
}
