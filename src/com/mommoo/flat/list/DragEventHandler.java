package com.mommoo.flat.list;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.IntConsumer;

class DragEventHandler<T extends Component> {
    private int beginIndex = 0;
    private int detectedIndex = 0;
    private MouseAdapter mouseDragEventListener;

    void addComponent(Component component){

    }

//    DragEventHandler(CompIndexList<T> compIndexList){
//        this.compIndexList = compIndexList;
//    }

    void dragStart(int beginIndex){
        this.beginIndex = beginIndex;
        this.detectedIndex = beginIndex;
    }

    private void unDetectedIndexDragTask(int currentDetectedIndex, IntConsumer consumer){
        boolean isMouseDragDown = currentDetectedIndex > detectedIndex;

        if (isMouseDragDown) unDetectedIndexDoDownDragTask(currentDetectedIndex, consumer);
        else unDetectedIndexDoUpDragTask(currentDetectedIndex, consumer);
    }

    private void unDetectedIndexDoDownDragTask(int currentDetectedIndex, IntConsumer consumer){

        for (int i = this.detectedIndex ; i < currentDetectedIndex ; i++){
            consumer.accept(i);
        }

        this.detectedIndex = currentDetectedIndex;
    }

    private void unDetectedIndexDoUpDragTask(int currentDetectedIndex, IntConsumer consumer){

        for (int i = this.detectedIndex ; i > currentDetectedIndex ; i--){
            consumer.accept(i);
        }

        this.detectedIndex = currentDetectedIndex;
    }

    MouseAdapter getMouseDragEventListener(){
        if (mouseDragEventListener == null){
            mouseDragEventListener = new MouseDragEventListener();
        }

        return mouseDragEventListener;
    }

    private class DragEventComponent {
        private final Component component;
        private final Color originalBackgroundColor;
        private final Color originalForegroundColor;
        private int position;

        private DragEventComponent(Component component, int position){
            this.component = component;
            this.originalBackgroundColor = component.getBackground();
            this.originalForegroundColor = component.getForeground();
            this.position = position;
        }
    }

    private class MouseDragEventListener extends MouseAdapter{
        private boolean isMouseDragging;
        private Window parentWindow;


//        @Override
//        public void mouseEntered(MouseEvent e) {
//            if (!isMouseDragging) return;
//
//            int currentIndex = compIndexList.getIndex(e.getComponent());
//
//            boolean isMouseDragDown = currentIndex > detectedIndex;
//            boolean isPositiveDirection = (beginIndex < currentIndex && isMouseDragDown) || (beginIndex > currentIndex && !isMouseDragDown);
//
//            if (isPositiveDirection){
//
//                IntConsumer consumer = index -> compIndexList.peek(index).setBackground(Color.RED);
//
//                if (isMouseDragDown) unDetectedIndexDoDownDragTask(currentIndex, consumer);
//                else unDetectedIndexDoUpDragTask(currentIndex, consumer);
//
//                compIndexList.peek(currentIndex).setBackground(Color.RED);
//
//            } else {
//
//                IntConsumer consumer = index -> compIndexList.peek(index).setBackground(Color.WHITE);
//
//                if (isMouseDragDown) {
//                    unDetectedIndexDoDownDragTask(currentIndex, consumer);
//                }else {
//                    unDetectedIndexDoUpDragTask(currentIndex, consumer);
//                }
//
//                detectedIndex = currentIndex;
//            }
//        }

        @Override
        public void mouseReleased(MouseEvent e) {
            this.isMouseDragging = false;
        }

//        @Override
//        public void mouseDragged(MouseEvent e) {
//            if (!this.isMouseDragging) {
//
//                for (int i = 0, size = compIndexList.getSize(); i < size; i++){
//                    compIndexList.peek(i).setBackground(Color.WHITE);
//                }
//
//                dragStart(compIndexList.getIndex(e.getComponent()));
//                this.isMouseDragging = true;
//                e.getComponent().setBackground(Color.RED);
//
//            }
//
//            if (parentWindow == null) parentWindow = SwingUtilities.getWindowAncestor(e.getComponent());
//
//            if (parentWindow.getLocation().y + parentWindow.getHeight() < e.getYOnScreen()){
//                System.out.println("바깥으로 나감" + parentWindow.getLocation());
//            } else {
//                System.out.println("핵 인싸");
//            }
//
//            System.out.println(e.getYOnScreen());
//
//        }
    }
}
