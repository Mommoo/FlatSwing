package com.mommoo.flat.text.textarea;

import com.mommoo.flat.component.MouseClickAdapter;
import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.util.ComputableDimension;
import com.mommoo.util.FontManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.util.Stack;
import java.util.function.IntConsumer;

public class FlatTextArea extends JTextPane{
    private final MutableAttributeSet ATTRIBUTE_SET = new SimpleAttributeSet();
    private Stack<IntConsumer> textMoveListenerStack = new Stack<>();

    private Dimension preferredDimension;
    private Dimension userWantedDimension;
    private MouseClickAdapter mouseClickAdapter;

    private int lineCount;

    private boolean isNeedToInspect;

    private boolean isNeedToCentered;
    private boolean lineWrap;
    private boolean wrapStyleWord;


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

            FlatTextArea area = new FlatTextArea("This is a Flexiable TextArea!!\n\n when no have space to print text, jump next line automatically");
            area.setVerticalCenteredTextAlignment();
            area.setTextAlignment(FlatTextAlignment.ALIGN_CENTER);
            area.setOpaque(true);
            area.setBackground(Color.RED);
//            area.setLineSpacing(3.0f);
            area.setFont(FontManager.getNanumGothicFont(Font.BOLD, 20));
            area.setCaretWidth(4);
            area.setSelectionColor(Color.BLUE);
//            area.setHeightFittedToWidth(200);
            area.setPreferredSize(new Dimension(350,350));
            area.setCaretColor(Color.PINK);
//            area.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
//            FlatScrollPane pane = new FlatScrollPane(area);
            JScrollPane pane = new JScrollPane(area);

            frame.getContainer().add(area);
            frame.getContainer().setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            frame.show();

        });
    }

    private void blockStrangeParentMethodInvoked() {
        super.setPreferredSize(super.getPreferredSize());
    }

    private void init() {
        blockStrangeParentMethodInvoked();
        setEditorKit(new FlatWrapEditorKit(new EditorListenerImpl()));
        setCaret(new FlatCaret());
        getDocument().addDocumentListener(createDocumentListener());
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    private int getAvailableWidth() {
        Insets childInsets = getInsets();
        Container parent = getParent();
        Insets parentInsets = parent.getInsets();
        if (userWantedDimension != null){
            return userWantedDimension.width - childInsets.left - childInsets.right;
        } else if (lineWrap) {
            return parent.getWidth() - parentInsets.left - parentInsets.right - childInsets.left - childInsets.right;
        } else {
            if (parent.getWidth() - parentInsets.left - parentInsets.right == getWidth()) {
                return getWidth() - childInsets.right - childInsets.left;
            } else {
                return 0;
            }
        }
    }

    private DocumentListener createDocumentListener() {
        return new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                isNeedToInspect = true;
                repaint();
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (isNeedToInspect) {
            isNeedToInspect = false;

            preferredDimension = getViewDimension(getAvailableWidth());

            int previousLineCount = lineCount;
            int viewCount = getWrappedLines();
            int wrapLineCount = autoResizeHandler.getWrapLineCount();

            FlatTextArea.this.lineCount = wrapLineCount;

            if (wrapStyleWord && viewCount != wrapLineCount && wrapLineCount != 1 && viewCount > 0) {
                preferredDimension.height = getContentsLineHeight(viewCount) + getInsets().bottom + getInsets().top;
                FlatTextArea.this.lineCount = viewCount;
            }

            if (textMoveListenerStack.empty() || previousLineCount == lineCount){
                revalidate();
                textMoveListenerStack.clear();
            } else {
                g.setColor(getBackground());
                g.fillRect(0,0,getWidth(),getHeight());
                textMoveListenerStack.pop().accept((getHeight() - preferredDimension.height)/2);
            }
            FlatTextArea.super.setPreferredSize(preferredDimension);
        }

    }

    private int getWrappedLines() {
        int lines = 0;

        View view = getUI().getRootView(this).getView(0);

        int paragraphs = view.getViewCount();

        for (int i = 0; i < paragraphs; i++) {
            lines += view.getView(i).getViewCount();
        }

        return lines;
    }

    private int getContentsLineHeight(int lineCount) {
        int fontHeight = getFontMetrics(getFont()).getHeight();
        int lineHeight = (int)(fontHeight * getLineSpacing());
        return (fontHeight * lineCount) + (lineHeight * (lineCount - 1));
    }

    private Dimension getViewDimension(int width) {
        Insets insets = getInsets();
        return new ComputableDimension(width <= 0 ? autoResizeHandler.getContentsFitSize() : autoResizeHandler.getContentsFitSize(width, wrapStyleWord))
                .addDimension(insets.left + insets.right, insets.top + insets.bottom);

    }

    private Dimension getPreferredLabelSize() {
        if (preferredDimension == null) {
            return getViewDimension(getAvailableWidth());
        }
        return preferredDimension;
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);

        if (ATTRIBUTE_SET == null) return;

        StyleConstants.setForeground(ATTRIBUTE_SET, fg);

        setParagraphAttributes(ATTRIBUTE_SET, true);
    }

    @Override
    public Dimension getPreferredSize() {
        if (userWantedDimension != null) {
            return userWantedDimension;
        }

        if (preferredDimension == null) {
            return getViewDimension(getAvailableWidth());
        }

        return super.getPreferredSize();
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

    public void setTextAlignment(FlatTextAlignment alignment) {
        StyleConstants.setAlignment(ATTRIBUTE_SET, alignment.ordinal());
        setParagraphAttributes(ATTRIBUTE_SET, true);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        if (this.mouseClickAdapter != null) removeMouseListener(this.mouseClickAdapter);
        this.mouseClickAdapter = new MouseClickAdapter(onClickListener);
        addMouseListener(mouseClickAdapter);
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        Container parent = SwingUtilities.getUnwrappedParent(this);
        if (parent instanceof JViewport) {
            return parent.getWidth() > getPreferredLabelSize().width;
        }
        return false;
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        userWantedDimension = preferredSize;
        super.setPreferredSize(preferredSize);
    }

    public OnClickListener getOnClickListener() {
        return this.mouseClickAdapter.getOnClickListener();
    }

    public void setVerticalCenteredTextAlignment() {
        isNeedToCentered = true;
    }

    public boolean isVerticalCentered() {
        return isNeedToCentered;
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

    public void setHeightFittedToWidth(int preferredWidth) {
        SwingUtilities.invokeLater(() -> {
            Dimension dimension = autoResizeHandler.getContentsFitSize(preferredWidth, wrapStyleWord);
            Insets insets = getInsets();
            dimension.width = preferredWidth;
            dimension.height += insets.top + insets.bottom;
            setPreferredSize(dimension);
        });
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

    private class EditorListenerImpl implements EditorListener{
        @Override
        public boolean isVerticalCentered() {
            return FlatTextArea.this.isVerticalCentered();
        }

        @Override
        public boolean isWrapStyleWord() {
            return FlatTextArea.this.isWrapStyleWord();
        }

        @Override
        public void revalidate() {
            FlatTextArea.this.revalidate();
        }

        @Override
        public void repaint() {
            FlatTextArea.this.repaint();
        }

        @Override
        public void executeTextMoveTask(IntConsumer task) {
            textMoveListenerStack.add(task);
        }

        @Override
        public int getContentsHeight() {
            return getContentsLineHeight(lineCount);
        }
    }

}
