package com.mommoo.flat.textfield;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.image.FlatImagePanel;
import com.mommoo.flat.image.ImageOption;
import com.mommoo.flat.label.FlatLabel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.util.ColorManager;
import com.mommoo.util.ImageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.image.ImageProducer;

public class FlatTextField extends FlatPanel {
	private static final int DEFAULT_BORDER_WIDTH = 2;

	private int borderWidth = DEFAULT_BORDER_WIDTH;
	private Color focusGainedColor = Color.PINK, focusLostColor = Color.LIGHT_GRAY;

	private final ImageTextField imageTextField;
	private final FlatPanel underLine = new FlatPanel();
	private boolean isSetHint;
	private boolean isPasswordMode;

	public FlatTextField(boolean passwordMode){
		this.isPasswordMode = passwordMode;

		imageTextField = new ImageTextField(passwordMode);

		initFlatTextFiled();
		initUnderLine();
		setTextFieldFocusListener();

		add(imageTextField, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
		add(this.underLine, new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT));
	}

	public FlatTextField(String text, boolean passwordMode){
		this(passwordMode);
		setText(text);
	}

	public static void main(String[] args){
		FlatTextField normalTextField = new FlatTextField(false);
		normalTextField.setHint("write name");
		normalTextField.setBackground(Color.WHITE);

		FlatTextField normalImageTextField = new FlatTextField(false);
		normalImageTextField.setIconImage(ImageManager.WRITE);
		normalImageTextField.setHint("write name");
		normalImageTextField.setBackground(Color.WHITE);
		normalImageTextField.setLineBorderWidth(10);

		FlatTextField passwordTextField = new FlatTextField(true);
		passwordTextField.setIconImage(ImageManager.WRITE);
		passwordTextField.setBackground(Color.WHITE);
		passwordTextField.setHint("write password");

		//normalTextField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		FlatFrame frame = new FlatFrame();
		frame.setTitle("FlatTextField test");
		frame.setSize(500,500);
		frame.setLocationOnScreenCenter();
		frame.getContainer().setBackground(Color.GRAY);
		frame.getContainer().setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		frame.getContainer().setLayout(new GridLayout(3,1,30,30));
		frame.getContainer().add(normalTextField);
		frame.getContainer().add(normalImageTextField);
		frame.getContainer().add(passwordTextField);
		frame.setResizable(true);
		frame.show();
	}

	private void initFlatTextFiled(){
		setBackground(ColorManager.getFlatComponentDefaultColor());
		setLayout(new LinearLayout(Orientation.VERTICAL, 0));
		setOpaque(true);
		setPreferredSize(new Dimension(200,50));
	}

	private void initUnderLine(){
		underLine.setOpaque(true);
		underLine.setBackground(focusLostColor);
		underLine.setPreferredSize(new Dimension(100,DEFAULT_BORDER_WIDTH));
	}

	private void setTextFieldFocusListener(){
		imageTextField.textFieldProxy
				.getTextField()
				.addFocusListener(new FocusListener() {
					@Override
					public void focusGained(FocusEvent e) {
						underLine.setBackground(focusGainedColor);
						if (isCurrentHintAppeared()) imageTextField.textFieldProxy.setText("");
					}

					@Override
					public void focusLost(FocusEvent e) {
						underLine.setBackground(focusLostColor);
						if (imageTextField.textFieldProxy.getText().equals("")) imageTextField.textFieldProxy.setHintText();
					}
				});
	}

	public void setHint(String hint){
		this.isSetHint = true;
		imageTextField.textFieldProxy.setHint(hint);
	}

	public void addKeyListener(KeyListener listener){
		imageTextField.textFieldProxy
				.getTextField()
				.addKeyListener(listener);
	}

	public void setLineBorderWidth(int borderWidth){
		this.borderWidth = borderWidth;
		this.underLine.setPreferredSize(new Dimension(100, borderWidth));
	}

	public int getBorderLineWidth(){
		return this.borderWidth;
	}
	
	public void setFocusGainedColor(Color color){
		this.focusGainedColor = color;
	}

	public Color getFocusGainedColor(){
		return this.focusGainedColor;
	}
	
