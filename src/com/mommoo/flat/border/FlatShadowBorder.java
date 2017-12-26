package com.mommoo.flat.border;


import com.mommoo.flat.button.FlatButton;
import com.mommoo.util.FastGaussianBlur;
import com.mommoo.util.ScreenManager;

import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class FlatShadowBorder extends AbstractBorder {
    public FlatShadowBorder(){
        this(ScreenManager.getInstance().dip2px(20));
    }

    private static final Map<Integer, Map<Position, Image>> SHADOW_FINDER = new HashMap<>();

    private final int SHADOW_SIZE;

    private BufferedImage createShadowBlurImage(int size, int radius){
        Shape shadowShape = new RoundRectangle2D.Float(radius, radius,size - radius*2  , size - radius*2, radius, radius);

        BufferedImage shadowBlurImage = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics2D = (Graphics2D)shadowBlurImage.getGraphics();
        graphics2D.setColor(new Color(0f,0f,0f, 0.3f));
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.fill(shadowShape);
        graphics2D.dispose();
        FastGaussianBlur.blur(shadowBlurImage, radius/2,3);
        return shadowBlurImage;
    }

    public FlatShadowBorder(int shadowWidth){
        this.SHADOW_SIZE = shadowWidth;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Map<Position, Image> shadowMap = SHADOW_FINDER.get(SHADOW_SIZE);
        if (shadowMap == null){
            shadowMap = new HashMap<>();

            int size = 200 + (SHADOW_SIZE * 2);
            BufferedImage shadowImage = createShadowBlurImage(size, SHADOW_SIZE/2);

            int chuckSize = SHADOW_SIZE * 2 ;

            shadowMap.put(Position.TOP_LEFT , shadowImage.getSubimage(0,0, chuckSize, chuckSize));
            shadowMap.put(Position.TOP      , shadowImage.getSubimage(chuckSize, 0,1, chuckSize));
            shadowMap.put(Position.TOP_RIGHT, shadowImage.getSubimage(size - chuckSize, 0, chuckSize, chuckSize));

            shadowMap.put(Position.LEFT     , shadowImage.getSubimage(0, chuckSize, chuckSize, 1));
            shadowMap.put(Position.RIGHT    , shadowImage.getSubimage(size - chuckSize, chuckSize, chuckSize, 1));

            shadowMap.put(Position.BOTTOM_LEFT, shadowImage.getSubimage(0,size - chuckSize, chuckSize, chuckSize));
            shadowMap.put(Position.BOTTOM, shadowImage.getSubimage(chuckSize, size - chuckSize, 1, chuckSize));
            shadowMap.put(Position.BOTTOM_RIGHT, shadowImage.getSubimage(size - chuckSize, size - chuckSize, chuckSize, chuckSize));

            SHADOW_FINDER.put(SHADOW_SIZE, shadowMap);
        }

        int topCornerHeight    = height >= 4 * SHADOW_SIZE / 3 ? 2 * SHADOW_SIZE / 3 : height / 2;
        int leftCornerWidth    = width  >= 4 * SHADOW_SIZE / 3 ? 2 * SHADOW_SIZE / 3 : width / 2;
        int bottomCornerHeight = height >= 4 * SHADOW_SIZE / 3 ? 2 * SHADOW_SIZE / 3 : height - (height / 2);
        int rightCornerWidth   = width  >= 4 * SHADOW_SIZE / 3 ? 2 * SHADOW_SIZE / 3 : width - (width / 2);

        int horizontalWidth = Math.max( width  - 4 * SHADOW_SIZE / 3 , 0);
        int verticalHeight  = Math.max( height - 4 * SHADOW_SIZE / 3 , 0);

        g.setClip(0,0, leftCornerWidth, topCornerHeight);
        g.drawImage(shadowMap.get(Position.TOP_LEFT).getScaledInstance(SHADOW_SIZE, SHADOW_SIZE, Image.SCALE_FAST), 0, 0, null);
        g.setClip(null);

        if ( horizontalWidth > 0 ){
            g.setClip(leftCornerWidth,0,horizontalWidth, topCornerHeight);
            g.drawImage(shadowMap.get(Position.TOP).getScaledInstance(horizontalWidth, SHADOW_SIZE, Image.SCALE_FAST),  leftCornerWidth, 0,null);
            g.setClip(null);
        }

        g.setClip(leftCornerWidth + horizontalWidth ,0,rightCornerWidth, topCornerHeight);
        g.drawImage(shadowMap.get(Position.TOP_RIGHT).getScaledInstance(SHADOW_SIZE, SHADOW_SIZE, Image.SCALE_FAST), width - SHADOW_SIZE, 0, null);
        g.setClip(null);

        if (verticalHeight > 0){

            g.setClip(0, topCornerHeight, leftCornerWidth, verticalHeight);
            g.drawImage(shadowMap.get(Position.LEFT).getScaledInstance(SHADOW_SIZE, verticalHeight , Image.SCALE_FAST), 0, topCornerHeight, null);
            g.setClip(null);

            g.setClip(leftCornerWidth + horizontalWidth, topCornerHeight, rightCornerWidth, verticalHeight);
            g.drawImage(shadowMap.get(Position.RIGHT).getScaledInstance(SHADOW_SIZE, verticalHeight, Image.SCALE_FAST), width - SHADOW_SIZE, topCornerHeight, null);
            g.setClip(null);
        }

        g.setClip(0,topCornerHeight + verticalHeight, leftCornerWidth, bottomCornerHeight);
        g.drawImage(shadowMap.get(Position.BOTTOM_LEFT).getScaledInstance(SHADOW_SIZE, SHADOW_SIZE, Image.SCALE_FAST), 0, height - SHADOW_SIZE, null);
        g.setClip(null);

        if (horizontalWidth > 0){
            g.setClip(leftCornerWidth,topCornerHeight + verticalHeight, horizontalWidth, bottomCornerHeight);
            g.drawImage(shadowMap.get(Position.BOTTOM).getScaledInstance(horizontalWidth , SHADOW_SIZE, Image.SCALE_FAST),
                    leftCornerWidth, height - SHADOW_SIZE, null);
            g.setClip(null);
        }

        g.setClip(leftCornerWidth + horizontalWidth ,topCornerHeight + verticalHeight, rightCornerWidth, topCornerHeight);
        g.drawImage(shadowMap.get(Position.BOTTOM_RIGHT).getScaledInstance(SHADOW_SIZE, SHADOW_SIZE, Image.SCALE_FAST), width - SHADOW_SIZE, height - SHADOW_SIZE, null);
        g.setClip(null);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        int top = SHADOW_SIZE/3;
        int left = SHADOW_SIZE/3;
        int bottom = SHADOW_SIZE/2;
        int right = left;
        return new Insets(top, left, bottom, right);
    }

    private enum Position{
        TOP,BOTTOM,LEFT,RIGHT,
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    public int getShadowWidth(){
        return SHADOW_SIZE;
    }
}
