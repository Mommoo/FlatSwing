package com.mommoo.flat.frame.colorpicker;

import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.text.textarea.alignment.FlatVerticalAlignment;
import com.mommoo.util.ColorManager;
import com.mommoo.util.FontManager;
import com.mommoo.util.ScreenManager;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class GuideView extends FlatLabel {
    private static final ScreenManager SCREEN = ScreenManager.getInstance();
    private static final Font FONT = FontManager.getNanumGothicBoldFont(SCREEN.dip2px(8));
    private static final Border PADDING_BORDER = new EmptyBorder(SCREEN.dip2px(4),0,SCREEN.dip2px(2),0);

    GuideView(String guideText){
        setVerticalAlignment(FlatVerticalAlignment.CENTER);
        setText(guideText.toUpperCase());
        setFont(FONT);
        setBorder(PADDING_BORDER);
    }
}
