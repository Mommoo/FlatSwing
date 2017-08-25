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
    private Image resizedImage;
    private ImageOption option;
    private int imageWidth, imageHeight;
    private MediaTracker mediaTracker = new MediaTracker(this);

    private Dimension getImageSize(Image image){
        ImageIcon imageIcon = new ImageIcon(image);
        return new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight());
    }

    public void setImage(Image image){
        Dimension imageDimen = getImageSize(image);
        setImage(image, imageDimen.width, imageDimen.height, ImageOption.BASIC_IMAGE_SIZE);
    }

    public void setImage(Image image, ImageOption option){
        Dimension imageDimen = getImageSize(image);
        setImage(image, imageDimen.width, imageDimen.height, option);
    }

    public void setImage(Image image, int width, int height){
        setImage(image, width, height, ImageOption.BASIC_IMAGE_SIZE);
    }

    public void setImage(Image image, int width, int height, ImageOption option){
        this.imageWidth = width;
        this.imageHeight = height;
        this.image = image;
        this.option = option;
        this.resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        awaitImageDraw(this.resizedImage);
        repaint();
    }

    public Image getImage(){
        return image;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        Insets insets = getInsets();

        int imageX = insets.left;
        int imageY = insets.top;

        int availableWidth = getWidth() - insets.left - insets.right;
        int availableHeight = getHeight() - insets.top - insets.bottom;

        if (option == ImageOption.BASIC_IMAGE_SIZE){


        } else if (option == ImageOption.BASIC_IMAGE_SIZE_CENTER){

            imageX += (availableWidth  - imageWidth)/2;
            imageY += (availableHeight - imageHeight)/2;

        } else if (option == ImageOption.MATCH_PARENT){

            imageWidth = availableWidth;
            imageHeight = availableHeight;

        }

        g2d.clipRect(imageX, imageY, availableWidth, availableHeight);
        g2d.drawImage(resizedImage, imageX, imageY, imageWidth, imageHeight, null);
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
        flatFrame.setResizable(true);
        flatFrame.setLocationOnScreenCenter();

        FlatImagePanel imagePanel = new FlatImagePanel();
        imagePanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        imagePanel.setImage(ImageManager.TEST);

        flatFrame.getContainer().add(imagePanel);

        flatFrame.show();
    }
}
