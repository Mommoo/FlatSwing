package com.mommoo.flat.select;

import com.mommoo.flat.component.OnClickListener;
import com.mommoo.util.ImageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class CheckBox extends JPanel implements MouseListener {
    private static int R = 255, G = 255, B = 255;
    private static int SUB_NUMBER = 20;
    private static final Color BACKGROUND_COLOR = new Color(R,G,B);
    private static final Color FOCUSED_COLOR = new Color(R-SUB_NUMBER,G-SUB_NUMBER,B-SUB_NUMBER);
    private static final Color PRESSED_COLOR = new Color(R-SUB_NUMBER*2,G-SUB_NUMBER*2,B-SUB_NUMBER*2);


    private boolean isMouseExited,isClicked;
    private int imageSize;
    private Image check = ImageManager.CHECK;

    private OnClickListener onClickListener;
    private boolean isCallBack;

    CheckBox(){
        addMouseListener(this);
        setBackground(BACKGROUND_COLOR);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        final Graphics2D GRAPHICS_2D = (Graphics2D) g;
        final int WIDTH = getWidth();
        final int HEIGHT = getHeight();

        GRAPHICS_2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GRAPHICS_2D.setColor(Color.BLACK);
        GRAPHICS_2D.drawRect(0, 0, WIDTH-1, HEIGHT-1);

        if(imageSize != HEIGHT){
            check = check.getScaledInstance(HEIGHT, HEIGHT, Image.SCALE_SMOOTH);
            imageSize = HEIGHT;
        }

        if(isCallBack){
            isClicked = !isClicked;
            callBack();
        }
        if(isClicked){
            GRAPHICS_2D.drawImage(check, 0,0,this);
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        setBackground(PRESSED_COLOR);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!isMouseExited) isCallBack = true;
        setBackground(BACKGROUND_COLOR);

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        isMouseExited = false;
        setBackground(FOCUSED_COLOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isMouseExited = true;
        isCallBack = false;
        setBackground(BACKGROUND_COLOR);
    }

    private void callBack(){
        if(!isMouseExited){
            if(onClickListener != null) onClickListener.onClick(this);
        }
        isCallBack = false;
    }

    void setChecked(boolean check){
        this.isClicked = check;
    }

    boolean isChecked(){
        return isClicked;
    }

    void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    MouseListener getMouseListener(){
        return this;
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(50,50);
    }
}

