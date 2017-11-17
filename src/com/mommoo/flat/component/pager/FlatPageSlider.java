package com.mommoo.flat.component.pager;

import com.mommoo.animation.AnimationAdapter;
import com.mommoo.animation.Animator;
import com.mommoo.animation.timeInterpolator.AccelerateInterpolator;
import com.mommoo.flat.component.FlatPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FlatPageSlider extends FlatPanel {
    private final SlideAnimator SLIDE_ANIMATOR = new SlideAnimator();
    private int pageIndex = 0;
    private int boundsX = 0;

    public FlatPageSlider(){
        setLayout(null);
    }

    @Override
    public void paint(Graphics g) {
        g.translate(-boundsX, 0);
        super.paint(g);
    }

    public void addPage(JPanel pager){
        final int index = getComponentCount();
        add(pager);
        SwingUtilities.invokeLater(()->{
            int x = getWidth() * index;
            int y = 0;
            int width = getWidth();
            int height = getHeight();
            pager.setBounds(x, y, width, height);
        });
    }

    public void slide(int pageIndex){
        SLIDE_ANIMATOR
                .setPageIndex(this.pageIndex)
                .stop()
                .start((pageIndex * getWidth()) - (this.pageIndex * getWidth()));
        this.pageIndex = pageIndex;
    }

    private class SlideAnimator extends AbstractAnimator {
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
            });
        }

        private SlideAnimator setPageIndex(int pageIndex){
            this.pageIndex = pageIndex;
            return this;
        }
    }
}
