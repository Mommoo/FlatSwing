package com.mommoo.flat.label;

import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.util.FontManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 *  FlatLabel is very convenient component.
 *  If text length over the label width, line jump automatically
 */

public class FlatLabel extends JTextPane{
	private static final int TEXT_FONT_SIZE = ScreenManager.getInstance().dip2px(10);
	private int lineHeight = 0;

	public FlatLabel(){
		init();
	}

	public FlatLabel(String text){
		init();
		setText(text);
	}

	public FlatLabel(StyledDocument doc) {
		super(doc);
		init();
	}

	private void init(){
		setHighlighter(null);
		setEditable(false);
		setFont(FontManager.getNanumGothicFont(Font.PLAIN,TEXT_FONT_SIZE));
		setEditorKit(new WrapEditorKet());
	}

	public void setLineHeight(int lineHeight){
		if(lineHeight >0) {
			this.lineHeight = lineHeight;
		}
	}

	public int getLineHeight(){
		return this.lineHeight;
	}

	public void setOnClickListener(OnClickListener onClickListener){
		this.addMouseListener(new FlatLabelMouseListener(onClickListener));
	}

	public void setTextAreaFitHeightToWidth(int textAreaWidth){
		Dimension tmpDimen = new Dimension(textAreaWidth, ScreenManager.getInstance().getWindowHeight());
		Dimension previousPreferredSize = this.getPreferredSize();
		this.setPreferredSize(tmpDimen);
		this.setSize(tmpDimen);

		Rectangle r = null;

		Dimension fitDimen = new Dimension(textAreaWidth,0);
		try {
			r = this.modelToView(this.getDocument().getLength());
			fitDimen.height += r.height + r.y + getInsets().bottom;
			if (lineHeight > 0) fitDimen.height -= (lineHeight - getFont().getSize() - 10);

		} catch (BadLocationException e) {
			e.printStackTrace();
			fitDimen.height = previousPreferredSize.height;
		}finally {
			this.setPreferredSize(fitDimen);
			this.setSize(fitDimen);
		}


	}

	@Override
	protected void paintComponent(Graphics g) {
		if(lineHeight > 0 ){
			Graphics2D g2 = (Graphics2D)g;
			int fontSize = g2.getFont().getSize();
			int errorValue = lineHeight - fontSize;
			g2.translate(0, -errorValue);
			g2.setColor(getBackground());
			g2.fillRect(0,0,getWidth(), getHeight()+ errorValue);
		}
		super.paintComponent(g);
	}

	public void setTextAlignment(FlatLabelAlign alignment){
		int align = alignment.ordinal();

		SimpleAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setAlignment(set, align);
		StyledDocument doc = (StyledDocument) this.getDocument();

		String text = getText();
		setText("");

		try {
			doc.insertString(0, text, set);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		doc.setParagraphAttributes(0, doc.getLength() - 1, set, false);
	}

	@Override
	public FontMetrics getFontMetrics(Font font) {
		return lineHeight > 0 ? new FlatLabelFontMetricsHelper(super.getFontMetrics(font), lineHeight) : super.getFontMetrics(font);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(-1,50);
	}


	public static void main(String[] args){
		FlatFrame flatFrame = new FlatFrame();
		flatFrame.setWindowExit(true);
		flatFrame.setTitle("FlatLabel Test");
		flatFrame.setLocationOnScreenCenter();

		FlatLabel flatLabel = new FlatLabel();
		flatLabel.setLineHeight(100);
		flatLabel.setFont(flatLabel.getFont().deriveFont(36.0f));
		flatLabel.setText("A Beautiful Label, this is automatically line jump if string width longer than label width. You just use it well!");
		flatLabel.setTextAlignment(FlatLabelAlign.CENTER);

		flatLabel.setTextAreaFitHeightToWidth(400);
		flatFrame.setSize(400,flatLabel.getPreferredSize().height + FlatFrame.getTitleBarHeight());

		flatFrame.getContainer().add(flatLabel);
		flatFrame.show();


	}
}
