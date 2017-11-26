package com.mommoo.flat.text.label;

import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.text.textarea.FlatTextArea;
import com.mommoo.flat.text.textarea.alignment.FlatHorizontalAlignment;
import com.mommoo.flat.text.textarea.alignment.FlatVerticalAlignment;
import com.mommoo.util.FontManager;

import javax.swing.text.StyledDocument;
import java.awt.*;

/**
 *  FlatLabel is very convenient component.
 *  If text length over the label width, line jump automatically
 */

public class FlatLabel extends FlatTextArea {

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
		FlatFrame flatFrame = new FlatFrame();
		flatFrame.setWindowExit(true);
		flatFrame.setTitle("FlatLabel Test");
		flatFrame.setLocationOnScreenCenter();

//			FlatLabel flatLabel = new FlatLabel("A Beautiful Label!!\n This is automatically line jump if string width longer than label width. You just use it well!");
		FlatLabel flatLabel = new FlatLabel("Test");
		flatLabel.setHorizontalAlignment(FlatHorizontalAlignment.CENTER);
		flatLabel.setVerticalAlignment(FlatVerticalAlignment.CENTER);
		flatLabel.setFont(FontManager.getNanumGothicFont(Font.BOLD, 50));
		flatLabel.setOnClickListener(component -> System.out.println(((FlatLabel) component).getText()));
//		flatLabel.setLineSpacing(0.7f);
		flatLabel.setPreferredSize(new Dimension(200,200));
		flatFrame.setSize(500, 700);
		flatFrame.getContainer().setLayout(new LinearLayout(Orientation.HORIZONTAL));
		flatFrame.getContainer().add(flatLabel);//, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));

		flatLabel.setOpaque(true);
		flatLabel.setBackground(Color.RED);
		flatFrame.show();
		flatLabel.select(0,2);
		flatLabel.replaceSelection("가나다라");
	}

	private void init(){
		setEnableSelection(false);
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	public boolean isEnableSelection(){
		return isFocusable();
	}

	public void setEnableSelection(boolean enableSelection){
		setFocusable(enableSelection);
	}
}
