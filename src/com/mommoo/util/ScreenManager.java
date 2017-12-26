package com.mommoo.util;

import java.awt.*;

public class ScreenManager {
	private int screenWidth;
	private int screenHeight;
	private int windowWidth;
	private int windowHeight;

	private static final ScreenManager INSTANCE = new ScreenManager();

	private ScreenManager(){
		readyToGetAccurateResolution();
		initScreenSize();
		initWindowSize();
	}

	/* To synchronize screen resolution */
	private static void readyToGetAccurateResolution(){
		/* At several graphics environments , sometimes JFrame graphics shutdown because of transparent background color
		 * This vm options prevent shutdown graphics  */
		System.setProperty("sun.java2d.opengl","true");
		Toolkit.getDefaultToolkit().getScreenResolution();
	}

	private void initScreenSize(){
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = res.width;
		screenHeight = res.height;
	}

	private void initWindowSize(){
		Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		windowWidth = winSize.width;
		windowHeight = winSize.height;
	}

	public static ScreenManager getInstance(){
		return INSTANCE;
	}

	public int getScreenWidth(){
		return screenWidth;
	}

	public int getScreenHeight(){
		return screenHeight;
	}

	public int getWindowWidth(){
		return windowWidth;
	}

	public int getWindowHeight(){
		return windowHeight;
	}

	public int dip2px(int px){
		return px*screenWidth/1000;
	}
	
	public int px2dip(int dip){
		return dip/screenWidth/1000;
	}
}
