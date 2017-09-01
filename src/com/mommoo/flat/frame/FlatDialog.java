package com.mommoo.flat.frame;

import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.dialog.TextInfo;
import com.mommoo.flat.label.FlatLabel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.util.ColorManager;
import com.mommoo.util.FontManager;
import com.mommoo.util.KeyManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

import static com.mommoo.flat.frame.FrameLocation.CENTER_AT_COMPONENT;
import static com.mommoo.flat.frame.FrameLocation.CENTER_AT_SCREEN;
import static com.mommoo.flat.frame.FrameLocation.RELATIVE_AT_COMPONENT;

public class FlatDialog {
	private final static ScreenManager SCREEN_MANAGER = ScreenManager.getInstance();
	private final static int FLAT_DIALOG_WIDTH = SCREEN_MANAGER.getWindowWidth()/5;

	private final static int PADDING = FLAT_DIALOG_WIDTH/28;

	private final CommonJFrame COMMON_FRAME = new CommonJFrame();

	private final Builder builder;

	private FlatDialog(Builder builder){
		this.builder = builder;
		initFrame();

		LinearConstraints constraints = new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT);

		Container container = getContainer();

		container.setLayout(new LinearLayout(Orientation.VERTICAL));
		container.setBackground(builder.dialogBackgroundColor);

