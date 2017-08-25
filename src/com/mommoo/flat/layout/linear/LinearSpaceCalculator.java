package com.mommoo.flat.layout.linear;

import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.util.ComputableDimension;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

class LinearSpaceCalculator {
    private Container container;
    private Orientation orientation;
    private int gap;
    private int weightSum;

    private Map<Component, LinearConstraints> linearConstraintsMap = new HashMap<>();

    private List<LinearCompDimen> layoutCompDimenList = new ArrayList<>();

    LinearSpaceCalculator(){ }

    void setData(Container container, Orientation orientation, int gap){
        this.container = container;
        this.orientation = orientation;
        this.gap = gap;
        initList();

        if (weightSum == 0){
            buildPreferredCompDimen();
            return;
        }

        buildPreferredAndWeightCompDimen();
    }

    void setWeightSum(int weightSum){
        this.weightSum = weightSum;
    }

    void setLinearConstraints(Component component, LinearConstraints linearConstraints){
        linearConstraintsMap.put(component, linearConstraints);
    }

    boolean isExistConstraints(Component component){
        return linearConstraintsMap.get(component) != null;
    }

    int removeLinearConstraints(Component component){
        LinearConstraints linearConstraints = linearConstraintsMap.get(component);

        if (linearConstraints == null) return 0;
        else linearConstraintsMap.remove(component);

        return linearConstraints.getWeight();
    }

    int getWeightSum(){
        return this.weightSum;
    }

    private void initList(){
        for (int i = 0, compCnt = container.getComponentCount(); i < compCnt ; i++){
            if (layoutCompDimenList.size() == i){
                layoutCompDimenList.add(LinearCompDimen.createZeroDimen());
            } else{
                layoutCompDimenList.get(i).setZeroDimension();
            }
        }
    }

    private void buildPreferredCompDimen(){
        componentForEach((index, component) -> buildCompDimen(index, component.getPreferredSize(), new Dimension(0,0)));
    }

    private void buildPreferredAndWeightCompDimen(){
        Dimension divisibleAreaDimen = getDivisibleAreaDimen();

        Dimension weightRatioDimen =  new ComputableDimension(divisibleAreaDimen)
                .divDimension(weightSum,weightSum);

        Dimension errorValueDimen = getErrorValueDimen(divisibleAreaDimen, weightRatioDimen);

        componentForEach((index, component) -> buildCompDimen(index, getWeightedDimension(index, weightRatioDimen),errorValueDimen));
    }

    private Dimension getErrorValueDimen(Dimension divisibleAreaDimen, Dimension weightRatioDimen){
        Dimension calculatedDivisibleArea = new ComputableDimension(weightRatioDimen)
                .mulDimension(weightSum,weightSum);

        return new ComputableDimension(divisibleAreaDimen)
                .subDimension(calculatedDivisibleArea);
    }

    private void componentForEach(BiConsumer<Integer,Component> componentConsumer){
        int index = 0;
        for (Component comp : container.getComponents()){
            componentConsumer.accept(index, comp);
            index++;
        }
    }

    /**
     * @param index component index at container
     * @param areaRatioOfWeight value of division available size into sum of component weight;
     * @return calculated weighted dimension
     */

    private Dimension getWeightedDimension(int index, Dimension areaRatioOfWeight){
        ComputableDimension weightedCompDimen = new ComputableDimension();
        Dimension compDimen = container.getComponent(index).getPreferredSize();

        LinearConstraints linearConstraints = linearConstraintsMap.get(container.getComponent(index));

        if (linearConstraints.getWeight() == 0) {
            weightedCompDimen.setSize(compDimen);
        }
        else {
            weightedCompDimen
                    .setZeroDimension()
                    .addDimension(areaRatioOfWeight)
                    .mulDimension(linearConstraints.getWeight(), linearConstraints.getWeight());

            if (orientation == Orientation.VERTICAL) weightedCompDimen.width = compDimen.width;
            else weightedCompDimen.height = compDimen.height;

        }

        return weightedCompDimen;
    }

