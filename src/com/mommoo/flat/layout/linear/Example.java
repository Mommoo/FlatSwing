package com.mommoo.flat.layout.linear;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.image.FlatImagePanel;
import com.mommoo.flat.image.ImageOption;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.text.textarea.alignment.FlatHorizontalAlignment;
import com.mommoo.util.FontManager;
import com.mommoo.util.ImageManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Example {

    public static void main(String[] args){
        FlatFrame frame = new FlatFrame();
        frame.setSize(500,500 + FlatFrame.getTitleBarHeight());
        frame.setResizable(true);
        frame.setTitle("Flat layout test example");
        frame.setLocationOnScreenCenter();
        frame.getContainer().add(new LinearLayoutPanel3());
        frame.show();
    }

    private static class LinearLayoutPanel extends JPanel {
        private LinearLayout linearLayout;
        private LinearLayoutPanel(){
            setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            setLinearLayout();
            addComponent();
        }

        private void setLinearLayout(){
            linearLayout = new LinearLayout(Orientation.VERTICAL, 1);
            setLayout(linearLayout);
        }

        private void addComponent(){
            add(createColorRect(Color.RED, "FIRST"), new LinearConstraints().setWeight(349).setLinearSpace(LinearSpace.MATCH_PARENT));
            add(createColorRect(Color.GREEN, "SECOND"), new LinearConstraints().setWeight(316).setLinearSpace(LinearSpace.MATCH_PARENT));
            add(createColorRect(Color.YELLOW, "THIRD"), new LinearConstraints().setWeight(441).setLinearSpace(LinearSpace.MATCH_PARENT));
        }
    }

    private static class LinearLayoutPanel2 extends JPanel{
        private LinearLayoutPanel2(){
            setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            setLinearLayout();
            LinearConstraints linearConstraints = new LinearConstraints();
            add(createRowPanel(Color.RED, Color.WHITE, Color.BLUE), linearConstraints.setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
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
            panel.setLayout(new LinearLayout(15));
            LinearConstraints linearConstraints = new LinearConstraints();
            panel.add(createColorRect(color1,""), linearConstraints.setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            panel.add(createColorRect(color2,""), linearConstraints.setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            panel.add(createColorRect(color3,""), linearConstraints.setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
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

    private static class LinearLayoutPanel3 extends JPanel{
        private LinearLayoutPanel3(){
            Component comp = new FlatImagePanel(ImageManager.LION, ImageOption.MATCH_PARENT);
            comp.setPreferredSize(new Dimension(100,100));
            setLayout(new LinearLayout(Orientation.VERTICAL, 30, Alignment.CENTER));
            setBorder(new EmptyBorder(30,30,30,30));
            add(createColorRect(Color.RED, "1"), new LinearConstraints(LinearSpace.WRAP_CENTER_CONTENT));
            add(comp, new LinearConstraints(LinearSpace.WRAP_CENTER_CONTENT));
            add(createColorRect(Color.BLUE, "2"), new LinearConstraints(LinearSpace.WRAP_CENTER_CONTENT));
        }
    }
}
