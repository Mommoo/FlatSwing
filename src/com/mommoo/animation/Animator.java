package com.mommoo.animation;

import com.mommoo.animation.timeInterpolator.AnticipateInterpolator;
import com.mommoo.animation.timeInterpolator.LinearTimeInterpolator;
import com.mommoo.animation.timeInterpolator.TimeInterpolator;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.text.textarea.FlatTextArea;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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

    private long time;

    public Animator setDelay(int delay){
        this.delay = delay;
        return this;
    }

    public int getDelay(){
        return delay;
    }

    public Animator setDuration(int duration){
        this.duration = duration;
        return this;
    }

    public int getDuration(){
        return duration;
    }

    public Animator setTimeInterpolator(TimeInterpolator timeInterpolator){
        this.timeInterpolator = timeInterpolator;
        return this;
    }

    public TimeInterpolator getTimeInterpolator() {
        return timeInterpolator;
    }

    public Animator setAnimationListener(AnimationListener animationListener){
        this.animationListener = animationListener;
        return this;
    }

    public AnimationListener getAnimationListener() {
        return animationListener;
    }

    public static void main(String[] args){
        FlatFrame frame = new FlatFrame();
        frame.setSize(ScreenManager.getInstance().getScreenWidth(), ScreenManager.getInstance().getScreenHeight());
        JButton button = new JButton("바튼");
        JPanel contentPane = new JPanel(){
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                System.out.println("foo");
            }
        };
        frame.getContainer().setLayout(new LinearLayout(Orientation.VERTICAL,10));
        frame.getContainer().add(button);
        frame.getContainer().add(new FlatTextArea("1: import java.awt.*;\n" +
                " 2:\n" +
                " 3: public class Animate extends javax.swing.JApplet\n" +
                " 4:   implements Runnable {\n" +
                " 5:\n" +
                " 6:   Image[] picture = new Image[6];\n" +
                " 7:   int totalPictures = 0;\n" +
                " 8:   int current = 0;\n" +
                " 9:   Thread runner;\n" +
                "10:   int pause = 500;\n" +
                "11:\n" +
                "12:   public void init() {\n" +
                "13:     for (int i = 0; i < 6; i++) {\n" +
                "14:       String imageText = null;\n" +
                "15:       imageText = getParameter(\"image\"+i);\n" +
                "16:       if (imageText != null) {\n" +
                "17:         totalPictures++;\n" +
                "18:         picture[i] = getImage(getCodeBase(), imageText);\n" +
                "19:       } else\n" +
                "20:         break;\n" +
                "21:     }\n" +
                "22:     String pauseText = null;\n" +
                "23:     pauseText = getParameter(\"pause\");\n" +
                "24:     if (pauseText != null) {\n" +
                "25:       pause = Integer.parseInt(pauseText);\n" +
                "26:     }\n" +
                "27:   }\n" +
                "28:\n" +
                "29:   public void paint(Graphics screen) {\n" +
                "30:     super.paint(screen);\n" +
                "31:     Graphics2D screen2D = (Graphics2D) screen;\n" +
                "32:     if (picture[current] != null)\n" +
                "33:       screen2D.drawImage(picture[current], 0, 0, this);\n" +
                "34:   }\n" +
                "35:\n" +
                "36:   public void start() {\n" +
                "37:     if (runner == null) {\n" +
                "38:       runner = new Thread(this);\n" +
                "39:       runner.start();\n" +
                "40:     }\n" +
                "41:   }\n" +
                "42:\n" +
                "43:   public void run() {\n" +
                "44:     Thread thisThread = Thread.currentThread();\n" +
                "45:     while (runner == thisThread) {\n" +
                "46:       repaint();\n" +
                "47:       current++;\n" +
                "48:       if (current >= totalPictures)\n" +
                "49:         current = 0;\n" +
                "50:       try {\n" +
                "51:         Thread.sleep(pause);\n" +
                "52:       } catch (InterruptedException e) { }\n" +
                "53:     }\n" +
                "54:   }\n" +
                "55:\n" +
                "56:   public void stop() {\n" +
                "57:     if (runner != null) {\n" +
                "58:       runner = null;\n" +
                "59:     }\n" +
                "60:   }\n" +
                "61: }"));
//        frame.setLocationOnScreenCenter();
        frame.show();
        button.addActionListener(e-> new Animator()
                .setTimeInterpolator(new AnticipateInterpolator())
                .setAnimationListener(new AnimationListener() {
                    private final Component comp = frame.getContainer();
                    private final int pick = comp.getX();
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onAnimation(List<Double> resultList) {
                        comp.setLocation(pick + resultList.get(0).intValue(), comp.getY());
                        Toolkit.getDefaultToolkit().sync();
                    }

                    @Override
                    public void onEnd() {
                        comp.setLocation(0,50);
                    }

                    @Override
                    public void onStop() {

                    }
                })
                .setDelay(1000)
                .setDuration(1000)
                .start(500));

    }

    public void start(double... elements){
        takenTime = -1;

        animatorTimer = new Timer(INTER_ACTION_DURATION, e -> {

            if (takenTime == -1){
                animationListener.onStart();
                time = System.currentTimeMillis();
            }

            takenTime = (int)(System.currentTimeMillis() - time);

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
        animatorTimer.setCoalesce(true);
        animatorTimer.start();
    }


    public Animator stop(){
        if (animatorTimer != null && animatorTimer.isRunning()){
            animationListener.onStop();
            animatorTimer.stop();
        }

        return this;
    }

    public boolean isRunning(){
        return animatorTimer != null && animatorTimer.isRunning();
    }
}