    private void buildCompDimen(int index, Dimension basicDimen, Dimension errorValueDimen){
        ComputableDimension componentDimen = new ComputableDimension(basicDimen);
        ComputableDimension occupiedAreaDimen = new ComputableDimension();

        occupiedAreaDimen.addDimension(new Dimension(gap, gap));

        if (index > 0){
            LinearCompDimen previousLinearCompDimen = layoutCompDimenList.get(index - 1);

            occupiedAreaDimen
                    .addDimension(previousLinearCompDimen.getComponentSize())
                    .addDimension(previousLinearCompDimen.getOccupiedSizeDimen());

        } else if (index == 0){
            occupiedAreaDimen.setZeroDimension();
        }

        LinearConstraints linearConstraints = linearConstraintsMap.get(container.getComponent(index));


        buildLinearSpace(linearConstraints, componentDimen, basicDimen);

        addErrorValueAtLastComp(index, componentDimen, errorValueDimen);

        boolean isCenter = linearConstraints.getLinearSpace() == LinearSpace.WRAP_CENTER_CONTENT;

        layoutCompDimenList
                .get(index)
                .setOccupiedSizeDimen(occupiedAreaDimen)
                .setComponentSizeDimen(componentDimen)
                .setCenter(isCenter);
    }

    private void buildLinearSpace(LinearConstraints linearConstraints,Dimension componentDimen ,Dimension basicDimen){
        boolean isMatchParent = linearConstraints.getLinearSpace() == LinearSpace.MATCH_PARENT;

        if (orientation == Orientation.HORIZONTAL) {
            componentDimen.height = isMatchParent ? getAvailableDimen().height : basicDimen.height;
        } else {
            componentDimen.width  = isMatchParent ? getAvailableDimen().width : basicDimen.width;
        }
    }

    private void addErrorValueAtLastComp(int index, ComputableDimension componentDimen, Dimension errorValueDimen){
        if (index == container.getComponentCount() -1){
            if (orientation == Orientation.HORIZONTAL) componentDimen.addDimension(errorValueDimen.width, 0);
            else componentDimen.addDimension(0, errorValueDimen.height);
        }
    }

    private List<Component> getNoWeightCompList(){
        List<Component> noWeightCompList = new ArrayList<>();

        componentForEach((index, component)->{
            LinearConstraints constraints = linearConstraintsMap.get(component);
            if (constraints.getWeight() == 0){
                noWeightCompList.add(component);
            }
        });

        return noWeightCompList;
    }

    private Dimension getDivisibleAreaDimen(){
        List<Component> noWeightCompList = getNoWeightCompList();

        ComputableDimension divisibleAreaDimen = new ComputableDimension(getAvailableDimen());
        noWeightCompList.forEach(component -> divisibleAreaDimen.subDimension(component.getPreferredSize()));

        int gapCnt = container.getComponentCount() - 1;
        divisibleAreaDimen.subDimension(gap * gapCnt, gap * gapCnt);

        return divisibleAreaDimen;
    }

    private Dimension getAvailableDimen(){
        Insets insets = container.getInsets();

        ComputableDimension availableDimen = new ComputableDimension(container.getSize());
        availableDimen.subDimension(insets.left + insets.right, insets.top + insets.bottom);

        return availableDimen;
    }

    LinearCompDimen getLinearCompDimen(int index){
        return layoutCompDimenList.get(index);
    }

    static class LinearCompDimen {
        private final Dimension occupiedSizeDimen;
        private final Dimension componentSize;
        private boolean isCenter;

        private LinearCompDimen(Dimension occupiedSizeDimen, Dimension componentSize){
            this.occupiedSizeDimen = occupiedSizeDimen;
            this.componentSize = componentSize;
        }

        private static LinearCompDimen createZeroDimen(){
            return new LinearCompDimen(new Dimension(0,0), new Dimension(0, 0));
        }

        private void setZeroDimension(){
            this.occupiedSizeDimen.setSize(0,0);
            this.componentSize.setSize(0, 0);
        }

        private LinearCompDimen setOccupiedSizeDimen(Dimension dimen){
            this.occupiedSizeDimen.setSize(dimen);
            return this;
        }

        private LinearCompDimen setComponentSizeDimen(Dimension dimen){
            this.componentSize.setSize(dimen);
            return this;
        }

        private LinearCompDimen setCenter(boolean isCenter){
            this.isCenter = isCenter;
            return this;
        }

        Dimension getOccupiedSizeDimen(){
            return occupiedSizeDimen;
        }

        Dimension getComponentSize(){
            return componentSize;
        }

        boolean isCenter(){
            return isCenter;
        }
    }
}
