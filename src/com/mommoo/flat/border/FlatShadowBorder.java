package com.mommoo.flat.border;


import com.mommoo.util.FastGaussianBlur;
import com.mommoo.util.ScreenManager;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class FlatShadowBorder implements Border {
    private static final Map<Integer, Map<Position, Image>> SHADOW_FINDER = new HashMap<>();
    private final int SHADOW_SIZE;
    private final Elevation ELEVATION;
    public FlatShadowBorder(){
        this(ScreenManager.getInstance().dip2px(4), Elevation.NORMAL);
    }

    public FlatShadowBorder(int shadowWidth){
        this(shadowWidth, Elevation.NORMAL);
    }

    public FlatShadowBorder(Elevation elevation){
        this(ScreenManager.getInstance().dip2px(4), elevation);
    }

    public FlatShadowBorder(int shadowWidth, Elevation elevation){
        this.SHADOW_SIZE = shadowWidth;
        this.ELEVATION = elevation;
    }

    private BufferedImage createShadowBlurImage(int width, int height, int gap){
        Shape shadowShape = new Rectangle2D.Float(SHADOW_SIZE, SHADOW_SIZE,width - SHADOW_SIZE *2 , height - SHADOW_SIZE * 2);
        BufferedImage shadowBlurImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics2D = (Graphics2D)shadowBlurImage.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setPaint(Color.BLACK);
        graphics2D.fill(shadowShape);
        graphics2D.dispose();
        FastGaussianBlur.blur(shadowBlurImage, SHADOW_SIZE /2d, gap);
        return shadowBlurImage;
    }

    private BufferedImage createShadowImage(int width, int height){
        BufferedImage shadowImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        BufferedImage outShadowImage = createShadowBlurImage(width, height, 3);
        BufferedImage innerShadowImage = createShadowBlurImage(width, height, 10);

        shadowImage.getGraphics().drawImage(outShadowImage, 0, 0, null);
        for (int i = 0 ; i < ELEVATION.getInnerShadowCount() ; i ++){
            shadowImage.getGraphics().drawImage(innerShadowImage, 0, 0, null);
        }
        shadowImage.getGraphics().dispose();
        return shadowImage;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (SHADOW_SIZE == 0) return ;

        Map<Position, Image> shadowMap = SHADOW_FINDER.get(SHADOW_SIZE);
        if (shadowMap == null){
            shadowMap = new HashMap<>();

            int size = ScreenManager.getInstance().dip2px(250);
            BufferedImage shadowImage = createShadowImage(size, size);

            shadowMap.put(Position.TOP_LEFT, shadowImage.getSubimage(0,0, SHADOW_SIZE, SHADOW_SIZE));
            shadowMap.put(Position.TOP, shadowImage.getSubimage(SHADOW_SIZE,0,1, SHADOW_SIZE));
            shadowMap.put(Position.TOP_RIGHT, shadowImage.getSubimage(size - SHADOW_SIZE, 0, SHADOW_SIZE, SHADOW_SIZE));

            shadowMap.put(Position.LEFT, shadowImage.getSubimage(0, SHADOW_SIZE, SHADOW_SIZE, 1));
            shadowMap.put(Position.RIGHT, shadowImage.getSubimage(size - SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE, 1));

            shadowMap.put(Position.BOTTOM_LEFT, shadowImage.getSubimage(0,size - SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE));
            shadowMap.put(Position.BOTTOM, shadowImage.getSubimage(SHADOW_SIZE, size - SHADOW_SIZE, 1, SHADOW_SIZE));
            shadowMap.put(Position.BOTTOM_RIGHT, shadowImage.getSubimage(size - SHADOW_SIZE, size - SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE));

            SHADOW_FINDER.put(SHADOW_SIZE, shadowMap);
        }

        g.drawImage(shadowMap.get(Position.TOP_LEFT), 0, 0, null);
        g.drawImage(shadowMap.get(Position.TOP).getScaledInstance(width - SHADOW_SIZE *2, SHADOW_SIZE, Image.SCALE_FAST), SHADOW_SIZE,0 ,null);
        g.drawImage(shadowMap.get(Position.TOP_RIGHT), width - SHADOW_SIZE, 0, null);

        g.drawImage(shadowMap.get(Position.LEFT).getScaledInstance(SHADOW_SIZE, height - SHADOW_SIZE * 2 , Image.SCALE_FAST), 0, SHADOW_SIZE, null);
        g.drawImage(shadowMap.get(Position.RIGHT).getScaledInstance(SHADOW_SIZE, height - SHADOW_SIZE * 2, Image.SCALE_FAST), width - SHADOW_SIZE, SHADOW_SIZE, null);

        g.drawImage(shadowMap.get(Position.BOTTOM_LEFT), 0, height - SHADOW_SIZE, null);
        g.drawImage(shadowMap.get(Position.BOTTOM).getScaledInstance(width - SHADOW_SIZE * 2 , SHADOW_SIZE, Image.SCALE_FAST), SHADOW_SIZE, height - SHADOW_SIZE, null);
        g.drawImage(shadowMap.get(Position.BOTTOM_RIGHT), width - SHADOW_SIZE, height - SHADOW_SIZE, null);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    public int getShadowWidth(){
        return SHADOW_SIZE;
    }

    public Elevation getElevation() {
        return ELEVATION;
    }

    private enum Position{
        TOP,BOTTOM,LEFT,RIGHT,
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;
    }
}
