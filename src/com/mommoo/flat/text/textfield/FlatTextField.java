package com.mommoo.flat.text.textfield;

import com.mommoo.example.ExampleFactory;
import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.image.FlatImagePanel;
import com.mommoo.flat.image.ImageOption;
import com.mommoo.flat.layout.linear.Alignment;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.text.textfield.format.FlatTextFormat;
import com.mommoo.util.ColorManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.List;

public class FlatTextField extends FlatPanel {
	private static final ScreenManager SCREEN = ScreenManager.getInstance();
	private static final int DEFAULT_BORDER_WIDTH = 2;

	private int borderWidth = DEFAULT_BORDER_WIDTH;
	private Color focusGainedColor = Color.PINK, focusLostColor = Color.LIGHT_GRAY;

	private boolean isPasswordMode;

	public FlatTextField(boolean passwordMode){
		this.isPasswordMode = passwordMode;

		setBackground(ColorManager.getFlatComponentDefaultColor());
		setLayout(new LinearLayout(Orientation.VERTICAL, 0, Alignment.CENTER));
		setOpaque(true);

		add(new ImageTextField(passwordMode), new LinearConstraints(LinearSpace.MATCH_PARENT));
		add(createUnderLine(),                new LinearConstraints(LinearSpace.MATCH_PARENT));

		setTextFieldFocusListener();

	}

	public FlatTextField(String text, boolean passwordMode){
		this(passwordMode);
		setText(text);
	}

	public static void main(String[] args){
		ExampleFactory.FlatTextFieldExample.example2();
	}

	private Component createUnderLine(){
		FlatPanel underLine = new FlatPanel();
		underLine.setOpaque(true);
		underLine.setBackground(focusLostColor);
		underLine.setPreferredSize(new Dimension(1, DEFAULT_BORDER_WIDTH));
		return underLine;
	}

	private Component getUnderLine(){
		return getComponent(1);
	}

	private ImageTextField getImageTextField(){
		return (ImageTextField) getComponent(0);
	}

	private TextFieldProxy getTextFieldProxy(){
		return getImageTextField().textFieldProxy;
	}

	private void setTextFieldFocusListener(){
		getTextField().addFocusListener(new FocusListener() {
					@Override
					public void focusGained(FocusEvent e) {
						getUnderLine().setBackground(focusGainedColor);
						if (getTextFieldProxy().isHintAppeared()) getTextFieldProxy().setNormalText("");
					}

					@Override
					public void focusLost(FocusEvent e) {
						getUnderLine().setBackground(focusLostColor);
						if (getTextFieldProxy().getText().equals("")) getTextFieldProxy().setHint(getHint());
					}
				});
	}

	public String getHint(){
		return getTextFieldProxy().getHint();
	}

	public void setHint(String hint){
		getTextFieldProxy().setHint(hint);
	}

	public Color getHintForeground(){
		return getTextFieldProxy().getHintForeground();
	}

	public void setHintForeground(Color hintForeground){
		getTextFieldProxy().setHintForeground(hintForeground);
	}

