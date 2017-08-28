package com.mommoo.flat.component;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mommoo on 2017-07-14.
 */
public class FlatPanel extends JPanel {

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

    public boolean isComponentContained(Component component){
        for (Component comp : getComponents()){
            if (comp == component) return true;
        }
        return false;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Insets insets = getInsets();
        int availableWidth = getWidth() - insets.left - insets.right;
        int availableHeight = getHeight() - insets.top - insets.bottom;
        draw((Graphics2D)g, availableWidth, availableHeight);
    }

    protected void draw(Graphics2D graphics2D, int availableWidth, int availableHeight){

    }
}
