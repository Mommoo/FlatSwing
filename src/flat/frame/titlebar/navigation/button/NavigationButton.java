package flat.frame.titlebar.navigation.button;

import flat.component.FlatComponent;
import flat.frame.titlebar.navigation.listener.FocusGainListener;
import flat.frame.titlebar.navigation.listener.FocusLostListener;
import flat.frame.titlebar.navigation.listener.MouseFocusAnimationListener;

import java.awt.*;

public abstract class NavigationButton extends FlatComponent {
    private static final long serialVersionUID = 1L;
    private static final BasicStroke BASIC_STROKE = new BasicStroke(2.0f);
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

    private Color buttonIconColor = Color.BLACK;
    private Color focusGainColor = Color.LIGHT_GRAY;

    private MouseFocusAnimationListener animationListener;

    NavigationButton(){
        setCursor(HAND_CURSOR);
        setMouseListener();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D G_2D = (Graphics2D)g;
        G_2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        G_2D.setColor(buttonIconColor);
        G_2D.setStroke(BASIC_STROKE);
        final int STANDARD_RECT_SIZE = 7*getHeight()/20;
        paintShape(STANDARD_RECT_SIZE ,G_2D);
    }

    private void setMouseListener(){
        animationListener = new MouseFocusAnimationListener(this, focusGainColor, Color.DARK_GRAY);
        addMouseListener(animationListener);
    }

    public void setButtonIconColor(Color color){
        this.buttonIconColor = color;
        repaint();
    }

    public Color getButtonIconColor(){
        return this.buttonIconColor;
    }

    public void setFocusColor(Color focusGainColor, Color focusLostColor){
        this.focusGainColor = focusGainColor;
        animationListener.setFocusAnimColor(focusGainColor, focusLostColor);
    }

    public void setFocusGainListener(FocusGainListener focusGainListener){
        animationListener.setFocusGainListener(focusGainListener);
    }

    public void setFocusLostListener(FocusLostListener focusLostListener){
        animationListener.setFocusLostListener(focusLostListener);
    }

    protected abstract void paintShape(final int STANDARD_RECT_SIZE, final Graphics2D GRAPHICS_2D);
}
