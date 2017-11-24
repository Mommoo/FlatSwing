package com.mommoo.flat.component.seekbar;

import com.mommoo.flat.component.FlatMouseAdapter;
import com.mommoo.util.CursorType;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.function.IntConsumer;

public class FlatSeekBar extends Component {
    private static final ScreenManager SCREEN = ScreenManager.getInstance();

    private final RoundRectangle2D.Float BOUNDS = new RoundRectangle2D.Float(0,0,0,0, SCREEN.dip2px(5), SCREEN.dip2px(5));
    private final FlatSeekBarColor SEEK_BAR_COLOR = new FlatSeekBarColor();
    private final FlatSeekBarHandle SEEK_BAR_HANDLE = new FlatSeekBarHandle();

    private double handleSize = (double)SCREEN.dip2px(20);
    private final Point2D.Double seekBarHandleCenter = new Point2D.Double(handleSize /2d, handleSize /2d);
    private boolean once;
    private double dragX;

    private IntConsumer onValueChangeListener = value -> {};
    private int maxValue = 100;
    private int minValue = 0;
    private int offset = 0;

    public FlatSeekBar() {
        MouseHoldEvent mouseHoldEvent = new MouseHoldEvent();
        addMouseListener(mouseHoldEvent);
        addMouseMotionListener(mouseHoldEvent);
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());

        FlatSeekBar seekBar = new FlatSeekBar();
        seekBar.setOffset(10)
                .setMinValue(10)
                .setMaxValue(50)
                .setOnValueChangeListener(System.out::println);
//                .setPreferredSize(new Dimension(400,50));

        frame.getContentPane().add(seekBar);
        frame.setVisible(true);

    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        validateArguments();
        validateValue();

        Graphics2D graphics2D = (Graphics2D)g;

        graphicsQualityUp(graphics2D);

