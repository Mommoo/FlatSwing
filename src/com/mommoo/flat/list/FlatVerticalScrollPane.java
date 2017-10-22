package com.mommoo.flat.list;

import com.mommoo.animation.AnimationAdapter;
import com.mommoo.animation.AnimationListener;
import com.mommoo.animation.Animator;
import com.mommoo.animation.timeInterpolator.AccelerateInterpolator;
import com.mommoo.flat.component.FlatScrollPane;
import com.mommoo.flat.text.textarea.FlatTextArea;
import com.mommoo.util.ColorManager;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

class FlatVerticalScrollPane<T extends Component> extends FlatScrollPane{
    private final FlatViewPort<T> VIEW_PORT = new FlatViewPort<>();
    private AutoScrollWorker autoScrollWorker = new AutoScrollWorker();

    private ComputeScrollWorker computeScrollWorker = new ComputeScrollWorker();

    FlatVerticalScrollPane(){
        setViewportView(VIEW_PORT);
        setHorizontalScrollBarPolicy(FlatScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setBorder(BorderFactory.createEmptyBorder());
        setThemeColor(ColorManager.getColorAccent());

        VIEW_PORT.setScrollListener(scrollSensitivity -> {
            if (scrollSensitivity == 0){
                autoScrollWorker.stop();
            } else {
                autoScrollWorker.setScrollSensitivity(scrollSensitivity).computeScroll();
            }
        });
    }


    void scrollByValue(int value){
        computeScrollWorker.scrollByValue(value);
    }

    void scrollByPosition(int position){
        computeScrollWorker.scrollByPosition(position);
    }

    void smoothScrollByValue(boolean relative, int value){
        computeScrollWorker.smoothScrollByValue(relative, value);
    }

    void smoothScrollByPosition(boolean relative, int position){
        computeScrollWorker.smoothScrollByPosition(relative, position);
    }

    int getScrollAnimationDuration(){
        return computeScrollWorker.getScrollAnimationDuration();
    }

    void setScrollAnimationDuration(int scrollAnimationDuration){
        computeScrollWorker.setScrollAnimationDuration(scrollAnimationDuration);
    }

    private void scroll(int value){
        getVerticalScrollBar().setValue(value);
    }

    private class AutoScrollWorker{
        private static final int FREQUENT_DURATION = 70;
        private int scrollSensitivity;
        private boolean isScrolling;

        private AutoScrollWorker(){ }

        private AutoScrollWorker setScrollSensitivity(int scrollSensitivity){
            this.scrollSensitivity = scrollSensitivity;
            return this;
        }

        private void sleep(){
            try {
                Thread.sleep(FREQUENT_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void computeScroll(){
            if (isScrolling) return;

            isScrolling = true;

            new SwingWorker<Void,Void>(){
                private JScrollBar verticalScrollBar = getVerticalScrollBar();

                @Override
                protected Void doInBackground() throws Exception {
                    while (isScrolling){
                        publish();
                        sleep();
                    }
                    return null;
                }

                @Override
                protected void process(List<Void> chunks) {
                    verticalScrollBar.setValue(verticalScrollBar.getValue() + scrollSensitivity);
                    VIEW_PORT.dispatchEvent(createMouseDragEvent());
                }

                private MouseEvent createMouseDragEvent(){
                    Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                    Point panelLocation = VIEW_PORT.getLocationOnScreen();

                    return new MouseEvent(VIEW_PORT,
                            MouseEvent.MOUSE_DRAGGED,
                            System.currentTimeMillis(),
                            MouseEvent.BUTTON1_DOWN_MASK,
                            mouseLocation.x - panelLocation.x,
                            mouseLocation.y - panelLocation.y,
                            0,
                            false);
                }
            }.execute();
        }

        private void stop(){
            isScrolling = false;
        }
    }

    private class ComputeScrollWorker {
        private int scrollAnimationDuration = 500;

        private int getScrollAnimationDuration(){
            return this.scrollAnimationDuration;
        }

        private void setScrollAnimationDuration(int scrollAnimationDuration){
            if (scrollAnimationDuration <= 0) return;
            this.scrollAnimationDuration = scrollAnimationDuration;
        }

        private int getScrollValueByPosition(int position){
            Component targetComp = VIEW_PORT.getComponent(position);
            return targetComp.getY();
        }

        private void scrollByValue(int value){
            if (value < 0) return;

            SwingUtilities.invokeLater(()-> scroll(value));
        }

        private void smoothScrollByValue(boolean relative, int value){
            if (value < 0) return;

            int originValue = getVerticalScrollBar().getValue();
            double targetValue = relative ? value - originValue : value;

            SwingUtilities.invokeLater(() -> new Animator()
                    .setTimeInterpolator(new AccelerateInterpolator())
                    .setAnimationListener(new AnimationAdapter() {
                        @Override
                        public void onAnimation(List<Double> resultList) {
                            int value = relative ? originValue + resultList.get(0).intValue() : resultList.get(0).intValue();
                            scroll(value);
                        }

                        @Override public void onEnd() { }
                    })
                    .setDuration(500)
                    .start(targetValue));
        }

        private void scrollByPosition(int position){
            if (position < 0) return;

            SwingUtilities.invokeLater(()-> {
                int targetPosition = position;

                if (VIEW_PORT.getItemSize() == 0) return;

                if (position >= VIEW_PORT.getItemSize()) {
                    targetPosition = VIEW_PORT.getItemSize() - 1;
                }
                scrollByValue(getScrollValueByPosition(targetPosition));
            });
        }

        private void smoothScrollByPosition(boolean relative, int position){
            if (position < 0) return;

            SwingUtilities.invokeLater(()-> {
                int targetPosition = position;

                if (VIEW_PORT.getItemSize() == 0) return;

                if (position >= VIEW_PORT.getItemSize()) {
                    targetPosition = VIEW_PORT.getItemSize() - 1;
                }

                smoothScrollByValue(relative, getScrollValueByPosition(targetPosition));
            });
        }
    }
}