		container.add(createTitleLabel(),   constraints);
		container.add(builder.upperView,           constraints);
		container.add(createContentLabel(), constraints);
		container.add(builder.lowerView,           constraints);
		container.add(createButtonPanel(),  constraints);
	}
	
	private void initFrame(){
		COMMON_FRAME.setType(JFrame.Type.UTILITY);
		COMMON_FRAME.setAlwaysOnTop(true);
		COMMON_FRAME.getCustomizablePanel().setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
	}

	private void initComponent(Component component, TextInfo textInfo){
		component.setForeground(textInfo.getTextColor());
		component.setBackground(textInfo.getBackgroundColor());
		component.setFont(textInfo.getTextFont());

		if (component instanceof JTextComponent){

			((JTextComponent) component).setText(textInfo.getText());

		}
	}

	private Component createTitleLabel(){
		FlatLabel TITLE_LABEL = new FlatLabel();
		initComponent(TITLE_LABEL, builder.titleInfo);
		TITLE_LABEL.setTextAreaFitHeightToWidth(builder.dialogWidth);
		return TITLE_LABEL;
	}

	private Component createContentLabel(){
		FlatLabel CONTENT = new FlatLabel();
		initComponent(CONTENT, builder.contentInfo);
		CONTENT.setLineHeight(builder.lineHeight);
		CONTENT.setTextAreaFitHeightToWidth(builder.dialogWidth);

		return CONTENT;
	}

	private Component createButtonPanel(){
		JPanel BTN_PARENT_PANEL = new JPanel();
		BTN_PARENT_PANEL.setBackground(builder.contentInfo.getBackgroundColor());
		BTN_PARENT_PANEL.setLayout(new FlowLayout(FlowLayout.RIGHT));
		BTN_PARENT_PANEL.add(createButton());

		return BTN_PARENT_PANEL;
	}

	private Component createButton(){
		FlatLabel label = new FlatLabel();
		initComponent(label, builder.buttonTextInfo);
		label.setBorder(BorderFactory.createEmptyBorder(0,15,0,15));
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));

		OnClickListener onClickListener = component -> {
			builder.onClickListener.onClick(label);
			COMMON_FRAME.dispose();
		};

		label.setOnClickListener(onClickListener);

		addKeyListener(() -> onClickListener.onClick(label));
		return label;
	}

	private Container getContainer(){
		return COMMON_FRAME.getCustomizablePanel();
	}

	private void addKeyListener(KeyManager.KeyEventListener eventListener){
		KeyManager.addEnterKeyListener(COMMON_FRAME.getCustomizablePanel(), eventListener);
		for(Component component : COMMON_FRAME.getCustomizablePanel().getComponents()){
			KeyManager.addEnterKeyListener(component, eventListener);
		}
	}

	public String getTitle(){
		return builder.titleInfo.getText();
	}

	public Font getTitleFont(){
		return builder.titleInfo.getTextFont();
	}

	public Font getContentFont(){
		return builder.contentInfo.getTextFont();
	}

	public Font getButtonFont(){
		return builder.buttonTextInfo.getTextFont();
	}

	public Color getButtonTextColor(){
		return builder.buttonTextInfo.getTextColor();
	}

	public String getContent(){
		return builder.contentInfo.getText();
	}

	public Color getBackgroundColor(){
		return builder.dialogBackgroundColor;
	}

	public Color getButtonBackgroundColor(){
		return builder.buttonTextInfo.getBackgroundColor();
	}

	public Point getLocation(){
		return COMMON_FRAME.getLocation();
	}

	private Point getProperLocation(){

		Dimension frameSize = COMMON_FRAME.getSize();

		switch(builder.frameLocation){
			case NONE :

				return new Point(0, 0);

			case CENTER_AT_COMPONENT :

				Point targetCompPoint = builder.locationComponent.getLocation();
				Dimension targetCompSize = builder.locationComponent.getSize();

				return new Point(targetCompPoint.x + (targetCompSize.width - frameSize.width)/2, targetCompPoint.y + (targetCompSize.height - frameSize.height)/2);

			case CENTER_AT_SCREEN :

				return new Point((SCREEN_MANAGER.getScreenWidth() - frameSize.width)/2, (SCREEN_MANAGER.getWindowHeight() - frameSize.height)/2);

			case RELATIVE_AT_COMPONENT:

				COMMON_FRAME.setLocationRelativeTo(builder.locationComponent);
				return COMMON_FRAME.getLocation();

			default:
				return null;
		}
	}

	public static class Builder{
		private TextInfo titleInfo = new TextInfo();
		private TextInfo contentInfo = new TextInfo();
		private TextInfo buttonTextInfo = new TextInfo();

		private JPanel upperView = new JPanel(), lowerView = new JPanel();

		private Color dialogBackgroundColor = Color.WHITE;

		private int dialogWidth = FLAT_DIALOG_WIDTH;
		private int lineHeight = -1;

		private OnClickListener onClickListener = o -> {};

		private Component locationComponent;
		private FrameLocation frameLocation = FrameLocation.NONE;

		public Builder(){
			titleInfo.setTextFont(FontManager.getNanumGothicFont(Font.BOLD, 44));
			contentInfo.setTextFont(FontManager.getNanumGothicFont(Font.PLAIN, 20));

			upperView.setPreferredSize(new Dimension(0,0));
			lowerView.setPreferredSize(new Dimension(0,0));

			buttonTextInfo.setTextFont(FontManager.getNanumGothicFont(Font.BOLD, 18));
			buttonTextInfo.setTextColor(ColorManager.getColorAccent());
			buttonTextInfo.setText("OK");
		}

		public Builder setTitle(String title){
			titleInfo.setText(title);
			return this;
		}

		public Builder setTitleFont(Font font){
			titleInfo.setTextFont(font);
			return this;
		}

		public Builder setTitleColor(Color color){
			titleInfo.setTextColor(color);
			return this;
		}

		public Builder setTitleBackgroundColor(Color color){
			titleInfo.setBackgroundColor(color);
			return this;
		}

		public Builder setContent(String content){
			contentInfo.setText(content);
			return this;
		}

		public Builder setContentFont(Font font){
			contentInfo.setTextFont(font);
			return this;
		}

		public Builder setContentColor(Color color){
			contentInfo.setTextColor(color);
			return this;
		}

		public Builder setContentBackgroundColor(Color color){
			contentInfo.setBackgroundColor(color);
			return this;
		}

		public Builder setUpperView(JPanel view){
			upperView = view;
			return this;
		}

		public Builder setLowerView(JPanel view){
			lowerView = view;
			return this;
		}

		public Builder setButtonText(String text){
			buttonTextInfo.setText(text);
			return this;
		}

		public Builder setButtonTextFont(Font font){
			buttonTextInfo.setTextFont(font);
			return this;
		}

		public Builder setButtonTextColor(Color color){
			buttonTextInfo.setTextColor(color);
			return this;
		}

		public Builder setButtonBackgroundColor(Color color){
			buttonTextInfo.setBackgroundColor(color);
			return this;
		}

		public Builder setBackgroundColor(Color color){
			dialogBackgroundColor = color;
			return this;
		}

		public Builder setLineHeight(int lineHeight){
			this.lineHeight = lineHeight;
			return this;
		}

		public Builder setLocationRelativeTo(Component c){
			locationComponent = c;
			frameLocation = RELATIVE_AT_COMPONENT;
			return this;
		}

		public Builder setLocationCenterTo(Component c){
			frameLocation = CENTER_AT_COMPONENT;
			locationComponent = c;
			return this;
		}

		public Builder setLocationScreenCenter(){
			locationComponent = null;
			frameLocation = CENTER_AT_SCREEN;
			return this;
		}

		public Builder setOnClickListener(OnClickListener onClickListener){
			this.onClickListener = onClickListener;
			return this;
		}

		public Builder setDialogWidth(int width){
			dialogWidth = width;
			return this;
		}

		public FlatDialog build(){
			return new FlatDialog(this);
		}
	}

	public void show(){
		COMMON_FRAME.pack();
		COMMON_FRAME.setLocation(getProperLocation());
		COMMON_FRAME.setVisible(true);
	}

	public static void main(String[] args){
		new FlatDialog.Builder()
				.setTitle("Beautiful Dialog!")
				.setContent("This is test message\n" +
						"When dialog message length increase, this dialog area is wider automatically \n" +
						"So, you don't consider that width or height size\n" +
						"Just use it!")
				.setDialogWidth(500)
				.setLocationScreenCenter()
				.setLineHeight(40)
				.build()
				.show();

		System.out.println(System.getProperty("user.home"));
	}
}
