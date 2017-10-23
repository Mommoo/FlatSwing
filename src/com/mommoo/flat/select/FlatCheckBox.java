package com.mommoo.flat.select;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.util.ImageManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FlatCheckBox extends FlatPanel {
	private static final int GAP = ScreenManager.getInstance().dip2px(3);
	private final CheckBox CHECK_BOX = new CheckBox();
	private final FlatLabel GUIDE_LABEL = new FlatLabel();
	private boolean isAnchor;
	
	public FlatCheckBox(String guideText){
		super.setLayout(new LinearLayout(GAP));
		setBackground(Color.WHITE);
		add(CHECK_BOX,   new LinearConstraints(LinearSpace.WRAP_CENTER_CONTENT));
		add(GUIDE_LABEL, new LinearConstraints(LinearSpace.WRAP_CENTER_CONTENT));
		GUIDE_LABEL.setText(guideText);
		GUIDE_LABEL.setOpaque(false);
	}

	public static void main(String[] args) throws Exception{
		FlatCheckBox flatCheckBox = new FlatCheckBox("Beautiful Check Box1");
		flatCheckBox.setAnchored(true);
		flatCheckBox.setChecked(true);
		flatCheckBox.setCheckBoxLineColor(Color.BLUE);
		flatCheckBox.setCheckColor(Color.BLUE);

		FlatCheckBox flatCheckBox2 = new FlatCheckBox("Beautiful Check Box2");
		flatCheckBox2.setGap(20);
		flatCheckBox2.setCheckBoxLineColor(Color.BLUE);
		flatCheckBox2.setCheckColor(Color.BLUE);
		flatCheckBox2.setEnabled(false);

		FlatFrame flatFrame = new FlatFrame();
		flatFrame.setTitle("FlatCheckBox Test");
		flatFrame.setSize(500,300);
		flatFrame.setLocationOnScreenCenter();
		flatFrame.getContainer().setLayout(new LinearLayout(Orientation.VERTICAL));
		flatFrame.getContainer().add(flatCheckBox);
		flatFrame.getContainer().add(flatCheckBox2);
//		flatFrame.getContainer().setBorder(BorderFactory.createEmptyBorder(100,100,100,100));
		flatFrame.show();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (!isEnabled()){
			g.setColor(Color.BLACK);
			g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		CHECK_BOX.setEnabled(enabled);
		GUIDE_LABEL.setEnabled(enabled);
	}

	@Override
	public LayoutManager getLayout(){
		return null;
	}

	@Override
	public void setLayout(LayoutManager layout){

	}

	public int getGap(){
		return ((LinearLayout)super.getLayout()).getGap();
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

	public void setGap(int gap){
		((LinearLayout)super.getLayout()).setGap(gap);
	}

	public Color getCheckColor(){
		return CHECK_BOX.getCheckColor();
	}

	public void setCheckColor(Color color){
		CHECK_BOX.setCheckColor(color);
	}

	public Color getCheckBoxLineColor(){
		return CHECK_BOX.getCheckBoxColor();
	}
	
	public void setOnClickEvent(OnClickListener onClickListener){
		CHECK_BOX.setOnClickListener(onClickListener);
	}

	public void setCheckBoxLineColor(Color boxLineColor){
		CHECK_BOX.setCheckBoxColor(boxLineColor);
	}
}