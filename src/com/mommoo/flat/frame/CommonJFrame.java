package com.mommoo.flat.frame;

import com.mommoo.util.ColorManager;
import com.mommoo.helper.ComponentResizer;
import com.sun.prism.j2d.paint.RadialGradientPaint;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import com.sun.prism.j2d.paint.MultipleGradientPaint.CycleMethod;
import sun.security.provider.SHA;

final class CommonJFrame extends JFrame {
	private static final int DEFAULT_BORDER_STROKE_WIDTH = 1;
	private static final int DEFAULT_SHADOW_DIP = 10;
	private static final Color DEFAULT_BORDER_COLOR = Color.BLACK;

	private final Rectangle2D SHADOW_SHAPE_RECT = new Rectangle2D.Float();
	private final ComponentResizer COMPONENT_RE_SIZER = new ComponentResizer();
	private final JPanel customizablePanel = new JPanel();

	private Color borderColor = DEFAULT_BORDER_COLOR;
	private int borderStrokeWidth = DEFAULT_BORDER_STROKE_WIDTH;
	private int shadowDip = DEFAULT_SHADOW_DIP;

	private ShadowPaintHandler shadowPaintHandler = new ShadowPaintHandler();

	CommonJFrame() {
		setUndecorated(true);
		setBorderColor(DEFAULT_BORDER_COLOR);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		initComponentResizer();
		setContentPane();
		applyBackgroundTransparent();
		attachUserCustomPanel();
	}
	
	private void initComponentResizer(){
		COMPONENT_RE_SIZER.setMinimumSize(new Dimension(40,40));
		COMPONENT_RE_SIZER.setDragInsets(new Insets(9,9,9,9));
		COMPONENT_RE_SIZER.setEnableChangeSize(new Dimension(2*shadowDip/3,2*shadowDip/3));
	}
	

	private void setContentPane(){
		JPanel container = new JPanel();
		container.setOpaque(true);
		container.setLayout(new BorderLayout());
		this.setContentPane(container);
	}
	
	private void applyBackgroundTransparent() {
		setBackground(ColorManager.getTransParentColor());
	}

	private void attachUserCustomPanel() {
		JPanel container = (JPanel)getContentPane();
		container.setBorder(BorderFactory.createEmptyBorder(shadowDip,shadowDip,shadowDip,shadowDip));
		container.add(customizablePanel, BorderLayout.CENTER);
	}

	private void drawShadow(Graphics2D g2) {



//		SHADOW_SHAPE_RECT.setFrame(0, 0, shadowDip, shadowDip);
//		g2.fill(SHADOW_SHAPE_RECT);
//
//		SHADOW_SHAPE_RECT.setFrame(WIDTH - shadowDip * 2, 0, shadowDip * 2, shadowDip * 2);
//		RadialGradientPaint topRightPaint = new RadialGradientPaint(SHADOW_SHAPE_RECT, DIST, COLORS, CycleMethod.NO_CYCLE);
//
//		g2.setPaint(topRightPaint);
//		g2.fill(new Rectangle2D.Float(WIDTH - shadowDip, 0, shadowDip, shadowDip));
//
//		RadialGradientPaint bottomLeftPaint = new RadialGradientPaint(
//				new Rectangle2D.Float(0, HEIGHT - shadowDip * 2, shadowDip * 2, shadowDip * 2), DIST,
//				COLORS, CycleMethod.NO_CYCLE);
//		g2.setPaint(bottomLeftPaint);
//		g2.fill(new Rectangle2D.Float(0, HEIGHT - shadowDip, shadowDip, shadowDip));
//
//		RadialGradientPaint bottomRightPaint = new RadialGradientPaint(
//				new Rectangle2D.Float(WIDTH - shadowDip * 2, HEIGHT - shadowDip * 2, shadowDip * 2,
//						shadowDip * 2),
//				DIST, COLORS, CycleMethod.NO_CYCLE);
//		g2.setPaint(bottomRightPaint);
//		g2.fill(new Rectangle2D.Float(WIDTH - shadowDip, HEIGHT - shadowDip, shadowDip, shadowDip));
//
//		/* RectGradient */
//		GradientPaint leftPaint = new GradientPaint(shadowDip, shadowDip, START_COLOR, 0, shadowDip, END_COLOR);
//		g2.setPaint(leftPaint);
//		g2.fill(new Rectangle2D.Float(0, shadowDip, shadowDip, HEIGHT - shadowDip * 2));
//
//		GradientPaint topPaint = new GradientPaint(shadowDip, shadowDip, START_COLOR, shadowDip, 0, END_COLOR);
//		g2.setPaint(topPaint);
//		g2.fillRect(shadowDip, 0, WIDTH - (shadowDip * 2), shadowDip);
//
//		GradientPaint rightPaint = new GradientPaint(WIDTH - shadowDip, shadowDip, START_COLOR, WIDTH,
//				shadowDip, END_COLOR);
//		g2.setPaint(rightPaint);
//		g2.fillRect(WIDTH - shadowDip, shadowDip, shadowDip, HEIGHT - shadowDip * 2);
//
//		GradientPaint bottomPaint = new GradientPaint(shadowDip, HEIGHT - shadowDip, START_COLOR, shadowDip,
//				HEIGHT, END_COLOR);
//		g2.setPaint(bottomPaint);
//		g2.fillRect(shadowDip, HEIGHT - shadowDip, WIDTH - (shadowDip * 2), shadowDip);

	}



