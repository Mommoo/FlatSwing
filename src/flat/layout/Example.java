package flat.layout;

import flat.frame.FlatFrame;
import flat.layout.linear.LinearLayout;
import flat.layout.linear.Orientation;
import flat.layout.linear.constraints.LinearConstraints;
import flat.layout.linear.constraints.LinearSpace;

import javax.swing.*;
import java.awt.*;

public class Example {
    public static void main(String[] args){
        FlatFrame frame = new FlatFrame();
        frame.setSize(500,500 + FlatFrame.getTitleBarHeight());
        frame.setTitle("Flat layout test example");
        frame.setLocationOnScreenCenter();
        frame.setResizable(true);
        frame.getContainer().add(new LinearLayoutPanel2());
        frame.show();
    }

    private static class LinearLayoutPanel extends JPanel {
        private LinearLayoutPanel(){
            setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            setLinearLayout();
            addComponent();
        }

        private void setLinearLayout(){
            LinearLayout linearLayout = new LinearLayout();
            linearLayout.setOrientation(Orientation.VERTICAL);
            linearLayout.setGap(15);
            setLayout(linearLayout);
        }

        private void addComponent(){
            LinearConstraints linearConstraints = new LinearConstraints();
            linearConstraints.setWeight(1);
            linearConstraints.setLinearSpace(LinearSpace.MATCH_PARENT);
            add(createColorRect(Color.RED, "FIRST"), linearConstraints);
            add(createColorRect(Color.GREEN, "SECOND"), new LinearConstraints().setWeight(10).setLinearSpace(LinearSpace.MATCH_PARENT));
            add(createColorRect(Color.YELLOW, "THIRD"), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            add(createColorRect(Color.PINK, "FORTH"), new LinearConstraints().setWeight(3).setLinearSpace(LinearSpace.MATCH_PARENT));
        }
    }

    private static class LinearLayoutPanel2 extends JPanel{
        private LinearLayoutPanel2(){
            setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            setLinearLayout();
            LinearConstraints linearConstraints = new LinearConstraints();
            JPanel panel = createRowPanel(Color.RED, Color.WHITE, Color.BLUE);
            panel.setBackground(Color.red);
            add(panel, linearConstraints.setWeight(3).setLinearSpace(LinearSpace.MATCH_PARENT));
            add(createRowPanel(Color.PINK, Color.GRAY, Color.YELLOW), linearConstraints.setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            add(createRowPanel(Color.GREEN, Color.ORANGE, Color.MAGENTA), linearConstraints.setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        }

        private void setLinearLayout(){
            LinearLayout linearLayout = new LinearLayout();
            linearLayout.setOrientation(Orientation.VERTICAL);
            linearLayout.setGap(15);
            setLayout(linearLayout);
        }

        private JPanel createRowPanel(Color color1, Color color2, Color color3){
            JPanel panel = new JPanel();
//            panel.setLayout(new LinearLayout(15));
//            LinearConstraints linearConstraints = new LinearConstraints();
//            panel.add(createColorRect(color1,""), linearConstraints.setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
//            panel.add(createColorRect(color2,""), linearConstraints.setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
//            panel.add(createColorRect(color3,""), linearConstraints.setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            return panel;
        }
    }

    private static JComponent createColorRect(Color color, String text){
        JLabel label = new JLabel(text,SwingConstants.CENTER);
        label.setOpaque(true);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 40.0f));
        label.setBackground(color);
        label.setPreferredSize(new Dimension(100,100));
        return label;
    }
}
