package flat.component;

import flat.frame.FlatFrame;
import flat.label.FlatLabel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by mommoo on 2017-03-11.
 */
public class FlatScrollPane extends JScrollPane {
    private static final Color TRACK_COLOR = new Color(243, 243, 243);
    private Color themeColor = Color.GRAY;

    private final FlatScrollBarUI VERTICAL_SCROLL_BAR = new FlatScrollBarUI(themeColor);
    private final FlatScrollBarUI HORIZONTAL_SCROLL_BAR = new FlatScrollBarUI(themeColor);

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

    private void init(){
        getVerticalScrollBar().setUI(VERTICAL_SCROLL_BAR);
        getHorizontalScrollBar().setUI(HORIZONTAL_SCROLL_BAR);
    }

    public void setThemeColor(Color themeColor){
        this.themeColor = themeColor;
        VERTICAL_SCROLL_BAR.setThemeColor(themeColor);
        HORIZONTAL_SCROLL_BAR.setThemeColor(themeColor);
    }

    public Color getThemeColor(){
        return this.themeColor;
    }


    private static class FlatScrollBarUI extends BasicScrollBarUI{

        private final RoundRectangle2D RECT = new RoundRectangle2D.Double();
        private Color themeColor;

        private FlatScrollBarUI(Color themeColor){
            this.themeColor = themeColor.brighter();
        }

        private void setThemeColor(Color color){
            this.themeColor = color.brighter();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(TRACK_COLOR);
            Graphics2D g2 = (Graphics2D)g;
            g2.fill(trackBounds);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            g.setColor(themeColor.darker());
            Graphics2D g2 = (Graphics2D)g;
            double padding = 2.0d;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(TRACK_COLOR);
            g2.fillRect(thumbBounds.x,thumbBounds.y,thumbBounds.width,thumbBounds.height);
            RECT.setRoundRect(thumbBounds.x + padding,thumbBounds.y + padding,  thumbBounds.width - padding*2, thumbBounds.height - padding*2,20.0d,20.0d);
            g.setColor(themeColor);
            g2.fill(RECT);
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return new ArrowButton(orientation) {
                @Override
                protected void paintArrow(Graphics2D graphics2D,Dimension parentDimension, Path2D line) {
                    double leftX = 0.0d, leftY = 0.0d;
                    double topX = 0.0d,topY = 0.0d;
                    double rightX = 0.0d ,rightY = 0.0d;
                    double bottomX = 0.0d,bottomY = 0.0d;
                    if(orientation == EAST){
                        topX = parentDimension.width/4.0d;
                        topY = parentDimension.height/4.0d;
                        rightX = 3*parentDimension.width/4.0d;
                        rightY = parentDimension.height/2.0d;
                        bottomX = topX;
                        bottomY = 3*parentDimension.height/4.0d;
                        line.moveTo(topX,topY);
                        line.lineTo(rightX,rightY);
                        line.lineTo(bottomX,bottomY);
                    }else if(orientation == SOUTH){
                        leftX = parentDimension.width/4.0d;
                        leftY = parentDimension.height/4.0d;
                        bottomX = parentDimension.width/2.0d;
                        bottomY = 3*parentDimension.height/4.0d;
                        rightX = parentDimension.width - leftX;
                        rightY = leftY;
                        line.moveTo(leftX,leftY);
                        line.lineTo(bottomX,bottomY);
                        line.lineTo(rightX,rightY);
                    }

                    graphics2D.draw(line);
                }
            };
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return new ArrowButton(orientation) {
                @Override
                protected void paintArrow(Graphics2D graphics2D,Dimension parentDimension, Path2D line) {
                    double leftX = 0.0d, leftY = 0.0d;
                    double topX = 0.0d,topY = 0.0d;
                    double rightX = 0.0d ,rightY = 0.0d;
                    double bottomX = 0.0d,bottomY = 0.0d;
                    if(orientation == WEST){
                        leftX = parentDimension.width/4.0d;
                        leftY = parentDimension.height/2.0d;
                        topX = 3*parentDimension.width/4.0d;
                        topY = parentDimension.height/4.0d;
                        bottomX = topX;
                        bottomY = 3*parentDimension.height/4.0d;
                        line.moveTo(topX,topY);
                        line.lineTo(leftX,leftY);
                        line.lineTo(bottomX,bottomY);
                    }else if(orientation == NORTH){
                        leftX = parentDimension.width/4.0d;
                        leftY = 3*parentDimension.height/4.0d;
                        topX = parentDimension.width/2.0d;
                        topY = parentDimension.height/4.0d;
                        rightX = 3*parentDimension.width/4.0d;
                        rightY = leftY;
                        line.moveTo(leftX,leftY);
                        line.lineTo(topX,topY);
                        line.lineTo(rightX,rightY);
                    }
                    graphics2D.draw(line);
                }
            };
        }

        private abstract class ArrowButton extends JButton{
            private final BasicStroke BASIC_STROKE = new BasicStroke(2.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);

            private ArrowButton(int orientation) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBackground(Color.WHITE);
                setBorderPainted(false);
                setFocusPainted(false);
                setModel(new DefaultButtonModel(){
                    private static final long serialVersionUID = 1L;
                    @Override
                    public boolean isPressed() {
                        return false;
                    }

                    @Override
                    public boolean isRollover() {
                        return false;
                    }

                    @Override
                    public void setRollover(boolean b) {

                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Dimension d = getSize();
                final Path2D LINE = new Path2D.Double();
                Graphics2D graphics2D = (Graphics2D)g;
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(themeColor.darker());
                graphics2D.setStroke(BASIC_STROKE);
                paintArrow(graphics2D,d,LINE);
                LINE.closePath();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(16, 16);
            }

            protected abstract void paintArrow(Graphics2D graphics2D, Dimension parentDimension ,Path2D line);
        }
    }

    public static void main(String[] ags){
        FlatScrollPane flatScrollPane = new FlatScrollPane();
        FlatLabel flatLabel = new FlatLabel();
        flatLabel.setText("1\n2\n3\n4\n5\n6\n7\n8\n9\n1\n2\n3\n4\n5\n6\n7\n8\n9\n");
        flatLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        flatScrollPane.setViewportView(flatLabel);

        FlatFrame flatFrame = new FlatFrame();
        flatFrame.setTitle("FlatScrollPane Test");
        flatFrame.setSize(500,300);
        flatFrame.setLocationOnScreenCenter();
        flatFrame.getContainer().add(flatScrollPane);
        flatFrame.show();
    }
}
