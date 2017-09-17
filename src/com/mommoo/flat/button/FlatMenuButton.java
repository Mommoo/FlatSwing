package com.mommoo.flat.button;

import com.mommoo.flat.component.OnClickListener;
import com.mommoo.util.ScreenManager;

import java.awt.*;
import java.awt.event.MouseEvent;

public class FlatMenuButton extends FlatButton{
	private static final ScreenManager SCREEN_MANAGER = ScreenManager.getInstance();
//	private final OnClickListener DEFAULT_CLICK_EVENT;
	private boolean isClicked;

//	private final FlatPopUpMenuList FLAT_POP_UP_MENU_LIST = new FlatPopUpMenuList();
	private boolean preventReOpen;
	
	public FlatMenuButton(){
//		FLAT_POP_UP_MENU_LIST.setMenuListFocusListener(new FlatPopUpMenuList.MenuListFocusListener() {
//			@Override
//			public void focusLost() {
//				preventReOpen = true;
//				new Thread(()->{
//					try {
//						Thread.sleep(500);
//						preventReOpen = false;
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}).start();
//				closeMenu();
//			}
//
//			@Override
//			public void focusGained() {
//			}
//		});
//		DEFAULT_CLICK_EVENT = (c)->{
//			if(FLAT_POP_UP_MENU_LIST.isListFocus()) {
//				FLAT_POP_UP_MENU_LIST.setListFocus(false);
//				return;
//			}
//			if (FLAT_POP_UP_MENU_LIST.getItemSize() == 0) return;
//
//			if (isClicked){
//				closeMenu();
//				return;
//			}
//			if(!preventReOpen) showMenu();
//			else preventReOpen = false;
//		};
//		super.setOnClickListener(DEFAULT_CLICK_EVENT);
	}
	
	public FlatMenuButton(String text){
		this();
		setText(text);
	}

//	public FlatPopUpMenuList getPopUpMenu(){
//		return FLAT_POP_UP_MENU_LIST;
//	}
	
	public void closeMenu(){
		isClicked = false;
//		FLAT_POP_UP_MENU_LIST.close();
		setBackground(getThemeColor());
	}
	
	public void showMenu(){
		isClicked = true;
		final Point SCREEN_LOCATION_POINT = this.getLocationOnScreen();
//		FLAT_POP_UP_MENU_LIST.show(SCREEN_LOCATION_POINT.x,this.getHeight()+SCREEN_LOCATION_POINT.y);
	}

	@Override
	public void setOnClickListener(OnClickListener onClickListener){
		this.onClickListener = onClickListener;
	}

	/** To show button selected, modify mouse-event using override MouseListener*/
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		if (!isMouseExited){
			if(onClickListener != null) {
//				DEFAULT_CLICK_EVENT.onClick(e.getComponent());
				onClickListener.onClick(e.getComponent());
			}
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		if (!isClicked) setBackground(getThemeColor());
		isMouseExited = true;
		super.mouseExited(e);
	}
}
