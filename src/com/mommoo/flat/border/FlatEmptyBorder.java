package com.mommoo.flat.border;

import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FlatEmptyBorder extends EmptyBorder{
    public FlatEmptyBorder(int top, int left, int bottom, int right) {
        super(top, left, bottom, right);
    }

    public FlatEmptyBorder(Insets borderInsets) {
        super(borderInsets);
    }

    public FlatEmptyBorder(int padding){
        super(padding, padding, padding, padding);
    }
}
