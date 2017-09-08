package com.mommoo.flat.list.content;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.component.FlatScrollPane;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.util.ComputableDimension;

import javax.swing.*;
import java.awt.*;

public class OverlapContentView extends FlatScrollPane{
    private final ViewPortPanel VIEW_PORT_PANEL = new ViewPortPanel();
    private FlatPanel flatPanel = new FlatPanel();

    public OverlapContentView(){
//        VIEW_PORT_PANEL.setOpaque(true);
//        VIEW_PORT_PANEL.setBackground(Color.BLUE);
        //flatPanel.setLayout(new LinearLayout(Orientation.VERTICAL, 0 ));
        setViewportView(VIEW_PORT_PANEL);
        setHorizontalScrollBarPolicy(FlatScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    }

    public void addComponent(Component comp){
        VIEW_PORT_PANEL.panel.add(comp, new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT));
//        flatPanel.add(comp, new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT));
//        VIEW_PORT_PANEL.viewPortView.addComponent(comp);
    }

    public void removeComponent(int index){
//        VIEW_PORT_PANEL.viewPortView.removeComponent(index);
    }

    public void setDivider(Color color, int thick){
//        VIEW_PORT_PANEL.viewPortView.setDivider(color, thick);
    }

    private class ViewPortPanel extends JLayeredPane implements Scrollable {

        private static final int scrollableUnit = 30;
        boolean once;
        private ComputableDimension previousDimen = new ComputableDimension();
        private FlatPanel panel = new FlatPanel();
//        private ViewPortView viewPortView = new ViewPortView();
        private FlatPanel glass = new FlatPanel();

        private ViewPortPanel(){
//            setOpaque(true);
//            setBackground(Color.RED);
            setPreferredSize(new Dimension(500,500));
//            setLayout(null);
//            glass.setOpaque(true);
//            glass.setBackground(Color.RED);
//            add(glass);
//            setLayout(new BorderLayout());
//            setSize(500,500);
//            panel.setPreferredSize(new Dimension(500,500));
            panel.setBounds(0,0,500,500);
            add(panel, new Integer(50));
            panel.setLayout(new LinearLayout(Orientation.VERTICAL,0));

            JPanel panel = new JPanel();
//            panel.setBackground(Color.RED);
//            panel.setBackground(new Color(0.1f, 0.1f, 0.1f, 0.1f));
            panel.setBackground(new Color(0.4f, 0.4f, 0.4f, 0.3f));
//            panel.setPreferredSize(this.panel.getPreferredSize());
//            panel.setSize(this.panel.getPreferredSize());
            add(panel, new Integer(51));
            panel.setBounds(1,1,500,500);

//            setLayer(this.panel, 3);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
//            System.out.println(getSize());

            if (once) return;

            once = true;



//            this.setComponentZOrder(panel, 1);

            //panel.setBackground(Color.RED);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return null;
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return scrollableUnit;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return scrollableUnit;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

        void computeSize(Dimension dimension){
//            setMinimumSize(dimension);
//            setSize(dimension.width, 667);
            setPreferredSize(new Dimension(dimension.width, 1000));
//            viewPortView.CONTENT_PANEL.revalidate();
//            viewPortView.CONTENT_PANEL.repaint();
        }
    }
}
