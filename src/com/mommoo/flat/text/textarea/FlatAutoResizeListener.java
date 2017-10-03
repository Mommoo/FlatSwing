package com.mommoo.flat.text.textarea;

import com.mommoo.flat.text.textarea.FlatTextArea;

import java.awt.*;

class FlatAutoResizeListener{
    private static final String NEW_LINE = System.getProperty("line.separator");
    private final FlatTextArea flatTextArea;
    private int lineCount = 1;

    FlatAutoResizeListener(FlatTextArea flatTextArea){
        this.flatTextArea = flatTextArea;
    }

    private char[] createCharArray(String text){
        int size = text.length();
        char[] charArray = new char[size];
        text.getChars(0, size, charArray, 0);

        return charArray;
    }

    private boolean isContainNewLine(char c){
        return NEW_LINE.contains(Character.toString(c));
    }

    private int getCountContainNewLine(String text){
        int cnt = 0;

        char[] charArray = createCharArray(text);

        for (char c : charArray){
            if(isContainNewLine(c)) cnt++;
        }

        return cnt;
    }

    private int getIndexOfNewLine(String text){
        int index = 0;

        char[] charArray = createCharArray(text);

        for (char c : charArray){
            if(isContainNewLine(c)) return index;
            index++;
        }

        return -1;
    }

    void setContentsFitSize(){
        Insets insets = flatTextArea.getInsets();
        int parentAvailableWidth = flatTextArea.getParent().getWidth() - insets.left - insets.right;
        setContentsFitSize(parentAvailableWidth);
    }

    void setContentsFitSize(int availableWidth){
        this.lineCount = 1;
        Font font = flatTextArea.getFont();
        FontMetrics fontMetrics = flatTextArea.getFontMetrics(font);
        Insets insets = flatTextArea.getInsets();

        String text = flatTextArea.getText();

        int stringWidth = fontMetrics.stringWidth(text);
        int properWidth = Math.min(stringWidth, availableWidth);
        int substringBeginIndex = 0;
        int substringEndIndex = 0;

        while (stringWidth > properWidth){
            //substringEndIndex = width/fontSize;
            substringEndIndex = 1;

            while (true){
                String parsedText = text.substring(substringBeginIndex, substringBeginIndex + substringEndIndex);

                if (properWidth > fontMetrics.stringWidth(parsedText)){
                    substringEndIndex++;
                }

                else if (properWidth <= fontMetrics.stringWidth(parsedText)){

                    while(true) {
                        parsedText = text.substring(substringBeginIndex, substringBeginIndex + (--substringEndIndex));
                        int indexOfNewLine = getIndexOfNewLine(parsedText);

                        if (indexOfNewLine != -1){
                            substringEndIndex = indexOfNewLine + 1;
                            break;
                        }

                        if (properWidth > fontMetrics.stringWidth(parsedText)) {
                            break;
                        }
                    }

                    if (substringEndIndex == 0) substringEndIndex = 1;

                    substringEndIndex += substringBeginIndex;
                    break;
                }
            }

            stringWidth -= fontMetrics.stringWidth(text.substring(substringBeginIndex, substringEndIndex));

            substringBeginIndex = substringEndIndex;

            lineCount++;

        }

        if (stringWidth <= properWidth){
            lineCount += getCountContainNewLine(text.substring(substringBeginIndex));
        }

        flatTextArea.setPreferredSize(new Dimension(properWidth + insets.left + insets.right, getFittedHeight(lineCount)));
    }

    int getLineCount(){
        return lineCount;
    }

    int getFittedHeight(int lineCount){
        Insets insets = flatTextArea.getInsets();

        FontMetrics fontMetrics = flatTextArea.getFontMetrics(flatTextArea.getFont());

        int lineHeight = (int)(fontMetrics.getHeight() * (1.0f + flatTextArea.getLineSpacing()));
        int firstLineHeight = fontMetrics.getHeight();

        return firstLineHeight + lineHeight * (lineCount-1) + insets.top + insets.bottom;
    }
}
