package com.mommoo.flat.text.textfield;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.util.FontManager;
import com.mommoo.util.ImageManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class FlatGuideTextPanel extends FlatPanel {
    private static final ScreenManager SCREEN = ScreenManager.getInstance();
    private static final Font DEFAULT_FONT = FontManager.getNanumGothicFont(Font.BOLD, SCREEN.dip2px(8)).deriveFont(getAttribute());

    public FlatGuideTextPanel(FlatTextField flatTextField){
        setOpaque(true);
        setLayout(new LinearLayout(Orientation.VERTICAL, 0));
        setBackground(flatTextField.getBackground());

        add(createGuideLabel(),   new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT));
        add(flatTextField, new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT));

        addTextFieldFocusListener(flatTextField);
    }

    public static void main(String[] args){
        FlatFrame flatFrame = new FlatFrame();
        flatFrame.setTitle("Beautiful FlatGuideTextPanel");
        flatFrame.setSize(500,500);
        flatFrame.setLocationOnScreenCenter();

        FlatTextField flatTextField = new FlatTextField(false);
        flatTextField.setHint("hint");
        flatTextField.setIconImage(ImageManager.CHECK);

        FlatGuideTextPanel flatGuideTextPanel = new FlatGuideTextPanel(flatTextField);
        flatGuideTextPanel.setGuideText("good".toUpperCase());

        FlatTextField flatTextField2 = new FlatTextField(false);
        flatTextField2.setHint("hint2");

        FlatGuideTextPanel flatGuideTextPanel2 = new FlatGuideTextPanel(flatTextField2);
        flatGuideTextPanel2.setGuideText("good".toUpperCase());

        flatFrame.getContainer().setLayout(new LinearLayout(Orientation.VERTICAL));
        flatFrame.getContainer().add(flatGuideTextPanel);
        flatFrame.getContainer().add(flatGuideTextPanel2);
        flatFrame.setResizable(true);

        flatFrame.show();

    }

    private Component createGuideLabel(){
        FlatLabel guideLabel = new FlatLabel();
        guideLabel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        guideLabel.setFont(DEFAULT_FONT);
        guideLabel.setOpaque(false);
        return guideLabel;
    }

    private static Map<TextAttribute, Object> getAttribute(){
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.TRACKING, 0.1);
        return attributes;
    }

    private FlatLabel getGuideLabel(){
        return (FlatLabel) getComponent(0);
    }

    public FlatTextField getTextField(){
        return (FlatTextField) getComponent(1);
    }

    private void addTextFieldFocusListener(FlatTextField flatTextField){
        flatTextField
                .getTextField()
                .addFocusListener(new FocusListener() {
            private Color previousForegroundColor = flatTextField.getForeground();

            @Override
            public void focusGained(FocusEvent e) {
                getGuideLabel().setForeground(flatTextField.getFocusGainedColor());
            }

            @Override
            public void focusLost(FocusEvent e) {
                getGuideLabel().setForeground(previousForegroundColor);
            }
        });
    }

    public String getGuideText(){
        return getGuideLabel().getText();
    }

    public void setGuideText(String text){
        getGuideLabel().setText(text);
    }

    public Font getGuideFont(){
        return getGuideLabel().getFont();
    }

    public void setGuideFont(Font font){
        getGuideLabel().setFont(font);
    }

    public Color getGuideForeground(){
        return getGuideLabel().getForeground();
    }

    public void setGuideForeground(Color color){
        getGuideLabel().setForeground(color);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if(getComponentCount() > 1) getTextField().setBackground(bg);
    }
}
