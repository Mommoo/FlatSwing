package com.mommoo.flat.frame;

import com.mommoo.animation.AnimationAdapter;
import com.mommoo.animation.Animator;
import com.mommoo.animation.timeInterpolator.AnticipateInterpolator;
import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.dialog.DialogButtonInfo;
import com.mommoo.flat.frame.dialog.DialogComponentInfo;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.util.ColorManager;
import com.mommoo.util.FontManager;
import com.mommoo.util.KeyManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import static com.mommoo.flat.frame.FrameLocation.CENTER_AT_COMPONENT;
import static com.mommoo.flat.frame.FrameLocation.CENTER_AT_SCREEN;
import static com.mommoo.flat.frame.FrameLocation.RELATIVE_AT_COMPONENT;

public class FlatDialog {
	private final static ScreenManager SCREEN_MANAGER = ScreenManager.getInstance();
	private final static int FLAT_DIALOG_WIDTH = SCREEN_MANAGER.getWindowWidth()/5;

	private final static int PADDING = FLAT_DIALOG_WIDTH/28;

	private final static int ANIMATION_DURATION = 300;

	private final CommonJFrame COMMON_FRAME = createCommonJFrame();
	private final JDialog HIDDEN_DIALOG_FOR_MODALITY = createHiddenModalDialog();

	private final Builder builder;

	private FlatDialog(Builder builder){
		this.builder = builder;

		LinearConstraints constraints = new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT);

		Container container = getContainer();

		container.setLayout(new LinearLayout(Orientation.VERTICAL, 0));
		container.setBackground(builder.dialogBackgroundColor);

