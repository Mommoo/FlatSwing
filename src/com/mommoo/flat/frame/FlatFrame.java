package com.mommoo.flat.frame;

import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.listener.OnExitListener;
import com.mommoo.flat.frame.listener.OnMinimizeListener;
import com.mommoo.flat.frame.listener.OnSizeChangeListener;
import com.mommoo.flat.frame.titlebar.navigation.button.NavigationButtonType;
import com.mommoo.flat.frame.titlebar.navigation.listener.NavigationControlListener;
import com.mommoo.util.ImageManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class FlatFrame {
	private final CommonJFrame COMMON_FRAME = new CommonJFrame();
	private final CommonTitleBar TITLE_BAR = new CommonTitleBar();

	private JPanel container = createContainer();
	private boolean isCenterLocation, isWindowExit, isEnableSizeButton;
	private ControlListener controlListener = new ControlListener();

	public FlatFrame() {
		setLocation(0,0);
		setIconImage(ImageManager.TEST);
		addComponent();
		setTitleBarDragEventListener();
		setTitleBarControlListener();
	}

	private JPanel createContainer(){
		JPanel container = new JPanel(new BorderLayout());
		container.setOpaque(true);
		container.setBackground(Color.WHITE);
		return container;
	}
	
	private void addComponent(){
		JPanel contentPane = (JPanel)COMMON_FRAME.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(TITLE_BAR, BorderLayout.NORTH);
		contentPane.add(container, BorderLayout.CENTER);
	}
	
	private void setTitleBarDragEventListener(){
		DragEventListener dragEventListener = new DragEventListener();
		TITLE_BAR.addMouseListener(dragEventListener);
		TITLE_BAR.addMouseMotionListener(dragEventListener);
	}

	private void setTitleBarControlListener(){
		TITLE_BAR.setControlListener(controlListener);
	}

	public JPanel getContainer() {
		return container;
	}

	public void setContainer(JPanel container){
		COMMON_FRAME.getContentPane().remove(1);
		COMMON_FRAME.getContentPane().add(container, BorderLayout.CENTER);
		this.container = container;
	}

	public Dimension getAvailableDimension(){
		return COMMON_FRAME
				.getAvailableDimension()
				.subDimension(0, getTitleBarHeight());
	}

	public void setTitle(String title){
		TITLE_BAR.setTitle(title);
	}

	public String getProcessTitle(){
		return COMMON_FRAME.getTitle();
	}

	public void setProcessTitle(String title) {
		COMMON_FRAME.setTitle(title);
	}
	
	public String getTitle(){
		return TITLE_BAR.getTitle();
	}
	
	public void setTitleColor(Color color){
		TITLE_BAR.setTitleColor(color);
	}
	
	public Color getTitleColor(){
		return TITLE_BAR.getTitleColor();
	}

	public Font getTitleFont(){
		return TITLE_BAR.getTitleFont();
	}

	public void setTitleFont(Font font) {
		TITLE_BAR.setTitleFont(font);
	}
	
	public static int getTitleBarHeight(){
		return CommonTitleBar.getTitleBarHeight();
	}
	
	public void setTitleBarColor(Color color){
		TITLE_BAR.setBackground(color);
	}

	public Color getTitleBarColor(){
		return TITLE_BAR.getBackground();
	}

	public void setMenuButtonColor(Color color){
		TITLE_BAR.setButtonIconColor(color);
	}

	public Color getMenuButtonColor(){
		return TITLE_BAR.getButtonIconColor();
	}

	public void setProcessIconImage(Image image){
		this.COMMON_FRAME.setIconImage(image);
	}
	
	public void setIconImage(Image image){
		if (image == null) return;
		this.TITLE_BAR.setIconImage(image);
	}

	public void removeOnIconClickListener(){
		this.TITLE_BAR.removeOnIconClickListener();
	}

	public OnClickListener getOnIconClickListener(){
		return this.TITLE_BAR.getOnIconClickListener();
	}

	public void setOnIconClickListener(OnClickListener onIconClickListener){
		this.TITLE_BAR.setOnIconClickListener(onIconClickListener);
	}

	public Color getIconBackgroundColor(){
		return this.TITLE_BAR.getIconBackgroundColor();
	}

	public void setIconBackgroundColor(Color backgroundColor){
		this.TITLE_BAR.setIconBackgroundColor(backgroundColor);
	}

	public void removeIconImage(){
		this.TITLE_BAR.removeIconImage();
	}

	public void removeControlPanel(){
		this.TITLE_BAR.removeControlPanel();
	}
	
	public void setBorderColor(Color color) {
		this.COMMON_FRAME.setBorderColor(color);
	}

	public Color getBorderColor() {
		return this.COMMON_FRAME.getBorderColor();
	}
	
	public void setBorderStrokeWidth(int borderStrokeWidth){
		COMMON_FRAME.setBorderStrokeWidth(borderStrokeWidth);
	}
	
	public int getBorderStrokeWidth(){
		return COMMON_FRAME.getBorderStrokeWidth();
	}
	
	public void setShadowWidth(int shadowWidth){
		this.COMMON_FRAME.setShadowWidth(shadowWidth);
	}
	
	public int getShadowWidth(){
		return COMMON_FRAME.getShadowWidth();
	}

	public Insets getShadowInsets(){
		return COMMON_FRAME.getShadowInsets();
	}
	
	public void setSize(int width,int height){
		COMMON_FRAME.setSize(width,height);
	}

	public void setSize(Dimension dimension){
		this.setSize(dimension.width, dimension.height);
	}
	
	public Dimension getSize(){
		return COMMON_FRAME.getSize();
	}
	
	public void setLocation(int x, int y){
		this.COMMON_FRAME.setLocation(x, y);
	}

	public void setLocation(Point point){
		this.setLocation(point.x, point.y);
	}

	public Point getLocation(){
		return this.COMMON_FRAME.getLocation();
	}

	public void pack(){
		COMMON_FRAME.pack();
	}

	public void setLocationOnScreenCenter(){
		isCenterLocation = true;
	}
	
	public void setWindowExit(boolean windowExit){
		isWindowExit = windowExit;
	}
	
	public void setResizable(boolean reSizable){
		COMMON_FRAME.setResizable(reSizable);
	}
	
	public boolean isResizable(){
		return COMMON_FRAME.isResizable();
	}

	public void setAlwaysOnTop(boolean alwaysOnTop){
		COMMON_FRAME.setAlwaysOnTop(alwaysOnTop);
	}

	public void setUtilityType(){
		COMMON_FRAME.setType(JFrame.Type.UTILITY);
	}

	public void setEnableSizeButton(boolean isEnable){
		isEnableSizeButton = isEnable;
	}

	public void setOnMinimizeListener(OnMinimizeListener onMinimizeListener){
		this.controlListener.onMinimizeListener = onMinimizeListener;
	}

	public void setOnChangeListener(OnSizeChangeListener onSizeChangeListener){
		this.controlListener.onSizeChangeListener = onSizeChangeListener;
	}

	public void setOnExitListener(OnExitListener onExitListener){
		this.controlListener.onExitListener = onExitListener;
	}

	public void setTitleBarVisible(boolean visible){
		this.TITLE_BAR.setVisible(visible);
	}

	public JFrame getJFrame(){
		return COMMON_FRAME;
	}
	
	public void show() {
		if (isCenterLocation) {
			ScreenManager screenManager = ScreenManager.getInstance();
			this.COMMON_FRAME.setLocation((screenManager.getWindowWidth() - getSize().width )/2, (screenManager.getWindowHeight() - getSize().height)/2);
		}

		COMMON_FRAME.setVisible(true);
	}
	
	public void hide(){
		COMMON_FRAME.setVisible(false);
	}

	private class ControlListener implements NavigationControlListener {
		private ScreenManager screenManager = ScreenManager.getInstance();
		private boolean isSizeUp;
		private Point frameLocation;
		private Dimension frameSize;

		private OnExitListener onExitListener = ()->{};
		private OnSizeChangeListener onSizeChangeListener = (dimen, location) -> {};
		private OnMinimizeListener onMinimizeListener = () -> {};

		@Override
		public void onNavigationClick(NavigationButtonType buttonType) {
			switch (buttonType){
				case MINI :

					onMinimizeListener.onMinimize();

					COMMON_FRAME.setState(Frame.ICONIFIED);

					break;

				case SIZE :

					if (!isEnableSizeButton) return;

					isSizeUp = !isSizeUp;

					if (isSizeUp){

						frameLocation = FlatFrame.this.getLocation();
						frameSize = FlatFrame.this.getSize();
						FlatFrame.this.setSize(screenManager.getWindowWidth(),screenManager.getWindowHeight());
						FlatFrame.this.setLocation(0, 0);
					} else {

						FlatFrame.this.setSize(frameSize);
						FlatFrame.this.setLocation(frameLocation);
					}

					onSizeChangeListener.onSizeChanged(FlatFrame.this.getSize(), FlatFrame.this.getLocation());

					break;

				case EXIT :

					onExitListener.onExit();

					if (isWindowExit) System.exit(1);
					else COMMON_FRAME.dispose();

					break;
			}
		}
	}

	private class DragEventListener extends MouseAdapter {
		private int pressX, pressY;
		private int moveX, moveY;

		@Override
		public void mousePressed(MouseEvent e) {
			pressX = e.getX();
			pressY = e.getY();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			moveX = e.getX() - pressX;
			moveY = e.getY() - pressY;
			COMMON_FRAME.setLocation(COMMON_FRAME.getLocation().x + moveX, COMMON_FRAME.getLocation().y + moveY);
		}
	}
}
