package com.mommoo.flat.text.textarea;

import java.awt.*;
import java.util.ArrayList;

class FlatAutoResizeListener {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private final StringCalculator STRING_CALCULATOR = new StringCalculator();
    private final FlatTextArea flatTextArea;
    private StringViewModel stringViewModel = new StringViewModel(0, 0, 1);

    FlatAutoResizeListener(FlatTextArea flatTextArea) {
        this.flatTextArea = flatTextArea;
    }

    void setContentsFitSize() {
        int parentAvailableWidth = 0;
        if (flatTextArea.getParent() == null) parentAvailableWidth = -1;
        else {
            Insets parentInsets = flatTextArea.getParent().getInsets();
            Insets insets = flatTextArea.getInsets();
            parentAvailableWidth = flatTextArea.getParent().getWidth() - insets.left - insets.right - parentInsets.left - parentInsets.right;

        }
        setContentsFitSize(parentAvailableWidth);
    }

    /**
     * This method is perform that after get parameter of available width, calculate proper width of component
     *
     * @param availableWidth capacity of space we can use
     * @Case_1 If string length smaller than available width , proper component width is width of string
     * But, if string contains new line character, we have to calculate line count
     * @Case_2 If string length bigger than available width , we need to width of line calculated available string width.
     * After get line count, through it, we have to calculate proper height
     */
    void setContentsFitSize(int availableWidth) {
        stringViewModel = STRING_CALCULATOR.getStringViewModel(availableWidth, flatTextArea.getText());
        flatTextArea.setPreferredSize(getPreferredSize());
    }

    Dimension getPreferredSize() {
//        System.out.println(stringViewModel);
        return new Dimension(stringViewModel.getStringWidth(), stringViewModel.getStringHeight());
//        return getDimenContainedPadding(stringViewModel.getStringWidth(), stringViewModel.getStringHeight());
    }

    int getLineCount() {
        return stringViewModel.getLineCount();
    }

    private FontMetrics getFontMetrics() {
        return flatTextArea.getFontMetrics(flatTextArea.getFont());
    }

    private int measureWidth(String string) {
        return getFontMetrics().stringWidth(string);
    }

    private int getFontHeight() {
        return getFontMetrics().getHeight();
//        return flatTextArea.getFont().getSize();
    }

    private int getLineHeight() {
        return (int) (getFontHeight() * flatTextArea.getLineSpacing());
    }

    private Dimension getDimenContainedPadding(int width, int height) {
        Insets insets = flatTextArea.getInsets();
        return new Dimension(width + insets.left + insets.right, height + insets.top + insets.bottom);
    }

    private class StringCalculator {

        /**
         * @param string string contains different character of new-line for each of the OS
         * @return string replaced linux formatting character of new-line
         */

        private String convertNewLineFormatToLinuxFormat(String string) {
            return string.replaceAll(NEW_LINE, "\n");
        }

        private int getStringViewHeight(int lineCount) {
            return (getFontHeight() * lineCount) + (getLineHeight() * (lineCount - 1));
        }

        private StringViewModel createViewModelContainedPadding(int width, int height, int lineCount) {
            Dimension dimension = getDimenContainedPadding(width, height);
            return new StringViewModel(dimension.width, dimension.height, lineCount);
//            return new StringViewModel(width, height, lineCount);
        }

        private int getNewLineCount(String string) {
            int newLineCount = 0;

            for (char c : string.toCharArray()) {
                if (c == '\n') newLineCount++;
            }

            return newLineCount;
        }

        private StringViewModel getStringViewModel(int maxWidth, String string) {
            string = convertNewLineFormatToLinuxFormat(string);
            String[] splitNewLineArray = string.split("\n");

            int stringWidth = 0;
            int lineCount = 1 + getNewLineCount(string);
            int stringHeight = getStringViewHeight(lineCount);

            if (string.length() == 0) {
                return createViewModelContainedPadding(0, stringHeight, 1);
            }


            if (maxWidth <= 0 || isStringSmallerThanWidth(maxWidth, string)) {
                int targetWidth = 0;

                for (String line : splitNewLineArray) {
                    targetWidth = Math.max(measureWidth(line), targetWidth);
                }
                System.out.println(targetWidth);
                return createViewModelContainedPadding(targetWidth, stringHeight, lineCount);
            }

            if (isOneLetterWidthBiggerThanWidth(maxWidth, string)) {
                lineCount = string.length() - 1;
                return createViewModelContainedPadding(maxWidth, getStringViewHeight(lineCount), string.length() - 1);
            }

            int position = 0;
            ArrayList<Integer> list = new ArrayList<>();
            for (String line : splitNewLineArray) {

                int newLineIndex = getNeedToMoveNewLineIndex(maxWidth, line);
                int beginIndex = newLineIndex;

                if (newLineIndex == -1) stringWidth = Math.max(stringWidth, measureWidth(line));
                else stringWidth = maxWidth;

                while (newLineIndex != -1) {
                    lineCount++;
                    list.add(position + beginIndex);
                    newLineIndex = getNeedToMoveNewLineIndex(maxWidth, line.substring(beginIndex, line.length()));
                    beginIndex += newLineIndex;
                }

                position += line.length() + 1;
            }
            return createViewModelContainedPadding(stringWidth, getStringViewHeight(lineCount), lineCount);
        }

        private boolean isStringSmallerThanWidth(int width, String string) {
            return measureWidth(string) <= width;
        }

        private boolean isOneLetterWidthBiggerThanWidth(int width, String string) {
            String oneLetter = string.substring(0, 1);
            return measureWidth(oneLetter) >= width;
        }

        /**
         * Don't put string contained new-line character
         */
        private int getNeedToMoveNewLineIndex(int maxWidth, String originStr) {
            int beginIndex = 0;
            int endIndex = originStr.length();

            if (measureWidth(originStr) <= maxWidth) {
                return -1;
            }

            while (beginIndex < endIndex) {
                int findIndex = beginIndex + (endIndex - beginIndex) / 2;

                if (measureWidth(originStr.substring(0, findIndex)) <= maxWidth && findIndex + 1 <= endIndex) {

                    if (measureWidth(originStr.substring(0, findIndex + 1)) > maxWidth) {

                        return findIndex;

                    } else {
                        beginIndex = findIndex;
                    }

                } else {
                    endIndex = findIndex;
                }
            }

            return -1;
        }
    }
}
