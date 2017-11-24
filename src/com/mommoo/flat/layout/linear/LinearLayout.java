package com.mommoo.flat.layout.linear;


import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.text.textarea.FlatTextArea;
import com.mommoo.util.ComponentUtils;

import java.awt.*;
import java.io.Serializable;

public class LinearLayout implements LayoutManager2, Serializable {
    private Orientation orientation;
    private final LinearSpaceInspector SPACE_INSPECTOR = new LinearSpaceInspector();
    private Alignment alignment;

    private int gap;

    public LinearLayout(){
        this(Orientation.HORIZONTAL, 10, Alignment.START);
    }

    public LinearLayout(Orientation orientation){
        this(orientation, 10, Alignment.START);
    }

    public LinearLayout(int gap){
        this(Orientation.HORIZONTAL, gap, Alignment.START);
    }

    public LinearLayout(Orientation orientation, int gap){
        this(orientation, gap, Alignment.START);
    }

    public LinearLayout(Orientation orientation, Alignment alignment){
        this(orientation, 10, alignment);
    }

    public LinearLayout(int gap, Alignment alignment){
        this(Orientation.HORIZONTAL, gap, alignment);
    }

    public LinearLayout(Alignment alignment){
        this(Orientation.HORIZONTAL, 10, alignment);
    }

    public LinearLayout(Orientation orientation, int gap, Alignment alignment){
        this.orientation = orientation;
        this.gap = gap;
        this.alignment = alignment;
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
            SPACE_INSPECTOR.setLinearConstraints(comp, ((LinearConstraints) constraints).clone());
        }
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException("Don't support method :: addLayoutComponent(String name, Component comp)");
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        synchronized (comp.getTreeLock()) {
            SPACE_INSPECTOR.removeComponent(comp);
        }
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            SPACE_INSPECTOR.setData(parent, orientation, gap);

            int moveX = 0, moveY = 0;

            Dimension availableSize = ComponentUtils.getAvailableSize(parent);

            Rectangle[] boundsArray = SPACE_INSPECTOR.getProperCompBoundsArray();

            int lastIndex = boundsArray.length - 1;

            if (lastIndex < 0) return;

            int occupiedWidth  = lastIndex > 0 ? boundsArray[lastIndex].x + boundsArray[lastIndex].width : boundsArray[0].width;
            int occupiedHeight = lastIndex > 0 ? boundsArray[lastIndex].y + boundsArray[lastIndex].height : boundsArray[0].height;

            switch(alignment){
                case START: break;

                case CENTER :
                    if (orientation == Orientation.HORIZONTAL){
                        moveX = (availableSize.width - occupiedWidth)/2;
                    } else {
                        moveY = (availableSize.height - occupiedHeight)/2;
                    }
                    break;

                    case END :
                    if (orientation == Orientation.HORIZONTAL){
                        moveX = (availableSize.width - occupiedWidth);
                    } else {
                        moveY = (availableSize.height - occupiedHeight);
                    }
                    break;
            }

            int index = 0;
            for (Component comp : parent.getComponents()){
                Rectangle bound = boundsArray[index++];
                bound.x += moveX;
                bound.y += moveY;
                comp.setBounds(bound);
                validateCompIfFlatTextArea(comp);
            }

        }
    }

    private void validateCompIfFlatTextArea(Component comp){
        if (comp.getBounds().width > 0 && comp instanceof FlatTextArea && orientation == Orientation.HORIZONTAL){
            ((FlatTextArea) comp).setPreferredWidth(comp.getBounds().width);
        }
    }

    public void setOrientation(Orientation orientation){
        this.orientation = orientation;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setGap(int gap){
        this.gap = gap;
    }

    public int getGap(){
        return this.gap;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment){
        this.alignment = alignment;
    }

    public int getWeightSum(){
        return SPACE_INSPECTOR.getWeightSum();
    }

    public void setWeightSum(int weightSum){
        if (weightSum <= 0){
            throw new IllegalArgumentException("weightSum can not smaller than zero value");
        }
        SPACE_INSPECTOR
                .setAutoWeightSum(false)
                .setWeightSum(weightSum);
    }
}

