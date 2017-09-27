package com.mommoo.flat.list;

import com.mommoo.flat.component.FlatScrollPane;
import com.mommoo.util.ColorManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

class FlatVerticalScrollPane<T extends Component> extends FlatScrollPane{
    private final FlatViewPort<T> VIEW_PORT = new FlatViewPort<>();
    private AutoScrollWorker autoScrollWorker = new AutoScrollWorker();

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
                    System.out.println("mouseEvent");
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

}
