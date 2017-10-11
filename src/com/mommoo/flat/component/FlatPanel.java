package com.mommoo.flat.component;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mommoo on 2017-07-14.
 */
public class FlatPanel extends JPanel {

    private MouseClickAdapter mouseClickAdapter;
    private OnClickListener onClickListener = comp -> {};
    private OnPaintListener onPaintListener = g2d -> {};
    private OnLayoutListener onLayoutListener = (width, height) -> {};

    public FlatPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        init();
    }

    public FlatPanel(LayoutManager layout) {
        super(layout);
        init();
    }

    public FlatPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        init();
    }

    public FlatPanel() {
        super();
        init();
    }

    private void init(){
        setOpaque(false);
    }

    @Override
    public void paint(Graphics g) {
        Insets insets = getInsets();
        int availableWidth = getWidth() - insets.left - insets.right;
        int availableHeight = getHeight() - insets.top - insets.bottom;

        Graphics2D graphics2D = (Graphics2D)g;
        preDraw(graphics2D, availableWidth, availableHeight);

        super.paint(g);

        onPaintListener.onPaint(graphics2D);
        postDraw(graphics2D, availableWidth, availableHeight);
        draw(graphics2D, availableWidth, availableHeight);
    }

    protected void preDraw(Graphics2D graphics2D, int availableWidth, int availableHeight){

    }

    protected void postDraw(Graphics2D graphics2D, int availableWidth, int availableHeight){

    }

    @Deprecated
    protected void draw(Graphics2D graphics2D, int availableWidth, int availableHeight){

    }

    public boolean isComponentContained(Component component){
        for (Component comp : getComponents()){
            if (comp == component) return true;
        }
        return false;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener){
        if (onClickListener == null) return;
        this.onClickListener = onClickListener;
        this.mouseClickAdapter = new MouseClickAdapter(onClickListener);
        addMouseListener(new MouseClickAdapter(onClickListener));
    }

    public void removeOnClickListener(){
        removeMouseListener(this.mouseClickAdapter);
        this.onClickListener = component -> {};
    }

    public OnPaintListener getOnPaintListener() {
        return onPaintListener;
    }

    public void setOnPaintListener(OnPaintListener onPaintListener){
        if (onPaintListener == null) return;
        this.onPaintListener = onPaintListener;
    }

    public void removeOnPaintListener(){
        onPaintListener = g -> {};
    }

    public OnLayoutListener getOnLayoutListener() {
        return onLayoutListener;
    }

    public void setOnLayoutListener(OnLayoutListener onLayoutListener){
        if (onLayoutListener == null) return;
        this.onLayoutListener = onLayoutListener;
    }

    public void removeOnLayoutListener(){
        onLayoutListener = (width, height) -> {};
    }
}
