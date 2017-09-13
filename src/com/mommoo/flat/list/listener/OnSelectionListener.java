package com.mommoo.flat.list.listener;

import java.awt.*;
import java.util.List;

@FunctionalInterface
public interface OnSelectionListener<T extends Component>{
    public void onSelection(int beginIndex, int endIndex, List<T> selectionList);
}
