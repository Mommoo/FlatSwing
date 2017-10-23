package com.mommoo.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {
    private ImageUtils(){

    }

    public static BufferedImage toBufferedImage(Image image){
        return toBufferedImage(image, false);
    }

    public static BufferedImage toBufferedImage(Image image, boolean antialias){
        ImageIcon imageIcon = new ImageIcon(image);
        BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        if (antialias){
            ((Graphics2D)bufferedImage.getGraphics()).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        bufferedImage.getGraphics().drawImage(image,0,0,null);
        bufferedImage.getGraphics().dispose();
        return bufferedImage;
    }
}
