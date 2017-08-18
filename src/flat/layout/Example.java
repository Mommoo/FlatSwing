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
        frame.setSize(500,500);
        frame.setTitle("Flat layout test example");
        frame.setLocationOnScreenCenter();
        frame.setResizable(true);
        frame.getContainer().add(new LinearLayoutPanel());
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
            linearLayout.setGap(30);
            setLayout(linearLayout);
        }

        private void addComponent(){
            add(createColorRect(Color.RED, "FIRST"), new LinearConstraints().setWeight(2).setLinearSpace(LinearSpace.MATCH_PARENT));
            add(createColorRect(Color.GREEN, "SECOND"), new LinearConstraints().setWeight(8).setLinearSpace(LinearSpace.MATCH_PARENT));
            add(createColorRect(Color.YELLOW, "THIRD"), new LinearConstraints().setWeight(2).setLinearSpace(LinearSpace.MATCH_PARENT));
        }

        private JComponent createColorRect(Color color, String text){
            JLabel label = new JLabel(text,SwingConstants.CENTER);
            label.setOpaque(true);
            label.setFont(label.getFont().deriveFont(Font.BOLD, 40.0f));
            label.setBackground(color);
            label.setPreferredSize(new Dimension(100,100));
            return label;
        }
    }
}
