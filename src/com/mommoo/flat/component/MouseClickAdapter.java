package com.mommoo.flat.component;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseClickAdapter extends FlatMouseAdapter{
    private final OnClickListener onClickListener;
    private Component source;

    public MouseClickAdapter(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public MouseClickAdapter(Component source, OnClickListener onClickListener){
        this(onClickListener);
        this.source = source;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (source == null) source = e.getComponent();
        if (isMouseClicked()){
            onClickListener.onClick(source);
        }
    }
}
