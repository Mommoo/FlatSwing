package com.mommoo.flat.frame.dialog;

import com.mommoo.flat.component.OnClickListener;

import java.awt.*;

public class DialogButtonInfo extends DialogComponentInfo{
    private OnClickListener onClickListener = component -> {};
    private boolean isExit, isEndAnimation;

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    @Override
    public DialogButtonInfo setText(String text) {
        super.setText(text);
        return this;
    }

    @Override
    public DialogButtonInfo setTextFont(Font textFont) {
        super.setTextFont(textFont);
        return this;
    }

    @Override
    public DialogButtonInfo setTextColor(Color color) {
        super.setTextColor(color);
        return this;
    }

    @Override
    public DialogButtonInfo setBackgroundColor(Color color) {
        super.setBackgroundColor(color);
        return this;
    }

    public DialogButtonInfo setOnClickListener(OnClickListener onClickListener){
        if (onClickListener == null) return this;
        this.onClickListener = onClickListener;
        return this;
    }

    public DialogButtonInfo setExitOption(boolean isExit, boolean isEndAnimation){
        this.isExit = isExit;
        this.isEndAnimation = this.isExit && isEndAnimation;
        return this;
    }

    public boolean isExit(){
        return isExit;
    }

    public boolean isEndAnimation(){
        return isEndAnimation;
    }
}
