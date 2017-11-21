package com.mommoo.flat.frame;

import com.mommoo.flat.frame.colorpicker.FlatColorPickerView;
import com.mommoo.util.ScreenManager;

import java.awt.*;
import java.util.function.Consumer;

public class FlatColorPicker {
    private static final ScreenManager SCREEN = ScreenManager.getInstance();
    private final FlatFrame PICKER_FRAME = new FlatFrame();

    public FlatColorPicker(){
        this(Orientation.VERTICAL);
    }

    public FlatColorPicker(Orientation orientation){
        FlatColorPickerView flatColorPickerView = new FlatColorPickerView();
        flatColorPickerView.setPreferredSize(orientation.getDimension());
        PICKER_FRAME.getContainer().setLayout(new FlowLayout());
        PICKER_FRAME.getContainer().add(flatColorPickerView);
        PICKER_FRAME.pack();
    }

    public static void main(String[] args){
        FlatColorPicker flatColorPicker = new FlatColorPicker(Orientation.HORIZONTAL);
        flatColorPicker.setLocationOnScreenCenter();
        flatColorPicker.setTitle("FlatColorPicker");
//        flatColorPicker.setOnColorChangeListener(System.out::println);
//        flatColorPicker.setOnConfirmListener(System.out::println);
        flatColorPicker.show();
    }

    private FlatColorPickerView getView(){
        return (FlatColorPickerView) PICKER_FRAME.getContainer().getComponent(0);
    }

    public void setLocation(int x, int y){
        PICKER_FRAME.setLocation(x, y);
    }

    public Point getLocation(){
        return PICKER_FRAME.getLocation();
    }

    public void setLocation(Point point){
        PICKER_FRAME.setLocation(point);
    }

    public void setLocationOnScreenCenter(){
        PICKER_FRAME.setLocationOnScreenCenter();
    }

    public void setOnColorChangeListener(Consumer<Color> onColorChangeListener){
        getView().setOnColorChangeListener(onColorChangeListener);
    }

    public void setOnConfirmListener(Consumer<Color> onConfirmListener){
        getView().setOnConfirmListener(onConfirmListener);
    }

    public void setOnCancelListener(Runnable onCancelListener){
        getView().setOnCancelListener(onCancelListener);
    }

    public String getTitle(){
        return PICKER_FRAME.getTitle();
    }

    public void setTitle(String title){
        PICKER_FRAME.setTitle(title);
    }

    public Color getTitleBarColor(){
        return PICKER_FRAME.getTitleBarColor();
    }

    public void setTitleBarColor(Color color){
        PICKER_FRAME.setTitleBarColor(color);
    }

    public Color getUiColor(){
        return getView().getUiColor();
    }

    public void setUiColor(Color color){
        getView().setUiColor(color);
    }

    public void show(){
        PICKER_FRAME.show();
    }

    public void hide(){
        PICKER_FRAME.hide();
    }

    public enum Orientation{
        HORIZONTAL(SCREEN.dip2px(340), SCREEN.dip2px(160)),
        VERTICAL(SCREEN.dip2px(180), SCREEN.dip2px(290));

        private final Dimension DIMENSION;

        private Orientation(int width, int height){
            this.DIMENSION = new Dimension(width, height);
        }

        private Dimension getDimension(){
            return this.DIMENSION;
        }
    }
}
