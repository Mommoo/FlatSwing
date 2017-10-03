package com.mommoo.flat.frame.dialog;

import com.mommoo.flat.component.OnClickListener;

public class DialogButtonInfo extends DialogComponentInfo{
    private OnClickListener onClickListener;

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
}
