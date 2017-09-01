package com.mommoo.flat.textfield;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.label.FlatLabel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.util.FontManager;
import com.mommoo.util.ImageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class GuideTextPanel extends FlatPanel {

    private final FlatLabel GUIDE_LABEL = new FlatLabel();
    private final FlatTextField flatTextField;

    public GuideTextPanel(FlatTextField flatTextField){
        this.flatTextField = flatTextField;
        initGuideLabel();
        initGuideTextPanel();

        add(GUIDE_LABEL,   new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT));
        add(flatTextField, new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT));

        addTextFieldFocusListener(flatTextField);
    }

    private void initGuideTextPanel(){
        LinearLayout linearLayout = new LinearLayout(Orientation.VERTICAL, 0);
        setLayout(linearLayout);
        setBackground(flatTextField.getBackground());
    }

    private void initGuideLabel(){
        GUIDE_LABEL.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        GUIDE_LABEL.setFont(FontManager.getNanumGothicFont(Font.BOLD, 14).deriveFont(getAttribute()));
        GUIDE_LABEL.setOpaque(false);
    }

    private Map<TextAttribute, Object> getAttribute(){
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.TRACKING, 0.1);
        return attributes;
    }

    private void addTextFieldFocusListener(FlatTextField flatTextField){
        flatTextField.getTextField().addFocusListener(new FocusListener() {
            private Color previousForegroundColor = flatTextField.getForeground();

            @Override
            public void focusGained(FocusEvent e) {
                GUIDE_LABEL.setForeground(flatTextField.getFocusGainedColor());
            }

            @Override
            public void focusLost(FocusEvent e) {
                GUIDE_LABEL.setForeground(previousForegroundColor);
            }
        });
    }

    public void setGuideText(String text){
        GUIDE_LABEL.setText(text);
    }

    public String getGuideText(){
        return GUIDE_LABEL.getText();
    }

    public void setGuideFont(Font font){
        GUIDE_LABEL.setFont(font);
    }

    public Font getGuideFont(){
        return GUIDE_LABEL.getFont();
    }

    public void setGuideForeground(Color color){
        GUIDE_LABEL.setForeground(color);
    }

    public Color getGuideForeground(){
        return GUIDE_LABEL.getForeground();
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if(flatTextField != null) flatTextField.setBackground(bg);
    }

    public static void main(String[] args){
        FlatFrame flatFrame = new FlatFrame();
        flatFrame.setTitle("Beautiful GuideTextPanel");
        flatFrame.setSize(500,500);
        flatFrame.setLocationOnScreenCenter();

        FlatTextField flatTextField = new FlatTextField(false);
        flatTextField.setHint("hint");
        flatTextField.setIconImage(ImageManager.CHECK);

        GuideTextPanel guideTextPanel = new GuideTextPanel(flatTextField);
        guideTextPanel.setGuideText("good".toUpperCase());
        guideTextPanel.setBackground(Color.RED);
        flatFrame.getContainer().add(guideTextPanel);
        flatFrame.setResizable(true);

        flatFrame.show();

    }
}
