package com.mommoo.flat.text.label;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.component.MouseClickAdapter;
import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.image.FlatImagePanel;
import com.mommoo.flat.image.ImageOption;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.text.textarea.FlatTextAlignment;
import com.mommoo.flat.text.textarea.FlatTextArea;
import com.mommoo.util.FontManager;
import com.mommoo.util.ImageManager;
import com.mommoo.util.ScreenManager;
import javafx.stage.Screen;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;

/**
 *  FlatLabel is very convenient component.
 *  If text length over the label width, line jump automatically
 */

public class FlatLabel extends FlatTextArea {
	private static final int TEXT_FONT_SIZE = ScreenManager.getInstance().dip2px(8);

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

	public static void main(String[] args){
		SwingUtilities.invokeLater(()->{
			FlatFrame flatFrame = new FlatFrame();
			flatFrame.setWindowExit(true);
			flatFrame.setTitle("FlatLabel Test");
			flatFrame.setLocationOnScreenCenter();

			FlatLabel flatLabel = new FlatLabel();
//		flatLabel.setFont();

			flatLabel.setText("A Beautiful Label!!\n This is automatically line jump if string width longer than label width. You just use it well!");
//			flatLabel.setText("10");
			flatLabel.setOnClickListener(component -> System.out.println(((FlatLabel)component).getText()));
//		flatLabel.setLineSpacing(0.7f);
			int padding = 50;

//			flatLabel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

			flatFrame.setSize(500,flatLabel.getFitHeightToWidth(500) + FlatFrame.getTitleBarHeight());
			flatFrame.getContainer().setLayout(new FlowLayout());
			flatLabel.setPreferredSize(new Dimension(100,300));
			flatFrame.getContainer().add(flatLabel);
			flatFrame.show();
		});
	}

	private void init(){
		setEditable(false);
		setFocusable(false);
		setFont(FontManager.getNanumGothicFont(Font.PLAIN, TEXT_FONT_SIZE));
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(-1,50);
	}
}
