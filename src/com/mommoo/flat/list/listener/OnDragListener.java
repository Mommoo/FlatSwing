package com.mommoo.flat.list.listener;

import java.awt.*;
import java.util.List;

public interface OnDragListener<T extends Component> {
    public void onDrag(int beginIndex, int endIndex, List<T> selectionList);
}
