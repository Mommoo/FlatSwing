package com.mommoo.flat.frame.titlebar.navigation.button;

import java.awt.*;

/**
 * Created by mommoo on 2017-07-13.
 */
public class NavigationButtonFactory {

    private NavigationButtonFactory(){}

    private static NavigationButton createNavigationButton(NavigationButtonType type){
        switch(type){
            case MINI : return new MiniMumNavigationButton();
            case SIZE : return new SizeNavigationButton();
            case EXIT : return new ExitNavigationButton();
            default : return null;
        }
    }

    public static NavigationButton[] createNavigationButtonArray(){
        NavigationButtonType[] buttonTypes = NavigationButtonType.values();
        NavigationButton[] buttonArray = new NavigationButton[buttonTypes.length];
        for (int i = 0 ; i < buttonTypes.length ; i++){
            buttonArray[i] = NavigationButtonFactory.createNavigationButton(buttonTypes[i]);
        }
        return buttonArray;
    }
}
