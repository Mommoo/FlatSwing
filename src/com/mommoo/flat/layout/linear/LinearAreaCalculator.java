package com.mommoo.flat.layout.linear;

import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.util.ComponentUtils;
import com.mommoo.util.ComputableDimension;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.List;

class LinearAreaCalculator {
    LinearAreaCalculator() { }

    Rectangle[] getBounds(LinearLayoutProperty property, Container container, ConstraintsFinder finder) {
        int size = container.getComponentCount();

        Rectangle[] bounds = new Rectangle[size];

        Dimension availableContainerDimen = ComponentUtils.getAvailableSize(container);

        /* have none - weight component put area first */
        Dimension preOccupiedArea = getPreOccupiedArea(container, finder);

        /* Need to sub both sum of none-weight components and sum of gaps */
        ComputableDimension weightEnableArea = new ComputableDimension()
                .setDimension(availableContainerDimen)
                .subDimension((size - 1) * property.getGap())
                .subDimension(preOccupiedArea);

        double weightW = weightEnableArea.getWidth() / (double) property.getWeightSum();
        double weightH = weightEnableArea.getHeight() / (double) property.getWeightSum();

        List<Component> weightList = Arrays.stream(container.getComponents())
                .filter(comp -> finder.find(comp).getWeight() > 0)
                .collect(Collectors.toList());

        /*
         *  Swing component put component using by pixel( int value ) so, error value may be exist
         *  We solve this problem by adding error value to last component had weight
         */
        Dimension errorDimension = getErrorDimension(weightList, finder, weightEnableArea, weightW, weightH);
        Component lastWeightComp = weightList.size() > 0 ? weightList.get(weightList.size() - 1) : null;

        int index = 0;

        for (Component comp : container.getComponents()) {
            boolean isLastWeightComp = lastWeightComp == comp;

            LinearConstraints constraints = finder.find(comp);

            double compX;
            double compY;
            double compW;
            double compH;

            if (property.getOrientation() == Orientation.HORIZONTAL) {

                compX = index == 0 ? container.getInsets().left : bounds[index - 1].getX() + bounds[index - 1].getWidth() + property.getGap();
                compY = container.getInsets().top;
                compW = constraints.getWeight() == 0 ? comp.getPreferredSize().width : weightW * (double) constraints.getWeight();
                compH = comp.getPreferredSize().height;

                if (constraints.getLinearSpace() == LinearSpace.MATCH_PARENT) {
                    compH = availableContainerDimen.height;
                } else if (constraints.getLinearSpace() == LinearSpace.WRAP_CONTENT) {
                    compY = container.getInsets().top;
                } else if (constraints.getLinearSpace() == LinearSpace.WRAP_CENTER_CONTENT) {
                    compY += (availableContainerDimen.height - compH) / 2.0d;
                } else {
                    compY += (availableContainerDimen.height - compH);
                }

                if (isLastWeightComp) {
                    compW += errorDimension.width;
                }

            } else {

                compX = container.getInsets().left;
                compY = index == 0 ? container.getInsets().top : bounds[index - 1].getY() + bounds[index - 1].getHeight() + property.getGap();
                compW = comp.getPreferredSize().width;
                compH = constraints.getWeight() == 0 ? comp.getPreferredSize().height : weightH * (double) constraints.getWeight();

                if (constraints.getLinearSpace() == LinearSpace.MATCH_PARENT) {
                    compW = availableContainerDimen.width;
                } else if (constraints.getLinearSpace() == LinearSpace.WRAP_CONTENT) {
                    compX = container.getInsets().left;
                } else if (constraints.getLinearSpace() == LinearSpace.WRAP_CENTER_CONTENT) {
                    compX += (availableContainerDimen.width - compW) / 2.0d;
                } else {
                    compX += (availableContainerDimen.width - compW);
                }

                if (isLastWeightComp) {
                    compH += errorDimension.height;
                }
            }

            bounds[index++] = new Rectangle((int) compX, (int) compY, (int) compW, (int) compH);
        }

        bounds = buildAlignment(bounds, container, property);

        return bounds;
    }

    private Dimension getPreOccupiedArea(Container container, ConstraintsFinder finder) {
        ComputableDimension occupiedArea = new ComputableDimension();

        Arrays.stream(container.getComponents())
                .filter(comp -> finder.find(comp).getWeight() == 0)
                .collect(Collectors.toList())
                .forEach(comp -> occupiedArea.addDimension(comp.getPreferredSize()));

        return occupiedArea;
    }

    private Dimension getErrorDimension(List<Component> weightList, ConstraintsFinder finder, Dimension weightEnableArea, double weightW, double weightH){
        ComputableDimension errorDimension = new ComputableDimension()
                .setDimension(weightEnableArea)
                .setMinimumSize(0, 0);

        weightList.forEach(comp -> {
            int weight = finder.find(comp).getWeight();
            errorDimension.subDimension((int) (weightW * weight), (int) (weightH * weight));
        });

        return errorDimension;
    }

    private Rectangle[] buildAlignment(Rectangle[] bounds, Container container, LinearLayoutProperty property) {
        int size = bounds.length;

        if (property.getAlignment() == Alignment.START && size == 0) {
            return bounds;
        }

        double usedW = bounds[size - 1].getX() + bounds[size - 1].getWidth() - bounds[0].getX();
        double usedH = bounds[size - 1].getY() + bounds[size - 1].getHeight() - bounds[0].getY();

        if (property.getOrientation() == Orientation.HORIZONTAL) {
            double remainW = ComponentUtils.getAvailableSize(container).getWidth() - usedW;
            double moveX = 0d;

            if (remainW >= 0) {
                if (property.getAlignment() == Alignment.CENTER) {
                    moveX = remainW / 2d;
                } else if (property.getAlignment() == Alignment.END) {
                    moveX = remainW;
                }

                for (int i = 0; i < bounds.length; i++) {
                    bounds[i].x += moveX;
                }
            }
        } else {
            double remainH = ComponentUtils.getAvailableSize(container).getHeight() - usedH;
            double moveY = 0d;

            if (remainH >= 0) {
                moveY = remainH / 2d;
            } else if (property.getAlignment() == Alignment.END) {
                moveY = remainH;
            }

            for (int i = 0; i < bounds.length; i++) {
                bounds[i].y += moveY;
            }
        }

        return bounds;
    }
}
