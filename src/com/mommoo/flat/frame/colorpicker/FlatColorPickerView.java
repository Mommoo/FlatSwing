package com.mommoo.flat.frame.colorpicker;

import com.mommoo.flat.border.FlatEmptyBorder;
import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.util.ColorManager;
import com.mommoo.util.ScreenManager;

import java.awt.*;
import java.util.function.Consumer;

public class FlatColorPickerView extends FlatPanel {
    private static final ScreenManager SCREEN = ScreenManager.getInstance();
    private Consumer<Color> onColorChangeListener = color -> {};
    private Consumer<Color> onConfirmListener = color -> {};
    private Runnable onCancelListener = () -> {};
    private HSB hsb = new HSB(0,0,100);
    private boolean isChangeHSB;
    private Color uiColor = ColorManager.getColorAccent();

    public FlatColorPickerView(){
        setBorder(new FlatEmptyBorder(SCREEN.dip2px(4)));
        setLayout(null);
        add(new HSB_View(), "hsbView");
        add(new HueView(), "hueView");
        add(new ColorInfoView(),"colorView");
        add(new ColorApplyView(), "applyView");
        add(new ColorConfirmView(), "confirmView");

        setUiColor(uiColor);

        getHueView().setOnHSBChangeListener(hue -> {
            hsb = hsb.changeHue(hue);
            if (!isChangeHSB) getHSB_View().setHue(hue);
            else {
                getHSB_View().setHSB(hsb);
                isChangeHSB  = false;
            }
        });

        getHSB_View().setOnHSBChangeListener(hsb-> {
            this.hsb = hsb;
            getColorInfoView().setHSB(hsb);
            onColorChangeListener.accept(hsb.getColor());
        });

        getColorApplyView().setOnColorApplyListener(hsb -> {
            this.hsb = hsb;
            isChangeHSB = true;
            getHueView().setHue(hsb.getHue());
            getColorInfoView().setHSB(hsb);
        });

        getConfirmView()
                .setOnConfirmListener(()-> onConfirmListener.accept(hsb.getColor()))
                .setOnCancelListener(()-> onCancelListener.run());
    }

    private HSB_View getHSB_View(){
        return (HSB_View)getComponent("hsbView");
    }

    private HueView getHueView(){
        return (HueView) getComponent("hueView");
    }

    private ColorInfoView getColorInfoView(){
        return (ColorInfoView) getComponent("colorView");
    }

    private ColorApplyView getColorApplyView(){
        return (ColorApplyView) getComponent("applyView");
    }

    private ColorConfirmView getConfirmView(){
        return (ColorConfirmView) getComponent("confirmView");
    }

    public void setOnColorChangeListener(Consumer<Color> onColorChangeListener){
        this.onColorChangeListener = onColorChangeListener;
    }

    public void setOnConfirmListener(Consumer<Color> onConfirmListener){
        this.onConfirmListener = onConfirmListener;
    }

    public void setOnCancelListener(Runnable onCancelListener){
        this.onCancelListener = onCancelListener;
    }

    public Color getUiColor(){
        return this.uiColor;
    }

    public void setUiColor(Color uiColor){
        this.uiColor = uiColor;
        getColorApplyView().setUiColor(uiColor);
        getConfirmView().setUiColor(uiColor);
    }

    @Override
    public void paint(Graphics g) {
        Insets insets = getInsets();
        final int AVAILABLE_WIDTH = getWidth() - insets.left - insets.right;
        final int AVAILABLE_HEIGHT = getHeight() - insets.top - insets.bottom;

        boolean isHorizontalMode = AVAILABLE_WIDTH > AVAILABLE_HEIGHT;

        int size = isHorizontalMode ? AVAILABLE_HEIGHT : AVAILABLE_WIDTH;
        int remainSize = isHorizontalMode ? AVAILABLE_WIDTH - AVAILABLE_HEIGHT : AVAILABLE_HEIGHT - AVAILABLE_WIDTH;
        int padding = 10;

        if (isHorizontalMode){
            int hueViewWidth = Math.max(70, remainSize/5);
            getHSB_View().setBounds(insets.left, insets.top, size, size);
            getHueView().setBounds(insets.left + size + padding, insets.top, hueViewWidth, size);
            getHueView().doLayout();

            int colorInfoViewHeight = getColorInfoView().getPreferredSize().height;
            int colorInfoViewX = insets.left + size + + padding + hueViewWidth  + padding;
            getColorInfoView().setBounds(colorInfoViewX, insets.top, AVAILABLE_WIDTH - colorInfoViewX, colorInfoViewHeight);
            getColorApplyView().doLayout();

            int colorApplyViewHeight = getColorApplyView().getPreferredSize().height;
            getColorApplyView().setBounds(colorInfoViewX, insets.top + colorInfoViewHeight + padding, AVAILABLE_WIDTH - colorInfoViewX, colorApplyViewHeight);
            getColorApplyView().doLayout();

            int confirmViewHeight = 50;
            getConfirmView().setBounds(colorInfoViewX, getHeight() - insets.bottom - confirmViewHeight, AVAILABLE_WIDTH - colorInfoViewX, confirmViewHeight);
        } else {
            int hueViewWidth = 70;
            int hsbViewSize = size - hueViewWidth - padding * 2;
            getHSB_View().setBounds(insets.left, insets.top, hsbViewSize, hsbViewSize);
            getHueView().setBounds(insets.left + hsbViewSize + padding, insets.top, hueViewWidth, hsbViewSize);
            getHueView().doLayout();

            int colorInfoViewHeight = getColorInfoView().getPreferredSize().height;
            int colorInfoViewX = insets.left;
            int colorInfoViewY = insets.top + hsbViewSize + padding;
            getColorInfoView().setBounds(colorInfoViewX, colorInfoViewY, AVAILABLE_WIDTH, colorInfoViewHeight);
            getColorApplyView().doLayout();

            int colorApplyViewHeight = getColorApplyView().getPreferredSize().height;
            getColorApplyView().setBounds(colorInfoViewX, colorInfoViewY + colorInfoViewHeight + padding, AVAILABLE_WIDTH, colorApplyViewHeight);
            getColorApplyView().doLayout();

            int confirmViewY = colorInfoViewY + colorApplyViewHeight + padding + colorApplyViewHeight + padding;
            getConfirmView().setBounds(colorInfoViewX, confirmViewY, AVAILABLE_WIDTH, insets.top + AVAILABLE_HEIGHT - confirmViewY);
        }
        super.paint(g);
    }
}
