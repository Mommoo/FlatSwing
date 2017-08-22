package com.mommoo.flat.label;

import com.mommoo.flat.component.OnClickListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class FlatLabelMouseListener extends MouseAdapter{

    private boolean isPressed;
    private boolean isEntered;
    private OnClickListener onClickListener;

    FlatLabelMouseListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        isPressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);

        if(isPressed && isEntered){
            onClickListener.onClick(e.getComponent());
        }

        isPressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        isEntered = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        isEntered = false;
    }
}
