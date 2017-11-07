package com.mommoo.util;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.util.Collections;

public class FontManager {
    private FontManager(){}

    public static Font getDefaultFont(int style, int fontSize){
        return new Font("sansSerif",style,fontSize);
    }

    public static Font getNanumGothicFont(int style, int fontSize){
        try {
            InputStream is =FontManager.class.getResourceAsStream("/com/mommoo/resource/font/NanumGothic.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(style, fontSize);
        } catch (Exception e) { e.printStackTrace(); }

        return getDefaultFont(style, fontSize);
    }

    public static Font getNanumGothicBoldFont(int fontSize){
        try {
            InputStream is =FontManager.class.getResourceAsStream("/com/mommoo/resource/font/NanumGothicBold.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(Font.BOLD, fontSize);
        } catch (Exception e) { e.printStackTrace(); }

        return getDefaultFont(Font.BOLD, fontSize);
    }
}
