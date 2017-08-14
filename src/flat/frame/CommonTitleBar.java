package flat.frame;

import flat.component.FlatPanel;
import flat.frame.titlebar.TitleLabel;
import flat.frame.titlebar.navigation.button.NavigationButtonType;
import flat.frame.titlebar.navigation.controller.NavigationControlPanel;
import flat.frame.titlebar.navigation.listener.NavigationControlListener;
import flat.image.FlatImagePanel;
import flat.image.ImageOption;
import util.ImageManager;

import javax.swing.*;
import java.awt.*;

class CommonTitleBar extends FlatPanel {
	private static final int TITLE_BAR_HEIGHT = 50;
	private static final int TITLE_BAR_LEFT_PADDING = 10;
	private static final int TITLE_TEXT_FONT_SIZE = TITLE_BAR_HEIGHT/3;

	private final TitleLabel TITLE_LABEL = new TitleLabel(TITLE_TEXT_FONT_SIZE);
	private final NavigationControlPanel controlPanel = new NavigationControlPanel(3*TITLE_BAR_HEIGHT/5);

	private FlatImagePanel mainIcon;

	CommonTitleBar() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(0,TITLE_BAR_LEFT_PADDING,0,10));
		createMainIcon();
		addTitleLabel();
		addControlPanel();
	}

	private void createMainIcon(){
		Dimension mainIconDimen = new Dimension(3*TITLE_BAR_HEIGHT/5, 3*TITLE_BAR_HEIGHT/5);
		mainIcon = new FlatImagePanel();
		mainIcon.setPreferredSize(mainIconDimen);
		mainIcon.setMinimumSize(mainIconDimen);
	}
	
	private void addTitleLabel(){
		addComponent(TITLE_LABEL,1,0,1,1,1,0);
	}
	
	private void addControlPanel(){
		addComponent(controlPanel,2,0,1,1,0,0);
	}
	
	private void addComponent(JComponent jComponent,
			int gridX, int gridY, int gridWidth, int gridHeight, double weightX, double weightY){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = gridX;
		gbc.gridy = gridY;
		gbc.gridwidth = gridWidth;
		gbc.gridheight = gridHeight;
		gbc.weightx = weightX;
		gbc.weighty = weightY;
		this.add(jComponent,gbc);
	}
	
	void setTitle(String title){
		TITLE_LABEL.setText(title);
	}
	
	String getTitle(){
		return TITLE_LABEL.getText();
	}
	
	void setIconImage(Image image){
		mainIcon.setIcon(image, ImageOption.MATCH_PARENT);
		if (!isComponentContained(mainIcon)){
			addComponent(mainIcon,0,0,1,1,0,0);
		}
	}

	void removeIconImage(){
		if (isComponentContained(mainIcon)) remove(mainIcon);
	}

	void setThemeColor(Color color){
		super.setBackground(color);
		mainIcon.setBackground(color);
		controlPanel.setButtonColor(color);
	}
	
	Color getThemeColor(){
		return super.getBackground();
	}

	void setButtonIconColor(Color color){
		controlPanel.setButtonIconColor(color);
	}

	Color getButtonIconColor(){
		return controlPanel.getButtonIconColor();
	}
	
	void setTitleColor(Color color){
		TITLE_LABEL.setForeground(color);
	}
	
	Color getTitleColor(){
		return TITLE_LABEL.getForeground();
	}

	void removeControlPanel(){
		remove(controlPanel);
	}

	void setControlListener(NavigationControlListener controlListener){
		controlPanel.setOnControlListener(controlListener);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width , TITLE_BAR_HEIGHT);
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	public static int getTitleBarHeight(){
		return TITLE_BAR_HEIGHT;
	}

	public static void main(String[] args){
		SwingUtilities.invokeLater(()->{
			FlatFrame f = new FlatFrame();
			f.setSize(700,500);
			f.setEnableSizeButton(true);
			f.setImageIcon(ImageManager.TEST);
			f.setTitle("A Beautiful Frame. You can customizing you want!");
			f.setLocationOnScreenCenter();
			f.show();
		});
	}
}