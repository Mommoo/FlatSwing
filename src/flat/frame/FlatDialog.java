package flat.frame;

import flat.button.FlatButton;
import flat.component.OnClickListener;
import flat.frame.dialog.TextInfo;
import flat.label.FlatLabel;
import util.FontManager;
import util.KeyManager;
import util.ScreenManager;

import javax.activation.CommandMap;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

import static flat.frame.FrameLocation.CENTER_AT_COMPONENT;
import static flat.frame.FrameLocation.CENTER_AT_SCREEN;
import static flat.frame.FrameLocation.RELATIVE_AT_COMPONENT;

public class FlatDialog {
	private final static ScreenManager SCREEN_MANAGER = ScreenManager.getInstance();
	private final static int FLAT_DIALOG_WIDTH = SCREEN_MANAGER.getWindowWidth()/5;

	private final static int PADDING = FLAT_DIALOG_WIDTH/28;

	private final CommonJFrame COMMON_FRAME = new CommonJFrame();

	private final Builder builder;

	private FlatDialog(Builder builder){
		this.builder = builder;
		initFrame();
		getContainer().setLayout(new BorderLayout());
		getContainer().add(createTitleLabel(builder),   BorderLayout.NORTH);
		getContainer().add(createContentLabel(builder), BorderLayout.CENTER);
		getContainer().add(createButtonPanel(builder),  BorderLayout.SOUTH);
		COMMON_FRAME.pack();

		setBackGroundColor(builder.dialogBackgroundColor);
	}
	
	private void initFrame(){
		COMMON_FRAME.setType(JFrame.Type.UTILITY);
		COMMON_FRAME.setAlwaysOnTop(true);
		COMMON_FRAME.getCustomizablePanel().setLayout(new BorderLayout());
		COMMON_FRAME.getCustomizablePanel().setPreferredSize(new Dimension(builder.dialogWidth,SCREEN_MANAGER.getScreenHeight()));
		COMMON_FRAME.getCustomizablePanel().setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
	}

	private void initComponent(Component component, TextInfo textInfo){
		component.setForeground(textInfo.getTextColor());
		component.setBackground(textInfo.getBackgroundColor());
		component.setFont(textInfo.getTextFont());

		if (component instanceof JTextComponent){

			((JTextComponent) component).setText(textInfo.getText());

		} else if (component instanceof FlatButton){

			((FlatButton) component).setText(textInfo.getText());
			((FlatButton) component).setThemeColor(textInfo.getBackgroundColor());
		}
	}

	private Component createTitleLabel(Builder builder){
		FlatLabel TITLE_LABEL = new FlatLabel();
		TITLE_LABEL.setBorder(BorderFactory.createEmptyBorder(0, 0, PADDING, 0));
		initComponent(TITLE_LABEL, builder.titleInfo);
		return TITLE_LABEL;
	}

	private Component createContentLabel(Builder builder){
		FlatLabel CONTENT = new FlatLabel();
		CONTENT.setBorder(BorderFactory.createEmptyBorder(0, 0, PADDING, 0));
		CONTENT.setLineHeight(builder.lineHeight);
		initComponent(CONTENT, builder.contentInfo);
		return CONTENT;
	}

	private Component createButtonPanel(Builder builder){
		JPanel BTN_PARENT_PANEL = new JPanel();
		BTN_PARENT_PANEL.setBackground(builder.contentInfo.getBackgroundColor());
		BTN_PARENT_PANEL.setLayout(new FlowLayout(FlowLayout.RIGHT));
		BTN_PARENT_PANEL.add(createButton(builder));

		return BTN_PARENT_PANEL;
	}

	private Component createButton(Builder builder){
		FlatButton BTN = new FlatButton();
		initComponent(BTN, builder.buttonTextInfo);
		BTN.setBorder(BorderFactory.createEmptyBorder(10,15,10,15));
		BTN.setOnClickListener(c-> {
			builder.onClickListener.onClick(BTN);
			COMMON_FRAME.dispose();
		});

		addKeyListener(BTN);
		return BTN;
	}

	private JPanel getContainer(){
		return COMMON_FRAME.getCustomizablePanel();
	}

	private void addKeyListener(FlatButton BTN){
		KeyManager.KeyEventListener eventListener = BTN::doClick;
		KeyManager.addEnterKeyListener(COMMON_FRAME.getCustomizablePanel(), eventListener);
		for(Component component : COMMON_FRAME.getCustomizablePanel().getComponents()){
			KeyManager.addEnterKeyListener(component, eventListener);
		}
	}

	private void setBackGroundColor(Color color){
		getContainer().setBackground(color);
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

	public static class Builder{
		private TextInfo titleInfo = new TextInfo();
		private TextInfo contentInfo = new TextInfo();
		private TextInfo buttonTextInfo = new TextInfo();

		private Color dialogBackgroundColor = Color.WHITE;

		private int dialogWidth = FLAT_DIALOG_WIDTH;
		private int lineHeight = -1;

		private OnClickListener onClickListener = o -> {};

		private Component locationComponent;
		private FrameLocation frameLocation;

		public Builder(){
			titleInfo.setTextFont(FontManager.getNanumGothicFont(Font.BOLD, 50));
			contentInfo.setTextFont(FontManager.getNanumGothicFont(Font.PLAIN, 24));
			buttonTextInfo.setTextFont(FontManager.getNanumGothicFont(Font.PLAIN, 30));
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

//		public FlatViewDialog.Builder setView(JPanel panel){
//			final JPanel WRAP_PANEL = new JPanel();
//			WRAP_PANEL.setLayout(new BorderLayout());
//			WRAP_PANEL.setBackground(new Color(1.0f,1.0f,1.0f,0f));
//			WRAP_PANEL.setPreferredSize(new Dimension(panel.getPreferredSize().width,panel.getPreferredSize().height+FlatDialog.PADDING));
//			WRAP_PANEL.setBorder(BorderFactory.createEmptyBorder(0,0,FlatDialog.PADDING,0));
//			WRAP_PANEL.add(panel,BorderLayout.CENTER);
//			FLAT_DIALOG.COMMON_FRAME.getCustomizablePanel().add(WRAP_PANEL,BorderLayout.CENTER);
//			return this;
//		}

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

	private Point getProperLocation(){

		Dimension frameSize = COMMON_FRAME.getSize();

		switch(builder.frameLocation){
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

	private int calculateDialogHeight(){
		int dialogHeight = PADDING * 2;

		for(Component component : getContainer().getComponents()){
			dialogHeight += component.getPreferredSize().height;
		}
		return dialogHeight;
	}

	public void show(){
		COMMON_FRAME.setSize(new Dimension(builder.dialogWidth, calculateDialogHeight()));
		COMMON_FRAME.setLocation(getProperLocation());
		COMMON_FRAME.setVisible(true);
	}

	public static void main(String[] args){
		new FlatDialog.Builder()
				.setTitle("TEST")
				.setContent("This is test message\n" +
						"When dialog message length increase, this dialog area is wider automatically \n" +
						"So, you don't consider that width or height size\n" +
						"Just use it!")
				.setLocationScreenCenter()
				.setLineHeight(40)
				.build()
				.show();
	}
}
