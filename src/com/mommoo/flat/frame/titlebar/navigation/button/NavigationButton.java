package com.mommoo.flat.frame.titlebar.navigation.button;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.component.OnClickListener;

import javax.swing.*;
import java.awt.*;

public abstract class NavigationButton extends FlatPanel implements ViewModel {
    private static final long serialVersionUID = 1L;
    private static final BasicStroke BASIC_STROKE = new BasicStroke(2.0f);
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

    private Color buttonIconColor = Color.BLACK;

    private NavigationButtonModel buttonModel = new NavigationButtonModel(this);

    private boolean block;

    NavigationButton() {
        setCursor(HAND_CURSOR);
        addMouseListener(buttonModel.getHoverAnimListener());
    }

    @Override
    protected void postDraw(Graphics2D graphics2D, int availableWidth, int availableHeight) {
        if (!block) buttonModel.paint(graphics2D);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(buttonIconColor);
        graphics2D.setStroke(BASIC_STROKE);
        final int STANDARD_RECT_SIZE = 7 * availableHeight / 20;
        paintShape(STANDARD_RECT_SIZE, graphics2D);
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        super.setOnClickListener(component -> {
            block = true;
            repaint();
            onClickListener.onClick(component);
            SwingUtilities.invokeLater(()-> block = false);
        });
    }

    public Color getButtonIconColor() {
        return this.buttonIconColor;
    }

    public void setButtonIconColor(Color color) {
        this.buttonIconColor = color;
        repaint();
    }

    public void setHoverColor(Color hoverColor) {
        this.buttonModel.setHoverColor(hoverColor);
    }

    public void setHoverInListener(Runnable hoverInListener) {
        this.buttonModel.setHoverInListener(hoverInListener);
    }

    public void setHoverOutListener(Runnable hoverOutListener) {
        this.buttonModel.setHoverOutListener(hoverOutListener);
    }

    protected abstract void paintShape(final int STANDARD_RECT_SIZE, final Graphics2D GRAPHICS_2D);
}
