package com.mommoo.flat.component.pager;

import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.text.textarea.alignment.FlatHorizontalAlignment;
import com.mommoo.flat.text.textarea.alignment.FlatVerticalAlignment;
import com.mommoo.util.FastGaussianBlur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.List;

public class FlatPage{
    private Viewport viewport = new Viewport();
    private List<OnPageEventListener> pageEventList = new ArrayList<>();

    public JPanel getView(){
        return viewport.getView();
    }

    public FlatPage(){

    }

    public FlatPage(JPanel view){
        this();
        setView(view);
    }

    public FlatPage setView(JPanel view){
        viewport.setView(view);
        return this;
    }

    Component getViewport(){
        return viewport;
    }

    List<OnPageEventListener> getPageEventList(){
        return pageEventList;
    }

    public boolean isEnable(){
        return !viewport.isGlassPaneVisible();
    }

    public FlatPage setEnable(boolean enable){
        viewport.setGlassPaneVisible(!enable);
        return this;
    }

    public String getDisableGuideText(){
        return viewport.getGuideLabel().getText();
    }

    public FlatPage setDisableGuideText(String disableGuideText){
        viewport.getGuideLabel().setText(disableGuideText);
        return this;
    }

    public Font getDisableGuideTextFont(){
        return viewport.getGuideLabel().getFont();
    }

    public FlatPage setDisableGuideTextFont(Font guideTextFont){
        viewport.getGuideLabel().setFont(guideTextFont);
        return this;
    }

    public FlatPage setDisableGuideTextColor(Color textColor){
        return this;
    }

    protected FlatPage addOnPageEventListener(OnPageEventListener onPageEventListener){
        pageEventList.add(onPageEventListener);
        return this;
    }

    protected FlatPage removeOnPageEventListener(int index){
        pageEventList.remove(index);
        return this;
    }

    protected OnPageEventListener getPageEventListener(int index){
        return pageEventList.get(index);
    }

    private static class Viewport extends JLayeredPane{
        private static final Integer GLASS_PANE_DEPTH = 300;
        private static final Integer CONTENT_DEPTH = 100;

        private Viewport(){
            setLayout(null);
            add(new ViewportGlassPane(), GLASS_PANE_DEPTH);
            add(new JPanel(), CONTENT_DEPTH);
            setGlassPaneVisible(false);
        }

        @Override
        public void paint(Graphics g) {
            for (Component comp : getComponents()){
                comp.setBounds(0,0,getWidth(),getHeight());
                comp.doLayout();
                comp.revalidate();
            }
            super.paint(g);
        }

        private JPanel getView(){
            return (JPanel) getComponent(1);
        }

        private void setView(JPanel panel){
            remove(1);
            add(panel, CONTENT_DEPTH);
        }

        private boolean isGlassPaneVisible(){
            return getComponent(0).isVisible();
        }

        private void setGlassPaneVisible(boolean visible){
            getComponent(0).setVisible(visible);
        }

        private FlatLabel getGuideLabel(){
            return ((FlatLabel)((JPanel)getComponent(0)).getComponent(0));
        }

        private static class ViewportGlassPane extends JPanel{
            private static final Color GLASS_COLOR = new Color(255,255,255, 170);

            private ViewportGlassPane(){
                setOpaque(false);
                setLayout(new BorderLayout());
                add(createGuideLabel());
                addMouseListener(createMouseBubbleEventBlocker());
            }

            private Component createGuideLabel(){
                FlatLabel guideLabel = new FlatLabel();
                guideLabel.setOpaque(false);
                guideLabel.setHorizontalAlignment(FlatHorizontalAlignment.CENTER);
                guideLabel.setVerticalAlignment(FlatVerticalAlignment.CENTER);
                return guideLabel;
            }

            @Override
            public void paint(Graphics g) {
                g.setColor(GLASS_COLOR);
                g.fillRect(0,0,getWidth(), getHeight());
                super.paint(g);

            }

            private MouseListener createMouseBubbleEventBlocker(){
                return new MouseAdapter(){
                    @Override
                    public void mousePressed(MouseEvent e) {
                        e.consume();
                    }
                };
            }
        }
    }
}
