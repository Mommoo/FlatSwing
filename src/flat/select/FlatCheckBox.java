package flat.select;

import flat.component.FlatPanel;
import flat.component.OnClickListener;
import flat.frame.FlatFrame;
import flat.label.FlatLabel;
import util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class FlatCheckBox extends FlatPanel {
	private static final int PADDING_LEFT = ScreenManager.getInstance().dip2px(8);
	private final CheckBox checkBox = new CheckBox();
	private final FlatLabel guideText = new FlatLabel();
	private int textWidth;
	private boolean isAnchor;
	private MouseListener guideTextMouseListener;
	
	public FlatCheckBox(String guideText){
		setLayout(null);
		setBackground(Color.WHITE);
		add(checkBox);
		add(this.guideText);
		this.guideText.setBorder(BorderFactory.createEmptyBorder(0,PADDING_LEFT,0,0));
		setText(guideText);
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		final int WIDTH = getWidth();
		final int HEIGHT = getHeight();
		g.setColor(getBackground());
		g.drawRect(0, 0, WIDTH, HEIGHT);

		final int CHECK_BOX_GUIDE_TEXT_WIDTH = textWidth < this.getParent().getWidth() ? textWidth : guideText.getPreferredSize().width;
		final int CHECK_BOX_GUIDE_TEXT_HEIGHT = guideText.getPreferredSize().height;
		final int CHECK_BOX_SIZE = CHECK_BOX_GUIDE_TEXT_HEIGHT;
		final int CHECK_BOX_PADDING_TOP = (HEIGHT - CHECK_BOX_SIZE)/2; 
		final int CHECK_BOX_GUIDE_TEXT_X = CHECK_BOX_SIZE;
		final int CHECK_BOX_GUIDE_TEXT_Y = (HEIGHT - CHECK_BOX_GUIDE_TEXT_HEIGHT)/2;
		
		checkBox.setBounds(0,CHECK_BOX_PADDING_TOP,CHECK_BOX_SIZE,CHECK_BOX_SIZE);
		guideText.setLocation(CHECK_BOX_GUIDE_TEXT_X,CHECK_BOX_GUIDE_TEXT_Y);
		guideText.setSize(new Dimension(CHECK_BOX_GUIDE_TEXT_WIDTH,CHECK_BOX_GUIDE_TEXT_HEIGHT));
		guideText.setPreferredSize(new Dimension(CHECK_BOX_GUIDE_TEXT_WIDTH,CHECK_BOX_GUIDE_TEXT_HEIGHT));
		setMinimumSize(new Dimension(CHECK_BOX_SIZE + CHECK_BOX_GUIDE_TEXT_WIDTH , CHECK_BOX_GUIDE_TEXT_HEIGHT));
		setPreferredSize(new Dimension(CHECK_BOX_SIZE + CHECK_BOX_GUIDE_TEXT_WIDTH , CHECK_BOX_GUIDE_TEXT_HEIGHT));
	}

	public void setText(String text){
		guideText.setText(text);
		textWidth = guideText.getFontMetrics(guideText.getFont()).stringWidth(text);
	}
	
	public String getText(){
		return guideText.getText();
	}
	
	public void setTextColor(Color color){
		guideText.setForeground(color);
	}
	
	public Color getTextColor(){
		return guideText.getForeground();
	}

	@Override
	public void setFont(Font font){
		super.setFont(font);
		if (guideText != null) guideText.setFont(font);
	}
	
	public void setAnchored(boolean isAnchored){
		this.isAnchor = isAnchored;
		guideText.setCursor(isAnchored?new Cursor(Cursor.HAND_CURSOR):new Cursor(Cursor.DEFAULT_CURSOR));
		
		if (isAnchor && guideTextMouseListener == null){
			guideTextMouseListener = checkBox.getMouseListener();
			guideText.addMouseListener(guideTextMouseListener);
		}

		if (!isAnchor && guideTextMouseListener != null){
			guideText.removeMouseListener(guideTextMouseListener);
			guideTextMouseListener = null;
		}
			
	}
	
	public boolean isAnchor(){
		return this.isAnchor;
	}
	
	public void setChecked(boolean check){
		checkBox.setChecked(check);
	}
	
	public boolean isChecked(){
		return this.checkBox.isChecked();
	}
	
	public void setOnClickEvent(OnClickListener onClickListener){
		checkBox.setOnClickListener(onClickListener);
	}

	public static void main(String[] args){
		FlatCheckBox flatCheckBox = new FlatCheckBox("beautiful check box");
		flatCheckBox.setAnchored(true);

		FlatFrame flatFrame = new FlatFrame();
		flatFrame.setTitle("FlatCheckBox Test");
		flatFrame.setSize(500,300);
		flatFrame.setLocationOnScreenCenter();
		flatFrame.getContainer().add(flatCheckBox);
		flatFrame.getContainer().setBorder(BorderFactory.createEmptyBorder(100,100,100,100));
		flatFrame.show();
	}
}