        drawBackgroundGuideRect(graphics2D);
        drawFrontGuideRect(graphics2D);
        drawSeekBarHandle(graphics2D);
    }

    private void validateArguments(){
        String exception;

        if (maxValue <= minValue) {
            exception = "maxValue can't smaller than minValue";
        } else if (maxValue < offset) {
            exception = "offset can't bigger than maxValue";
        } else if (offset < minValue) {
            exception = "offset can't smaller than minValue";
        } else {
            return;
        }

        throw new IllegalArgumentException(exception);
    }

    private void validateValue(){
        handleSize = Math.min(handleSize, Math.min(getWidth(), getHeight()));
        SEEK_BAR_HANDLE.setSize((int) handleSize, (int) handleSize);
        dragX = Math.min(Math.max(0, dragX), getWidth() - handleSize);

        if (!once){
            double targetW = getWidth() - handleSize;
            dragX = targetW * (offset - minValue ) / (maxValue - minValue);
            seekBarHandleCenter.x += dragX;
            once = true;
        }
    }

    private void graphicsQualityUp(Graphics2D graphics2D){
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void drawBackgroundGuideRect(Graphics2D graphics2D){
        double guideRectWidth  = getWidth() - handleSize;
        double guideRectHeight = getHeight() / 6d;

        double x = handleSize/2d;
        double y = (getHeight() - guideRectHeight)/2d;
        BOUNDS.setFrame(x, y, guideRectWidth, guideRectHeight);
        graphics2D.setColor(getBackgroundSeekBarColor());
        graphics2D.fill(BOUNDS);
    }

    private void drawFrontGuideRect(Graphics2D graphics2D){
        double guideRectWidth  = dragX;
        double guideRectHeight = getHeight() / 6d;

        double x = handleSize/2d;
        double y = (getHeight() - guideRectHeight)/2d;
        BOUNDS.setFrame(x,y, guideRectWidth, guideRectHeight);
        graphics2D.setPaint(getFrontSeekBarColor());
        graphics2D.fill(BOUNDS);
    }

    private void drawSeekBarHandle(Graphics2D graphics2D){
        graphics2D.translate(dragX,(getHeight() - handleSize)/2d);
        SEEK_BAR_HANDLE.paint(graphics2D);
    }

    public Color getBackgroundSeekBarColor(){
        return SEEK_BAR_COLOR.getBackgroundSeekBarColor();
    }

    public FlatSeekBar setBackgroundSeekBarColor(Color backgroundSeekBarColor){
        SEEK_BAR_COLOR.setBackgroundSeekBarColor(backgroundSeekBarColor);
        return this;
    }

    public Color getFrontSeekBarColor(){
        return SEEK_BAR_COLOR.getFrontSeekBarColor();
    }

    public FlatSeekBar setFrontSeekBarColor(Color frontSeekBarColor){
        SEEK_BAR_COLOR.setFrontSeekBarColor(frontSeekBarColor);
        return this;
    }

    public Color getSeekBarHandleColor(){
        return SEEK_BAR_COLOR.getSeekBarHandleColor();
    }

    public FlatSeekBar setSeekBarHandleColor(Color seekBarHandleColor){
        SEEK_BAR_COLOR.setSeekBarHandleColor(seekBarHandleColor);
        SEEK_BAR_HANDLE.setSeekBarHandleColor(seekBarHandleColor);
        return this;
    }

    public int getMaxValue(){
        return maxValue;
    }

    public FlatSeekBar setMaxValue(int maxValue){
        this.maxValue = maxValue;
        return this;
    }

    public int getMinValue(){
        return minValue;
    }

    public FlatSeekBar setMinValue(int minValue){
        this.minValue = minValue;
        if(this.offset < minValue){
            this.offset = minValue;
        }
        return this;
    }

    public int getOffset(){
        return offset;
    }

    public FlatSeekBar setOffset(int offset){
        this.offset = Math.max(minValue, offset);
        return this;
    }

    public int getCurrentValue(){
        double targetW = getWidth() - handleSize;
        double percent = Math.max(0, Math.min(100, dragX * 100d / targetW));
        return minValue + (int)((maxValue - minValue) * percent / 100d);
    }

    public int getHandleSize(){
        return (int)this.handleSize;
    }

    public FlatSeekBar setHandleSize(int handleSize){
        this.handleSize = handleSize;
        return this;
    }

    public IntConsumer getOnValueChangeListener() {
        return onValueChangeListener;
    }

    public FlatSeekBar setOnValueChangeListener(IntConsumer onValueChangeListener){
        this.onValueChangeListener = onValueChangeListener;
        return this;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dimension = super.getPreferredSize();

        if (dimension.width == 1 || dimension.height == 1){
            dimension.setSize(handleSize * 10 , handleSize);
        }
        return dimension;
    }

    private class MouseHoldEvent extends FlatMouseAdapter {
        private boolean isDragReady;
        private double previousX;
        private int previousValue;

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            if (isDragReady){
                previousX = e.getX();
                SEEK_BAR_HANDLE.grab();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (isDragReady){
                seekBarHandleCenter.setLocation(dragX + handleSize/2d, handleSize/2d);
                SEEK_BAR_HANDLE.unGrab();
                e.getComponent().repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            float userX = e.getX();
            float userY = e.getY();
            isDragReady = Math.pow(userX - seekBarHandleCenter.x, 2) + Math.pow(userY - getHeight()/2d, 2) <= Math.pow(handleSize/4d, 2);

            if (isDragReady) {
                setCursor(CursorType.HAND.getCursor());
            } else {
                setCursor(CursorType.DEFAULT.getCursor());
            }

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if (isDragReady){
                dragX += e.getX() - previousX;
                previousX = e.getX();

                if (previousValue != getCurrentValue()){
                    previousValue = getCurrentValue();
                    onValueChangeListener.accept(previousValue);
                }

                e.getComponent().repaint();
            }
        }
    }
}
