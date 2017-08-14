package flat.textfield;

import flat.frame.FlatFrame;
import flat.image.FlatImagePanel;
import flat.image.ImageOption;
import util.ColorManager;
import util.ImageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;

public class FlatTextField extends JPanel{
	private static final int DEFAULT_BORDER_WIDTH = 2;

	private int borderWidth = DEFAULT_BORDER_WIDTH;
	private Color focusGainedColor = Color.PINK, focusLostColor = Color.LIGHT_GRAY;

	private boolean isSetHint;
	private boolean repaintComponent;

	private FlatImagePanel imagePanel = new FlatImagePanel();
	private TextFieldProxy textFieldProxy;

	public FlatTextField(boolean passwordMode){
		createProperTextFieldProxy(passwordMode);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createMatteBorder(0, 0, borderWidth, 0, focusLostColor));
		setBackground(ColorManager.getFlatComponentDefaultColor());
		setTextFieldFocusListener();
		add(this.textFieldProxy.getTextField(), BorderLayout.CENTER);
	}

	public FlatTextField(String text, boolean passwordMode){
		this(passwordMode);
		setText(text);
	}

	private void createProperTextFieldProxy(boolean isPasswordMode){
		if (isPasswordMode) this.textFieldProxy = new PasswordTextField();
		else this.textFieldProxy = new NormalTextField();
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

	private boolean isContainedImagePanel(){
		for (Component component : getComponents()){
			if (component == imagePanel) return true;
		}
		return false;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!repaintComponent) return;

		int imageSize = getMinimumSize().height;
		imagePanel.setSize(imageSize, imageSize);
		imagePanel.setLocation(0,(getHeight() - imageSize)/2);
		this.textFieldProxy.getTextField().setLocation(imageSize,this.textFieldProxy.getTextField().getLocation().y);
	}

	private void repaintComponent(){
		repaintComponent = true;
		repaint();
	}

	public void setImageIcon(Image image){
		imagePanel.setIcon(image, ImageOption.MATCH_PARENT);
		if (!isContainedImagePanel()) add(imagePanel, BorderLayout.WEST);
		repaintComponent();
	}
	
	public void setHint(String hint){
		this.isSetHint = true;
		this.textFieldProxy.setHint(hint);
	}

	public void setMargin(Insets insets){
		this.textFieldProxy
				.getTextField()
				.setBorder(BorderFactory.createEmptyBorder(insets.top,insets.left,insets.bottom,insets.right));
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
	
	public void setFocusLostColor(Color color){
		this.focusLostColor = color;
		super.setBorder(BorderFactory.createMatteBorder(0, 0, borderWidth, 0, focusLostColor));
	}

	private boolean isCurrentHintAppeared(){
		boolean isEqualsHintText = this.textFieldProxy.getHint().equals(textFieldProxy.getTextField().getText());
		boolean isEqualsHintForegroundColor = this.textFieldProxy.getHintColor().equals(textFieldProxy.getTextField().getForeground());
		return isSetHint && isEqualsHintText && isEqualsHintForegroundColor;
	}



	public void setText(String text){
		textFieldProxy.setText(text);
	}

	public String getText(){
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

		FlatFrame frame = new FlatFrame();
		frame.setTitle("FlatTextField test");
		frame.setSize(500,300);
		frame.setLocationOnScreenCenter();
		frame.getContainer().setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		frame.getContainer().setLayout(new GridLayout(2,1,0,25));
		frame.getContainer().add(normalTextField);
		frame.getContainer().add(passwordTextField);
		frame.show();
	}
}
