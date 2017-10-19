package com.mommoo.flat.text.textfield.format;

import com.mommoo.util.StringUtils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class FormattedDocument extends PlainDocument{
    private FlatTextFormat format = FlatTextFormat.NORMAL;
    private boolean isHintStatus;
    private StringBuilder stringBuilder = new StringBuilder();

    public FlatTextFormat getFormat() {
        return format;
    }

    public void setFormat(FlatTextFormat format){
        this.format = format;
    }

    public void setHintStatus(boolean hintStatus){
        this.isHintStatus = hintStatus;
    }

    @Override
    public void insertString(int offs, String string, AttributeSet a) throws BadLocationException {
        if (isHintStatus) super.insertString(offs, string, a);

        char[] charArray = string.toCharArray();
        stringBuilder.delete(0, stringBuilder.length());
        switch(format){
            case NORMAL : break;

            case ONLY_NUMBER_DECIMAL :
                for (char c : charArray){
                    if (StringUtils.isCharNumber(c)) {
                        stringBuilder.append(c);
                    }
                }
                break;

            case ONLY_TEXT :
                for (char c : charArray){
                    if (!StringUtils.isCharNumber(c)) {
                        stringBuilder.append(c);
                    }
                }
                break;

            case EXCEPT_SPECIAL_CHARACTER:
                for (char c : charArray){
                    if (!StringUtils.isSpecialChar(c)) {
                        stringBuilder.append(c);
                    }
                }
                break;

        }
        super.insertString(offs, stringBuilder.toString(), a);
    }
}
