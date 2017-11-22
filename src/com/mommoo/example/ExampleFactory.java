package com.mommoo.example;

import com.mommoo.flat.button.FlatButton;
import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.image.FlatImagePanel;
import com.mommoo.flat.image.ImageOption;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.list.FlatListView;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.text.textfield.FlatTextField;
import com.mommoo.flat.text.textfield.format.FlatTextFormat;
import com.mommoo.util.FontManager;
import com.mommoo.util.ImageManager;

import javax.swing.*;
import java.awt.*;

public class ExampleFactory {
    static FlatFrame createCommonFrame() {
        FlatFrame commonFrame = new FlatFrame();
        commonFrame.setSize(700, 700);
        commonFrame.setLocationOnScreenCenter();

        return commonFrame;
    }

    public static class FlatImagePanelExample {
        public static void example1() {
            FlatFrame frame = createCommonFrame();
            frame.setTitle("Example1 : Grid Image Galley(3x3) ");
            frame.getContainer().setLayout(new LinearLayout(Orientation.VERTICAL));

            frame.getContainer().add(createPanelOfImages(ImageManager.CAT, ImageManager.DOG, ImageManager.FROG), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            frame.getContainer().add(createPanelOfImages(ImageManager.LION, ImageManager.GIRIN, ImageManager.PIG), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            frame.getContainer().add(createPanelOfImages(ImageManager.TIGER, ImageManager.PANDA, ImageManager.SHEEP), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));

            frame.show();
        }

        public static void example2() {
            FlatFrame frame = createCommonFrame();
            frame.setTitle("Example2 : Grid Image Galley(9x9) ");
//            frame.getContainer().setLayout(new LinearLayout(Orientation.VERTICAL));
            FlatPanel panel = new FlatPanel(new LinearLayout(Orientation.VERTICAL));
            panel.setForeground(new Color(0,0,0,20));
            panel.setBackground(new Color(24, 24, 24,20));
            frame.getContainer().add(panel);
            panel.add(createPanelOfImages(ImageManager.LION, ImageManager.GIRIN, ImageManager.PIG, ImageManager.CAT, ImageManager.DOG, ImageManager.FROG, ImageManager.TIGER, ImageManager.PANDA, ImageManager.SHEEP), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            panel.add(createPanelOfImages(ImageManager.TIGER, ImageManager.PANDA, ImageManager.SHEEP, ImageManager.LION, ImageManager.GIRIN, ImageManager.PIG, ImageManager.LION, ImageManager.GIRIN, ImageManager.PIG), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            panel.add(createPanelOfImages(ImageManager.CAT, ImageManager.DOG, ImageManager.FROG, ImageManager.TIGER, ImageManager.PANDA, ImageManager.SHEEP, ImageManager.LION, ImageManager.GIRIN, ImageManager.PIG), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));

            panel.add(createPanelOfImages(ImageManager.TIGER, ImageManager.PANDA, ImageManager.SHEEP, ImageManager.LION, ImageManager.GIRIN, ImageManager.PIG, ImageManager.LION, ImageManager.GIRIN, ImageManager.PIG), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            panel.add(createPanelOfImages(ImageManager.LION, ImageManager.GIRIN, ImageManager.PIG, ImageManager.CAT, ImageManager.DOG, ImageManager.FROG, ImageManager.TIGER, ImageManager.PANDA, ImageManager.SHEEP), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            panel.add(createPanelOfImages(ImageManager.CAT, ImageManager.DOG, ImageManager.FROG, ImageManager.TIGER, ImageManager.PANDA, ImageManager.SHEEP, ImageManager.LION, ImageManager.GIRIN, ImageManager.PIG), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));

            panel.add(createPanelOfImages(ImageManager.LION, ImageManager.GIRIN, ImageManager.PIG, ImageManager.CAT, ImageManager.DOG, ImageManager.FROG, ImageManager.TIGER, ImageManager.PANDA, ImageManager.SHEEP), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            panel.add(createPanelOfImages(ImageManager.TIGER, ImageManager.PANDA, ImageManager.SHEEP, ImageManager.LION, ImageManager.GIRIN, ImageManager.PIG, ImageManager.LION, ImageManager.GIRIN, ImageManager.PIG), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            panel.add(createPanelOfImages(ImageManager.CAT, ImageManager.DOG, ImageManager.FROG, ImageManager.TIGER, ImageManager.PANDA, ImageManager.SHEEP, ImageManager.LION, ImageManager.GIRIN, ImageManager.PIG), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));

            frame.show();
        }

        private static Component createPanelOfImages(Image... images) {
            FlatPanel parentPanel = new FlatPanel(new LinearLayout());
            for (Image image : images) {
                FlatImagePanel flatImagePanel = new FlatImagePanel(image, ImageOption.MATCH_PARENT);
                parentPanel.add(flatImagePanel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            }

            return parentPanel;
        }
    }

    public static class FlatListViewExample {
        public static void example1() {
            FlatFrame frame = createCommonFrame();
            frame.setTitle("Example1 : Auto Scrolling");
            frame.getContainer().setLayout(new LinearLayout(Orientation.VERTICAL));

            FlatPanel insertPanel = new FlatPanel(new LinearLayout());
            FlatTextField textField = new FlatTextField(false);
            insertPanel.add(textField, new LinearConstraints().setWeight(4).setLinearSpace(LinearSpace.MATCH_PARENT));
            FlatButton button = new FlatButton("INSERT");
            insertPanel.add(button, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));

            frame.getContainer().add(insertPanel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            FlatListView<Component> listView = new FlatListView<>();
            frame.getContainer().add(listView.getComponent(), new LinearConstraints().setWeight(5).setLinearSpace(LinearSpace.MATCH_PARENT));

            button.setOnClickListener(e -> {
                FlatLabel label = new FlatLabel("POSITION " + (listView.getItemSize() + 1) + " : " + textField.getText());
                JPanel panel = new JPanel();
                label.setFont(label.getFont().deriveFont(50.0f));
                panel.setLayout(new LinearLayout(10));

                panel.add(label, new LinearConstraints(2));

                panel.add(new JLabel("ddddddddddd"), new LinearConstraints(4));

                listView.addItem(panel);
                listView.getScroller().smoothScrollByPosition(true, listView.getItemSize() - 1);
            });

            frame.show();
        }

        public static void example2(){
            FlatFrame frame = createCommonFrame();
            frame.setSize(250,500);
            frame.setTitle("Example2 : panelWrap Test");
            frame.getContainer().setLayout(new LinearLayout(Orientation.VERTICAL));
            FlatListView<FlatPanel> listView = new FlatListView<>();
            Font font = FontManager.getNanumGothicFont(Font.BOLD, 50);
            for (int i = 0 ; i < 30 ; i++){
                listView.addItem(createWrapPanel("position : " + (i + 1),font));
            }
            frame.getContainer().add(listView.getComponent(), new LinearConstraints().setWeight(5).setLinearSpace(LinearSpace.MATCH_PARENT));
            frame.show();
        }

        public static void example3(){
            FlatFrame frame = createCommonFrame();
            frame.getContainer().setLayout(new LinearLayout(Orientation.VERTICAL));
            JPanel panel = new JPanel(new FlowLayout());
            JButton button1 = new JButton("추가");
            JButton button2 = new JButton("삭제");
            JButton button3 = new JButton("삭제 & 추가");
            panel.add(button1);
            panel.add(button2);
            panel.add(button3);

            FlatListView<Component> listView = new FlatListView<>();
            listView.setDivider(Color.BLACK, 20);

            button1.addActionListener(e->{
                listView.addItem(new FlatLabel("하나씩 아이템!! "));
            });

            button2.addActionListener(e->{
                listView.removeItem(listView.getItemSize() - 1);
            });

            button3.addActionListener(e->{
                listView.clear();
                for (int i = 0 ; i < 20 ; i ++){
                    listView.addItem(new FlatLabel("아이템!!  " + i));
                }
            });

            frame.getContainer().add(panel, new LinearConstraints(2,LinearSpace.MATCH_PARENT));
            frame.getContainer().add(listView.getComponent(), new LinearConstraints(6, LinearSpace.MATCH_PARENT));

            frame.show();
        }

        private static FlatPanel createWrapPanel(String text,Font font){
            FlatPanel panel = new FlatPanel(new BorderLayout());
            panel.setAlpha(0.8f);
            FlatLabel label = new FlatLabel(text);

//            panel.setPreferredSize(new Dimension(1,50));
            label.setFont(font);
            panel.add(label);
            return panel;
        }
    }

    public static class FlatTextFieldExample {
        public static void example1() {
            FlatFrame frame = createCommonFrame();
            frame.setTitle("Example1 : Ten of TextField");
            frame.getContainer().setLayout(new LinearLayout(Orientation.VERTICAL));

            for (int i = 0; i < 10; i++) {
                FlatTextField component = new FlatTextField(true);
                component.setHint("HINT");
                component.setFormat(FlatTextFormat.NUMBER_DECIMAL);
                component.setHintForeground(Color.BLUE);
                component.setForeground(Color.RED);
                frame.getContainer().add(component, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            }
            ((FlatTextField)frame.getContainer().getComponent(3)).setText("가나다");
            frame.show();
        }

        public static void example2() {
//            FlatFrame frame = createCommonFrame();
//            frame.getContainer().setLayout(new FlowLayout());
            FlatFrame frame = new FlatFrame();
            frame.setTitle("Example2 : Basic Textfield");
            frame.getContainer().setLayout(new LinearLayout(Orientation.VERTICAL, 1));

            frame.getContainer().add(createTextField(), new LinearConstraints(LinearSpace.MATCH_PARENT));
            frame.getContainer().add(createTextField(), new LinearConstraints(LinearSpace.MATCH_PARENT));
            JPanel panel = new JPanel();
            JButton button = new JButton("가나다");
            panel.add(button);
            button.setFont(FontManager.getNanumGothicFont(Font.BOLD, 50));
            frame.getContainer().add(panel);
            frame.getJFrame().pack();
            frame.setLocationOnScreenCenter();
            frame.show();

        }

        private static Component createTextField(){

            FlatTextField textField = new FlatTextField(false);
            textField.setIconImage(ImageManager.DOG);
            textField.setFont(FontManager.getNanumGothicFont(Font.BOLD, 16));
            textField.setTextFieldPadding(20,0,20,0);
            textField.setImagePadding(10);
//            textField.setColumns(40);
            textField.setHint("I'm a Dog");
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(textField);
            return panel;
        }
    }
}
