package com.mommoo.flat.layout.linear;


import com.mommoo.flat.layout.exception.MismatchException;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.text.textarea.FlatTextArea;

import java.awt.*;
import java.io.Serializable;

public class LinearLayout implements LinearLayoutProperty, LayoutManager2, Serializable {
    private Orientation orientation;
    private Alignment alignment;
    private int gap;
    private final Validator VALIDATOR = new Validator();
    private final ConstraintsFinder FINDER = new ConstraintsFinder();
    private final LinearAreaCalculator CALCULATOR = new LinearAreaCalculator();
    private int weightSum;

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
            if (constraints instanceof LinearConstraints) {
                LinearConstraints linearConstraints = ((LinearConstraints) constraints).clone();
                FINDER.put(comp, linearConstraints);
            }
        }
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException("Don't support method :: addLayoutComponent(String name, Component comp)");
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        synchronized (comp.getTreeLock()) {
            FINDER.remove(comp);
        }
    }

    @Override
    public void layoutContainer(Container container) {
        synchronized (container.getTreeLock()) {
            if (VALIDATOR.isValidate(container)){
                Rectangle[] bounds = CALCULATOR.getBounds(this, container, FINDER);
                int index = 0;
                for (Component comp : container.getComponents()){
                    comp.setBounds(bounds[index++]);
                }
            }
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
        return weightSum;
    }

    public void setWeightSum(int weightSum){
        if (weightSum <= 0){
            throw new IllegalArgumentException("weightSum can not smaller than zero value");
        }

        this.weightSum = weightSum;
        this.VALIDATOR.setAutoWeightSum(false);
    }


    private class Validator {
        private boolean isAutoWeightSum = true;

        private boolean isValidate(Container container){
            validateWeightSumIfNotAuto();
            sumWeightsIfAutoMode();

            return container.getComponentCount() > 0;
        }

        private void validateWeightSumIfNotAuto(){
            if (! isAutoWeightSum &&  FINDER.getWeightSum() > weightSum) {
                try {
                    throw new MismatchException();
                } catch (MismatchException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sumWeightsIfAutoMode(){
            if (isAutoWeightSum) {
                weightSum = FINDER.getWeightSum();
            }
        }

        private void setAutoWeightSum(boolean autoWeightSum){
            this.isAutoWeightSum = autoWeightSum;
        }
    }
}

