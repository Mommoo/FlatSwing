package flat.image;

import flat.component.FlatComponent;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mommoo on 2017-07-09.
 */
public class FlatImagePanel extends FlatComponent {
    private Image image;
    private ImageOption option;
    private boolean isNeedToPaint;

    public void setIcon(Image image, int width, int height){
        setIcon(new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
    }

    public void setIcon(Image image, int width, int height, ImageOption option){
        setIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), option);
    }

    public void setIcon(Image image, ImageOption option){
        this.image = image;
        this.option = option;
        this.isNeedToPaint = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (option != null && option == ImageOption.BASIC_IMAGE_SIZE_CENTER){
            ImageIcon imageIcon = new ImageIcon(this.image);
            int imageX = (getWidth() - imageIcon.getIconWidth())/2;
            int imageY = (getHeight() - imageIcon.getIconHeight())/2;
            g.drawImage(image, imageX, imageY, null);
        }

        if (option == null || !isNeedToPaint){
            return;
        }

        isNeedToPaint = false;

        if (option == ImageOption.MATCH_PARENT){

            setIcon(this.image, getWidth(), getHeight());

        } else if (option == ImageOption.BASIC_IMAGE_SIZE){

            ImageIcon imageIcon = new ImageIcon(this.image);
            setIcon(this.image, imageIcon.getIconWidth(), imageIcon.getIconHeight());

        }
    }
}
