package com.mommoo.flat.text.textarea;

public enum FlatTextAlignment {
    /**
     * A possible value for paragraph alignment.  This
     * specifies that the text is aligned to the left
     * indent and extra whitespace should be placed on
     * the right.
     */
    ALIGN_LEFT,
    /**
     * A possible value for paragraph alignment.  This
     * specifies that the text is aligned to the center
     * and extra whitespace should be placed equally on
     * the left and right.
     */
    ALIGN_CENTER,

    /**
     * A possible value for paragraph alignment.  This
     * specifies that the text is aligned to the right
     * indent and extra whitespace should be placed on
     * the left.
     */
    ALIGN_RIGHT,

    /**
     * A possible value for paragraph alignment.  This
     * specifies that extra whitespace should be spread
     * out through the rows of the paragraph with the
     * text lined up with the left and right indent
     * except on the last line which should be aligned
     * to the left.
     */
    ALIGN_JUSTIFIED;
}
