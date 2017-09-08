package com.mommoo.flat.list.content;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.util.ColorManager;

import javax.swing.*;
import java.awt.*;

class ViewPortView extends JLayeredPane {
    private FlatPanel GLASS_PANEL = new FlatPanel();
    private FlatPanel[] array = {GLASS_PANEL, CONTENT_PANEL};
    private int glassPanelHeight = 0;
    FlatPanel CONTENT_PANEL = new FlatPanel(){
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            int windowPanelHeight = getWindowPanelHeight();
            System.out.println("windowPanelHeight : " + windowPanelHeight);
            if (windowPanelHeight == glassPanelHeight) return;



            int size = GLASS_PANEL.getComponentCount();
            System.out.println("size : "  + size);
            for (int i = 0 ; i < size ; i++){
                GLASS_PANEL.getComponent(i).setPreferredSize(CONTENT_PANEL.getComponent(i).getPreferredSize());
            }
            setSize(new Dimension(getWidth(), windowPanelHeight));
            setPreferredSize(new Dimension(getWidth(), windowPanelHeight));
            revalidate();
            repaint();
            CONTENT_PANEL.setSize(new Dimension(getWidth(), windowPanelHeight));
            CONTENT_PANEL.setPreferredSize(new Dimension(getWidth(), windowPanelHeight));
            CONTENT_PANEL.revalidate();
            CONTENT_PANEL.repaint();
            GLASS_PANEL.setSize(new Dimension(getWidth(), windowPanelHeight));
            GLASS_PANEL.setPreferredSize(new Dimension(getWidth(), windowPanelHeight));
            GLASS_PANEL.revalidate();
            GLASS_PANEL.repaint();
            glassPanelHeight = windowPanelHeight;
        }
    };

    ViewPortView(){
        setLinearLayout();
        //add(GLASS_PANEL, 200);
        add(CONTENT_PANEL, 50);
//        GLASS_PANEL.setOpaque(true);
//        GLASS_PANEL.setBackground(Color.YELLOW);
//
//        CONTENT_PANEL.setOpaque(true);
//        CONTENT_PANEL.setBackground(Color.GREEN);
    }

    private void setLinearLayout(){
        GLASS_PANEL.setLayout(new LinearLayout(Orientation.VERTICAL, 0));
        CONTENT_PANEL.setLayout(new LinearLayout(Orientation.VERTICAL, 0));
    }

    void setDivider(Color color, int gap){
        ((LinearLayout) GLASS_PANEL.getLayout()).setGap(gap);
        ((LinearLayout) CONTENT_PANEL.getLayout()).setGap(gap);

//        GLASS_PANEL.setOpaque(true);
//        GLASS_PANEL.setBackground(color);
    }

    void computeSize(Dimension dimension){
//        setPreferredSize(dimension);
//        setSize(dimension);

//        for (FlatPanel panel : array) {
//            panel.setPreferredSize(dimension);
//            panel.setSize(dimension);
//        }
//        GLASS_PANEL.setPreferredSize(dimension);
//        GLASS_PANEL.setSize(dimension);
//        CONTENT_PANEL.setPreferredSize(dimension);
//        CONTENT_PANEL.setSize(dimension);
    }

    void addComponent(Component comp){

        LinearConstraints constraints = new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT);
        CONTENT_PANEL.add(comp, constraints);
        GLASS_PANEL.add(createBlankComponent(), constraints);
    }

    void removeComponent(int index){
        CONTENT_PANEL.remove(index);
        GLASS_PANEL.remove(index);
    }

    private Component createBlankComponent(){
        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(getBackground());
                g.drawRect(0,0,getWidth(), getHeight());
            }
        };
        panel.setOpaque(false);
//        panel.setBackground(ColorManager.getTransParentColor());

        return panel;
    }

    private int getWindowPanelHeight(){
        int windowPanelHeight = 0;

        for (Component comp : CONTENT_PANEL.getComponents()){
            System.out.println("component Size : " + comp.getSize());
            System.out.println("component Pre : "+ comp.getPreferredSize());
            windowPanelHeight += comp.getPreferredSize().height;
        }

        return windowPanelHeight;
    }
}
