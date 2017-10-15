package com.mommoo.flat.text.textarea;

import java.awt.*;
import java.util.function.IntConsumer;

public interface EditorListener {
    public boolean isVerticalCentered();
    public boolean isWrapStyleWord();
    public void revalidate();
    public void repaint();
    public void executeTextMoveTask(IntConsumer task);
    public int getContentsHeight();
}
