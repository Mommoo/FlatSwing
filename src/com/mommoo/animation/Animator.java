package com.mommoo.animation;

import com.mommoo.animation.timeInterpolator.AccelerateInterpolator;
import com.mommoo.animation.timeInterpolator.LinearTimeInterpolator;
import com.mommoo.animation.timeInterpolator.TimeInterpolator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Animator {
    private static final int INTER_ACTION_DURATION = 10;
    private Timer animatorTimer;

    private int delay;
    private int duration = 500;
    private TimeInterpolator timeInterpolator = new LinearTimeInterpolator();
    private AnimationListener animationListener = new AnimationListener() {
        @Override public void onStart() { }
        @Override public void onAnimation(List<Double> resultList) { }
        @Override public void onEnd() {}
        @Override public void onStop() { }
    };

    private int takenTime;

    public static void main(String[] args){
        SwingUtilities.invokeLater(()-> new Animator()
                .setTimeInterpolator(new AccelerateInterpolator())
                .setAnimationListener(new AnimationListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onAnimation(List<Double> resultList) {
                        System.out.println(resultList);
                    }

                    @Override
                    public void onEnd() {

                    }

                    @Override
                    public void onStop() {

                    }
                })
                .setDelay(1000)
                .setDuration(1000)
                .start(1000));

    }

    public Animator setDelay(int delay){
        this.delay = delay;
        return this;
    }

    public Animator setDuration(int duration){
        this.duration = duration;
        return this;
    }

    public Animator setTimeInterpolator(TimeInterpolator timeInterpolator){
        this.timeInterpolator = timeInterpolator;
        return this;
    }

    public Animator setAnimationListener(AnimationListener animationListener){
        this.animationListener = animationListener;
        return this;
    }

    public void start(double... elements){
        takenTime = 0;

        animatorTimer = new Timer(INTER_ACTION_DURATION, e -> {
            takenTime += INTER_ACTION_DURATION;

            if (takenTime == INTER_ACTION_DURATION){
                animationListener.onStart();
            }

            double percent = Math.min(1.0, (double) takenTime / duration);

            double factor = timeInterpolator.getFactor(percent);

            ArrayList<Double> list = new ArrayList<>();
            for (double element : elements){
                list.add(factor * element);
            }

            animationListener.onAnimation(list);

            if (takenTime >= duration) {
                SwingUtilities.invokeLater(()->{
                    animatorTimer.stop();
                    animationListener.onEnd();
                });
            }
        });

        animatorTimer.setInitialDelay(delay);
        animatorTimer.start();
    }

    public Animator stop(){
        if (animatorTimer != null && animatorTimer.isRunning()){
            animatorTimer.stop();
            animationListener.onStop();
        }

        return this;
    }

    public boolean isRunning(){
        return animatorTimer != null && animatorTimer.isRunning();
    }
}
