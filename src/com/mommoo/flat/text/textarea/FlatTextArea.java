package com.mommoo.flat.text.textarea;

import com.mommoo.flat.component.MouseClickAdapter;
import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.text.textarea.alignment.FlatHorizontalAlignment;
import com.mommoo.flat.text.textarea.alignment.FlatVerticalAlignment;
import com.mommoo.util.ComputableDimension;
import com.mommoo.util.FontManager;
import com.mommoo.util.RXTextUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FlatTextArea extends JTextPane{
    private final MutableAttributeSet ATTRIBUTE_SET = new SimpleAttributeSet();

    private ContentsSizeCalculator contentsSizeCalculator = new ContentsSizeCalculator();

    private Dimension preferredDimension;
    private Dimension userWantedDimension;
    private MouseClickAdapter mouseClickAdapter;

    private int lineCount = 1;
    private int preferredWidth = - 1;

    private boolean lineWrap;
    private boolean wrapStyleWord;

    private boolean isNeedToVerticalCalculate;

    private FlatVerticalAlignment flatVerticalAlignment = FlatVerticalAlignment.TOP;

    public FlatTextArea() {
        init();
    }

    public FlatTextArea(String text) {
        init();
        setText(text);
    }

    public FlatTextArea(StyledDocument doc) {
        super(doc);
        init();
    }

    private FlatAutoResizeHandler autoResizeHandler = new FlatAutoResizeHandler(new AutoResizeModelImpl());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatFrame frame = new FlatFrame();
            frame.setSize(500, 500);
            frame.setLocationOnScreenCenter();

            FlatTextArea area = new FlatTextArea();
            area.setHorizontalAlignment(FlatHorizontalAlignment.CENTER);
            area.setVerticalAlignment(FlatVerticalAlignment.CENTER);
            area.setPreferredSize(new Dimension(300,300));
            area.setOpaque(true);
            area.setBackground(Color.RED);
            area.setFont(FontManager.getNanumGothicFont(Font.BOLD, 20));
            area.setCaretWidth(4);
            area.setSelectionColor(Color.BLUE);
            area.setText("This is a Flexiable TextArea!!\n\n when no have space to print text, jump next line automatically");
            area.setCaretColor(Color.BLUE);
//            area.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
//            FlatScrollPane pane = new FlatScrollPane(area);
            JScrollPane pane = new JScrollPane(area);

            frame.getContainer().setLayout(new FlowLayout());
            frame.getContainer().add(area);

            frame.show();

        });
    }

    private void init() {
        setEditorKit(new FlatWrapEditorKit(new EditorProperty()));
        setCaret(new FlatCaret());
        setLineWrap(true);
        setWrapStyleWord(true);
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update(){
                isNeedToVerticalCalculate = true;
                repaint();
            }
        });
    }



    public void setPreferredWidth(int width){
        ContentsBounds contentsBounds = contentsSizeCalculator.getBounds(width);
        preferredDimension = contentsBounds.getDimension();
        lineCount = contentsBounds.getLineCount();
        super.setPreferredSize(preferredDimension);
    }

    private int getAvailableWidth() {
        Container parent = getParent();
        Insets childInsets = getInsets();
        if (userWantedDimension != null){
            return userWantedDimension.width - childInsets.left - childInsets.right;
        } else if(preferredWidth != -1){
            return preferredWidth - childInsets.left - childInsets.right;
        } else if (getParent() == null) {
            return 0;
        } else if (lineWrap) {
            Insets parentInsets = parent.getInsets();
            return parent.getWidth() - parentInsets.left - parentInsets.right - childInsets.left - childInsets.right;
        } else {
            Insets parentInsets = parent.getInsets();
            if (parent.getWidth() - parentInsets.left - parentInsets.right == getWidth()) {
                return getWidth() - childInsets.right - childInsets.left;
            } else {
                return 0;
            }
        }
    }

    private int getContentsLineHeight(int lineCount) {
        int fontHeight = getFontMetrics(getFont()).getHeight();
        float lineHeight = fontHeight * getLineSpacing();
        return (fontHeight * lineCount) + Math.round((lineHeight * (lineCount - 1)));
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);

        if (ATTRIBUTE_SET == null) return;

        StyleConstants.setForeground(ATTRIBUTE_SET, fg);

        setParagraphAttributes(ATTRIBUTE_SET, true);
    }

    @Override
    public void paint(Graphics g) {
        /* need to calculate vertical alignment data setting */
        if(isNeedToVerticalCalculate) {
            isNeedToVerticalCalculate = false;
            setPreferredWidth(getAvailableWidth());
        }
        super.paint(g);
    }

    @Override
    public Dimension getPreferredSize() {
        if (userWantedDimension != null) {
            return userWantedDimension;
        }

        if (preferredWidth != -1){
            Insets insets = getInsets();
            ContentsBounds contentsBounds = contentsSizeCalculator.getBounds(preferredWidth - insets.left - insets.right);
            preferredDimension = contentsBounds.getDimension();
            /* fix col width */
            preferredDimension.width = preferredWidth;
            lineCount = contentsBounds.getLineCount();

            return preferredDimension;
        }
        setPreferredWidth(getAvailableWidth());
        return preferredDimension;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);

        if (ATTRIBUTE_SET != null) {
            StyleConstants.setFontFamily(ATTRIBUTE_SET, font.getFamily());
            StyleConstants.setFontSize(ATTRIBUTE_SET, font.getSize());

            if (font.getStyle() == Font.BOLD) {
                StyleConstants.setBold(ATTRIBUTE_SET, true);
            } else if (font.getStyle() == Font.ITALIC) {
                StyleConstants.setItalic(ATTRIBUTE_SET, true);
            }
            StyledDocument document = getStyledDocument();
            document.setParagraphAttributes(0, document.getLength() + 1, ATTRIBUTE_SET, false);
        }
    }

    public boolean isLineWrap() {
        return this.lineWrap;
    }

    public void setLineWrap(boolean lineWrap) {
        this.lineWrap = lineWrap;
    }

    public boolean isWrapStyleWord() {
        return this.wrapStyleWord;
    }

    public void setWrapStyleWord(boolean wrapStyleWord) {
        this.wrapStyleWord = wrapStyleWord;
    }

    public int getLineCount() {
        return this.lineCount;
    }

    public float getLineSpacing() {
        return StyleConstants.getLineSpacing(ATTRIBUTE_SET);
    }

    public void setLineSpacing(float lineSpacing) {
        if (lineSpacing < 0.0f) {
            return;
        }

        StyleConstants.setLineSpacing(ATTRIBUTE_SET, lineSpacing);
        getStyledDocument().setParagraphAttributes(0, Integer.MAX_VALUE, ATTRIBUTE_SET, true);
    }

    @Deprecated
    public void setVerticalCenteredTextAlignment() {
        setVerticalAlignment(FlatVerticalAlignment.CENTER);
    }

    public FlatHorizontalAlignment getHorizontalAlignment(){
        return FlatHorizontalAlignment.values()[StyleConstants.getAlignment(ATTRIBUTE_SET)];
    }

    public void setHorizontalAlignment(FlatHorizontalAlignment alignment){
        StyleConstants.setAlignment(ATTRIBUTE_SET, alignment.ordinal());
        setParagraphAttributes(ATTRIBUTE_SET, true);
    }

    public FlatVerticalAlignment getVerticalAlignment() {
        return flatVerticalAlignment;
    }

    public void setVerticalAlignment(FlatVerticalAlignment alignment){
        this.flatVerticalAlignment = alignment;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        removeOnClickListener();
        addMouseListener(mouseClickAdapter = new MouseClickAdapter(onClickListener));
    }

    public void removeOnClickListener(){
        if (this.mouseClickAdapter != null){
            removeMouseListener(this.mouseClickAdapter);
        }
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        userWantedDimension = preferredSize;
        setPreferredWidth(getAvailableWidth());
        super.setPreferredSize(preferredSize);
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        Container parent = SwingUtilities.getUnwrappedParent(this);
        if (parent instanceof JViewport) {
            return parent.getWidth() > getPreferredSize().width;
        }
        return false;
    }

    public OnClickListener getOnClickListener() {
        return this.mouseClickAdapter.getOnClickListener();
    }

    /**
     * Caret API
     */

    public int getCaretWidth() {
        if (getCaret() instanceof FlatCaret) {
            return ((FlatCaret) getCaret()).getCursorWidth();
        }

        return 1;
    }

    public void setCaretWidth(int cursorWidth) {
        if (getCaret() instanceof FlatCaret) {
            ((FlatCaret) getCaret()).setCursorWidth(cursorWidth);
        }
    }

    public void fixWidth(int preferredWidth) {
        this.preferredWidth = preferredWidth ;
        Insets insets = getInsets();
        setPreferredWidth(preferredWidth - insets.left - insets.right);
    }

    private class EditorProperty implements EditorListener{

        @Override
        public FlatVerticalAlignment getVerticalAlignment() {
            return FlatTextArea.this.getVerticalAlignment();
        }

        @Override
        public boolean isLineWrap() {
            return FlatTextArea.this.isLineWrap();
        }

        @Override
        public boolean isWrapStyleWord() {
            return FlatTextArea.this.isWrapStyleWord();
        }

        @Override
        public int getViewHeight() {
            return getHeight();
        }

        @Override
        public int getContentsHeight() {
            return getContentsLineHeight(lineCount) + getInsets().top + getInsets().bottom;
        }
    }

    private class AutoResizeModelImpl implements AutoResizeModel {
        @Override
        public String getText() {
            return FlatTextArea.this.getText();
        }

        @Override
        public FontMetrics getFontMetrics() {
            Font font = FlatTextArea.this.getFont();
            return FlatTextArea.this.getFontMetrics(font);
        }

        @Override
        public float getLineSpacing() {
            return FlatTextArea.this.getLineSpacing();
        }
    }

    private class ContentsSizeCalculator{
        private JTextArea GRAPHIC_BUFFER = new JTextArea();

        private Component getGraphicBuffer(Dimension dimension){
            GRAPHIC_BUFFER.setDocument(getDocument());
            GRAPHIC_BUFFER.setBorder(getBorder());
            GRAPHIC_BUFFER.setMargin(getInsets());
            GRAPHIC_BUFFER.setWrapStyleWord(isWrapStyleWord());
            GRAPHIC_BUFFER.setLineWrap(isLineWrap());
            GRAPHIC_BUFFER.setFont(getFont());
            GRAPHIC_BUFFER.setDocument(getDocument());
            GRAPHIC_BUFFER.setPreferredSize(dimension);
            GRAPHIC_BUFFER.setSize(dimension);
            return GRAPHIC_BUFFER;
        }

        private Graphics createGraphics(Dimension dimension){
            return new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB).getGraphics();
        }

        public ContentsBounds getBounds(int width) {

            Insets insets = getInsets();

            Dimension viewDimension = new ComputableDimension(width <= 0 ? autoResizeHandler.getContentsFitSize() : autoResizeHandler.getContentsFitSize(width))
                    .setMinimumSize(10, 10)
                    .addDimension(insets.left + insets.right, insets.top + insets.bottom);

            Component graphicBuffer = getGraphicBuffer(viewDimension);
            graphicBuffer.paint(createGraphics(viewDimension));

            int lineCount = RXTextUtilities.getWrappedLines((JTextArea) graphicBuffer);
            int height = getContentsLineHeight(lineCount) + getInsets().top + getInsets().bottom;

            return new ContentsBounds(viewDimension.width, height, lineCount);
        }
    }
}