	public void setHorizontalTextFieldAlignment(int JTextFieldAlignment){
		imageTextField.textFieldProxy
				.getTextField()
				.setHorizontalAlignment(JTextFieldAlignment);
	}

	public Color getFocusLostColor(){
		return this.focusLostColor;
	}

	public void setFocusLostColor(Color color){
		this.focusLostColor = color;
		this.underLine.setBackground(color);
	}

	private boolean isCurrentHintAppeared(){
		boolean isEqualsHintText = imageTextField.textFieldProxy.getHint().equals(imageTextField.textFieldProxy.getTextField().getText());
		boolean isEqualsHintForegroundColor = imageTextField.textFieldProxy.getHintColor().equals(imageTextField.textFieldProxy.getTextField().getForeground());
		return isSetHint && isEqualsHintText && isEqualsHintForegroundColor;
	}

	JTextField getTextField(){
		return imageTextField.textFieldProxy.getTextField();
	}

	public String getText(){
		if (isCurrentHintAppeared()) return "";
		return imageTextField.textFieldProxy.getText();
	}

	public void setText(String text){
		imageTextField.textFieldProxy.setText(text);
	}

	public void clear(){
		imageTextField.textFieldProxy.clear();
	}

	@Override
	public void requestFocus() {
		imageTextField.textFieldProxy.getTextField().requestFocus();
	}

	@Override
	public boolean requestFocus(boolean temporary) {
		return imageTextField.textFieldProxy.getTextField().requestFocus(temporary);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(200,50);
	}

	/*
	 * There is a situation that When this super class load, setBackground method invoked
	 * But, in that time, child class member is loaded not yet
	 * Therefore, if not any do something, invoked null-pointer error
	 * So, to prevent above error, we need to null checking
	 * */
	@Override
	public void setBackground(Color color){
		super.setBackground(color);
	}

	@Override
	public boolean requestFocusInWindow() {
		return imageTextField.textFieldProxy.getTextField().requestFocusInWindow();
	}

	@Override
	public void setFont(Font font){
		super.setFont(font);
		if (imageTextField != null) imageTextField.textFieldProxy.getTextField().setFont(font);
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (imageTextField != null) imageTextField.textFieldProxy.getTextField().setForeground(fg);
	}

	public boolean isPasswordMode(){
		return isPasswordMode;
	}

	public void setIconImage(Image image){
		this.imageTextField.setIconImage(image);
		revalidate();
		repaint();
	}

	private class ImageTextField extends FlatPanel{
		private boolean isNeedReDraw;
		private TextFieldProxy textFieldProxy;

		private ImageTextField(boolean passwordMode){
			textFieldProxy = createProperTextFieldProxy(passwordMode);
			setLayout(new LinearLayout(0));
			add(createImagePanel(), new LinearConstraints().setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
			add(textFieldProxy.getTextField(), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
		}

		private Component createImagePanel(){
			FlatImagePanel imagePanel = new FlatImagePanel();
			imagePanel.setPreferredSize(new Dimension(0, 0));
			return imagePanel;
		}

		private TextFieldProxy createProperTextFieldProxy(boolean isPasswordMode){
			if (isPasswordMode) this.textFieldProxy = new PasswordTextField();
			else this.textFieldProxy = new NormalTextField();

			initTextField();

			return this.textFieldProxy;
		}

		private void initTextField(){
			JTextField textField = this.textFieldProxy.getTextField();
			textField.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
			textField.setOpaque(false);
		}

		@Override
		protected void draw(Graphics2D graphics2D, int availableWidth, int availableHeight) {
			super.draw(graphics2D, availableWidth, availableHeight);

			if (!isNeedReDraw) return;

			int standardSize = availableWidth >= availableHeight ? availableHeight : availableWidth;
			int padding = standardSize/5;

			FlatImagePanel imagePanel = (FlatImagePanel)getComponent(0);

			imagePanel.setPreferredSize(new Dimension(standardSize, standardSize));
			imagePanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
			imagePanel.reDraw();

			revalidate();
			repaint();

			isNeedReDraw = false;
		}

		private void setIconImage(Image image){
			FlatImagePanel imagePanel = (FlatImagePanel)getComponent(0);
			imagePanel.setImage(image, ImageOption.MATCH_PARENT);
			isNeedReDraw = true;
			repaint();
		}
	}
}
