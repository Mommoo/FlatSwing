package com.mommoo.flat.text.textarea;

import com.mommoo.util.StringUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

class FlatAutoResizeHandler {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private final ContentsBoundsCalculator BOUNDS_CALCULATOR = new ContentsBoundsCalculator();
    private final AutoResizeModel resizeModel;
    private ContentsBounds contentsBounds = new ContentsBounds(0, 0, 1);

    FlatAutoResizeHandler(AutoResizeModel resizeModel) {
        this.resizeModel = resizeModel;
    }

    /**
     * This method is perform that after get parameter of available width, calculate proper width of component
     *
     * @param availableWidth capacity of space we can use
     *
     *
     * @Case_1 If string length smaller than available width , proper component width is width of string
     * But, if string contains new line character, we have to calculate line count
     * @Case_2 If string length bigger than available width , we need to width of line calculated available string width.
     * After get line count, through it, we have to calculate proper height
     */
    Dimension getContentsFitSize(int availableWidth) {
        contentsBounds = BOUNDS_CALCULATOR.getContentsDimensionFitToWidth(availableWidth, resizeModel.getText());

        return new Dimension(contentsBounds.getContentsWidth(), contentsBounds.getContentsHeight());
    }

    Dimension getContentsFitSize(){
        contentsBounds = BOUNDS_CALCULATOR.getPreferredContentsDimension(resizeModel.getText());
        return new Dimension(contentsBounds.getContentsWidth(), contentsBounds.getContentsHeight());
    }

    int getWrapLineCount() {
        return contentsBounds.getLineCount();
    }

    private int measureWidth(String string) {
        return resizeModel.getFontMetrics().stringWidth(string);
    }

    private int getFontHeight() {
        return resizeModel.getFontMetrics().getHeight();
    }

    private int getLineHeight() {
        return (int) (getFontHeight() * resizeModel.getLineSpacing());
    }

//    private Dimension getDimenContainedPadding(int width, int height) {
//        Insets insets = resizeModel.getInsets();
//        return new Dimension(width + insets.left + insets.right, height + insets.top + insets.bottom);
//    }

    private class ContentsBoundsCalculator {
        private static final int DO_NOT_NEED_TO_NEW_LINE = -1;


        /**
         * @param string string contains different character of new-line for each of the OS
         * @return string replaced linux formatting character of new-line
         */

        private String convertNewLineFormatToLinuxFormat(String string) {
            return string.replaceAll(NEW_LINE, "\n");
        }

        private int getContentsLineHeight(int lineCount) {
            return (getFontHeight() * lineCount) + (getLineHeight() * (lineCount - 1));
        }

        private int getNewLineCount(String string) {
            int newLineCount = 0;

            for (char c : string.toCharArray()) {
                if (c == '\n') newLineCount++;
            }

            return newLineCount;
        }

        private List<String> getListSplitByNewLine(String string, boolean wordWrapStyle){
            String[] splitKeys = wordWrapStyle ? new String[]{"\n","\t"," "} : new String[]{"\n"};
            return StringUtils.splitOfAny(string, splitKeys);
        }

        /**
         * @param basicWidth The basic width for calculating pivot of string split
         * @param originString Needed to calculate the size of contents
//         * @param autoResizeWidth If this value is true, contents width has range : 0<= width <= basicWidth
         *
         * @return The dimension of contents
         */
        private ContentsBounds getContentsDimensionFitToWidth(int basicWidth, String originString) {
            String convertedToLinuxFormatString = convertNewLineFormatToLinuxFormat(originString);

            List<String> listSplitByNewLine = Arrays.asList(convertedToLinuxFormatString.split("\n"));

            int contentsWidth = basicWidth;
            int lineCount = getNewLineCount(convertedToLinuxFormatString) + 1;

            if (originString.length() == 0){
                return new ContentsBounds(0, getContentsLineHeight(1), 1);
            }

            if (isOneLetterWidthBiggerThanWidth(contentsWidth, originString)){
                lineCount = originString.length();
                return new ContentsBounds(contentsWidth, getContentsLineHeight(lineCount), lineCount);
            }

            int maxStringLineWidth = 0;

            for (String string : listSplitByNewLine){
                int newLineIndex = getIndexToBeNewLine(basicWidth, string);

                if (newLineIndex == DO_NOT_NEED_TO_NEW_LINE) {
                    maxStringLineWidth = Math.max(maxStringLineWidth, measureWidth(string));
                    continue;
                } else {
                    maxStringLineWidth = basicWidth;
                }

                int beginIndex = newLineIndex;


                while (newLineIndex != DO_NOT_NEED_TO_NEW_LINE) {
                    lineCount++;
                    String subString = string.substring(beginIndex, string.length());
                    newLineIndex = getIndexToBeNewLine(basicWidth, subString);

                    beginIndex += newLineIndex;
                }
            }

            contentsWidth = Math.min(basicWidth, maxStringLineWidth);

            return new ContentsBounds(contentsWidth, getContentsLineHeight(lineCount), lineCount);
        }

        private ContentsBounds getPreferredContentsDimension(String string){
            String convertedToLinuxFormatString = convertNewLineFormatToLinuxFormat(string);
            List<String> listSplitByNewLine = getListSplitByNewLine(convertedToLinuxFormatString, false);

            if (string.length() == 0){
                return new ContentsBounds(0, getContentsLineHeight(1), 1);
            }

            int maxStringLineWidth = 0;

            OptionalInt optionalMaxWidth = listSplitByNewLine
                    .stream()
                    .mapToInt(FlatAutoResizeHandler.this::measureWidth)
                    .max();

            if (optionalMaxWidth.isPresent()){
                maxStringLineWidth = optionalMaxWidth.getAsInt();
            }

            int contentsWidth = Math.max(10, maxStringLineWidth);
            int lineCount = getNewLineCount(convertedToLinuxFormatString) + 1;
            int contentsHeight = Math.max(10, getContentsLineHeight(lineCount));
            return new ContentsBounds(contentsWidth, contentsHeight, lineCount);
        }

        private boolean isOneLetterWidthBiggerThanWidth(int width, String string) {
            String oneLetter = string.substring(0, 1);
            return measureWidth(oneLetter) >= width;
        }

        /**
         * Don't put string contained new-line character
         */
        private int getIndexToBeNewLine(int maxWidth, String string) {
            int beginIndex = 0;
            int endIndex = string.length();
            if (measureWidth(string) <= maxWidth) {
                return DO_NOT_NEED_TO_NEW_LINE;
            }

            while (beginIndex < endIndex) {
                int findIndex = beginIndex + (endIndex - beginIndex) / 2;

                if (measureWidth(string.substring(0, findIndex)) <= maxWidth && findIndex + 1 <= endIndex) {

                    if (measureWidth(string.substring(0, findIndex + 1)) > maxWidth) {

                        return findIndex;

                    } else {
                        beginIndex = findIndex;
                    }

                } else {
                    endIndex = findIndex;
                }
            }

            return DO_NOT_NEED_TO_NEW_LINE;
        }
    }
}
