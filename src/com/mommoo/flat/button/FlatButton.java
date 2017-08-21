package com.mommoo.flat.button;

import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.util.FontManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FlatButton extends JLabel implements MouseListener{
	private static final int BUTTON_TEXT_FONT_SIZE = ScreenManager.getInstance().dip2px(10);
	private final BasicStroke ONE_STROKE = new BasicStroke(1.0f);
	private final BasicStroke TWO_STROKE = new BasicStroke(2.0f);
	private Color themeColor = Color.PINK;
	private Color darkenColor = themeColor.darker();
	private Color lightenColor = themeColor.brighter();
	private Color doubleDarkenColor = darkenColor.darker();
	
	private boolean isOutLine; 
	protected OnClickListener onClickListener;
	protected boolean isMouseExited,isMouseEntered,isMousePressed;
	
	public FlatButton(){
		setHorizontalAlignment(JLabel.CENTER);
		setOpaque(true);
		setFont(FontManager.getNanumGothicFont(Font.BOLD,BUTTON_TEXT_FONT_SIZE));
		setBackground(themeColor);
		setForeground(Color.WHITE);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(this);
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		if(!isOutLine) return; 
		final Graphics2D G_2D = (Graphics2D)g;
		final int WIDTH = getWidth();
		final int HEIGHT = getHeight();

		G_2D.setColor(Color.LIGHT_GRAY);
		G_2D.drawRect(0, 0, WIDTH-1, getHeight()-1);

		if(isMousePressed){
			G_2D.setColor(doubleDarkenColor);
			G_2D.setStroke(TWO_STROKE);
			G_2D.drawLine(2,2,WIDTH-2,2);
			G_2D.drawLine(2,2,2,HEIGHT-2);
			G_2D.setStroke(ONE_STROKE);
			G_2D.drawLine(2,HEIGHT-2,WIDTH-2,HEIGHT-2);
			G_2D.drawLine(WIDTH-2,2,WIDTH-2,HEIGHT-2);
		}
		else if(isMouseEntered){
			G_2D.setStroke(TWO_STROKE);
			G_2D.setColor(doubleDarkenColor);
			G_2D.drawRect(1, 1, WIDTH-2, HEIGHT-2);
		}
		
	}
	
	public FlatButton(String text){
		this();
		setText(text);
	}

	public void setThemeColor(Color themeColor){
		this.themeColor = themeColor;
		darkenColor = themeColor.darker();
		lightenColor = themeColor.brighter();
		doubleDarkenColor = themeColor.darker();
		setBackground(themeColor);
	}

	public Color getThemeColor(){
		return this.themeColor;
	}
	
	public void doClick(){
		setBackground(darkenColor);
		new Thread(){
			private final int MILLISECOND = 70;
			@Override
			public void run(){
				try {
					Thread.sleep(MILLISECOND);
					setBackground(themeColor);
					Thread.sleep(MILLISECOND);
					if(onClickListener != null) onClickListener.onClick(FlatButton.this);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}.start();
	}
	
	public void setTextColor(Color color){
		setForeground(color);
	}
	
	public Color getTextColor(){
		return getForeground();
	}
	
	public void setOnClickListener(OnClickListener onClickListener){
		this.onClickListener = onClickListener;
	}
	
	public void setOutLine(boolean isOutLine){
		this.isOutLine = isOutLine;
	}
	
	public boolean isOutLine(){
		return this.isOutLine;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		setBackground(themeColor);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		isMousePressed = true;
		setBackground(doubleDarkenColor);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		isMousePressed = false;
		setBackground(themeColor);
		repaint();
		if (!isMouseExited){
			if(onClickListener != null) onClickListener.onClick(this);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		isMouseExited = false;
		isMouseEntered = true;
		if(!isOutLine) setBackground(darkenColor);
		if(isMousePressed) setBackground(doubleDarkenColor);
		else repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		isMouseExited = true;
		isMouseEntered = false;
		setBackground(themeColor);
		repaint();
	}

	public static void main(String[] args){
		FlatButton flatButton = new FlatButton("TEST");

		FlatFrame flatFrame = new FlatFrame();
		flatFrame.setTitle("FlatButton test");
		flatFrame.setSize(400,200);
		flatFrame.getContainer().setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		flatFrame.getContainer().add(flatButton);
		flatFrame.setLocationOnScreenCenter();
		flatFrame.show();
	}
}
