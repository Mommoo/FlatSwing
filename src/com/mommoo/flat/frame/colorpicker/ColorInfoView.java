package com.mommoo.flat.frame.colorpicker;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.text.textarea.alignment.FlatHorizontalAlignment;
import com.mommoo.flat.text.textarea.alignment.FlatVerticalAlignment;
import com.mommoo.util.ComputableDimension;
import com.mommoo.util.FontManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ColorInfoView extends FlatPanel {
    private static final ScreenManager SCREEN = ScreenManager.getInstance();
    private static final Font GUIDE_FONT = FontManager.getNanumGothicBoldFont(SCREEN.dip2px(10));

    public ColorInfoView() {
        setLayout(new LinearLayout(Orientation.VERTICAL, 0));
        add(new GuideView("information"), new LinearConstraints(LinearSpace.MATCH_PARENT));
        add(new HSB_InfoView(), new LinearConstraints(1, LinearSpace.MATCH_PARENT), "hsbView");
    }


    public void setHSB(HSB hsb){
        ((HSB_InfoView)getComponent("hsbView")).setHSB(hsb);
    }


    private class HSB_InfoView extends FlatPanel {
        private HSB_InfoView(){
            setLayout(new LinearLayout(Orientation.HORIZONTAL, 0));
            add(createColorView());
            add(createInfoView(), new LinearConstraints(1, LinearSpace.MATCH_PARENT));
        }

        private Component createColorView(){
            JPanel colorView = new JPanel();
            colorView.setBorder(new LineBorder(Color.BLACK, 1));
            colorView.setOpaque(true);
            colorView.setBackground(Color.RED);

            setOnLayoutListener((availableWidth, availableHeight) -> {
                int size = Math.min(availableWidth, availableHeight);
                colorView.setPreferredSize(new ComputableDimension(size));
                doLayout();
                removeOnLayoutListener();
            });

            return colorView;
        }

        private Component createInfoView(){
            FlatPanel infoView = new FlatPanel(new LinearLayout(Orientation.VERTICAL, 0));
            infoView.add(new LineView("H","S","B"), new LinearConstraints(1, LinearSpace.MATCH_PARENT));
            infoView.add(new LineView("R","G","B"), new LinearConstraints(1, LinearSpace.MATCH_PARENT));
            return infoView;
        }

        private void setHSB(HSB hsb){
            Component colorView = getComponent(0);
            colorView.setBackground(hsb.getColor());

            JPanel infoView = (JPanel)getComponent(1);

            LineView HSB_View = (LineView) infoView.getComponent(0);
            HSB_View.setValue(0, Integer.toString(hsb.getHue()));
            HSB_View.setValue(1, Integer.toString(hsb.getSaturation()));
            HSB_View.setValue(2, Integer.toString(hsb.getBrightness()));

            LineView RGB_View = (LineView) infoView.getComponent(1);
            RGB_View.setValue(0, Integer.toString(hsb.getColor().getRed()));
            RGB_View.setValue(1, Integer.toString(hsb.getColor().getGreen()));
            RGB_View.setValue(2, Integer.toString(hsb.getColor().getBlue()));
        }

        private class LineView extends FlatPanel{
            private LineView(String... guideTexts){
                setLayout(new LinearLayout(0));
                for (String guideText : guideTexts){
                    add(new GuideLabel(guideText), new LinearConstraints(1, LinearSpace.MATCH_PARENT));
                }
            }

            private void setValue(int index, String valueText){
                ((GuideLabel)getComponent(index)).setValue(valueText);
            }
        }

        private class GuideLabel extends JPanel {
            private GuideLabel(String guideText){
                setLayout(new LinearLayout(0));
                add(createGuideLabel(guideText), new LinearConstraints(1, LinearSpace.MATCH_PARENT));
                add(createCommonLabel(), new LinearConstraints(1, LinearSpace.MATCH_PARENT));
            }

            private FlatLabel createCommonLabel(){
                FlatLabel flatLabel = new FlatLabel();
                flatLabel.setText("g");
                flatLabel.setHorizontalAlignment(FlatHorizontalAlignment.CENTER);
                flatLabel.setVerticalAlignment(FlatVerticalAlignment.CENTER);
                return flatLabel;
            }

            private Component createGuideLabel(String guideText){
                FlatLabel guideLabel = createCommonLabel();
                guideLabel.setText(guideText);
                guideLabel.setFont(GUIDE_FONT);
                return guideLabel;
            }

            private void setValue(String value){
                ((FlatLabel)getComponent(1)).setText(value);
            }
        }
    }
}
