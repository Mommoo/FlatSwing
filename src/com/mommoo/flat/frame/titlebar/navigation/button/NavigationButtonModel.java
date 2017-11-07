package com.mommoo.flat.frame.titlebar.navigation.button;

import com.mommoo.animation.AnimationAdapter;
import com.mommoo.animation.Animator;
import com.mommoo.animation.timeInterpolator.AccelerateInterpolator;
import com.mommoo.flat.component.FlatMouseAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

class NavigationButtonModel {
    private final ViewModel viewModel;
    private float alpha = 0.0f;
    private Color hoverColor = Color.LIGHT_GRAY;

    private Runnable hoverInListener = ()->{};
    private Runnable hoverOutListener = ()->{};

    NavigationButtonModel(ViewModel viewModel){
        this.viewModel = viewModel;
    }

    public void setHoverColor(Color hoverColor){
        this.hoverColor = hoverColor;
    }

    void paint(Graphics2D graphics2D){
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        graphics2D.setColor(hoverColor);
        graphics2D.fillRect(0, 0, viewModel.getWidth(), viewModel.getHeight());
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    HoverAnimListener getHoverAnimListener(){
        return new HoverAnimListener();
    }

    void setHoverInListener(Runnable hoverInListener){
        this.hoverInListener = hoverInListener;
    }

    void setHoverOutListener(Runnable hoverOutListener){
        this.hoverOutListener = hoverOutListener;
    }

    class HoverAnimListener extends FlatMouseAdapter {
        private static final int HOVER_IN_DURATION  = 100;
        private static final int HOVER_OUT_DURATION = 100;

        private final Animator animator = new Animator().setTimeInterpolator(new AccelerateInterpolator());

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            animator
                    .stop()
                    .setDuration(HOVER_IN_DURATION)
                    .setAnimationListener(new AnimationAdapter(){
                        @Override
                        public void onAnimation(List<Double> resultList) {
                            alpha = Math.max(resultList.get(0).floatValue(), 1.0f);
                            e.getComponent().repaint();
                        }

                        @Override
                        public void onStop() {
                            hoverInListener.run();
                        }

                        @Override
                        public void onEnd() {
                            hoverInListener.run();
                        }
                    })
                    .start(1.0d);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            animator
                    .stop()
                    .setDuration(HOVER_OUT_DURATION)
                    .setAnimationListener(new AnimationAdapter(){
                        private float previousAlpha = alpha;

                        @Override
                        public void onAnimation(List<Double> resultList) {
                            alpha = Math.max(0.0f,previousAlpha - resultList.get(0).floatValue());
                            e.getComponent().repaint();
                        }

                        @Override
                        public void onStop() {
                            hoverOutListener.run();
                        }

                        @Override
                        public void onEnd() {
                            hoverOutListener.run();
                        }
                    })
                    .start(alpha);
        }
    }
}
