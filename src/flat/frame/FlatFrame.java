package flat.frame;

import flat.frame.titlebar.navigation.button.NavigationButtonType;
import flat.frame.titlebar.navigation.listener.NavigationControlListener;
import util.ImageManager;
import util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class FlatFrame {
	private static final Color DEFAULT_THEME_COLOR = Color.decode("#eeeeee");
	private final CommonJFrame COMMON_FRAME = new CommonJFrame();
	private final CommonTitleBar TITLE_BAR = new CommonTitleBar();
	private final JPanel USER_CUSTOMIZABLE_PANEL = new JPanel();

	private boolean isCenterLocation, isWindowExit, isEnableSizeButton;

	public FlatFrame() {
		initFrame();
		initUserCustomizablePanel();
		addComponent();
		setTitleBarDragEventListener();
		setTitleBarControlListener();
	}

	private void initFrame(){
		setThemeColor(DEFAULT_THEME_COLOR);
		setLocation(0,0);
		setImageIcon(ImageManager.TEST);
	}

	private void initUserCustomizablePanel(){
		USER_CUSTOMIZABLE_PANEL.setLayout(new BorderLayout());
	}
	
	private void addComponent(){
		JPanel customizablePanel = COMMON_FRAME.getCustomizablePanel();
		customizablePanel.setLayout(new BorderLayout());
		customizablePanel.add(TITLE_BAR, BorderLayout.NORTH);
		customizablePanel.add(USER_CUSTOMIZABLE_PANEL, BorderLayout.CENTER);
	}
	
	private void setTitleBarDragEventListener(){
		DragEventListener dragEventListener = new DragEventListener();
		TITLE_BAR.addMouseListener(dragEventListener);
		TITLE_BAR.addMouseMotionListener(dragEventListener);
	}

	private void setTitleBarControlListener(){
		TITLE_BAR.setControlListener(new ControlListener());
	}

	public void setThemeColor(Color color){
		TITLE_BAR.setThemeColor(color);
		USER_CUSTOMIZABLE_PANEL.setBackground(color.brighter());
	}

	public JPanel getContainer() {
		return USER_CUSTOMIZABLE_PANEL;
	}

	public void setTitle(String title){
		TITLE_BAR.setTitle(title);
	}
	
	public String getTitle(){
		return TITLE_BAR.getTitle();
	}
	
	public void setTitleColor(Color color){
		TITLE_BAR.setTitleColor(color);
	}
	
	public Color getTitleColor(Color color){
		return TITLE_BAR.getTitleColor();
	}
	
	public static int getTitleBarHeight(){
		return CommonTitleBar.getTitleBarHeight();
	}
	
	public void setTitleBarColor(Color color){
		TITLE_BAR.setThemeColor(color);
	}

	public Color getTitleBarColor(){
		return TITLE_BAR.getThemeColor();
	}

	public void setMenuButtonColor(Color color){
		TITLE_BAR.setButtonIconColor(color);
	}

	public Color getMenuButtonColor(){
		return TITLE_BAR.getButtonIconColor();
	}
	
	public void setImageIcon(Image image){
		if (image == null) return;
		this.COMMON_FRAME.setIconImage(image);
		this.TITLE_BAR.setIconImage(image);
	}

	public void removeImageIcon(){
		this.COMMON_FRAME.setIconImage(null);
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

		@Override public void onNavigationClick(NavigationButtonType buttonType) {
			switch (buttonType){
				case MINI :

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

					break;

				case EXIT :

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
