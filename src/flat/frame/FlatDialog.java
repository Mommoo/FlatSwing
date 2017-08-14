package flat.frame;

import flat.button.FlatButton;
import flat.component.OnClickListener;
import flat.label.FlatLabel;
import helper.ClassUtils;
import util.KeyManager;
import util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class FlatDialog {
	private final static ScreenManager SCREEN_MANAGER = ScreenManager.getInstance();
	private final static int FLAT_DIALOG_WIDTH = SCREEN_MANAGER.getWindowWidth()/5;
	final static int PADDING = FLAT_DIALOG_WIDTH/28;
	final CommonJFrame COMMON_JFRAME = new CommonJFrame();
	private final FlatLabel TITLE_LABEL = new FlatLabel();
	final FlatLabel CONTENT = new FlatLabel();
	private final JPanel BTN_PARENT_PANEL = new JPanel();
	private final FlatButton BTN = new FlatButton("확인");
	private String message;
	private String title;
	private Component targetComponent;
	private boolean isForceChangeWidth;
	private boolean isScreenCenterShow;
	private int preferredWidth;

	protected FlatDialog(){
		initContentPane();
		initTitleLabel();
		initContent();
		initButton();
		addComponent();
		addKeyListener();
		setBackgroundColor(Color.WHITE);
	}
	
	private void initContentPane(){
		COMMON_JFRAME.setType(JFrame.Type.UTILITY);
		COMMON_JFRAME.setAlwaysOnTop(true);
		COMMON_JFRAME.setShadowWidth(8);
		COMMON_JFRAME.getCustomizablePanel().setLayout(new BorderLayout());
		COMMON_JFRAME.getCustomizablePanel().setPreferredSize(new Dimension(FLAT_DIALOG_WIDTH,SCREEN_MANAGER.getScreenHeight()));
		COMMON_JFRAME.getCustomizablePanel().setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
	}

	private void initTitleLabel(){
		TITLE_LABEL.setBorder(BorderFactory.createEmptyBorder(0, 0, PADDING, 0));
		TITLE_LABEL.setFont(TITLE_LABEL.getFont().deriveFont((float)SCREEN_MANAGER.dip2px(17)));
	}

	private void initContent(){
		CONTENT.setBorder(BorderFactory.createEmptyBorder(0, 0, PADDING, 0));
		CONTENT.setFont(CONTENT.getFont().deriveFont((float)SCREEN_MANAGER.dip2px(8)));
		CONTENT.setLineHeight(PADDING*2);
	}

	private void initButton(){
		BTN_PARENT_PANEL.setLayout(new FlowLayout(FlowLayout.RIGHT));
		BTN_PARENT_PANEL.add(BTN);
		final int BTN_WIDTH = FLAT_DIALOG_WIDTH/5;
		BTN.setPreferredSize(new Dimension(BTN_WIDTH,BTN_WIDTH/2));
//		BTN.setOutLine(true);
		BTN.setFont(BTN.getFont().deriveFont((float)SCREEN_MANAGER.dip2px(10)));
		BTN.setOnClickListener((c)->{
			COMMON_JFRAME.dispose();
		});
//		BTN.setBackground(Color.decode("#dddddd"));
	}

	private void addComponent(){
		COMMON_JFRAME.getCustomizablePanel().add(TITLE_LABEL,BorderLayout.NORTH);
		COMMON_JFRAME.getCustomizablePanel().add(CONTENT,BorderLayout.CENTER);
		COMMON_JFRAME.getCustomizablePanel().add(BTN_PARENT_PANEL,BorderLayout.SOUTH);
		COMMON_JFRAME.pack();
	}

	private void addKeyListener(){
		KeyManager.KeyEventListener eventListener = ()->BTN.doClick();
		KeyManager.addEnterKeyListener(COMMON_JFRAME.getCustomizablePanel(), eventListener);
		for(Component component : COMMON_JFRAME.getCustomizablePanel().getComponents()){
			KeyManager.addEnterKeyListener(component, eventListener);
		}
	}

	protected void setTitle(String title){
		this.title = title;
		this.TITLE_LABEL.setText(title);
	}

	public String getTitle(){
		return title;
	}

	protected void setTitleFont(Font font){
		this.TITLE_LABEL.setFont(font);
	}

	public Font getTitleFont(){
		return this.TITLE_LABEL.getFont();
	}

	protected void setContentFont(Font font){
		this.CONTENT.setFont(font);
	}

	public Font getContentFont(){
		return this.CONTENT.getFont();
	}

	protected void setButtonFont(Font font){
		this.BTN.setFont(font);
	}

	public Font getButtonFont(){
		return this.BTN.getFont();
	}

	protected void setButtonTextColor(Color color){
		this.BTN.setTextColor(color);
	}

	public Color getButtonTextColor(){
		return this.BTN.getTextColor();
	}

	protected void setMessage(String message){
		this.message = message;
		CONTENT.setText(message);
	}

	public String getMessage(){
		return message;
	}

	protected void setBackgroundColor(Color color){
		COMMON_JFRAME.getCustomizablePanel().setBackground(Color.WHITE);
		for(Component component : COMMON_JFRAME.getCustomizablePanel().getComponents()){
			component.setBackground(Color.WHITE);
		}
	}

	public Color getBackgroundColor(){
		return COMMON_JFRAME.getCustomizablePanel().getBackground();
	}

	protected void setButtonBackgroundColor(Color color){
		this.BTN.setThemeColor(color);
	}

	public Color getButtonBackgroundColor(){
		return this.BTN.getThemeColor();
	}

	protected void setLocationRelativeTo(Component c){
		COMMON_JFRAME.setLocationRelativeTo(c);
	}

	protected void setLocationCenterTo(Component c){
		targetComponent = c;
	}

	protected void setLocationScreenCenter(){
		this.isScreenCenterShow = true;
	}

	public Point getLocation(){
		return COMMON_JFRAME.getLocation();
	}

	protected void applyPreferredWidth(int width){
		COMMON_JFRAME.getCustomizablePanel().setPreferredSize(new Dimension(width,SCREEN_MANAGER.getScreenHeight()));
		isForceChangeWidth = true;
		preferredWidth = width;
	}

	protected void setOnClickEvent(OnClickListener onClickListener){
		this.BTN.setOnClickListener((c)->{
			onClickListener.onClick(c);
			COMMON_JFRAME.dispose();
		});
	}


	public static class Builder<T extends FlatDialog>{
		protected Class<T> ANONYMOUSE_CLASS;
		protected FlatDialog INSTANCE;

		protected Builder(){
			try {
				ANONYMOUSE_CLASS = (Class<T>) ClassUtils.getReclusiveGenericClass(getClass(), 0);
	            if (ANONYMOUSE_CLASS != null) {
	            	Constructor[] constructors = ANONYMOUSE_CLASS.getDeclaredConstructors();
	            	constructors[0].setAccessible(true);
	            	INSTANCE = (T)constructors[0].newInstance();
	            }else{
	            	INSTANCE = new FlatDialog();
	            }
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		public Builder<T> setTitle(String title){
			INSTANCE.setTitle(title);
			return this;
		}

		public Builder<T> setTitleFont(Font font){
			INSTANCE.setTitleFont(font.deriveFont(font.getSize2D()));
			return this;
		}

		public Builder<T> setContentFont(Font font){
			INSTANCE.setContentFont(font.deriveFont(font.getSize2D()));
			return this;
		}

		public Builder<T> setButtonFont(Font font){
			INSTANCE.setButtonFont(font.deriveFont(font.getSize2D()));
			return this;
		}

		public Builder<T> setButtonTextColor(Color color){
			INSTANCE.setButtonTextColor(color);
			return this;
		}

		public Builder<T> setMessage(String message){
			INSTANCE.setMessage(message);
			return this;
		}

		public Builder<T> setBackgroundColor(Color color){
			INSTANCE.setBackgroundColor(color);
			return this;
		}

		public Builder<T> setButtonBackgroundColor(Color color){
			INSTANCE.setButtonBackgroundColor(color);
			return this;
		}

		public Builder<T> setLocationRelativeTo(Component c){
			INSTANCE.setLocationRelativeTo(c);
			return this;
		}

		public Builder<T> setLocationCenterTo(Component c){
			INSTANCE.setLocationCenterTo(c);
			return this;
		}

		public Builder<T> setLocationScreenCenter(){
			INSTANCE.setLocationScreenCenter();
			return this;
		}

		public Builder<T> setOnClickEvent(OnClickListener onClickListener){
			INSTANCE.setOnClickEvent(onClickListener);
			return this;
		}

		public Builder<T> applyPreferredWidth(int width){
			INSTANCE.applyPreferredWidth(width);
			return this;
		}

		public T build(){
			return (T)INSTANCE;
		}
	}

	public void show(){
		int dialogHeight =PADDING*2;

		for(Component component : COMMON_JFRAME.getCustomizablePanel().getComponents()){
			dialogHeight += component.getPreferredSize().height;
		}

		COMMON_JFRAME.setSize(new Dimension(isForceChangeWidth?preferredWidth:FLAT_DIALOG_WIDTH,dialogHeight));

		if(targetComponent != null){
			Point p = targetComponent.getLocation();
			Dimension d = targetComponent.getSize();
			Dimension myD = COMMON_JFRAME.getSize();
			COMMON_JFRAME.setLocation(p.x + (d.width - myD.width)/2,p.y+(d.height - myD.height)/2);
		}

		if(isScreenCenterShow){
			COMMON_JFRAME.setLocation((SCREEN_MANAGER.getScreenWidth() - COMMON_JFRAME.getWidth())/2,
					(SCREEN_MANAGER.getScreenHeight() - COMMON_JFRAME.getHeight())/2);
		}
		COMMON_JFRAME.setVisible(true);
	}
	public static void main(String[] args){
		new FlatDialog.Builder()
				.setTitle("TEST")
				.setMessage("This is test message\n" +
						"When dialog message length increase, this dialog area is wider automatically \n" +
						"So, you don't consider that width or height size\n" +
						"Just use it!")
				.setLocationScreenCenter()
				.build()
				.show();
	}
}
