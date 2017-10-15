package com.mommoo.flat.layout.linear;


import com.mommoo.flat.layout.linear.constraints.LinearConstraints;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class LinearLayout implements LayoutManager2, Serializable {
    private static final int GAP = 10;
    private Orientation orientation;
    private LinearSpaceInspector spaceInspector = new LinearSpaceInspector();
    private boolean once;

    private int gap;

    public LinearLayout(){
        this(Orientation.HORIZONTAL, GAP);
    }

    public LinearLayout(Orientation orientation){
        this(orientation, GAP);
    }

    public LinearLayout(int gap){
        this(Orientation.HORIZONTAL, gap);
    }

    public LinearLayout(Orientation orientation, int gap){
        this.orientation = orientation;
        this.gap = gap;
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()){
            Dimension dimension = new Dimension(0,0);

            for (Component component : parent.getComponents()){

                if (!component.isVisible()) continue;

                Dimension compDimen = component.getPreferredSize();

                if (orientation == Orientation.HORIZONTAL){
                    dimension.width += compDimen.width;
                    dimension.height = Math.max(dimension.height, compDimen.height);
                } else {
                    dimension.width  = Math.max(dimension.width, compDimen.width);
                    dimension.height += compDimen.height;
                }
            }

        /* Add padding size */
            Insets insets = parent.getInsets();
            dimension.width += insets.left + insets.right;
            dimension.height += insets.top + insets.bottom;

            int occupiedGapSize = (parent.getComponentCount() - 1) * gap;

        /* Add gap size */
            if (orientation == Orientation.HORIZONTAL) {
                dimension.width += occupiedGapSize;
            } else {
                dimension.height += occupiedGapSize;
            }

            return dimension;
        }
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Dimension dimension = new Dimension(0,0);
            Insets insets = parent.getInsets();

            dimension.width = insets.left + insets.right;
            dimension.height = insets.top + insets.bottom;
            return dimension;
        }
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(Container target) { }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        synchronized (comp.getTreeLock()) {
            if (!(constraints instanceof LinearConstraints)) return;
            spaceInspector.setLinearConstraints(comp, ((LinearConstraints) constraints).clone());
        }
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException("Don't support method :: addLayoutComponent(String name, Component comp)");
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        synchronized (comp.getTreeLock()) {
            spaceInspector.removeComponent(comp);
        }
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            //fixComponentSizeAtOnce(parent);

            spaceInspector.setData(parent, orientation, gap);

            int index = 0;

            for (Component comp : parent.getComponents()){
                System.out.println(index + "  :  " + comp.getPreferredSize());
                comp.setBounds(spaceInspector.getProperCompBounds(index++));
            }

        }
    }

//    private void fixComponentSizeAtOnce(Container container){
//        if (once) {
//            return;
//        }
//        once = true;
//
//        for (Component comp : container.getComponents()){
//            comp.setPreferredSize(comp.getPreferredSize());
//        }
//    }

    public void setOrientation(Orientation orientation){
        this.orientation = orientation;
    }

    public void setGap(int gap){
        this.gap = gap;
    }

    public int getGap(){
        return this.gap;
    }

    public void setWeightSum(int weightSum){
        spaceInspector.setWeightSum(weightSum);
    }

    public int getWeightSum(){
        return spaceInspector.getWeightSum();
    }
}

