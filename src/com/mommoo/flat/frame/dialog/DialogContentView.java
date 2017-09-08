package com.mommoo.flat.frame.dialog;

import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;

import javax.swing.*;
import java.awt.*;

public class DialogContentView{

    private final ContentView contentView = new ContentView();

    public DialogContentView(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.RED);

        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.YELLOW);

        setUpperView(panel,300,400);
        setLowerView(panel2,300, 400);
        getLabel().setText("test");

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.add(contentView);
        frame.setVisible(true);

    }

    public static void main(String[] args){
        new DialogContentView();
    }

    public void setUpperView(JPanel panel, int width, int height){
        contentView.setUpperView(panel, width, height);
    }

    public void setLowerView(JPanel panel, int width, int height){
        contentView.setLowerView(panel, width, height);
    }

    private FlatLabel getLabel(){
        return contentView.textView;
    }

    private class ContentView extends JPanel{
        private JPanel upperView = new JPanel();
        private FlatLabel textView = new FlatLabel();
        private JPanel lowerView = new JPanel();

        private ContentView(){
            setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
            LinearLayout linearLayout = new LinearLayout(Orientation.HORIZONTAL, 30);
//            linearLayout.setWeightSum(10);
            setLayout(linearLayout);
            LinearConstraints linearConstraints = new LinearConstraints();
            linearConstraints.setWeight(2);
            add(upperView);
            add(textView, linearConstraints.setWeight(6));
            add(lowerView);
            add(new JButton("가니쉬"), linearConstraints.setWeight(3));
//            linearLayout.setWeightSum(10);
            setBackground(Color.BLUE);
        }

        private void addComponent(Component component, int gridY){
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.fill = GridBagConstraints.NONE;
            constraints.gridx = 0;
            constraints.gridy = gridY;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;

            add(component, constraints);
        }

        private void setUpperView(JPanel panel, int width, int height){
            remove(upperView);
            upperView = panel;
            upperView.setPreferredSize(new Dimension(width, height));
            LinearConstraints linearConstraints = new LinearConstraints();
            add(upperView, linearConstraints.setWeight(2), 0);
//            add(upperView,0);
        }

        private void setLowerView(JPanel panel, int width, int height){
            remove(lowerView);
            lowerView = panel;
            lowerView.setPreferredSize(new Dimension(width, height));
            LinearConstraints linearConstraints = new LinearConstraints();
            add(lowerView, linearConstraints.setWeight(2),2);
//            add(lowerView,2);
        }
        boolean flag;
        @Override public void paint(Graphics g) {
            super.paint(g);
//            for (Component comp : getComponents()){
//                if (!comp.getPreferredSize().equals(comp.getMinimumSize())){
//                    System.out.println("===========================");
//                    System.out.println(comp);
//                    System.out.println(comp.getPreferredSize());
//                    System.out.println(comp.getMinimumSize());
//                    comp.setMinimumSize(comp.getPreferredSize());
//
//                    flag = true;
//                }
//            }
//
//            if (flag){
//                revalidate();
//                for (Component comp : getComponents()){
//                    System.out.println("*********************");
//                    System.out.println(comp);
//                    System.out.println(comp.getPreferredSize());
//                    System.out.println(comp.getMinimumSize());
//                    System.out.println(comp.getSize());
//                    System.out.println(comp.getLocation());
//                }
//
//                flag = false;
//            }
        }
    }
}
