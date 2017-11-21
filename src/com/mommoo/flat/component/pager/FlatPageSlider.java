package com.mommoo.flat.component.pager;

import com.mommoo.animation.AnimationAdapter;
import com.mommoo.flat.component.FlatPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntConsumer;

class FlatPageSlider extends JPanel {
    private final SlideAnimator SLIDE_ANIMATOR = new SlideAnimator();
    private int pageIndex = 0;
    private boolean needToArrange = true;
    private final Set<Integer> screenOffLoadIndex = new TreeSet<>();

    private List<OnPageSelectedListener> onPageSelectedListenerList = new ArrayList<>();

    FlatPageSlider(){
        setOpaque(false);
        setLayout(null);
    }

    private void executeScreenOffLoad(int pageIndex, Graphics g){
        if(screenOffLoadIndex.contains(pageIndex)) getComponent(pageIndex).paint(g);
    }

    @Override
    public void paint(Graphics g) {
        if (needToArrange){
            for (int i = 0, size = getComponentCount(); i < size ; i ++){
                int x = getWidth() * i;
                getComponent(i).setBounds(x, 0, getWidth(), getHeight());
                executeScreenOffLoad(i, g);
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

    void slide(int pageIndex, boolean animation){
        if (animation){
            SLIDE_ANIMATOR
                    .setOnFinishListener(()-> this.pageIndex = pageIndex)
                    .stop()
                    // have to moving distance of component0
                    .start(- getWidth() * pageIndex + Math.abs(getComponent(0).getX()));
        } else {
            for (int i = 0, size = getComponentCount(); i < size ; i ++){
                int x = getComponent(i).getX() - pageIndex * getWidth();
                getComponent(i).setLocation(x,0);
            }
            repaint();
            this.pageIndex = pageIndex;
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
                        onPageSelectedListener.onPageSelected(pageIndex);
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