	public void addKeyListener(KeyListener listener){
		getTextField().addKeyListener(listener);
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
	
	public void setLineBorderWidth(int borderWidth){
		this.borderWidth = borderWidth;
		getUnderLine().setPreferredSize(new Dimension(getUnderLine().getPreferredSize().width, borderWidth));
	}

	public Color getFocusLostColor(){
		return this.focusLostColor;
	}

	public void setHorizontalTextFieldAlignment(int JTextFieldAlignment){
		getTextField().setHorizontalAlignment(JTextFieldAlignment);
	}

	public void setFocusLostColor(Color color){
		this.focusLostColor = color;
		getUnderLine().setBackground(color);
	}

	public List<FlatTextFormat> getFormatList(){
		return Collections.unmodifiableList(getTextFieldProxy().getFormatList());
	}

	public void setFormat(FlatTextFormat... formats){
		getTextFieldProxy().setFormat(formats);
	}

	JTextField getTextField(){
		return getTextFieldProxy().getTextField();
	}

	public void clear(){
		getTextFieldProxy().clear();
		getTextField().transferFocus();
	}

	@Override
	public void requestFocus() {
		getTextField().requestFocus();
	}

	@Override
	public boolean requestFocus(boolean temporary) {
		return getTextField().requestFocus(temporary);
	}

	@Override
	public boolean requestFocusInWindow() {
		return getTextFieldProxy().getTextField().requestFocusInWindow();
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
	public void setFont(Font font){
		super.setFont(font);
		if (getComponentCount() >= 2) getTextFieldProxy().getTextField().setFont(font);
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (getComponentCount() >= 2) getTextFieldProxy().getTextField().setForeground(fg);
	}

	public void setIconImage(Image image){
		getImageTextField().setIconImage(image);
		revalidate();
		repaint();
	}

	public boolean isPasswordMode(){
		return isPasswordMode;
	}

	public String getText(){
		if (getTextFieldProxy().isHintAppeared()) return getTextFieldProxy().getHint();
		else return getTextFieldProxy().getText();
	}

	public void setText(String text){
		getTextFieldProxy().setText(text);
	}

	public Color getSelectionColor(){
		return getTextField().getSelectionColor();
	}

	public void setSelectionColor(Color color) {
		getTextField().setSelectionColor(color);
	}

	public void setSelectedTextColor(Color color) {
		getTextField().setSelectedTextColor(color);
	}

	public Color getSelectedTextColor(Color color) {
		return getTextField().getSelectedTextColor();
	}

	public void setTextFieldPadding(int top, int left, int bottom, int right){
		getTextField().setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
	}

	public Insets getTextFieldPadding(){
		return getTextField().getInsets();
	}

	public void setTextFieldPadding(int padding){
		setTextFieldPadding(padding, padding, padding, padding);
	}

	public int getColumns(){
		return getTextField().getColumns();
	}

	public void setColumns(int columns){
		getTextField().setColumns(columns);
	}

	public void setImagePadding(int top, int left, int bottom, int right){
		getImageTextField().getImageView().setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
	}

	public Insets getImagePadding(){
		return getImageTextField().getImageView().getInsets();
	}

	public void setImagePadding(int padding){
		setImagePadding(padding, padding, padding, padding);
	}

	private class ImageTextField extends FlatPanel{
		private boolean isNeedReDraw;
		private TextFieldProxy textFieldProxy;

		/**
		 * Confirm Order
		 * @param passwordMode
		 */
		private ImageTextField(boolean passwordMode){
			textFieldProxy = createProperTextFieldProxy(passwordMode);
			setLayout(new LinearLayout(0));
			setBorder(new EmptyBorder(SCREEN.dip2px(3),0,SCREEN.dip2px(3),0));
			add(createImagePanel(), new LinearConstraints().setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
			add(textFieldProxy.getTextField(), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
		}

		private Component createImagePanel(){
			FlatImagePanel imagePanel = new FlatImagePanel();
			imagePanel.setPreferredSize(new Dimension(0, 0));
			return imagePanel;
		}

		private TextFieldProxy createProperTextFieldProxy(boolean isPasswordMode){
			return isPasswordMode ? new PasswordTextField() : new NormalTextField();
		}

		@Override
		protected void postDraw(Graphics2D graphics2D, int availableWidth, int availableHeight) {
			super.postDraw(graphics2D, availableWidth, availableHeight);

			if (!isNeedReDraw) return;

			int standardSize = availableWidth >= availableHeight ? availableHeight : availableWidth;

			FlatImagePanel imagePanel = getImageView();

			imagePanel.setPreferredSize(new Dimension(standardSize, standardSize));
			imagePanel.reDraw();

			revalidate();
			repaint();

			isNeedReDraw = false;
		}

		private FlatImagePanel getImageView(){
			return (FlatImagePanel) getComponent(0);
		}

		private void setIconImage(Image image){
			FlatImagePanel imagePanel = (FlatImagePanel)getComponent(0);
			imagePanel.setImage(image, ImageOption.MATCH_PARENT);
			isNeedReDraw = true;
			repaint();
		}
	}
}
