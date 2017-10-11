package com.mommoo.util;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystem;

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
}
