package com.mommoo.flat.layout.linear;

import com.mommoo.flat.layout.exception.MismatchException;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;

import java.awt.*;

class LinearSpaceInspector {
    private Container container;
    private Orientation orientation;
    private LinearSpaceCalculator calculator = new LinearSpaceCalculator();
    private int addedWeight;

    LinearSpaceInspector(){ }

    private void inspectValidState(Container container){

        setConstraintsIfAbsent(container);
        setWeightSumIfAbsent();

        Exception exception = null;

        if (calculator.getWeightSum() < addedWeight){
            exception = new MismatchException();
        }

        if (exception != null){
            try {
                throw exception;
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    private void setConstraintsIfAbsent(Container container){
        for (Component component : container.getComponents()){
            if (!calculator.isExistConstraints(component)) setLinearConstraints(component, new LinearConstraints());
        }
    }

    private void setWeightSumIfAbsent(){
        if (addedWeight > 0 && calculator.getWeightSum() == 0) {
            calculator.setWeightSum(addedWeight);
        }
    }

    void setData(Container container, Orientation orientation, int gap){
        inspectValidState(container);
        this.container = container;
        this.orientation = orientation;
        calculator.setData(this.container,this.orientation, gap);

    }

    void setWeightSum(int weightSum){
        calculator.setWeightSum(weightSum);
    }

    void setLinearConstraints(Component comp, LinearConstraints linearConstraints){
        calculator.setLinearConstraints(comp, linearConstraints);
        addedWeight += linearConstraints.getWeight();
    }

    void removeComponent(Component comp){
        int previousCompWeight = calculator.removeLinearConstraints(comp);
        addedWeight -= previousCompWeight;
    }

    int getWeightSum(){
        return calculator.getWeightSum();
    }

    Rectangle getProperCompBounds(int index){

        Insets insets = container.getInsets();
        int availableWidth = container.getWidth() - (insets.left + insets.right);
        int availableHeight = container.getHeight() - (insets.top + insets.bottom);

        LinearSpaceCalculator.LinearCompDimen linearCompDimen = calculator.getLinearCompDimen(index);

        boolean isHorizontal = orientation == Orientation.HORIZONTAL;

        int x = insets.left;
        int y = insets.top;
        int width  = linearCompDimen.getComponentSize().width;
        int height = linearCompDimen.getComponentSize().height;
        boolean isCenter = linearCompDimen.isCenter();

        if (isHorizontal){
            int occupiedWidth = linearCompDimen.getOccupiedSizeDimen().width;

            if (availableWidth <= width + occupiedWidth){
                width = availableWidth - occupiedWidth;
            }

            if (availableHeight <= height){
                height = availableHeight;
            }

            x += occupiedWidth;
            if (isCenter) y += (availableHeight - height)/2;
        }

        else {
            int occupiedHeight = linearCompDimen.getOccupiedSizeDimen().height;

            if (availableWidth <= width){
                width = availableWidth;
            }

            if (availableHeight <= height + occupiedHeight){
                height = availableHeight - occupiedHeight;
            }

            if (isCenter) x += (availableWidth - width)/2;
            y += occupiedHeight;
        }

        return new Rectangle(x, y, width, height);
    }
}
