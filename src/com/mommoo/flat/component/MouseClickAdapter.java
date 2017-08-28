package com.mommoo.flat.component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseClickAdapter extends MouseAdapter{
    private boolean mousePressed, mouseEntered;
    private final OnClickListener onClickListener;

    public MouseClickAdapter(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        mousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (mousePressed && mouseEntered){
            onClickListener.onClick(e.getComponent());
        }
        mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        mouseEntered = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        mouseEntered = false;
    }
}
