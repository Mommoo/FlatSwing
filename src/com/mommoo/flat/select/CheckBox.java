package com.mommoo.flat.select;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.component.MouseClickAdapter;
import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.image.FlatImagePanel;
import com.mommoo.flat.image.ImageOption;
import com.mommoo.util.ImageManager;
import com.mommoo.util.ImageUtils;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

class CheckBox extends FlatPanel{
    private static final ScreenManager screenManager = ScreenManager.getInstance();
    private static final Dimension CHECK_BOX_DIMENSION = new Dimension(screenManager.dip2px(15), screenManager.dip2px(15));
    private static final Map<Color, BufferedImage> checkMap = new HashMap<>();
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

    private MouseClickAdapter userMouseClickAdapter;

    private Color checkColor = Color.BLACK;
    private Color checkBoxColor = Color.BLACK;

    CheckBox(){
        setCheckBoxColor(checkBoxColor);
        setLayout(new BorderLayout());
        super.setOnClickListener(comp-> getComponent(0).setVisible(!getComponent(0).isVisible()));
        add(new FlatImagePanel(ImageManager.CHECK,ImageOption.MATCH_PARENT));
        getCheckImageView().setVisible(false);
        setCursor(HAND_CURSOR);
    }

    private static BufferedImage getColorCheckImage(Color color){
        if (checkMap.get(color) == null){
            checkMap.put(color, createNewColorCheckImage(color));
        }

        return checkMap.get(color);
    }

    private static BufferedImage createNewColorCheckImage(Color color) {
        BufferedImage bufferedImage = ImageUtils.toBufferedImage(ImageManager.CHECK, true);
        int width  = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        BufferedImage newColorCheckImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                int rgba = bufferedImage.getRGB(x, y);
                boolean isTrans = (rgba & 0xff000000) == 0;

                if (isTrans) {
                    newColorCheckImage.setRGB(x, y, (color.getRGB()&0x00ffffff));
                } else {
                    newColorCheckImage.setRGB(x, y, color.getRGB());
                }
            }
        }

        return newColorCheckImage;
    }

    Color getCheckColor(){
        return this.checkColor;
    }

    void setCheckColor(Color checkColor){
        this.checkColor = checkColor;
        getCheckImageView().setImage(getColorCheckImage(checkColor), ImageOption.MATCH_PARENT);
    }

    Color getCheckBoxColor(){
        return this.checkBoxColor;
    }

    void setCheckBoxColor(Color checkBoxColor){
        setBorder(BorderFactory.createLineBorder(checkBoxColor, 1));
    }

    void doClick(Component source){
        getOnClickListener().onClick(source);
        if (this.userMouseClickAdapter != null) this.userMouseClickAdapter.getOnClickListener().onClick(source);
    }

    void setOnClickListener(Component source, OnClickListener onClickListener){
        removeOnClickListener();
        addMouseListener(userMouseClickAdapter = new MouseClickAdapter(source, onClickListener));
    }

    @Override
    public void removeOnClickListener() {
        if (userMouseClickAdapter != null) {
            removeMouseListener(userMouseClickAdapter);
        }
    }

    boolean isChecked(){
        return getComponent(0).isVisible();
    }

    void setChecked(boolean check){
        getCheckImageView().setVisible(check);
    }

    private FlatImagePanel getCheckImageView(){
        return (FlatImagePanel) getComponent(0);
    }

    @Override
    public Dimension getMaximumSize() {
        return CHECK_BOX_DIMENSION;
    }

    @Override
    public Dimension getPreferredSize() {
        return CHECK_BOX_DIMENSION;
    }

    @Override
    public Dimension getMinimumSize() {
        return CHECK_BOX_DIMENSION;
    }
}

