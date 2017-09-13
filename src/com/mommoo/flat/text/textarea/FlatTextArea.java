package com.mommoo.flat.text.textarea;

import com.mommoo.flat.component.MouseClickAdapter;
import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
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

public class FlatTextArea extends JTextPane {
    private final MutableAttributeSet ATTRIBUTE_SET = new SimpleAttributeSet();
    private FlatAutoResizeListener flatAutoResizeListener;
    private ComputableDimension previousSize = new ComputableDimension(0,0);
    private MouseClickAdapter mouseClickAdapter;

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

    public static void main(String[] args) {
        FlatFrame frame = new FlatFrame();
        frame.setSize(500, 500);
        frame.setLocationOnScreenCenter();


        JScrollPane pane = new JScrollPane();
        FlatTextArea area = new FlatTextArea("굳굳굳굳굳");
        area.setLineSpacing(0.7f);
        area.setBackground(Color.YELLOW);
        area.setFont(FontManager.getNanumGothicFont(Font.BOLD, 17));
        area.setCaretWidth(4);
//        area.setCursorColor(Color.RED);
        area.setForeground(Color.PINK);
        area.setSelectionColor(Color.BLUE);
//        area.setTextAlignment(FlatTextAlignment.ALIGN_CENTER);
        area.setCaretColor(Color.PINK);
        pane.setViewportView(area);
        frame.getContainer().setLayout(new LinearLayout(Orientation.VERTICAL, 10));
        frame.getContainer().add(area, new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT));
        JTextArea JArea = new JTextArea("가낭란ㅇ라날날날날날날나란라ㅏ");
        JArea.setCaretColor(Color.RED);
        frame.setShadowWidth(100);
        frame.setResizable(true);
        frame.getContainer().add(JArea);
        frame.show();
        frame.setEnableSizeButton(true);
    }

    private void init(){
        this.flatAutoResizeListener = new FlatAutoResizeListener(this);
        setEditorKit(new FlatWrapEditorKit());
        setCaret(new FlatCaret());
        getDocument().addDocumentListener(createDocumentListener());
    }

    private DocumentListener createDocumentListener(){
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
                if (getWidth() != 0){
                    flatAutoResizeListener.setHeightFitToWidth();
                }
//                pre = getPreferredSize();
            }
        };
    }

    public int getLineCount(){
        return this.flatAutoResizeListener.getLineCount();
    }

    public float getLineSpacing() {
        return StyleConstants.getLineSpacing(ATTRIBUTE_SET);
    }

    public void setLineSpacing(float lineSpacing) {
        if (lineSpacing < 0.0f){
            return;
        }

        StyleConstants.setLineSpacing(ATTRIBUTE_SET, lineSpacing);
        getStyledDocument().setParagraphAttributes(0,Integer.MAX_VALUE,ATTRIBUTE_SET,true);

    }

    public void setTextAlignment(FlatTextAlignment alignment){
        StyleConstants.setAlignment(ATTRIBUTE_SET, alignment.ordinal());
        setParagraphAttributes(ATTRIBUTE_SET, true);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);

        if (ATTRIBUTE_SET == null) return;

        StyleConstants.setFontFamily(ATTRIBUTE_SET, font.getFamily());
        StyleConstants.setFontSize(ATTRIBUTE_SET, font.getSize());

        if (font.getStyle() == Font.PLAIN) {

        } else if (font.getStyle() == Font.BOLD) {
            StyleConstants.setBold(ATTRIBUTE_SET, true);
        } else {
            StyleConstants.setItalic(ATTRIBUTE_SET, true);
        }

        setParagraphAttributes(ATTRIBUTE_SET, true);
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);

        if (ATTRIBUTE_SET == null) return;

        StyleConstants.setForeground(ATTRIBUTE_SET, fg);

        setParagraphAttributes(ATTRIBUTE_SET, true);
    }

    public int getCaretWidth(){
        if (getCaret() instanceof FlatCaret){
            return ((FlatCaret) getCaret()).getCursorWidth();
        }

        return 1;
    }

    /**
     *   Caret API
     */

    public void setCaretWidth(int cursorWidth){
        if (getCaret() instanceof FlatCaret){
            ((FlatCaret) getCaret()).setCursorWidth(cursorWidth);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (previousSize.getSize().equals(getSize())) return;

        previousSize.setSize(getSize());

        flatAutoResizeListener.setHeightFitToWidth();
    }

    public void setOnClickListener(OnClickListener onClickListener){
        if (this.mouseClickAdapter != null) removeMouseListener(this.mouseClickAdapter);
        this.mouseClickAdapter = new MouseClickAdapter(onClickListener);
        addMouseListener(mouseClickAdapter);
    }

    public int getFitHeightToWidth(int textAreaWidth){
        Dimension previousPreferredSize = this.getPreferredSize();

        boolean matchParent = getSize().height > previousPreferredSize.height;

        this.setSize(new Dimension(textAreaWidth, previousPreferredSize.height));

        Rectangle r = null;

        try {
            r = this.modelToView(getText().length());

            int preferredHeight = r.height + r.y + getInsets().bottom;

            return matchParent ? Math.max(preferredHeight, getHeight()) : preferredHeight;

        } catch (Exception e) {

            e.printStackTrace();
            return previousPreferredSize.height;

        } finally {
            this.setSize(previousPreferredSize);
        }
    }
}
