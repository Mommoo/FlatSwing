package com.mommoo.flat.select;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.util.ScreenManager;

import java.awt.*;

public class FlatCheckBox extends FlatPanel {
	private static final int GAP = ScreenManager.getInstance().dip2px(3);
	private boolean isAnchor;
	
	public FlatCheckBox(String guideText){
		super.setLayout(new LinearLayout(GAP));
		setBackground(Color.WHITE);
		add(new CheckBox(),   new LinearConstraints(LinearSpace.WRAP_CENTER_CONTENT), "checkBox");
		add(createGuideTextLabel(guideText), new LinearConstraints(LinearSpace.WRAP_CENTER_CONTENT), "guideLabel");
	}

	private Component createGuideTextLabel(String guideText){
		FlatLabel guideTextLabel = new FlatLabel(guideText);
		guideTextLabel.setOpaque(false);
		return guideTextLabel;
	}

	private CheckBox getCheckBox(){
		return (CheckBox)getComponent("checkBox");
	}

	private FlatLabel getGuideTextLabel(){
		return (FlatLabel) getComponent("guideLabel");
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
		getCheckBox().setEnabled(enabled);
		getGuideTextLabel().setEnabled(enabled);
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
		return getGuideTextLabel().getText();
	}
	
	public Color getTextColor(){
		return getGuideTextLabel().getForeground();
	}
	
	public void setTextColor(Color color){
		getGuideTextLabel().setForeground(color);
	}

	@Override
	public void setFont(Font font){
		super.setFont(font);
		if (getGuideTextLabel() != null) getGuideTextLabel().setFont(font);
	}
	
	public void setAnchored(boolean isAnchored){
		this.isAnchor = isAnchored;
		getGuideTextLabel().setCursor(isAnchored ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));

		if (isAnchored){
			getGuideTextLabel().setOnClickListener(comp -> getCheckBox().doClick(this));
		} else {
			getGuideTextLabel().removeOnClickListener();
		}

	}
	
	public boolean isAnchor(){
		return this.isAnchor;
	}
	
	public boolean isChecked(){
		return getCheckBox().isChecked();
	}
	
	public void setChecked(boolean check){
		getCheckBox().setChecked(check);
	}

	public void setGap(int gap){
		((LinearLayout)super.getLayout()).setGap(gap);
	}

	public Color getCheckColor(){
		return getCheckBox().getCheckColor();
	}

	public void setCheckColor(Color color){
		getCheckBox().setCheckColor(color);
	}

	public Color getCheckBoxLineColor(){
		return getCheckBox().getCheckBoxColor();
	}

	public void setCheckBoxLineColor(Color boxLineColor){
		getCheckBox().setCheckBoxColor(boxLineColor);
	}

	@Override
	public void setOnClickListener(OnClickListener onClickListener) {
		super.setOnClickListener(onClickListener);
		getCheckBox().setOnClickListener(this, getOnClickListener());
	}

	@Override
	public void removeOnClickListener() {
		super.removeOnClickListener();
		getCheckBox().removeOnClickListener();
	}
}