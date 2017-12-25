package com.mommoo.flat.frame;

import com.mommoo.flat.border.FlatShadowBorder;
import com.mommoo.helper.ComponentResizer;
import com.mommoo.util.ColorManager;
import com.mommoo.util.ComputableDimension;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

final class CommonJFrame extends JFrame {
    private static final int SHADOW_WIDTH = ScreenManager.getInstance().dip2px(20);
    private final ComponentResizer COMPONENT_RE_SIZER = createComponentResizer();

    CommonJFrame() {
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setShadowPane();
        setContentPane(createContentPane());
        setBorderLine(Color.GRAY, 1);
    }

    private static JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
        contentPane.setBackground(Color.WHITE);
        return contentPane;
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            CommonJFrame jf = new CommonJFrame();
            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jf.setSize(500, 500);
            jf.setLocation(300, 300);
            jf.setLayout(new BorderLayout());
            jf.setResizable(true);
            jf.setShadowWidth(10);
            jf.getContentPane().add(new JButton("BUTTON"));
            jf.setVisible(true);
        });
    }

    private ComponentResizer createComponentResizer() {
        ComponentResizer componentResizer = new ComponentResizer();
        componentResizer.setMinimumSize(new Dimension(40, 40));
        componentResizer.setDragInsets(new Insets(9, 9, 9, 9));
        componentResizer.setEnableChangeSize(new Dimension(40,40));
        return componentResizer;
    }

    private void setShadowPane() {
        JPanel shadowPane = new JPanel(new BorderLayout());
        shadowPane.setOpaque(true);
        shadowPane.setBackground(Color.WHITE);
        shadowPane.setBorder(new FlatShadowBorder(SHADOW_WIDTH));
        super.setContentPane(shadowPane);
    }

    private void setBorderLine(Color color, int thickness){
        ((JPanel)getContentPane()).setBorder(BorderFactory.createLineBorder(color, thickness));
    }

    @Override
    public Container getContentPane() {
        return (Container) getShadowPane().getComponent(0);
    }

    @Override
    public void setContentPane(Container contentPane) {
        getShadowPane().add(contentPane, BorderLayout.CENTER, 0);
    }

    private JPanel getShadowPane() {
        return (JPanel) super.getContentPane();
    }

    @Override
    public void setResizable(boolean reSizable) {
        super.setResizable(reSizable);
        if (reSizable) COMPONENT_RE_SIZER.registerComponent(this);
        else COMPONENT_RE_SIZER.deregisterComponent(this);
    }

    ComputableDimension getAvailableDimension(){
        Insets insets = getShadowPane().getInsets();
        return new ComputableDimension(getSize())
                .setMinimumSize(0,0)
                .subDimension(insets.left, insets.top)
                .subDimension(insets.right, insets.bottom);
    }

    Color getBorderColor() {
        JPanel borderPanel = (JPanel) getContentPane();
        LineBorder lineBorder = (LineBorder)borderPanel.getBorder();
        return lineBorder.getLineColor();
    }

    void setBorderColor(Color color) {
        JPanel borderPanel = (JPanel) getContentPane();
        LineBorder lineBorder = (LineBorder)borderPanel.getBorder();
        setBorderLine(color, lineBorder.getThickness());
    }

    int getBorderStrokeWidth() {
        JPanel borderPanel = (JPanel) getContentPane();
        LineBorder lineBorder = (LineBorder)borderPanel.getBorder();
        return lineBorder.getThickness();
    }

    void setBorderStrokeWidth(int borderWidth) {
        JPanel borderPanel = (JPanel) getContentPane();
        LineBorder lineBorder = (LineBorder)borderPanel.getBorder();
        setBorderLine(lineBorder.getLineColor(), borderWidth);
    }

    int getShadowWidth() {
        return ((FlatShadowBorder)getShadowPane().getBorder()).getShadowWidth();
    }

    void setShadowWidth(int shadowWidth) {
        if (shadowWidth < 0) shadowWidth = 0;
        getShadowPane().setBorder(new FlatShadowBorder(shadowWidth));
    }

    Insets getShadowInsets(){
        return getShadowPane().getBorder().getBorderInsets(getShadowPane());
    }
}
