package com.mommoo.flat.image;

import com.mommoo.flat.component.FlatComponent;
import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.util.ImageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * Created by mommoo on 2017-07-09.
 */
public class FlatImagePanel extends FlatPanel {
    private Image image;
    private Image resizedImage;
    private ImageOption option;

    private MediaTracker mediaTracker = new MediaTracker(this);

    private boolean reDraw = true;

    public static void main(String[] args){
        FlatFrame flatFrame = new FlatFrame();
        flatFrame.setSize(500,500);
        flatFrame.setResizable(true);
        flatFrame.setLocationOnScreenCenter();

        FlatImagePanel imagePanel = new FlatImagePanel();
        imagePanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        imagePanel.setImage(ImageManager.TEST, ImageOption.MATCH_PARENT);

        flatFrame.getContainer().add(imagePanel);

        flatFrame.show();
    }

    public void setImage(Image image, ImageOption option){
        this.image = image;
        this.option = option;
    }

    public Image getImage(){
        return image;
    }

    public void setImage(Image image){
        setImage(image, ImageOption.BASIC_IMAGE_SIZE);
    }

    public void reDraw(){
        reDraw = true;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        Insets insets = getInsets();

        Image image = this.image;

        int imageX = insets.left;
        int imageY = insets.top;

        int availableWidth = getWidth() - insets.left - insets.right;
        int availableHeight = getHeight() - insets.top - insets.bottom;

        if (option == ImageOption.BASIC_IMAGE_SIZE){

        } else if (option == ImageOption.BASIC_IMAGE_SIZE_CENTER){

            Dimension imageDimension = getImageDimension();
            imageX += (availableWidth  - imageDimension.width)/2;
            imageY += (availableHeight - imageDimension.height)/2;

        } else if (option == ImageOption.MATCH_PARENT){

            if (reDraw){
                resizedImage = this.image.getScaledInstance(availableWidth, availableHeight, Image.SCALE_SMOOTH);
                awaitImageDraw(resizedImage);
                reDraw = false;
            }

            image = resizedImage;
        }

        g2d.clipRect(imageX, imageY, availableWidth, availableHeight);
        g2d.drawImage(image,imageX,imageY,null);
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

    private Dimension getImageDimension(){
        ImageIcon imageIcon = new ImageIcon(image);
        return new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight());
    }
}
