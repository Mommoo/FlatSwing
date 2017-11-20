package com.mommoo.flat.component.pager;

import com.mommoo.animation.AnimationAdapter;
import com.mommoo.flat.component.FlatPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

class FlatPageSlider extends JPanel {
    private final SlideAnimator SLIDE_ANIMATOR = new SlideAnimator();
    private int pageIndex = 0;
    private int boundsX = 0;
    private boolean needToArrange = true;

    private List<OnPageSelectedListener> onPageSelectedListenerList = new ArrayList<>();

    FlatPageSlider(){
        setOpaque(false);
        setLayout(null);
    }

    @Override
    public void paint(Graphics g) {
        if (needToArrange){
            for (int i = 0, size = getComponentCount(); i < size ; i ++){
                int x = getWidth() * i;
                int y = 0;
                int width = getWidth();
                int height = getHeight();
                getComponent(i).setBounds(x, y, width, height);
            }
            needToArrange = false;
        }
        g.translate(-boundsX, 0);
        super.paint(g);
    }

    void addPage(Component pageView){
        final int index = getComponentCount();
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
                    .setPageIndex(this.pageIndex)
                    .stop()
                    .start((pageIndex * getWidth()) - (this.pageIndex * getWidth()));
        } else {
            boundsX = pageIndex * getWidth();
            repaint();
        }

        this.pageIndex = pageIndex;
    }

    void addOnPageSelectedListener(OnPageSelectedListener onPageSelectedListener){
        this.onPageSelectedListenerList.add(onPageSelectedListener);
    }

    void setOnPageSelectedListener(int index, OnPageSelectedListener onPageSelectedListener){
        this.onPageSelectedListenerList.set(index, onPageSelectedListener);
    }

    OnPageSelectedListener getOnPageSelectedListener(int index){
        return this.onPageSelectedListenerList.get(index);
    }

    FlatAnimator getAnimator(){
        return SLIDE_ANIMATOR;
    }

    private class SlideAnimator extends FlatAnimator {
        private int pageIndex;

        private SlideAnimator(){
            setAnimationListener(new AnimationAdapter(){
                private int previousX;

                @Override
                public void onStart() {
                    previousX = pageIndex * getWidth();
                }

                @Override
                public void onAnimation(List<Double> resultList) {
                    int x = resultList.get(0).intValue();
                    boundsX = previousX + x;
                    repaint();
                }

                @Override
                public void onEnd() {
                    for (OnPageSelectedListener onPageSelectedListener : onPageSelectedListenerList){
                        onPageSelectedListener.onPageSelected(FlatPageSlider.this.pageIndex);
                    }
                }
            });
        }

        private SlideAnimator setPageIndex(int pageIndex){
            this.pageIndex = pageIndex;
            return this;
        }
    }
}
