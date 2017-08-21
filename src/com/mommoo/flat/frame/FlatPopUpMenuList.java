package com.mommoo.flat.frame;

import com.mommoo.flat.component.OnPositionClickListener;
import com.mommoo.flat.label.FlatMenu;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

/**
 * Created by mommoo on 2017-03-17.
 */
public class FlatPopUpMenuList {

    private static final ScreenManager SCREEN_MANAGER = ScreenManager.getInstance();
    private static final int PADDING = SCREEN_MANAGER.dip2px(2);
    private final CommonJFrame COMMON_JFRAME = new CommonJFrame();
    private final ArrayList<FlatMenu> FLAT_MENU_LIST = new ArrayList<>();
    private Color mouseEnterMenuColor;
    private Color menuColor;
    private Font menuFont;
    private Color mouseEnterMenuTextColor;
    private Color menuTextColor;
    private OnPositionClickListener onPositionClickListener;
    private boolean isWindowFocusLost;

    public interface MenuListFocusListener{
        public void focusLost();
        public void focusGained();
    }

    private MenuListFocusListener menuListFocusListener;

    public FlatPopUpMenuList(){
        COMMON_JFRAME.setType(JFrame.Type.UTILITY);
        COMMON_JFRAME.getCustomizablePanel().setBackground(Color.WHITE);
        COMMON_JFRAME.getCustomizablePanel().setLayout(new GridLayout(-1,1));
        COMMON_JFRAME.getCustomizablePanel().setBorder(BorderFactory.createEmptyBorder(PADDING,PADDING,PADDING,PADDING));
        COMMON_JFRAME.setAlwaysOnTop(true);
        COMMON_JFRAME.setShadowWidth(SCREEN_MANAGER.dip2px(2));
        COMMON_JFRAME.addWindowFocusListener(new WindowFocusListener(){

            @Override
            public void windowGainedFocus(WindowEvent e) {
                if(menuListFocusListener != null) menuListFocusListener.focusGained();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                if(menuListFocusListener != null) menuListFocusListener.focusLost();
                isWindowFocusLost = true;
                close();
                new Thread(){
                    @Override
                    public void run(){
                        try {
                            Thread.sleep(70);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isWindowFocusLost = false;
                    }
                }.start();
            }

        });
    }

    public void setListFocus(boolean focus){
        isWindowFocusLost = focus;
    }

    public boolean isListFocus(){
        return isWindowFocusLost;
    }

    public void setMenuListFocusListener(MenuListFocusListener menuListFocusListener){
        this.menuListFocusListener = menuListFocusListener;
    }

    public void addMenu(FlatMenu menu){
        FLAT_MENU_LIST.add(menu);
        COMMON_JFRAME.getCustomizablePanel().add(menu);
        if(this.mouseEnterMenuColor != null) menu.setMouseEnterMenuColor(mouseEnterMenuColor);
        if(this.menuColor != null) menu.setMenuColor(menuColor);
        if(this.menuFont != null) menu.setFont(menuFont);
        if(this.menuTextColor != null) menu.setForeground(menuTextColor);
        if(this.mouseEnterMenuTextColor != null) menu.setMouseEnterMenuTextColor(mouseEnterMenuTextColor);
        if(this.onPositionClickListener != null) menu.setOnClickListener(c-> onPositionClickListener.onClick(FLAT_MENU_LIST.size()-1,menu));
    }

    public void removeAllMenu(){
        FLAT_MENU_LIST.clear();
        COMMON_JFRAME.getCustomizablePanel().removeAll();
    }

    public void removeMenu(int index){
        COMMON_JFRAME.getCustomizablePanel().remove(FLAT_MENU_LIST.get(index));
        FLAT_MENU_LIST.remove(index);
    }

    public void setMouseEnterMenuColor(Color color){
        this.mouseEnterMenuColor = color;
        for(FlatMenu f : FLAT_MENU_LIST){
            f.setMouseEnterMenuColor(color);
        }
    }

    public void setMouseEnterMenuTextColor(Color color){
        this.mouseEnterMenuTextColor = color;
        for(FlatMenu f : FLAT_MENU_LIST){
            f.setMouseEnterMenuTextColor(color);
        }
    }

    public void setMenuColor(Color color){
        this.menuColor = color;
        for(FlatMenu f : FLAT_MENU_LIST){
            f.setMenuColor(color);
        }
    }

    public void setMenuTextColor(Color color){
        this.menuTextColor = color;
        for(FlatMenu f : FLAT_MENU_LIST){
            f.setForeground(color);
        }
    }

    public void setMenuTextFont(Font font){
        this.menuFont = font;
        for(FlatMenu f : FLAT_MENU_LIST){
            f.setFont(font);
        }
    }

    public void setOnPositionClickListener(OnPositionClickListener onPositionClickListener){
        this.onPositionClickListener = onPositionClickListener;
        for(int i = 0 ; i < FLAT_MENU_LIST.size(); i++ ){
            final int position = i;
            FLAT_MENU_LIST.get(i).setOnClickListener((c)-> onPositionClickListener.onClick(position,FLAT_MENU_LIST.get(position)));
        }
    }

    public int getItemSize(){
        return FLAT_MENU_LIST.size();
    }

    public void close(){
        COMMON_JFRAME.dispose();
    }

    private void pack(){
        COMMON_JFRAME.pack();
        COMMON_JFRAME.setSize(COMMON_JFRAME.getCustomizablePanel().getPreferredSize());
    }

    public void show(int x, int y){
        pack();
        COMMON_JFRAME.setLocation(x,y);
        COMMON_JFRAME.setVisible(true);
    }

    public void properLocationShow(int x, int y){
        pack();
        Dimension dimension = COMMON_JFRAME.getPreferredSize();
        int properLocationX = SCREEN_MANAGER.getScreenWidth() < x + dimension.width ? x - dimension.width : x;
        int properLocationY = SCREEN_MANAGER.getScreenHeight() < y + dimension.height ? y - dimension.height : y;
        show(properLocationX,properLocationY);
    }
}
