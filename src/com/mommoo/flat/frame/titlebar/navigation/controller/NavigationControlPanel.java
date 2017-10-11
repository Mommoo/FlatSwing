package com.mommoo.flat.frame.titlebar.navigation.controller;


import com.mommoo.flat.frame.titlebar.navigation.button.NavigationButton;
import com.mommoo.flat.frame.titlebar.navigation.button.NavigationButtonFactory;
import com.mommoo.flat.frame.titlebar.navigation.button.NavigationButtonType;
import com.mommoo.flat.frame.titlebar.navigation.listener.NavigationControlListener;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mommoo on 2017-07-10.
 */
public class NavigationControlPanel extends JPanel {
    private static final ScreenManager SCREEN_MANAGER = ScreenManager.getInstance();
    private static final int GAP = SCREEN_MANAGER.dip2px(5);

    private final NavigationButton[] NAVIGATION_BTN_ARRAY = NavigationButtonFactory.createNavigationButtonArray();

    public NavigationControlPanel(int titleBarHeight) {
        setLayout(new GridLayout(0, NAVIGATION_BTN_ARRAY.length, GAP, GAP));
        setMinimumSize(getMinimumPanelDimen(titleBarHeight));
        addNavigationButtons(getButtonDimen(titleBarHeight));
        setTransparent();
    }

    private Dimension getButtonDimen(int titleBarHeight){
        return new Dimension((int)(titleBarHeight*1.5), titleBarHeight);
    }

    private Dimension getMinimumPanelDimen(int titleBarHeight){
        return new Dimension((getButtonDimen(titleBarHeight).width * NAVIGATION_BTN_ARRAY.length) + (GAP * (NAVIGATION_BTN_ARRAY.length - 1)), -1);
    }

    private void addNavigationButtons(Dimension buttonDimen) {
        for (Component comp : NAVIGATION_BTN_ARRAY) {
            comp.setPreferredSize(buttonDimen);
            comp.setMinimumSize(buttonDimen);
            add(comp);
        }
    }

    private void setTransparent() {
        setOpaque(false);
    }

    private int findArrayIndexExitButton(){
        return NavigationButtonType.EXIT.ordinal();
    }

    private void setExitButtonColor(){
        int index = findArrayIndexExitButton();
        NAVIGATION_BTN_ARRAY[index].setHoverColor(Color.RED.darker());
        final Color originalMenuIconColor = NAVIGATION_BTN_ARRAY[index].getButtonIconColor();
        NAVIGATION_BTN_ARRAY[index].setHoverInListener(()-> NAVIGATION_BTN_ARRAY[index].setButtonIconColor(Color.WHITE));
        NAVIGATION_BTN_ARRAY[index].setHoverOutListener(()-> NAVIGATION_BTN_ARRAY[index].setButtonIconColor(originalMenuIconColor));
    }

    public void setButtonColor(Color color){
        for (NavigationButton navigationButton : NAVIGATION_BTN_ARRAY){
            navigationButton.setBackground(color);
            navigationButton.setHoverColor(color.darker());
        }

        setExitButtonColor();
    }

    public void setButtonIconColor(Color color){
        for (NavigationButton navigationButton : NAVIGATION_BTN_ARRAY){
            navigationButton.setButtonIconColor(color);
        }
    }

    public Color getButtonIconColor(){
        return NAVIGATION_BTN_ARRAY[0].getButtonIconColor();
    }

    public void setOnControlListener(NavigationControlListener controlListener) {
        NavigationButtonType[] buttonTypes = NavigationButtonType.values();

        for (int i = 0 , size = NAVIGATION_BTN_ARRAY.length ; i < size ; i++){
            NavigationButtonType buttonType = buttonTypes[i];
            NAVIGATION_BTN_ARRAY[i].setOnClickListener(component -> controlListener.onNavigationClick(buttonType));
        }
    }
}
