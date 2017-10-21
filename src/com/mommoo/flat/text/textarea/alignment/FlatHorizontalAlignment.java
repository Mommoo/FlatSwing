package com.mommoo.flat.text.textarea.alignment;

public enum FlatHorizontalAlignment {
    /**
     * A possible value for paragraph alignment.  This
     * specifies that the text is aligned to the left
     * indent and extra whitespace should be placed on
     * the right.
     */
    LEFT,
    /**
     * A possible value for paragraph alignment.  This
     * specifies that the text is aligned to the center
     * and extra whitespace should be placed equally on
     * the left and right.
     */
    CENTER,

    /**
     * A possible value for paragraph alignment.  This
     * specifies that the text is aligned to the right
     * indent and extra whitespace should be placed on
     * the left.
     */
    RIGHT,

    /**
     * A possible value for paragraph alignment.  This
     * specifies that extra whitespace should be spread
     * out through the rows of the paragraph with the
     * text lined up with the left and right indent
     * except on the last line which should be aligned
     * to the left.
     */
    JUSTIFIED;
}
