package com.mommoo.flat.frame.titlebar;

import com.mommoo.util.FontManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mommoo on 2017-07-13.
 */
public class TitleLabel extends JLabel {
    private static final int TITLE_TEXT_LEFT_PADDING = ScreenManager.getInstance().dip2px(4);

    public TitleLabel(int fontSize){
        setHorizontalAlignment(JLabel.LEFT);
        setVerticalAlignment(JLabel.CENTER);
        setOpaque(false);
        setFont(FontManager.getNanumGothicFont(Font.PLAIN, fontSize));
        setBorder(BorderFactory.createEmptyBorder(0,TITLE_TEXT_LEFT_PADDING,0,0));
    }
}
