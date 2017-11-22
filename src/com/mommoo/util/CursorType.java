package com.mommoo.util;

import java.awt.*;

public enum CursorType {
    DEFAULT,
    HAND;

    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

    public Cursor getCursor(){

        switch(this) {
            case DEFAULT : return DEFAULT_CURSOR;
            case HAND    : return HAND_CURSOR;
        }

        return DEFAULT_CURSOR;
    }
}
