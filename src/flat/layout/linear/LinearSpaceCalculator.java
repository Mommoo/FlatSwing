package flat.layout.linear;

import flat.layout.linear.constraints.LinearConstraints;
import flat.layout.linear.constraints.LinearSpace;
import util.ComputableDimension;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

class LinearSpaceCalculator {
    private Container container;
    private Orientation orientation;
    private LinearSpaceRatioMode linearSpaceRatioMode = LinearSpaceRatioMode.NORMAL;
    private int weightSum;

    private Map<Component, LinearConstraints> linearConstraintsMap = new HashMap<>();

    private List<LinearCompDimen> layoutCompDimenList = new ArrayList<>();

    LinearSpaceCalculator(){ }

    void setData(Container container, Orientation orientation){
        this.container = container;
        this.orientation = orientation;
        buildLayoutCompDimen();
    }

    void setWeightSum(int weightSum){
        this.linearSpaceRatioMode = LinearSpaceRatioMode.WEIGHT;
        this.weightSum = weightSum;
    }

    void setLinearConstraints(Component component, LinearConstraints linearConstraints){
        this.linearSpaceRatioMode = LinearSpaceRatioMode.WEIGHT;
        linearConstraintsMap.put(component, linearConstraints);
    }

    int removeComponentWeight(Component component){
        LinearConstraints linearConstraints = linearConstraintsMap.get(component);
        if (linearConstraints == null) return 0;
        else linearConstraintsMap.remove(component);

        return linearConstraints.getWeight();
    }

    int getWeightSum(){
        return this.weightSum;
    }

    private void buildLayoutCompDimen(){
        initList();
        if (linearSpaceRatioMode == LinearSpaceRatioMode.NORMAL) normalBuild();
        else weightBuild();
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
    private void componentForEach(BiConsumer<Integer,Component> componentConsumer){
        int index = 0;
        for (Component comp : container.getComponents()){
            componentConsumer.accept(index, comp);
            index++;
        }
    }

    private void normalBuild(){
        componentForEach((index, component)-> buildCompDimen(index, component.getPreferredSize()));
    }

    private void weightBuild(){
        Dimension divisibleAreaDimen =  getDivisibleAreaDimen();

        Dimension areaRatioOfWeight =  new ComputableDimension(divisibleAreaDimen)
                .divDimension(weightSum,weightSum);

        Dimension calculatedDivisibleArea = new ComputableDimension(areaRatioOfWeight)
                .mulDimension(weightSum,weightSum);

        Dimension errorValueDimen = new ComputableDimension(divisibleAreaDimen)
                .subDimension(calculatedDivisibleArea);

        componentForEach((index, component) -> buildCompDimen(index, getWeightedDimension(index, areaRatioOfWeight, errorValueDimen)));
    }

    /**
     * @param index component index at container
     * @param areaRatioOfWeight value of division available size into sum of component weight
     * @param errorValueDimen value of error that when get areaRatioOfWeight value lost data
     * @return calculated weighted dimension
     */

    private Dimension getWeightedDimension(int index, Dimension areaRatioOfWeight, Dimension errorValueDimen){
        ComputableDimension weightedCompDimen = new ComputableDimension();
        Dimension compDimen = container.getComponent(index).getPreferredSize();
        LinearConstraints linearConstraints = linearConstraintsMap.get(container.getComponent(index));

        if (linearConstraints == null) {
            return compDimen;
        }

        weightedCompDimen
                .setZeroDimension()
                .addDimension(areaRatioOfWeight)
                .mulDimension(linearConstraints.getWeight(), linearConstraints.getWeight());

        boolean isMatchParent = linearConstraints.getLinearSpace() == LinearSpace.MATCH_PARENT;

        if (orientation == Orientation.HORIZONTAL) {
            weightedCompDimen.height = isMatchParent ? getAvailableDimen().height : compDimen.height;
        } else {
            weightedCompDimen.width  = isMatchParent ? getAvailableDimen().width : compDimen.width;
        }

        if (index == container.getComponentCount() -1){
            if (orientation == Orientation.HORIZONTAL) weightedCompDimen.addDimension(errorValueDimen.width, 0);
            else weightedCompDimen.addDimension(0, errorValueDimen.height);
        }

        return weightedCompDimen;
    }

    private void buildCompDimen(int index, Dimension compDimen){
        ComputableDimension occupiedAreaDimen = new ComputableDimension();

        occupiedAreaDimen
                .addDimension(new Dimension(orientation.getGap(),orientation.getGap()));

        if (index > 0){
            LinearCompDimen previousLinearCompDimen = layoutCompDimenList.get(index - 1);

            occupiedAreaDimen
                    .addDimension(previousLinearCompDimen.getComponentSize())
                    .addDimension(previousLinearCompDimen.getOccupiedSizeDimen());

        } else if (index == 0){
            occupiedAreaDimen.setZeroDimension();
        }

        layoutCompDimenList
                .get(index)
                .setOccupiedSizeDimen(occupiedAreaDimen)
                .setComponentSizeDimen(compDimen)
                .setCenter(linearConstraintsMap.get(container.getComponent(index)).getLinearSpace() == LinearSpace.WRAP_CENTER_CONTENT);
    }

    private List<Component> getWeightAbsentCompList(){
        List<Component> weightAbsentCompList = new ArrayList<>();

        componentForEach((index, component)->{
            if (linearConstraintsMap.get(component) == null){
                weightAbsentCompList.add(component);
            }
        });
        return weightAbsentCompList;
    }

    private Dimension getDivisibleAreaDimen(){
        List<Component> weightAbsentCompList = getWeightAbsentCompList();

        ComputableDimension divisibleAreaDimen = new ComputableDimension(getAvailableDimen());
        weightAbsentCompList.forEach(component -> divisibleAreaDimen.subDimension(component.getPreferredSize()));

        int gapCnt = container.getComponentCount() - 1;
        divisibleAreaDimen.subDimension(orientation.getGap() * gapCnt, orientation.getGap() * gapCnt);

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
