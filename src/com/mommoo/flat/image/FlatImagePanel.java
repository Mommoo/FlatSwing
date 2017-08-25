package com.mommoo.flat.image;

import com.mommoo.flat.component.FlatComponent;
import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.util.ImageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * Created by mommoo on 2017-07-09.
 */
public class FlatImagePanel extends FlatPanel {
    private Image image;
    private ImageOption option;
    private int imageWidth, imageHeight;
    private MediaTracker mediaTracker = new MediaTracker(this);

    private Dimension getImageSize(){
        ImageIcon imageIcon = new ImageIcon(image);
        return new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight());
    }

    private Dimension getBasicImageSize(){
        Dimension imageDimen;

        if (imageWidth != 0 && imageHeight != 0) imageDimen = getImageSize();
        else imageDimen = new Dimension(imageWidth, imageHeight);

        Insets insets = getInsets();

        int availableWidth = getWidth() - insets.left - insets.right;
        int availableHeight = getHeight() - insets.top - insets.bottom;

        if (availableWidth < imageDimen.width){
            imageDimen.width = availableWidth;
        }

        if (availableHeight < imageDimen.height){
            imageDimen.height = availableHeight;
        }

        return imageDimen;
    }

    public void setImage(Image image){
        setImage(image, ImageOption.BASIC_IMAGE_SIZE);
    }

    public void setImage(Image image, int width, int height){
        setImage(image, width, height, ImageOption.BASIC_IMAGE_SIZE);
    }

    public void setImage(Image image, int width, int height, ImageOption option){
        this.imageWidth = width;
        this.imageHeight = height;
        setImage(image, option);
    }

    public void setImage(Image image, ImageOption option){
        this.image = image;
        this.option = option;
        Dimension imageSize = getBasicImageSize();

        repaint();
    }

    public Image getImage(){
        return image;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Insets insets = getInsets();

        int imageX = insets.left;
        int imageY = insets.top;

        Dimension imageSize = getBasicImageSize();

        int imageWidth = imageSize.width;
        int imageHeight = imageSize.height;

        if (option == ImageOption.BASIC_IMAGE_SIZE){


        } else if (option == ImageOption.BASIC_IMAGE_SIZE_CENTER){

            imageX += (getWidth() - imageWidth)/2;
            imageY += (getHeight() - imageHeight)/2;

        } else if (option == ImageOption.MATCH_PARENT){

            imageWidth = getWidth() - insets.left - insets.right;
            imageHeight = getHeight() - insets.top - insets.bottom;

        }

        Image image = this.image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);

        awaitImageDraw(image);

        g.drawImage(image, imageX, imageY, null);
    }

    private void awaitImageDraw(Image image){
        mediaTracker.addImage(image, 0);

        try {
            mediaTracker.waitForID(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mediaTracker.removeImage(image);
    }

    public static void main(String[] args){
        FlatFrame flatFrame = new FlatFrame();
        flatFrame.setSize(500,500);
        flatFrame.setLocationOnScreenCenter();

        FlatImagePanel imagePanel = new FlatImagePanel();
        imagePanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        imagePanel.setImage(ImageManager.TEST, ImageOption.MATCH_PARENT);

        flatFrame.getContainer().add(imagePanel);

        flatFrame.show();
    }
}
