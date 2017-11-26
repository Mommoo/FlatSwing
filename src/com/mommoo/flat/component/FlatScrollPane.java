package com.mommoo.flat.component;

import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.text.textarea.FlatTextArea;
import com.mommoo.util.ColorManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * Created by mommoo on 2017-03-11.
 */
public class FlatScrollPane extends JScrollPane {
    private Color themeColor = ColorManager.getColorAccent();

    public FlatScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        init();
    }

    public FlatScrollPane(Component view) {
        super(view);
        init();
    }

    public FlatScrollPane(int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
        init();
    }

    public FlatScrollPane() {
        super();
        init();
    }

    public static void main(String[] ags) {
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setPreferredSize(new Dimension(400, 400));
//
//        JPanel content = new JPanel();
//        content.setBackground(Color.WHITE);
//        content.setPreferredSize(new Dimension(500, 500));
//        ;
//        content.add(new JTextArea("testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest"));
//        frame.add(new ModernScrollPane(content));
//
//        frame.pack();
//        frame.setVisible(true);
        FlatScrollPane flatScrollPane = new FlatScrollPane();
//        FlatTextArea flatLabel = new FlatTextArea();
        JTextArea flatLabel = new JTextArea("testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest");
//        flatLabel.setLineWrap(false);
        flatLabel.setText("1\n2\n3\n4\n5\n6\n7\n8\n9\n1\n2\n3\n4\n5\n6\n7\n8\n9\n");
        flatLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        flatScrollPane.setViewportView(flatLabel);

        FlatFrame flatFrame = new FlatFrame();
        flatFrame.setTitle("FlatScrollPane Test");
        flatFrame.setSize(500,300);
        flatFrame.setLocationOnScreenCenter();
        flatFrame.getContainer().add(flatScrollPane);
        flatFrame.show();
        flatScrollPane.setThemeColor(Color.YELLOW);
    }

    public Color getThemeColor() {
        return this.themeColor;
    }

    public void setThemeColor(Color themeColor) {
        this.themeColor = themeColor;
        repaint();
    }

    public Color getVerticalScrollTrackColor() {
        return getVerticalScrollBar().getBackground();
    }

    public void setVerticalScrollTrackColor(Color trackColor) {
        getVerticalScrollBar().setOpaque(true);
        getVerticalScrollBar().setBackground(trackColor);
        repaint();
    }

    public Color getHorizontalScrollTrackColor() {
        return getHorizontalScrollBar().getBackground();
    }

    public void setHorizontalScrollTrackColor(Color trackColor) {
        getHorizontalScrollBar().setOpaque(true);
        getHorizontalScrollBar().setBackground(trackColor);
        repaint();
    }

    private void init() {
        setBorder(BorderFactory.createEmptyBorder());
        setVerticalScrollBar(new FlatScrollBar());
        setHorizontalScrollBar(new FlatScrollBar());
    }

    private class FlatScrollBar extends JScrollBar {

        FlatScrollBar() {
            setUI(new FlatScrollBarUI());
            setPreferredSize(new Dimension(10, 0));
            setOpaque(false);
        }
    }

    private class FlatScrollBarUI extends BasicScrollBarUI {

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color color;
            if (!c.isEnabled() || thumbBounds.width > thumbBounds.height) {
                return;
            } else if (isDragging) {
                color = themeColor.darker();
            } else if (isThumbRollover()) {
                color = themeColor.brighter();
            } else {
                color = themeColor;
            }

            g2.setPaint(color);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 5, 5);
            g2.dispose();
        }

        @Override
        protected void setThumbBounds(int x, int y, int width, int height) {
            super.setThumbBounds(x, y, width, height);
            scrollbar.repaint();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            Dimension zeroDim = new Dimension(0, 0);
            button.setPreferredSize(zeroDim);
            button.setMinimumSize(zeroDim);
            button.setMaximumSize(zeroDim);
            return button;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
//        @Override
//        protected JButton createIncreaseButton(int orientation) {
//            return new ArrowButton(orientation) {
//                @Override
//                protected void paintArrow(Graphics2D graphics2D,Dimension parentDimension, Path2D line) {
//                    double leftX = 0.0d, leftY = 0.0d;
//                    double topX = 0.0d,topY = 0.0d;
//                    double rightX = 0.0d ,rightY = 0.0d;
//                    double bottomX = 0.0d,bottomY = 0.0d;
//                    if(orientation == EAST){
//                        topX = parentDimension.width/4.0d;
//                        topY = parentDimension.height/4.0d;
//                        rightX = 3*parentDimension.width/4.0d;
//                        rightY = parentDimension.height/2.0d;
//                        bottomX = topX;
//                        bottomY = 3*parentDimension.height/4.0d;
//                        line.moveTo(topX,topY);
//                        line.lineTo(rightX,rightY);
//                        line.lineTo(bottomX,bottomY);
//                    }else if(orientation == SOUTH){
//                        leftX = parentDimension.width/4.0d;
//                        leftY = parentDimension.height/4.0d;
//                        bottomX = parentDimension.width/2.0d;
//                        bottomY = 3*parentDimension.height/4.0d;
//                        rightX = parentDimension.width - leftX;
//                        rightY = leftY;
//                        line.moveTo(leftX,leftY);
//                        line.lineTo(bottomX,bottomY);
//                        line.lineTo(rightX,rightY);
//                    }
//
//                    graphics2D.draw(line);
//                }
//            };
//        }
//
//        @Override
//        protected JButton createDecreaseButton(int orientation) {
//            return new ArrowButton(orientation) {
//                @Override
//                protected void paintArrow(Graphics2D graphics2D,Dimension parentDimension, Path2D line) {
//                    double leftX = 0.0d, leftY = 0.0d;
//                    double topX = 0.0d,topY = 0.0d;
//                    double rightX = 0.0d ,rightY = 0.0d;
//                    double bottomX = 0.0d,bottomY = 0.0d;
//                    if(orientation == WEST){
//                        leftX = parentDimension.width/4.0d;
//                        leftY = parentDimension.height/2.0d;
//                        topX = 3*parentDimension.width/4.0d;
//                        topY = parentDimension.height/4.0d;
//                        bottomX = topX;
//                        bottomY = 3*parentDimension.height/4.0d;
//                        line.moveTo(topX,topY);
//                        line.lineTo(leftX,leftY);
//                        line.lineTo(bottomX,bottomY);
//                    }else if(orientation == NORTH){
//                        leftX = parentDimension.width/4.0d;
//                        leftY = 3*parentDimension.height/4.0d;
//                        topX = parentDimension.width/2.0d;
//                        topY = parentDimension.height/4.0d;
//                        rightX = 3*parentDimension.width/4.0d;
//                        rightY = leftY;
//                        line.moveTo(leftX,leftY);
//                        line.lineTo(topX,topY);
//                        line.lineTo(rightX,rightY);
//                    }
//                    graphics2D.draw(line);
//                }
//            };
//        }
//
//        private abstract class ArrowButton extends JButton{
//            private final BasicStroke BASIC_STROKE = new BasicStroke(2.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
//
//            private ArrowButton(int orientation) {
//                setCursor(new Cursor(Cursor.HAND_CURSOR));
//                setBackground(Color.WHITE);
//                setBorderPainted(false);
//                setFocusPainted(false);
//                setModel(new DefaultButtonModel(){
//                    private static final long serialVersionUID = 1L;
//                    @Override
//                    public boolean isPressed() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRollover() {
//                        return false;
//                    }
//
//                    @Override
//                    public void setRollover(boolean b) {
//
//                    }
//                });
//            }
//
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                Dimension d = getSize();
//                final Path2D LINE = new Path2D.Double();
//                Graphics2D graphics2D = (Graphics2D)g;
//                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                graphics2D.setColor(themeColor.darker());
//                graphics2D.setStroke(BASIC_STROKE);
//                paintArrow(graphics2D,d,LINE);
//                LINE.closePath();
//            }
//
//            @Override
//            public Dimension getPreferredLabelSize() {
//                return new Dimension(16, 16);
//            }
//
//            protected abstract void paintArrow(Graphics2D graphics2D, Dimension parentDimension ,Path2D line);
//        }
    }
}
