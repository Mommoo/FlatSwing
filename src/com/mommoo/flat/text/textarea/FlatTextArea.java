package com.mommoo.flat.text.textarea;

import com.mommoo.flat.component.MouseClickAdapter;
import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.util.ComputableDimension;
import com.mommoo.util.FontManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class FlatTextArea extends JTextPane{
    private final MutableAttributeSet ATTRIBUTE_SET = new SimpleAttributeSet();

    private ComputableDimension previousDimen = new ComputableDimension();
    private Dimension userWantedDimension;
    private MouseClickAdapter mouseClickAdapter;

    private boolean isNeedToCentered;

    private int preferredWidth = -1;

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

    private FlatAutoResizeListener flatAutoResizeListener = new FlatAutoResizeListener(this);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatFrame frame = new FlatFrame();
            frame.setSize(500, 500);
            frame.setLocationOnScreenCenter();

            FlatTextArea area = new FlatTextArea("This is a Flexiable TextArea!! when no have space to print text, jump next line automatically");
            area.setVerticalCenteredTextAlignment();
            area.setTextAlignment(FlatTextAlignment.ALIGN_CENTER);
            area.setOpaque(true);
            area.setBackground(Color.RED);
            area.setLineSpacing(0.7f);
            area.setFont(FontManager.getNanumGothicFont(Font.BOLD, 40));
            area.setCaretWidth(4);
            area.setSelectionColor(Color.BLUE);

            area.setCaretColor(Color.PINK);
            area.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            frame.getContainer().setLayout(new FlowLayout());

            frame.getContainer().add(area);

            frame.show();
        });
    }

    private void blockStrangeParentMethodInvoked() {
        super.setPreferredSize(getPreferredSize());
    }

    private void init() {
        blockStrangeParentMethodInvoked();
        setEditorKit(new FlatWrapEditorKit(createEditorListener()));
        setCaret(new FlatCaret());
        getDocument().addDocumentListener(createDocumentListener());
    }

    private boolean isBeforeDrawing(){
        return getWidth() == 0;
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
                if (!isBeforeDrawing()){
                    autoContentsFitSize();
                }
            }
        };
    }

    private EditorListener createEditorListener(){
        return new EditorListener() {
            @Override
            public boolean isVerticalCentered() {
                return FlatTextArea.this.isVerticalCentered();
            }

            @Override
            public Dimension getPreferredSize() {
                return autoContentsFitSize();
            }
        };
    }


    private Dimension autoContentsFitSize(){

        if (this.preferredWidth != -1){
            flatAutoResizeListener.setContentsFitSize(preferredWidth, true);
        }else {
            flatAutoResizeListener.setContentsFitSize();
        }

        return flatAutoResizeListener.getPreferredSize();
    }

    @Override
    public void paint(Graphics g) {
        Dimension preferredSize = getPreferredSize();
        if (!previousDimen.equals(preferredSize) || preferredWidth != -1) {
            previousDimen.setSize(autoContentsFitSize());
        }
        super.paint(g);
    }

    @Override
    public Dimension getPreferredSize() {
        if (userWantedDimension == null && isBeforeDrawing()){
            return autoContentsFitSize();
        }
        return super.getPreferredSize();
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);

        if (ATTRIBUTE_SET == null) return;

        StyleConstants.setForeground(ATTRIBUTE_SET, fg);

        setParagraphAttributes(ATTRIBUTE_SET, true);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);

        if (ATTRIBUTE_SET != null) {
            StyleConstants.setFontFamily(ATTRIBUTE_SET, font.getFamily());
            StyleConstants.setFontSize(ATTRIBUTE_SET, font.getSize());

            if (font.getStyle() == Font.PLAIN) {

            } else if (font.getStyle() == Font.BOLD) {
                StyleConstants.setBold(ATTRIBUTE_SET, true);
            } else {
                StyleConstants.setItalic(ATTRIBUTE_SET, true);
            }
            StyledDocument document = getStyledDocument();
            document.setParagraphAttributes(0, document.getLength() + 1, ATTRIBUTE_SET, false);
        }
    }

    public int getLineCount() {
        return flatAutoResizeListener.getLineCount();
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
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

    void setPreferredLabelSize(Dimension preferredSize){
        if (userWantedDimension == null){
            super.setPreferredSize(preferredSize);
        }
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        userWantedDimension = preferredSize;
        super.setPreferredSize(preferredSize);
    }

    public void setHeightFittedToWidth(int preferredWidth) {
        Insets insets = getInsets();

        userWantedDimension = null;
        this.preferredWidth = preferredWidth - insets.left - insets.right;

        if (!isBeforeDrawing()){
            autoContentsFitSize();
        }
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


}
