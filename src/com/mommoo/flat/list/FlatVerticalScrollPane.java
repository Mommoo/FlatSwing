package com.mommoo.flat.list;

import com.mommoo.animation.AnimationListener;
import com.mommoo.animation.Animator;
import com.mommoo.animation.timeInterpolator.AccelerateInterpolator;
import com.mommoo.flat.component.FlatScrollPane;
import com.mommoo.util.ColorManager;

import javax.swing.*;
import java.awt.*;
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
            }

            else {
                autoScrollWorker
                        .setScrollSensitivity(scrollSensitivity)
                        .computeScroll();
            }

        });
    }

    void scrollByValue(int value){
        computeScrollWorker.scrollByValue(value);
    }

    void scrollByPosition(int position){
        computeScrollWorker.scrollByPosition(position);
    }

    void smoothScrollByValue(int value){
        computeScrollWorker.smoothScrollByValue(value);
    }

    void smoothScrollByPosition(int position){
        computeScrollWorker.smoothScrollByPosition(position);
    }

    @Override
    public void paint(Graphics g) {
        computeScrollWorker.executeTask();
        super.paint(g);
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
        private Runnable afterDrawTask = ()->{};

        private boolean isVisible(){
            return getWidth() != 0;
        }

        private void executeTaskIfVisible(){
            if (isVisible()){
                executeTask();
            }
        }

        private int getScrollValueByPosition(int position){
            Component targetComp = VIEW_PORT.getComponent(position);
            return targetComp.getY() + targetComp.getHeight();
        }

        private void scrollByValue(int value){
            if (value < 0) return;

            afterDrawTask = ()-> scroll(value);
            executeTaskIfVisible();

        }

        private void smoothScrollByValue(int value){
            if (value < 0) return;

            afterDrawTask = () -> new Animator()
                    .setTimeInterpolator(new AccelerateInterpolator())
                    .setAnimationListener(new AnimationListener() {
                        @Override public void onStart() { }

                        @Override
                        public void onAnimation(List<Double> resultList) {
                            scroll(resultList.get(0).intValue());
                        }

                        @Override public void onEnd() { }
                    })
                    .setDuration(500)
                    .start(value);

            executeTaskIfVisible();
        }

        private void scrollByPosition(int position){
            if (position < 0) return;

            afterDrawTask = ()-> {
                if (position >= VIEW_PORT.getItemSize()) return;
                scrollByValue(getScrollValueByPosition(position));
            };

            executeTaskIfVisible();
        }

        private void smoothScrollByPosition(int position){
            if (position < 0) return;

            afterDrawTask = ()-> {
                if (position >= VIEW_PORT.getItemSize()) return;
                smoothScrollByValue(getScrollValueByPosition(position));
            };

            executeTaskIfVisible();
        }

        private void executeTask() {
            afterDrawTask.run();
            afterDrawTask = ()->{};
        }
    }
}