		container.add(createTitleLabel(),   constraints);
		container.add(builder.upperView,    constraints);
		container.add(createContentLabel(), constraints);
		container.add(builder.lowerView,    constraints);
		container.add(createButtonPanel(),  constraints);

	}
	
    public static void main(String[] args){
        DialogButtonInfo buttonInfo = new DialogButtonInfo();
        buttonInfo.setText("customButton")
				.setOnClickListener(comp->System.out.println("customButton"));

        new FlatDialog.Builder()
                .setTitle("Beautiful Dialog!")
//                .setContent("This is test message\n" +
//                        "When dialog message length increase, this dialog area is wider automatically \n" +
//                        "So, you don't consider that width or height size\n" +
//                        "Just use it!")
				.setTitle("이상하네")
				.setContent("잘지내에에에에ㅔ")
                .setDialogWidth(1000)
                .setLocationScreenCenter()
                .appendButton(buttonInfo)
                .build()
                .show();

        System.out.println("blocked this message when dialog showing, if you exit dialog, you can see this message");
    }

	private CommonJFrame createCommonJFrame(){
    	CommonJFrame COMMON_FRAME = new CommonJFrame();
		COMMON_FRAME.setType(JFrame.Type.UTILITY);
		COMMON_FRAME.setShadowWidth(SCREEN_MANAGER.dip2px(3));
		COMMON_FRAME.setAlwaysOnTop(true);
		COMMON_FRAME.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
		return COMMON_FRAME;
	}

	private JDialog createHiddenModalDialog(){
		JDialog HIDDEN_DIALOG_FOR_MODALITY = new JDialog(COMMON_FRAME, true);
		HIDDEN_DIALOG_FOR_MODALITY.setUndecorated(true);
		HIDDEN_DIALOG_FOR_MODALITY.setSize(0,0);
		HIDDEN_DIALOG_FOR_MODALITY.setFocusable(false);
		HIDDEN_DIALOG_FOR_MODALITY.setFocusableWindowState(false);
		HIDDEN_DIALOG_FOR_MODALITY.setAutoRequestFocus(false);
		return HIDDEN_DIALOG_FOR_MODALITY;
	}

	private FlatLabel createCommonLabel(DialogComponentInfo dialogComponentInfo){
		FlatLabel commonLabel = new FlatLabel();
		commonLabel.setForeground(dialogComponentInfo.getTextColor());
		commonLabel.setBackground(dialogComponentInfo.getBackgroundColor());
		commonLabel.setFont(dialogComponentInfo.getTextFont());
		commonLabel.setText(dialogComponentInfo.getText());
		return commonLabel;
	}

	private Component createTitleLabel(){
		FlatLabel TITLE_LABEL = createCommonLabel(builder.titleInfo);
		TITLE_LABEL.setBorder(BorderFactory.createEmptyBorder(PADDING,PADDING,PADDING,PADDING));
		TITLE_LABEL.setHeightFittedToWidth(builder.dialogWidth);
		return TITLE_LABEL;
	}

	private Component createContentLabel(){
		FlatLabel CONTENT = createCommonLabel(builder.contentInfo);
		CONTENT.setBorder(BorderFactory.createEmptyBorder(PADDING,PADDING,PADDING,PADDING));
		CONTENT.setLineSpacing(builder.lineSpacing);
		CONTENT.setHeightFittedToWidth(builder.dialogWidth);
		return CONTENT;
	}

	private Component createButtonPanel(){
		JPanel BTN_PARENT_PANEL = new JPanel();
		BTN_PARENT_PANEL.setBorder(BorderFactory.createEmptyBorder(PADDING,PADDING,PADDING,PADDING));
		BTN_PARENT_PANEL.setOpaque(true);
		BTN_PARENT_PANEL.setBackground(builder.buttonAreaBackgroundColor);
		BTN_PARENT_PANEL.setLayout(new FlowLayout(FlowLayout.RIGHT));

		for (DialogButtonInfo buttonInfo : builder.dialogButtonInfoList){
		    BTN_PARENT_PANEL.add(createButton(buttonInfo));
        }

		FlatLabel okButton = (FlatLabel)BTN_PARENT_PANEL.getComponent(builder.dialogButtonInfoList.size() - 1);
		addEnterKeyListener(() -> okButton.getOnClickListener().onClick(okButton));
		return BTN_PARENT_PANEL;
	}

	private FlatLabel createButton(DialogButtonInfo buttonInfo){
		FlatLabel button = createCommonLabel(buttonInfo);
		button.setBorder(BorderFactory.createEmptyBorder(SCREEN_MANAGER.dip2px(5),SCREEN_MANAGER.dip2px(7),SCREEN_MANAGER.dip2px(5),SCREEN_MANAGER.dip2px(7)));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setOnClickListener(comp -> {
			buttonInfo.getOnClickListener().onClick(comp);

			if (buttonInfo.isExit()){
				executeExit(buttonInfo.isEndAnimation() ? ANIMATION_DURATION : 0);
			}
		});
		return button;
	}

	private void executeExit(int animDuration){
		final int previousY = COMMON_FRAME.getY();
		new Animator()
				.setDuration(animDuration)
				.setTimeInterpolator(new AnticipateInterpolator())
				.setAnimationListener(new AnimationAdapter(){
					@Override
					public void onAnimation(List<Double> resultList) {
						COMMON_FRAME.setLocation(COMMON_FRAME.getX(), previousY - resultList.get(0).intValue());
					}

					@Override
					public void onEnd() {
						COMMON_FRAME.dispose();
					}
				})
				.start(previousY  + COMMON_FRAME.getHeight());
	}


	private Container getContainer(){
		return COMMON_FRAME.getCustomizablePanel();
	}

	private void addEnterKeyListener(KeyManager.KeyEventListener eventListener){
		KeyManager.addEnterKeyListener(COMMON_FRAME, eventListener);
		KeyManager.addEnterKeyListener(COMMON_FRAME.getCustomizablePanel(), eventListener);
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
		return builder.buttonDialogComponentInfo.getTextFont();
	}

	public String getContent(){
		return builder.contentInfo.getText();
	}

	public Color getBackgroundColor(){
		return builder.dialogBackgroundColor;
	}

	public Color getButtonTextColor(){
		return builder.buttonDialogComponentInfo.getTextColor();
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

	public Color getButtonBackgroundColor(){
		return builder.buttonDialogComponentInfo.getBackgroundColor();
	}

	public void show(){
		COMMON_FRAME.pack();
		COMMON_FRAME.setLocation(getProperLocation());
		COMMON_FRAME.setVisible(true);
		COMMON_FRAME.getCustomizablePanel().requestFocus();
		COMMON_FRAME.getCustomizablePanel().requestFocusInWindow();
		COMMON_FRAME.requestFocus();
		COMMON_FRAME.requestFocusInWindow();

		HIDDEN_DIALOG_FOR_MODALITY.setVisible(true);
	}

	public static class Builder{
		private static Font DEFAULT_TITLE_FONT = FontManager.getNanumGothicFont(Font.BOLD, SCREEN_MANAGER.dip2px(16));
		private static Font DEFAULT_CONTENT_FONT = FontManager.getNanumGothicFont(Font.PLAIN, SCREEN_MANAGER.dip2px(10));
		private static Font DEFAULT_BUTTON_FONT  = FontManager.getNanumGothicFont(Font.BOLD, SCREEN_MANAGER.dip2px(9));

		private DialogComponentInfo titleInfo = new DialogComponentInfo();
		private DialogComponentInfo contentInfo = new DialogComponentInfo();
		private DialogButtonInfo buttonDialogComponentInfo = new DialogButtonInfo();
		private Color buttonAreaBackgroundColor = buttonDialogComponentInfo.getBackgroundColor();

		private List<DialogButtonInfo> dialogButtonInfoList = new ArrayList<>();

		private JPanel upperView = new JPanel(), lowerView = new JPanel();

		private Color dialogBackgroundColor = Color.WHITE;

		private int dialogWidth = FLAT_DIALOG_WIDTH;
		private float lineSpacing = 0.3f;

		private Component locationComponent;
		private FrameLocation frameLocation = FrameLocation.NONE;

		public Builder(){
			titleInfo.setTextFont(DEFAULT_TITLE_FONT);
			contentInfo.setTextFont(DEFAULT_CONTENT_FONT);

			upperView.setPreferredSize(new Dimension(0,0));
			lowerView.setPreferredSize(new Dimension(0,0));

			buttonDialogComponentInfo.setTextFont(DEFAULT_BUTTON_FONT);
			buttonDialogComponentInfo.setTextColor(ColorManager.getColorAccent());
			buttonDialogComponentInfo.setExitOption(true, true);
			buttonDialogComponentInfo.setText("OK");
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
			buttonDialogComponentInfo.setText(text);
			return this;
		}

		public Builder setButtonTextFont(Font font){
			buttonDialogComponentInfo.setTextFont(font);
			return this;
		}

		public Builder setButtonTextColor(Color color){
			buttonDialogComponentInfo.setTextColor(color);
			return this;
		}

		public Builder setButtonAreaBackgroundColor(Color color){
			buttonAreaBackgroundColor = color;
			return this;
		}

		public Builder setButtonBackgroundColor(Color color){
			buttonDialogComponentInfo.setBackgroundColor(color);
			return this;
		}

		public Builder setBackgroundColor(Color color){
			dialogBackgroundColor = color;
			return this;
		}

		public Builder setButtonAnimation(boolean animation){
			buttonDialogComponentInfo.setExitOption(true, animation);
			return this;
		}

		public Builder appendButton(DialogButtonInfo buttonInfo){
		    dialogButtonInfoList.add(buttonInfo);
		    return this;
        }

		public Builder setLineSpacing(float lineSpacing){
			this.lineSpacing = lineSpacing;
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
			this.buttonDialogComponentInfo.setOnClickListener(onClickListener);
			return this;
		}

		public Builder setDialogWidth(int width){
			dialogWidth = width;
			return this;
		}

		public FlatDialog build(){
			this.dialogButtonInfoList.add(this.buttonDialogComponentInfo);
			return new FlatDialog(this);
		}
	}
}
