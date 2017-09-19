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

    void setHeightFitToWidth(){
        this.lineCount = 1;
        Font font = flatTextArea.getFont();
        FontMetrics fontMetrics = flatTextArea.getFontMetrics(font);
        Insets insets = flatTextArea.getInsets();
        int fontSize = font.getSize();
        String text = flatTextArea.getText();

        int stringWidth = fontMetrics.stringWidth(text);

        int width = flatTextArea.getWidth() - insets.left - insets.right;
        int substringBeginIndex = 0;
        int substringEndIndex = 0;

        int lineHeight = (int)(fontMetrics.getHeight() * (1.0f + flatTextArea.getLineSpacing()));

        while (stringWidth > width){
            substringEndIndex = width/fontSize;

            while (true){
                String parsedText = text.substring(substringBeginIndex, substringBeginIndex + substringEndIndex);

                if (width > fontMetrics.stringWidth(parsedText)){
                    substringEndIndex++;
                }

                else if (width <= fontMetrics.stringWidth(parsedText)){

                    while(true) {
                        parsedText = text.substring(substringBeginIndex, substringBeginIndex + (--substringEndIndex));
                        int indexOfNewLine = getIndexOfNewLine(parsedText);

                        if (indexOfNewLine != -1){
                            substringEndIndex = indexOfNewLine + 1;
                            break;
                        }

                        if (width > fontMetrics.stringWidth(parsedText)) {
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

        if (stringWidth <= width){
            lineCount += getCountContainNewLine(text.substring(substringBeginIndex));
        }

        int firstLineHeight = fontMetrics.getHeight();
        int height = firstLineHeight + lineHeight * (lineCount-1) + insets.top + insets.bottom;

        flatTextArea.setPreferredSize(new Dimension(flatTextArea.getWidth(), height));
    }

    int getLineCount(){
        return lineCount;
    }
}
