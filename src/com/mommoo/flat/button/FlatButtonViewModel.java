package com.mommoo.flat.button;

import com.mommoo.animation.AnimationAdapter;
import com.mommoo.animation.Animator;
import com.mommoo.animation.timeInterpolator.AccelerateInterpolator;
import com.mommoo.flat.button.ripple.RippleModel;
import com.mommoo.flat.component.FlatMouseAdapter;

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

    FlatButtonViewModel(ButtonViewModel viewModel, RippleModel rippleModel) {
        this.viewModel = viewModel;
        this.rippleModel = rippleModel;
    }

    MouseAdapter getRippleAnimMouseListener() {
        return rippleMouseEventListener;
    }

    void paintRippleEffect(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rippleMouseEventListener.drawRipple(graphics2D);
    }

    void executeRippleEffect(Point startLocation, ActionEvent actionEvent){
        rippleMouseEventListener.userMouseLocation = startLocation;

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
        private Animator animator = new Animator();

        private int takenDuration = 0;
        private double radius;

        private Runnable animationEndListener = () -> {};

        private ActionEvent convertMouseEventToActionEvent(){
            MouseEvent mouseEvent = getMouseEvent();
            return new ActionEvent(mouseEvent.getSource(), mouseEvent.getID(), mouseEvent.paramString());
        }

        private void postDelay(Runnable runnable, int duration){
            Timer timer = new Timer(0, e ->{
                runnable.run();
                ((Timer)e.getSource()).stop();
            });
            timer.setInitialDelay(duration);
            timer.start();
        }

        private void animationReady() {
            takenDuration = 0;
            radius = 0;
            isAnimationEnd = false;
        }

        private void animationEnd() {
            radius = 0;
            isAnimationEnd = true;

            postDelay(() -> {
                viewModel.repaint();

                if (isMouseClicked() && rippleModel.isOnEventLaterEffect()){
                    executeEvent(convertMouseEventToActionEvent());
                }

                animationEndListener.run();

            }, rippleModel.getRippleHoldDuration());
        }

        private void executeEvent(ActionEvent actionEvent){
            SwingUtilities.invokeLater(()-> BUTTON_EVENT_REPOSITORY.getEventList().forEach(event -> event.accept(actionEvent)));
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            executePreRippleEffect();
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
                    .setTimeInterpolator(new AccelerateInterpolator())
                    .setDuration(rippleModel.getRippleDuration() * 2)
                    .setAnimationListener(new AnimationAdapter() {
                        private long startTime;

                        @Override
                        public void onStart() {
                            animationReady();
                            userMouseLocation = FlatAreaMouseLocation.getLocation(getMouseEvent().getComponent());
                            startTime = System.currentTimeMillis();
                        }

                        @Override
                        public void onAnimation(List<Double> resultList) {
                            radius = resultList.get(0);
                            ellipse2D.setFrame(userMouseLocation.getX() - radius,
                                    userMouseLocation.getY() - radius,
                                    radius * 2,
                                    radius * 2);
                            viewModel.repaint();
                        }

                        @Override
                        public void onStop() {
                            takenDuration = (int) ((System.currentTimeMillis() - startTime) / 2L);
                        }

                        @Override
                        public void onEnd() {
                            takenDuration = rippleModel.getRippleDuration();
                        }
                    })
                    .start(getDiagonalSize());
        }

        private void executePostRippleEffect(){
            animator.stop()
                    .setTimeInterpolator(new AccelerateInterpolator())
                    .setDuration(rippleModel.getRippleDuration() - takenDuration)
                    .setAnimationListener(new AnimationAdapter() {
                        final double previousRadius = radius;

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

        private double getDiagonalSize() {
            return Math.sqrt(Math.pow(viewModel.getWidth(), 2) + Math.pow(viewModel.getHeight(), 2));
        }

        private void drawRipple(Graphics2D graphics2D){
            if (!isAnimationEnd){
                graphics2D.setColor(createOpacityColor());
                graphics2D.fill(ellipse2D);
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
