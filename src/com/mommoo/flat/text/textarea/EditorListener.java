package com.mommoo.flat.text.textarea;

import com.mommoo.flat.text.textarea.alignment.FlatVerticalAlignment;

import java.awt.*;
import java.util.function.IntConsumer;

public interface EditorListener {
    public FlatVerticalAlignment getVerticalAlignment();
    public boolean isLineWrap();
    public boolean isWrapStyleWord();
    public int getViewHeight();
    public int getContentsHeight();
}
