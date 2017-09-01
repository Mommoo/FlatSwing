package com.mommoo.flat.select;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.label.FlatLabel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class FlatCheckBox extends FlatPanel {
	private static final int GAP = ScreenManager.getInstance().dip2px(8);
	private final CheckBox CHECK_BOX = new CheckBox();
	private final FlatLabel GUIDE_LABEL = new FlatLabel();
	private boolean isAnchor;
	
	public FlatCheckBox(String guideText){
		initFlatCheckBox();
		add(CHECK_BOX);
		add(GUIDE_LABEL);
		this.GUIDE_LABEL.setText(guideText);
		this.GUIDE_LABEL.setOpaque(false);
	}

	private void initFlatCheckBox(){
		setLayout(new LinearLayout(GAP));
		setBackground(Color.WHITE);
	}
	
	public String getText(){
		return GUIDE_LABEL.getText();
	}
	
	public Color getTextColor(){
		return GUIDE_LABEL.getForeground();
	}
	
	public void setTextColor(Color color){
		GUIDE_LABEL.setForeground(color);
	}

	@Override
	public void setFont(Font font){
		super.setFont(font);
		if (GUIDE_LABEL != null) GUIDE_LABEL.setFont(font);
	}
	
	public void setAnchored(boolean isAnchored){
		this.isAnchor = isAnchored;
		GUIDE_LABEL.setCursor(isAnchored ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
        GUIDE_LABEL.setOnClickListener(comp -> CHECK_BOX.doClick());
	}
	
	public boolean isAnchor(){
		return this.isAnchor;
	}
	
	public boolean isChecked(){
		return this.CHECK_BOX.isChecked();
	}
	
	public void setChecked(boolean check){
		CHECK_BOX.setChecked(check);
	}
	
	public void setOnClickEvent(OnClickListener onClickListener){
		CHECK_BOX.setOnClickListener(onClickListener);
	}

	public static void main(String[] args){
		FlatCheckBox flatCheckBox = new FlatCheckBox("beautiful check box");
		flatCheckBox.setAnchored(true);
		flatCheckBox.setChecked(true);

		FlatFrame flatFrame = new FlatFrame();
		flatFrame.setTitle("FlatCheckBox Test");
		flatFrame.setSize(500,300);
		flatFrame.setLocationOnScreenCenter();
		flatFrame.getContainer().add(flatCheckBox);
		flatFrame.getContainer().setBorder(BorderFactory.createEmptyBorder(100,100,100,100));
		flatFrame.show();
	}
}