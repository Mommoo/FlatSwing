package com.mommoo.flat.button;

import com.mommoo.flat.border.FlatShadowBorder;
import com.mommoo.flat.button.ripple.RippleEffect;
import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.List;

public class FlatButton extends JButton implements ButtonViewModel{
	private static final int BUTTON_TEXT_FONT_SIZE = ScreenManager.getInstance().dip2px(11);
	private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

	private final RippleEffect RIPPLE_EFFECT = new RippleEffect();
	private final FlatButtonViewModel buttonViewModel = new FlatButtonViewModel(this, RIPPLE_EFFECT);

	private final List<ActionListener> ACTION_LISTENER_LIST = new ArrayList<>();
	private OnClickListener onClickListener = e -> {};
	private int cornerRound = 0;
	private boolean autoClick;

	public FlatButton(){
		setAlignmentCentered();
		setTextDecoration();
		setOpaque(true);
		setBackground(Color.PINK);
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

//		flatButton.doClick();

		FlatFrame flatFrame = new FlatFrame();
		flatFrame.setTitle("FlatButton test");
		flatFrame.setSize(500,500);
		flatFrame.getContainer().setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		flatFrame.getContainer().add(flatButton);

		flatFrame.setLocationOnScreenCenter();
		flatFrame.setResizable(true);
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
		setBorder(new FlatShadowBorder());
		getRippleEffect().setRippleDrawOverBorder(false);
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
		getBorder().paintBorder(this, g,0,0, getWidth(), getHeight());
		paintBackground(g);
		inspectAutoClick();
		super.paint(g);
		paintRipple(g);
	}

	private void paintBackground(Graphics g){
		g.setColor(getBackground());
		Insets borderInsets = getBorder().getBorderInsets(this);
		g.fillRoundRect(borderInsets.left, borderInsets.top, getWidth() - borderInsets.left - borderInsets.right,
				getHeight() - borderInsets.top - borderInsets.bottom
				, cornerRound, cornerRound);
	}

	private void inspectAutoClick(){
		if (autoClick){
			buttonViewModel.executeRippleEffect(new Point(getWidth()/2, getHeight()/2), new ActionEvent(this, ActionEvent.ACTION_FIRST, "AUTO_CLICK"));
			autoClick = false;
		}
	}

	private void paintRipple(Graphics g){
		Rectangle clipRect = new Rectangle();

		if (getRippleEffect().isRippleDrawOverBorder()){
			clipRect.setBounds(0,0,getWidth(),getHeight());
		} else {
			Insets borderInsets = getBorder().getBorderInsets(this);
			clipRect.setBounds(borderInsets.left,
					borderInsets.top,
					getWidth() - borderInsets.left - borderInsets.right,
					getHeight() - borderInsets.top - borderInsets.bottom);
		}
		g.setClip(clipRect);
		buttonViewModel.paintRippleEffect((Graphics2D)g);

		g.setClip(null);
	}

	public int getCornerRound(){
		return this.cornerRound;
	}

	public void setCornerRound(int cornerRound){
		this.cornerRound = cornerRound;
	}

	public void doClick(){
		autoClick = true;
		repaint();
	}

	public RippleEffect getRippleEffect(){
		return RIPPLE_EFFECT;
	}
	public static class GaussianBlur {

		static ConvolveOp[] createFilters(int radius) {
			ConvolveOp[] filters = new ConvolveOp[2];

			double sigma = radius / 3.0;
			double sigmaSquareDivisor = 2.0 * Math.pow(sigma, 2);

			double sqrtDivisor = Math.sqrt(sigmaSquareDivisor * Math.PI);

			float total = 0f;
			float [] matrix = new float[radius * 2];
			for (int i = -radius; i < radius; i++) {

				double distance = -(i * i);
				double midpoint = Math.exp(distance / sigmaSquareDivisor) / sqrtDivisor;

				matrix[i + radius] = (float) midpoint;

				// keep this to normalise the matrix to avoid a darkening or
				// brightening of the image
				total += (float) midpoint;
			}

			// normalise the matrix now
			for (int i = 0; i < matrix.length; i++) {
				matrix[i] /= total;
			}

			Kernel horizontalKernel = new Kernel(matrix.length, 1, matrix);
			Kernel verticalKernel = new Kernel(1, matrix.length, matrix);

			filters[0] = new ConvolveOp(horizontalKernel, ConvolveOp.EDGE_NO_OP, null);
			filters[1] = new ConvolveOp(verticalKernel, ConvolveOp.EDGE_NO_OP, null);

			return filters;
		}

		public static BufferedImage applyFilter(int radius, BufferedImage src) {
			ConvolveOp[] filters = GaussianBlur.createFilters(radius);

			src = filters[0].filter(src, null);
			src = filters[1].filter(src, null);

			return src;
		}
	}
}
