package com.mommoo.flat.textfield;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.image.FlatImagePanel;
import com.mommoo.flat.image.ImageOption;
import com.mommoo.flat.label.FlatLabel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.util.ColorManager;
import com.mommoo.util.ImageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;

public class FlatTextField extends FlatPanel {
	private static final int DEFAULT_BORDER_WIDTH = 2;

	private int borderWidth = DEFAULT_BORDER_WIDTH;
	private Color focusGainedColor = Color.PINK, focusLostColor = Color.LIGHT_GRAY;

	private boolean isSetHint;

	private final FlatImagePanel imagePanel = new FlatImagePanel();
	private TextFieldProxy textFieldProxy;

	public FlatTextField(boolean passwordMode){
		createProperTextFieldProxy(passwordMode);
		setLayout(new LinearLayout());
		setBorder(BorderFactory.createMatteBorder(0, 0, borderWidth, 0, focusLostColor));
		setBackground(ColorManager.getFlatComponentDefaultColor());
		setTextFieldFocusListener();
		add(this.textFieldProxy.getTextField(), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
	}

	public FlatTextField(String text, boolean passwordMode){
		this(passwordMode);
		setText(text);
	}

	private void createProperTextFieldProxy(boolean isPasswordMode){
		if (isPasswordMode) this.textFieldProxy = new PasswordTextField();
		else this.textFieldProxy = new NormalTextField();

		this.textFieldProxy.getTextField().setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
	}

	private void setTextFieldFocusListener(){
		this.textFieldProxy
				.getTextField()
				.addFocusListener(new FocusListener() {
					@Override
					public void focusGained(FocusEvent e) {
						FlatTextField.this.setBorder(BorderFactory.createMatteBorder(0, 0, borderWidth, 0, focusGainedColor));
						if (isCurrentHintAppeared()) textFieldProxy.setText("");
					}

					@Override
					public void focusLost(FocusEvent e) {
						FlatTextField.this.setBorder(BorderFactory.createMatteBorder(0, 0, borderWidth, 0, focusLostColor));
						if (textFieldProxy.getText().equals("")) textFieldProxy.setHintText();
					}
				});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (image == null) return;
		Insets insets = getInsets();

		int availableWidth = getWidth() - insets.left - insets.right;
		int availableHeight = getHeight() - insets.top - insets.bottom;

		int standardSize = availableWidth >= availableHeight ? availableHeight : availableWidth;
		int padding = standardSize/5;
		imagePanel.setImage(image, standardSize - padding * 2, standardSize - padding * 2);
		imagePanel.setBorder(BorderFactory.createEmptyBorder(padding,padding,padding,padding));
		imagePanel.setPreferredSize(new Dimension(standardSize, standardSize));
	}

	private Image image;

	public void setImageIcon(Image image){
		this.image = image;
		imagePanel.setBackground(getBackground());
		if (!isComponentContained(imagePanel)) add(imagePanel, new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT), 0);
		repaint();
	}
	
	public void setHint(String hint){
		this.isSetHint = true;
		this.textFieldProxy.setHint(hint);
	}

	public void addKeyListener(KeyListener listener){
		this.textFieldProxy
				.getTextField()
				.addKeyListener(listener);
	}

	public void setBorderWidth(int borderWidth){
		this.borderWidth = borderWidth;
		setBorder(BorderFactory.createMatteBorder(0, 0, borderWidth, 0, focusLostColor));
	}

	public int getBorderWidth(){
		return this.borderWidth;
	}

	public void setHorizontalTextFieldAlignment(int JTextFieldAlignment){
		this.textFieldProxy
				.getTextField()
				.setHorizontalAlignment(JTextFieldAlignment);
	}
	
	public void setFocusGainedColor(Color color){
		this.focusGainedColor = color;
	}

	public Color getFocusGainedColor(){
		return this.focusGainedColor;
	}
	
	public void setFocusLostColor(Color color){
		this.focusLostColor = color;
		super.setBorder(BorderFactory.createMatteBorder(0, 0, borderWidth, 0, focusLostColor));
	}

	public Color getFocusLostColor(){
		return this.focusLostColor;
	}

	private boolean isCurrentHintAppeared(){
		boolean isEqualsHintText = this.textFieldProxy.getHint().equals(textFieldProxy.getTextField().getText());
		boolean isEqualsHintForegroundColor = this.textFieldProxy.getHintColor().equals(textFieldProxy.getTextField().getForeground());
		return isSetHint && isEqualsHintText && isEqualsHintForegroundColor;
	}

	JTextField getTextField(){
		return textFieldProxy.getTextField();
	}

	public void setText(String text){
		textFieldProxy.setText(text);
	}

	public String getText(){
		if (isCurrentHintAppeared()) return "";
		return textFieldProxy.getText();
	}

	@Override
	public void requestFocus() {
		textFieldProxy.getTextField().requestFocus();
	}

	@Override
	public boolean requestFocus(boolean temporary) {
		return textFieldProxy.getTextField().requestFocus(temporary);
	}

	@Override
	public boolean requestFocusInWindow() {
		return textFieldProxy.getTextField().requestFocusInWindow();
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
		if (textFieldProxy != null) textFieldProxy.getTextField().setBackground(color);
		if (imagePanel != null) imagePanel.setBackground(color);
	}

	@Override
	public void setFont(Font font){
		super.setFont(font);
		if (textFieldProxy != null) textFieldProxy.getTextField().setFont(font);
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (textFieldProxy != null) textFieldProxy.getTextField().setForeground(fg);
	}

	public static void main(String[] args){
		FlatTextField normalTextField = new FlatTextField(false);
		normalTextField.setImageIcon(ImageManager.WRITE);
		normalTextField.setHint("write name");
		FlatTextField passwordTextField = new FlatTextField(true);
		passwordTextField.setImageIcon(ImageManager.WRITE);
		passwordTextField.setHint("write password");
		System.out.println(passwordTextField.getText());

		FlatFrame frame = new FlatFrame();
		frame.setTitle("FlatTextField test");
		frame.setSize(500,300);
		frame.setLocationOnScreenCenter();
		frame.getContainer().setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		frame.getContainer().setLayout(new GridLayout(2,1,0,25));
		frame.getContainer().add(normalTextField);
		frame.getContainer().add(passwordTextField);
		frame.setResizable(true);
		frame.show();
	}
}
