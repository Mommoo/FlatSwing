package com.mommoo.flat.text.textarea;

import java.awt.*;
import java.util.function.IntConsumer;

public interface EditorListener {
    public boolean isVerticalCentered();
    public boolean isLineWrap();
    public boolean isWrapStyleWord();
    public int getViewHeight();
    public int getContentsHeight();
}
