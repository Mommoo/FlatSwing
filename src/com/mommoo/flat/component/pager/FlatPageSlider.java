package com.mommoo.flat.component.pager;

import com.mommoo.animation.AnimationAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class FlatPageSlider extends JPanel {
    private final SlideAnimator SLIDE_ANIMATOR = new SlideAnimator();
    private int offsets = 0;
    private boolean needToArrange = true;
    private final Set<Integer> screenOffLoadIndex = new TreeSet<>();

    private List<OnPageSelectedListener> onPageSelectedListenerList = new ArrayList<>();

    FlatPageSlider(){
        setOpaque(false);
        setLayout(null);
    }

    private void executeScreenOffLoad(int pageIndex){
        if(screenOffLoadIndex.contains(pageIndex)) {
            BufferedImage screenOffBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = screenOffBuffer.createGraphics();
            getComponent(pageIndex).paintAll(graphics);
            graphics.dispose();
        }
    }

    @Override
    public void paint(Graphics g) {
        if (needToArrange){
            for (int i = 0, size = getComponentCount(); i < size ; i ++){
                int x = getWidth() * (i - offsets);
                getComponent(i).setBounds(x, 0, getWidth(), getHeight());
                executeScreenOffLoad(i);
            }
            needToArrange = false;

        }
        super.paint(g);
    }

    void addPage(Component pageView){
        add(pageView);
        needToArrange = true;
        repaint();
    }

    void removePage(int index){
        remove(index);
        needToArrange = true;
        repaint();
    }

    void setOffset(int offsets){
        this.offsets = offsets;
    }

    void slide(int pageIndex, boolean isAnimation){
        if (isAnimation){
            SLIDE_ANIMATOR
                    .setOnFinishListener(()-> this.offsets = pageIndex)
                    .stop()
                    // have to moving distance of component0
                    .start(- getWidth() * pageIndex + Math.abs(getComponent(0).getX()));
        } else {
            for (int i = 0, size = getComponentCount(); i < size ; i ++){
                getComponent(i).setLocation((- pageIndex * getWidth()) + (getWidth() * i),0);
            }
            repaint();
            this.offsets = pageIndex;
        }
    }

    void addOnPageSelectedListener(OnPageSelectedListener onPageSelectedListener){
        this.onPageSelectedListenerList.add(onPageSelectedListener);
    }

    void setOnPageSelectedListener(int index, OnPageSelectedListener onPageSelectedListener){
        this.onPageSelectedListenerList.set(index, onPageSelectedListener);
    }

    void setScreenOffLoadIndex(int index){
        screenOffLoadIndex.add(index);
    }

    OnPageSelectedListener getOnPageSelectedListener(int index){
        return this.onPageSelectedListenerList.get(index);
    }

    FlatAnimator getAnimator(){
        return SLIDE_ANIMATOR;
    }

    private class SlideAnimator extends FlatAnimator {

        private Runnable onFinishListener = () -> {};

        private SlideAnimator(){
            setAnimationListener(new AnimationAdapter(){
                private int previousX;

                @Override
                public void onStart() {
                    if (getComponentCount() > 0){
                        previousX = getComponent(0).getX();
                    }
                }

                @Override
                public void onAnimation(List<Double> resultList) {
                    int x = resultList.get(0).intValue();

                    Component firstComp = getComponent(0);
                    firstComp.setLocation(previousX + x, 0);

                    for (int i = 1, size = getComponentCount(); i < size ; i ++){
                        getComponent(i).setLocation((firstComp.getX() + (getWidth() * i)),0);
                    }
                    repaint();
                }

                @Override
                public void onEnd() {
                    onFinishListener.run();
                    for (OnPageSelectedListener onPageSelectedListener : onPageSelectedListenerList){
                        onPageSelectedListener.onPageSelected(offsets);
                    }
                }
            });
        }

        public SlideAnimator setOnFinishListener(Runnable onFinishListener){
            this.onFinishListener = onFinishListener;
            return this;
        }
    }
}
