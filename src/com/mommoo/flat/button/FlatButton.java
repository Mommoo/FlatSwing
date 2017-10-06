package com.mommoo.flat.button;

import com.mommoo.flat.button.ripple.RippleEffect;
import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.util.ColorManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.ArrayList;

public class FlatButton extends JButton implements ButtonViewModel{
	private static final int BUTTON_TEXT_FONT_SIZE = ScreenManager.getInstance().dip2px(11);
	private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

	private final RippleEffect RIPPLE_EFFECT = new RippleEffect();
	private final FlatButtonViewModel buttonViewModel = new FlatButtonViewModel(this, RIPPLE_EFFECT);
	private final List<ActionListener> ACTION_LISTENER_LIST = new ArrayList<>();
	private OnClickListener onClickListener = e -> {};
	private boolean autoClick;
	
	public FlatButton(){
		setAlignmentCentered();
		setTextDecoration();
		setOpaque(true);
		setBackground(ColorManager.getColorAccent());
		setCursor(HAND_CURSOR);
		addRippleAnimMouseListener();
		removeBasicButtonGraphics();
	}

	public FlatButton(String text){
		this();
		setText(text);
	}

	public static void main(String[] args){
		FlatButton flatButton = new FlatButton("TEST");
		flatButton.setOnClickListener(comp-> System.out.println("onClick"));
		flatButton.addActionListener(e -> System.out.println("onAction"));

		flatButton.doClick();
		FlatFrame flatFrame = new FlatFrame();
		flatFrame.setTitle("FlatButton test");
		flatFrame.setSize(400,200);
		flatFrame.getContainer().setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		flatFrame.getContainer().add(flatButton);
		flatFrame.setLocationOnScreenCenter();
		flatFrame.show();
	}

	private void setAlignmentCentered(){
		setHorizontalAlignment(JButton.CENTER);
		setVerticalAlignment(JButton.CENTER);
	}

	private void setTextDecoration(){
		setFont(getFont().deriveFont(Font.BOLD, BUTTON_TEXT_FONT_SIZE));
		setForeground(Color.WHITE);
	}

	private void removeBasicButtonGraphics(){
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);
	}

	private void addRippleAnimMouseListener(){
		MouseAdapter rippleAnimMouseListener = buttonViewModel.getRippleAnimMouseListener();
		addMouseListener(rippleAnimMouseListener);
		addMouseMotionListener(rippleAnimMouseListener);
	}

	public OnClickListener getOnClickListener(){
		return onClickListener;
	}

	public void setOnClickListener(OnClickListener onClickListener){
		buttonViewModel.getButtonEventRepository().removeEvent(this.onClickListener);
		buttonViewModel.getButtonEventRepository().addEvent(this.onClickListener, event -> onClickListener.onClick(this));
		this.onClickListener = onClickListener;
	}

	@Override
	public void addActionListener(ActionListener actionListener) {
		buttonViewModel.getButtonEventRepository().addEvent(actionListener, actionListener::actionPerformed);
		ACTION_LISTENER_LIST.add(actionListener);
	}

	@Override
	public void removeActionListener(ActionListener actionListener) {
		buttonViewModel.getButtonEventRepository().removeEvent(actionListener);
		ACTION_LISTENER_LIST.remove(actionListener);
	}

	@Override
	public ActionListener[] getActionListeners() {
		return ACTION_LISTENER_LIST.toArray(new ActionListener[ACTION_LISTENER_LIST.size()]);
	}

	@Override
	public boolean isContentAreaFilled() {
		return false;
	}

	@Override
	public boolean isBorderPainted() {
		return false;
	}
	
	@Override
	public boolean isFocusPainted() {
		return false;
	}

	@Override
	public void paint(Graphics g){
		paintBackground(g);
		paintPreBorder(g);
		inspectAutoClick();
		buttonViewModel.paintRippleEffect((Graphics2D)g);
		super.paint(g);
		paintPostBorder(g);
	}

	private void paintBackground(Graphics g){
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	private void paintPreBorder(Graphics g){
		if (RIPPLE_EFFECT.isRippleDrawOverBorder()){
			getBorder().paintBorder(this, g, 0,0,getWidth(), getHeight());
		}
	}

	private void inspectAutoClick(){
		if (autoClick){
			buttonViewModel.executeRippleEffect(new Point(getWidth()/2, getHeight()/2), new ActionEvent(this, ActionEvent.ACTION_FIRST, "AUTO_CLICK"));
			autoClick = false;
		}
	}

	private void paintPostBorder(Graphics g){
		if (!RIPPLE_EFFECT.isRippleDrawOverBorder()){
			getBorder().paintBorder(this, g, 0,0,getWidth(), getHeight());
		}
	}

	public void doClick(){
		autoClick = true;
	}

	public RippleEffect getRippleEffect(){
		return RIPPLE_EFFECT;
	}
}