	@Override
	public void paint(Graphics g) {
		super.paint(g);
		final Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		final int WIDTH = getWidth();
		final int HEIGHT = getHeight();

		shadowPaintHandler.changePaint(WIDTH, HEIGHT, shadowDip);

		/* RadialGradient */
		drawTopLeftCornerShadow(graphics2D);
		drawTopRightCornerShadow(graphics2D);
		drawBottomLeftCornerShadow(graphics2D);
		drawBottomRightCornerShadow(graphics2D);

		/* gradient */
		drawTopShadow(graphics2D);
		drawLeftShadow(graphics2D);
		drawRightShadow(graphics2D);
		drawBottomShadow(graphics2D);
	}

	private void drawTopLeftCornerShadow(Graphics2D graphics2D){
		graphics2D.setPaint(shadowPaintHandler.getTopLeftShadowGradient());
		graphics2D.fill(shadowPaintHandler.getTopLeftShadowShape());

	}

	private void drawTopRightCornerShadow(Graphics2D graphics2D){
		graphics2D.setPaint(shadowPaintHandler.getTopRightShadowGradient());
		graphics2D.fill(shadowPaintHandler.getTopRightShadowShape());
	}

	private void drawBottomLeftCornerShadow(Graphics2D graphics2D){
		graphics2D.setPaint(shadowPaintHandler.getBottomLeftShadowGradient());
		graphics2D.fill(shadowPaintHandler.getBottomLeftShadowShape());
	}

	private void drawBottomRightCornerShadow(Graphics2D graphics2D){
		graphics2D.setPaint(shadowPaintHandler.getBottomRightShadowGradient());
		graphics2D.fill(shadowPaintHandler.getBottomRightShadowShape());
	}

	private void drawTopShadow(Graphics2D graphics2D){
		graphics2D.setPaint(shadowPaintHandler.getTopShadowGradient());
		graphics2D.fill(shadowPaintHandler.getTopShadowShape());
	}

	private void drawLeftShadow(Graphics2D graphics2D){
		graphics2D.setPaint(shadowPaintHandler.getLeftShadowGradient());
		graphics2D.fill(shadowPaintHandler.getLeftShadowShape());
	}

	private void drawRightShadow(Graphics2D graphics2D){
		graphics2D.setPaint(shadowPaintHandler.getRightShadowGradient());
		graphics2D.fill(shadowPaintHandler.getRightShadowShape());
	}

	private void drawBottomShadow(Graphics2D graphics2D){
		graphics2D.setPaint(shadowPaintHandler.getBottomShadowGradient());
		graphics2D.fill(shadowPaintHandler.getBottomShadowShape());
	}

	@Override
	public void setLocation(int x,int y){
		super.setLocation(x-shadowDip, y-shadowDip);
	}

	@Override
	public void setLocation(Point p){
		this.setLocation(p.x,p.y);
	}

	@Override
	public Point getLocation(){
		return new Point(super.getLocation().x + shadowDip,super.getLocation().y + shadowDip);
	}

	@Override
	public int getX() {
		return super.getX() + shadowDip;
	}

	@Override
	public int getY() {
		return super.getY() + shadowDip;
	}

	@Override
	public Point getLocationOnScreen() {
		Point locationOnScreen = super.getLocationOnScreen();
		locationOnScreen.x += shadowDip;
		locationOnScreen.y += shadowDip;
		return locationOnScreen;
	}

	@Override
	public Dimension getSize(){
		final Dimension PARENT_DIMENSION = super.getSize();
		return new Dimension(PARENT_DIMENSION.width - shadowDip*2 ,
				PARENT_DIMENSION.height - shadowDip*2);
	}

	@Override
	public int getWidth(){
		return super.getSize().width;
	}

	@Override
	public int getHeight(){
		return super.getSize().height;
	}
	
	@Override
	public void setResizable(boolean reSizable){
		super.setResizable(reSizable);
		if (reSizable) COMPONENT_RE_SIZER.registerComponent(this);
		else COMPONENT_RE_SIZER.deregisterComponent(this);
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width + shadowDip * 2,height + shadowDip * 2);
	}

	@Override
	public void setSize(Dimension d) {
		super.setSize(d.width+ shadowDip * 2,d.height + shadowDip * 2);
	}

	void setBorderColor(Color color){
		this.borderColor = color;
		this.customizablePanel.setBorder(BorderFactory.createLineBorder(borderColor, borderStrokeWidth));
	}
	
	Color getBorderColor(){
		return this.borderColor;
	}
	
	void setBorderStrokeWidth(int borderWidth){
		this.borderStrokeWidth = borderWidth;
		this.customizablePanel.setBorder(BorderFactory.createLineBorder(borderColor, borderStrokeWidth));
	}
	
	int getBorderStrokeWidth(){
		return this.borderStrokeWidth;
	}
	
	void setShadowWidth(int shadowWidth){
		this.shadowDip = shadowWidth;
		((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(shadowDip,shadowDip,shadowDip,shadowDip));
	}
	
	int getShadowWidth(){
		return this.shadowDip;
	}

	JPanel getCustomizablePanel(){
		return this.customizablePanel;
	}
	
	public static void main(String args[]){
//		CommonJFrame jf = new CommonJFrame();
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(500,500);
		jf.setLocation(300,300);
		jf.setLayout(new BorderLayout());
		jf.add(new JButton("버튼"));
		jf.setVisible(true);
	}
}
