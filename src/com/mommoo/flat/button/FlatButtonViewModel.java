package com.mommoo.flat.button;

import com.mommoo.animation.AnimationAdapter;
import com.mommoo.animation.Animator;
import com.mommoo.animation.timeInterpolator.AccelerateInterpolator;
import com.mommoo.flat.button.ripple.RippleModel;
import com.mommoo.flat.component.FlatMouseAdapter;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.List;

class FlatButtonViewModel {
    private final ButtonViewModel viewModel;
    private final RippleModel rippleModel;
    private final ButtonEventRepository BUTTON_EVENT_REPOSITORY = new ButtonEventRepository();
    private final RippleMouseEventListener rippleMouseEventListener = new RippleMouseEventListener();

    private Runnable onRippleStartListener = () -> {};
    private Runnable onRippleEndListener = () -> {};

    FlatButtonViewModel(ButtonViewModel viewModel, RippleModel rippleModel) {
        this.viewModel = viewModel;
        this.rippleModel = rippleModel;
    }

    MouseAdapter getRippleAnimMouseListener() {
        return rippleMouseEventListener;
    }

    void paintRippleEffect(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rippleMouseEventListener.drawRippleEffect(graphics2D);
    }

    FlatButtonViewModel setOnRippleEndListener(Runnable onRippleEndListener){
        this.onRippleEndListener = onRippleEndListener;
        return this;
    }

    FlatButtonViewModel setOnRippleStartListener(Runnable onRippleStartListener){
        this.onRippleStartListener = onRippleStartListener;
        return this;
    }

    void executeRippleEffect(Point startLocation, ActionEvent actionEvent){
        rippleMouseEventListener.userMouseLocation = startLocation;
        rippleMouseEventListener.animationReady();

        if (rippleModel.isOnEventLaterEffect()){
            rippleMouseEventListener.animationEndListener = () -> {
                rippleMouseEventListener.executeEvent(actionEvent);
                rippleMouseEventListener.animationEndListener = () -> {};
            };
        } else {
            rippleMouseEventListener.executeEvent(actionEvent);
        }

        rippleMouseEventListener.executePostRippleEffect();
    }

    ButtonEventRepository getButtonEventRepository(){
        return BUTTON_EVENT_REPOSITORY;
    }

    private class RippleMouseEventListener extends FlatMouseAdapter {
        private boolean isAnimationEnd;

        private Ellipse2D ellipse2D = new Ellipse2D.Double();
        private Point userMouseLocation = new Point(0, 0);
        private Animator animator = new Animator().setTimeInterpolator(new AccelerateInterpolator());

        private int restDuration;
        private float opacity = 1.0f;
        private double radius;

        private Runnable animationEndListener = () -> {};
        private Color rippleOpacityColor;

        private AnimationAdapter onPreAnimationListener = createOnPreAnimationListener();

        private AnimationAdapter createOnPreAnimationListener(){
            return new AnimationAdapter() {
                private long startTime;
                @Override
                public void onStart() {
                    animationReady();
                    userMouseLocation = FlatAreaMouseLocation.getLocation(getMouseEvent().getComponent());
                    startTime = System.currentTimeMillis();
                    SwingUtilities.invokeLater(()->{
                        onRippleStartListener.run();
                    });
                }

                @Override
                public void onAnimation(List<Double> resultList) {
                    radius = Math.max(radius,resultList.get(0));
                    ellipse2D.setFrame(userMouseLocation.getX() - radius,
                            userMouseLocation.getY() - radius,
                            radius * 2,
                            radius * 2);
                    viewModel.repaint();

                }

                @Override
                public void onStop() {
                    restDuration = rippleModel.getRippleDuration() - (int) ((System.currentTimeMillis() - startTime) / 2L);
                    restDuration = Math.max(100, restDuration);
                }

                @Override
                public void onEnd() {
                    restDuration = 0;
                }
            };
        }

        private ActionEvent convertMouseEventToActionEvent(){
            MouseEvent mouseEvent = getMouseEvent();
            return new ActionEvent(mouseEvent.getSource(), mouseEvent.getID(), mouseEvent.paramString());
        }

        private void animationReady() {
            restDuration = rippleModel.getRippleDuration();
            rippleOpacityColor = createOpacityColor();
            opacity = 1.0f;
            radius = 0;
            isAnimationEnd = false;
        }

        private void animationEnd() {
            radius = 0;
            executePostAlphaAnimation(() -> {

                isAnimationEnd = true;
//                viewModel.repaint();

                if (isMouseClicked() && rippleModel.isOnEventLaterEffect()){
                    executeEvent(convertMouseEventToActionEvent());
                }

                animationEndListener.run();
                onRippleEndListener.run();
            });
        }

        private void executeEvent(ActionEvent actionEvent){
            SwingUtilities.invokeLater(()-> BUTTON_EVENT_REPOSITORY.getEventList().forEach(event -> event.accept(actionEvent)));
        }

        @Override
        public void mousePressed(MouseEvent e) {
            executePreRippleEffect();
            super.mousePressed(e);

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (!rippleModel.isOnEventLaterEffect() && isMouseClicked()) {
                executeEvent(convertMouseEventToActionEvent());
            }
            executePostRippleEffect();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            userMouseLocation = FlatAreaMouseLocation.getLocation(e.getComponent());
            viewModel.repaint();
        }

        private void executePreRippleEffect(){
            animator.stop()
                    .setDuration(rippleModel.getRippleDuration() * 2)
                    .setAnimationListener(onPreAnimationListener)
                    .start(getDiagonalSize());
        }

        private void executePostRippleEffect(){
            animator.stop()
                    .setDuration(restDuration)
                    .setAnimationListener(new AnimationAdapter() {
                        private final double previousRadius = radius;

                        @Override
                        public void onStart() {
                            animationReady();
                        }

                        @Override
                        public void onAnimation(List<Double> resultList) {
                            radius = previousRadius + resultList.get(0);
                            ellipse2D.setFrame(userMouseLocation.getX() - radius,
                                    userMouseLocation.getY() - radius,
                                    radius * 2,
                                    radius * 2);
                            viewModel.repaint();
                        }

                        @Override
                        public void onEnd() {
                            animationEnd();
                        }
                    })
                    .start(getDiagonalSize() - radius);
        }

        private void executePostAlphaAnimation(Runnable animationEndListener){
            animator.stop()
                    .setDuration(rippleModel.getRippleHoldDuration())
                    .setAnimationListener(new AnimationAdapter(){
                        @Override
                        public void onAnimation(List<Double> resultList) {
                            opacity = 1.0f - resultList.get(0).floatValue();
                            viewModel.repaint();
                        }

                        @Override
                        public void onEnd() {
                            animationEndListener.run();
                        }
                    })
                    .start(1.0f);
        }

        private double getDiagonalSize() {
            return Math.sqrt(Math.pow(viewModel.getWidth(), 2) + Math.pow(viewModel.getHeight(), 2)) * 2;
        }

        private void drawRippleEffect(Graphics2D graphics2D){
            if (!isAnimationEnd){
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                graphics2D.setColor(rippleOpacityColor);
                graphics2D.fill(ellipse2D);
                graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                graphics2D.setClip(null);
            }
        }

        private Color createOpacityColor(){
            return new Color (rippleModel.getRippleColor().getRed() / 255f,
                    rippleModel.getRippleColor().getGreen() / 255f,
                    rippleModel.getRippleColor().getBlue() / 255f,
                    rippleModel.getRippleColorOpacity());
        }
    }
}
