package com.mommoo.flat.label;

import com.mommoo.flat.component.FlatScrollPane;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.util.FontManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;

/**
 *  FlatLabel is very convenient component.
 *  If text length over the label width, line jump automatically
 */

public class FlatLabel extends JTextArea{
	private static final int TEXT_FONT_SIZE = ScreenManager.getInstance().dip2px(10);
	private int lineHeight = 0;

	public FlatLabel(){
		init();
	}
	
	public FlatLabel(Document doc, String text, int rows, int columns) {
		super(doc,text,rows,columns);
		init();
	}

	public FlatLabel(Document doc) {
		super(doc);
		init();
	}

	public FlatLabel(int rows, int columns) {
		super(rows,columns);
		init();
	}

	public FlatLabel(String text, int rows, int columns) {
		super(text,rows,columns);
		init();
	}

	public FlatLabel(String text) {
		super(text);
		init();
	}

	private void init(){
		setWrapStyleWord(true);
		setLineWrap(true);
		setHighlighter(null);
		setEditable(false);
		setFont(FontManager.getNanumGothicFont(Font.PLAIN,TEXT_FONT_SIZE));
	}

	public void setLineHeight(int lineHeight){
		if(lineHeight >0) this.lineHeight = lineHeight;
	}

	public int getLineHeight(){
		return this.lineHeight;
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
		FlatLabel flatLabel = new FlatLabel();
		flatLabel.setLineHeight(50);
		flatLabel.setText("A Beautiful Label\nThis label is automatically line jump if string width longer than label width.\nYou just use it well!");

		FlatScrollPane flatScrollPane = new FlatScrollPane(flatLabel);

		FlatFrame flatFrame = new FlatFrame();
		flatFrame.setTitle("FlatLabel Test");
		flatFrame.setLocationOnScreenCenter();
		flatFrame.setSize(400,300);
		flatFrame.getContainer().setBorder(BorderFactory.createEmptyBorder(30,10,30,10));
		flatFrame.getContainer().add(flatScrollPane);
		flatFrame.show();
	}
}
