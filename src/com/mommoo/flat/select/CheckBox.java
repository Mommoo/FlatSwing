package com.mommoo.flat.select;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.component.MouseClickAdapter;
import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.image.FlatImagePanel;
import com.mommoo.flat.image.ImageOption;
import com.mommoo.util.ImageManager;
import com.mommoo.util.ScreenManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

class CheckBox extends FlatPanel{
    private static final ScreenManager screenManager = ScreenManager.getInstance();
    private static final Dimension CHECK_BOX_DIMENSION = new Dimension(screenManager.dip2px(15), screenManager.dip2px(15));

    private MouseClickAdapter userMouseClickAdapter;

    private Color checkColor = Color.BLACK;
    private Color checkBoxColor = Color.BLACK;

    CheckBox(){
        setCheckBoxColor(checkBoxColor);
        setLayout(new BorderLayout());
        setOnClickListener(comp-> getComponent(0).setVisible(!getComponent(0).isVisible()));
//        addMouseListener(new HoverColorChangeListener());
        add(new FlatImagePanel(ImageManager.CHECK,ImageOption.MATCH_PARENT));
        getCheckImageView().setVisible(false);
    }

    private static BufferedImage createNewColorCheckBoxImage(Color color) {
        try {
            File checkBoxImageFile = new File(CheckBox.class.getResource("/com/mommoo/resource/img/check.png").toURI());
            BufferedImage bufferedImage = ImageIO.read(checkBoxImageFile);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    Color getCheckColor(){
        return this.checkColor;
    }

    void setCheckColor(Color checkColor){
        BufferedImage bufferedImage = createNewColorCheckBoxImage(checkColor);

        if (bufferedImage != null){
            this.checkColor = checkColor;
            FlatImagePanel imagePanel = (FlatImagePanel) getComponent(0);
            imagePanel.setImage(bufferedImage, ImageOption.MATCH_PARENT);
            imagePanel.reDraw();
        }
    }

    Color getCheckBoxColor(){
        return this.checkBoxColor;
    }

    void setCheckBoxColor(Color checkBoxColor){
        setBorder(BorderFactory.createLineBorder(checkBoxColor, 1));
    }

    void doClick(){
        getOnClickListener().onClick(this);
        if (this.userMouseClickAdapter != null) this.userMouseClickAdapter.getOnClickListener().onClick(this);
    }

    public void setOnClickListener(OnClickListener onClickListener){
        if (userMouseClickAdapter != null) removeMouseListener(userMouseClickAdapter);
        this.userMouseClickAdapter = new MouseClickAdapter(onClickListener);
        addMouseListener(this.userMouseClickAdapter);
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

