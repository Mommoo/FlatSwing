package com.mommoo.flat.frame.titlebar.navigation.listener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseFocusAnimationListener extends MouseAdapter{
    private static final int FOCUS_GAIN_ANIMATION_TIME = 200;
    private static final int FOCUS_LOST_ANIMATION_TIME = 100;

    private Color focusGainColor, focusLostColor;
    private BackgroundAnimWorker focusGainWorker, focusLostWorker;

    private boolean isMouseExited;

    private FocusGainListener focusGainListener = ()->{};

    public MouseFocusAnimationListener(Component targetComponent, Color focusGainColor, Color focusLostColor){
        focusGainWorker = new BackgroundAnimWorker(targetComponent, FOCUS_GAIN_ANIMATION_TIME);
        focusLostWorker = new BackgroundAnimWorker(targetComponent, FOCUS_LOST_ANIMATION_TIME);
        setFocusAnimColor(focusGainColor, focusLostColor);
    }

    private void createColorCacheArray(float[] focusGainColorRGB, float[] focusLostColorRGB){
        float subR = focusGainColorRGB[0] - focusLostColorRGB[0];
        float subG = focusGainColorRGB[1] - focusLostColorRGB[1];
        float subB = focusGainColorRGB[2] - focusLostColorRGB[2];

        Color[] focusGainColorArray = new Color[100];
        Color[] focusLostColorArray = new Color[100];

        for (int i = 0 ; i <100 ; i++){
            focusGainColorArray[i] = new Color(focusGainColorRGB[0], focusGainColorRGB[1], focusGainColorRGB[2],0.01f * i);
            int ratio = 100 - i ;
            focusLostColorArray[i] = new Color(
                    focusGainColorRGB[0] - (subR/ratio),
                    focusGainColorRGB[1] - (subG/ratio),
                    focusGainColorRGB[2] - (subB/ratio));
        }

        focusGainWorker.setColorCacheArray(focusGainColorArray);
        focusLostWorker.setColorCacheArray(focusLostColorArray);
    }

    public void setFocusAnimColor(Color focusGainColor, Color focusLostColor){
        this.focusGainColor = focusGainColor;
        this.focusLostColor = focusLostColor;

        float[] focusGainColorRGB = new float[3];
        float[] focusLostColorRGB = new float[3];

        focusGainColor.getRGBColorComponents(focusGainColorRGB);
        focusLostColor.getRGBColorComponents(focusLostColorRGB);

        createColorCacheArray(focusGainColorRGB, focusLostColorRGB);
    }

    public void setFocusGainListener(FocusGainListener focusGainListener){
        this.focusGainListener = focusGainListener;
    }

    public void setFocusLostListener(FocusLostListener focusLostListener){
        this.focusLostWorker.setCallback(focusLostListener::lost);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        e.getComponent().setBackground(focusGainColor.darker());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (!isMouseExited) e.getComponent().setBackground(focusLostColor.darker());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        isMouseExited = false;
        focusGainListener.gain();
        focusLostWorker.stop();
        focusGainWorker.start();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        isMouseExited = true;
        focusGainWorker.stop();
        focusLostWorker.start();
    }

    private class BackgroundAnimWorker{
        private Component targetComponent;
        private int animationDuration;
        private Color[] colorCacheArray;
        private boolean workerStop;
        private Runnable callback = ()->{};

        private BackgroundAnimWorker(Component targetComponent, int animationDuration){
            this.targetComponent = targetComponent;
            this.animationDuration = animationDuration;
        }

        private void setColorCacheArray(Color[] colorCacheArray){
            this.colorCacheArray = colorCacheArray;
        }

        private void setCallback(Runnable callback){
            this.callback = callback;
        }

        private void start(){
            workerStop = false;

            final int COLOR_RANGE = colorCacheArray.length;

            new Thread(()->{

                for (Color color : colorCacheArray){

                    if (workerStop) break;

                    threadSleep(animationDuration /COLOR_RANGE);

                    targetComponent.setBackground(color);
                }

                this.callback.run();
            }).start();
        }

        private void stop(){
            workerStop = true;
        }

        private void threadSleep(int sleepMilliTime){
            try {
                Thread.sleep(sleepMilliTime);
            } catch (InterruptedException e) {}
        }
    }
}
