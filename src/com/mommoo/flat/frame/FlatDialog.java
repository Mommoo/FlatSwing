package com.mommoo.flat.frame;

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
import javax.swing.text.JTextComponent;
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

	private final CommonJFrame COMMON_FRAME = new CommonJFrame();
	private final JDialog HIDDEN_DIALOG_FOR_MODALITY = new JDialog(COMMON_FRAME, true);

	private final Builder builder;

	private FlatDialog(Builder builder){
		this.builder = builder;
		initFrame();

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
        buttonInfo.setText("customButton");
        buttonInfo.setOnClickListener(comp->System.out.println("customButton"));

        new FlatDialog.Builder()
                .setTitle("Beautiful Dialog!")
				.setContent("good")
//                .setContent("This is test message\n" +
//                        "When dialog message length increase, this dialog area is wider automatically \n" +
//                        "So, you don't consider that width or height size\n" +
//                        "Just use it!")
                //.setDialogWidth(500)
                .setLocationScreenCenter()
                .appendButton(buttonInfo)
                .build()
                .show();

        System.out.println("blocked this message when dialog showing, if you exit dialog, you can see this message");
    }

	private void initFrame(){
		COMMON_FRAME.setType(JFrame.Type.UTILITY);
		COMMON_FRAME.setAlwaysOnTop(true);
		COMMON_FRAME.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
		HIDDEN_DIALOG_FOR_MODALITY.setUndecorated(true);
		HIDDEN_DIALOG_FOR_MODALITY.setSize(0,0);
	}

	private void initComponent(Component component, DialogComponentInfo dialogComponentInfo){
		component.setForeground(dialogComponentInfo.getTextColor());
		component.setBackground(dialogComponentInfo.getBackgroundColor());
		component.setFont(dialogComponentInfo.getTextFont());

		if (component instanceof JTextComponent){

			((JTextComponent) component).setText(dialogComponentInfo.getText());

		}
	}

	private Component createTitleLabel(){
		FlatLabel TITLE_LABEL = new FlatLabel();
		TITLE_LABEL.setBorder(BorderFactory.createEmptyBorder(PADDING,PADDING,PADDING,PADDING));
		initComponent(TITLE_LABEL, builder.titleInfo);
		TITLE_LABEL.setHeightFittedToWidth(builder.dialogWidth);
		return TITLE_LABEL;
	}

	private Component createContentLabel(){
		FlatLabel CONTENT = new FlatLabel();
		CONTENT.setBorder(BorderFactory.createEmptyBorder(PADDING,PADDING,PADDING,PADDING));
		initComponent(CONTENT, builder.contentInfo);
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
		    BTN_PARENT_PANEL.add(createUserCustomButton(buttonInfo));
        }

        BTN_PARENT_PANEL.add(createOKButton());

		return BTN_PARENT_PANEL;
	}

	private FlatLabel createButton(DialogComponentInfo componentInfo){
		FlatLabel button = new FlatLabel();
		initComponent(button, componentInfo);
		button.setBorder(BorderFactory.createEmptyBorder(SCREEN_MANAGER.dip2px(5),SCREEN_MANAGER.dip2px(7),SCREEN_MANAGER.dip2px(5),SCREEN_MANAGER.dip2px(7)));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
//		button.setHeightFittedToText();
		return button;
	}

    private Component createOKButton(){
        FlatLabel okButton = createButton(builder.buttonDialogComponentInfo);

        OnClickListener onClickListener = component -> {
            builder.onClickListener.onClick(okButton);
            COMMON_FRAME.dispose();
        };

        okButton.setOnClickListener(onClickListener);

        addKeyListener(() -> onClickListener.onClick(okButton));
        return okButton;
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

	private Component createUserCustomButton(DialogButtonInfo buttonInfo){
	    FlatLabel userButton = createButton(buttonInfo);
	    userButton.setOnClickListener(buttonInfo.getOnClickListener());
	    return userButton;
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
		HIDDEN_DIALOG_FOR_MODALITY.setVisible(true);
	}

	public static class Builder{
		private DialogComponentInfo titleInfo = new DialogComponentInfo();
		private DialogComponentInfo contentInfo = new DialogComponentInfo();
		private DialogComponentInfo buttonDialogComponentInfo = new DialogComponentInfo();
		private Color buttonAreaBackgroundColor = buttonDialogComponentInfo.getBackgroundColor();

		private List<DialogButtonInfo> dialogButtonInfoList = new ArrayList<>();

		private JPanel upperView = new JPanel(), lowerView = new JPanel();

		private Color dialogBackgroundColor = Color.WHITE;

		private int dialogWidth = FLAT_DIALOG_WIDTH;
		private float lineSpacing = 0.3f;

		private OnClickListener onClickListener = o -> {};

		private Component locationComponent;
		private FrameLocation frameLocation = FrameLocation.NONE;

		public Builder(){
			titleInfo.setTextFont(FontManager.getNanumGothicFont(Font.BOLD, SCREEN_MANAGER.dip2px(18)));
			contentInfo.setTextFont(FontManager.getNanumGothicFont(Font.PLAIN, SCREEN_MANAGER.dip2px(10)));

			upperView.setPreferredSize(new Dimension(0,0));
			lowerView.setPreferredSize(new Dimension(0,0));

			buttonDialogComponentInfo.setTextFont(FontManager.getNanumGothicFont(Font.BOLD, SCREEN_MANAGER.dip2px(9)));
			buttonDialogComponentInfo.setTextColor(ColorManager.getColorAccent());
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
}
