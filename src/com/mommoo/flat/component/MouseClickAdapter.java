package com.mommoo.flat.component;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseClickAdapter extends FlatMouseAdapter{
    private final OnClickListener onClickListener;

    public MouseClickAdapter(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (isMouseClicked()){
            onClickListener.onClick(e.getComponent());
        }
    }
}
