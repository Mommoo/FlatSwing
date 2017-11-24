package com.mommoo.flat.component;

import com.mommoo.util.ScreenManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PaddingWrapPanel extends JPanel {
    private static final int DEFAULT_PADDING = ScreenManager.getInstance().dip2px(6);
    public PaddingWrapPanel(){
        this(DEFAULT_PADDING);
    }

    public PaddingWrapPanel(int padding){
        this(padding, padding, padding, padding);
    }

    public PaddingWrapPanel(int topBottom, int leftRight){
        this(topBottom, leftRight, topBottom, leftRight);
    }

    public PaddingWrapPanel(int top, int left, int bottom, int right){
        super(new BorderLayout());
        setOpaque(false);
        setPadding(top, left, bottom, right);
    }

    @Override
    public void setBorder(Border border) {

    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    public void setPadding(int padding){
        setPadding(padding, padding, padding, padding);
    }

    public void setPadding(int topBottom, int leftRight){
        setPadding(topBottom, leftRight, topBottom, leftRight);
    }

    public void setPadding(int top, int left, int bottom, int right){
        super.setBorder(new EmptyBorder(top, left, bottom, right));
    }

    @Override
    public Component add(Component comp) {
        return super.add(comp);
    }

    @Override
    public Component add(String name, Component comp) {
        invokeException();
        return super.add(comp);
    }

    @Override
    public Component add(Component comp, int index) {
        invokeException();
        return super.add(comp, index);
    }

    @Override
    public void add(Component comp, Object constraints) {
        invokeException();
        super.add(comp);
    }

    @Override
    public void add(Component comp, Object constraints, int index) {
        invokeException();
        super.add(comp, index);
    }

    private void invokeException(){
        try {
            throw new IllegalAccessException("this method not supported, try use 'add(Component comp)' method");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
