package com.mommoo.flat.layout.linear;

import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.util.ComponentUtils;
import com.mommoo.util.ComputableDimension;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.stream.Collectors;

class LinearAreaCalculator {
    LinearAreaCalculator(){ }

    Rectangle2D.Double[] getBounds(LinearLayoutProperty property, Container container, ConstraintsFinder finder){
        Rectangle2D.Double[] bounds = new Rectangle2D.Double[container.getComponentCount()];

        Dimension availableContainerDimen = ComponentUtils.getAvailableSize(container);

        /* have none - weight component put area first */
        Dimension preOccupiedArea = getPreOccupiedArea(container, finder);

        /* Need to sub both sum of none-weight components and sum of gaps */
        ComputableDimension weightEnableArea = new ComputableDimension()
                .setDimension(availableContainerDimen)
                .subDimension((container.getComponentCount() - 1 ) * property.getGap())
                .subDimension(preOccupiedArea);

        double weightW = weightEnableArea.getWidth()  / (double) property.getWeightSum();
        double weightH = weightEnableArea.getHeight() / (double) property.getWeightSum();

        int index = 0;

        for (Component comp : container.getComponents()) {

            LinearConstraints constraints = finder.find(comp);

            double compX = container.getInsets().left;
            double compY = container.getInsets().top;
            double compW = constraints.getWeight() == 0 ? comp.getPreferredSize().width  : weightW * (double)constraints.getWeight();
            double compH = constraints.getWeight() == 0 ? comp.getPreferredSize().height : weightH * (double)constraints.getWeight();

            if (property.getOrientation() == Orientation.HORIZONTAL) {

                compX = index == 0 ? container.getInsets().left : bounds[index - 1].getX() + bounds[index - 1].getWidth() + property.getGap();

                if (constraints.getLinearSpace() == LinearSpace.MATCH_PARENT) {
                    compH = availableContainerDimen.height;
                } else if (constraints.getLinearSpace() == LinearSpace.WRAP_CONTENT) {
                    compY = container.getInsets().top;
                } else if (constraints.getLinearSpace() == LinearSpace.WRAP_CENTER_CONTENT) {
                    compY = ( availableContainerDimen.height - compH ) / 2.0d;
                } else {
                    compY = ( availableContainerDimen.height - compH );
                }

            }

            else {

                if (constraints.getLinearSpace() == LinearSpace.MATCH_PARENT) {
                    compW = availableContainerDimen.width;
                } else if (constraints.getLinearSpace() == LinearSpace.WRAP_CONTENT) {
                    compX = container.getInsets().left;
                } else if (constraints.getLinearSpace() == LinearSpace.WRAP_CENTER_CONTENT) {
                    compX = ( availableContainerDimen.width - compW ) / 2.0d;
                } else {
                    compX = ( availableContainerDimen.width - compW );
                }

                compY = index == 0 ? container.getInsets().top : bounds[index - 1].getY() + bounds[index - 1].getHeight() + property.getGap();
            }

            bounds[index++] = new Rectangle2D.Double(compX, compY, compW, compH);
        }

        bounds = buildAlignment(bounds, container, property);

        return bounds;
    }

    private Dimension getPreOccupiedArea(Container container, ConstraintsFinder finder){
        ComputableDimension occupiedArea = new ComputableDimension();

        Arrays.stream(container.getComponents())
                .filter(comp->finder.find(comp).getWeight() == 0)
                .collect(Collectors.toList())
                .forEach(comp -> occupiedArea.addDimension(comp.getPreferredSize()));

        return occupiedArea;
    }

    private Rectangle2D.Double[] buildAlignment(Rectangle2D.Double[] bounds, Container container, LinearLayoutProperty property) {
        int size = bounds.length;

        if (property.getAlignment() == Alignment.START && size == 0) {
            return bounds;
        }

        double usedW = bounds[size- 1].getX() + bounds[size - 1].getWidth()  - bounds[0].getX();
        double usedH = bounds[size- 1].getY() + bounds[size - 1].getHeight() - bounds[0].getY();

        if (property.getOrientation() == Orientation.HORIZONTAL) {
            double remainW = ComponentUtils.getAvailableSize(container).getWidth() - usedW;
            double moveX = 0d;

            if (remainW >= 0) {
                if (property.getAlignment() == Alignment.CENTER) {
                    moveX = remainW / 2d;
                } else if (property.getAlignment() == Alignment.END) {
                    moveX = remainW;
                }

                for (int i = 0 ; i < bounds.length ; i ++) {
                    bounds[i].x += moveX;
                }
            }
        }

        else {
            double remainH = ComponentUtils.getAvailableSize(container).getHeight() - usedH;
            double moveY = 0d;

            if (remainH >= 0) {
                moveY = remainH / 2d;
            } else if (property.getAlignment() == Alignment.END) {
                moveY = remainH;
            }

            for (int i = 0 ; i < bounds.length ; i ++) {
                bounds[i].y += moveY;
            }
        }

        return bounds;
    }
}
