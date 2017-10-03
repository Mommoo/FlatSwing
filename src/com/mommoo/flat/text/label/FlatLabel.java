package com.mommoo.flat.text.label;

import com.mommoo.flat.button.FlatButton;
import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.text.textarea.FlatTextAlignment;
import com.mommoo.flat.text.textarea.FlatTextArea;
import com.mommoo.flat.text.textfield.FlatTextField;
import com.mommoo.util.FontManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 *  FlatLabel is very convenient component.
 *  If text length over the label width, line jump automatically
 */

public class FlatLabel extends FlatTextArea {
	private static final int TEXT_FONT_SIZE = ScreenManager.getInstance().dip2px(8);

	public FlatLabel(){
		init();
	}

	public FlatLabel(String text){
		init();
		setText(text);
	}

	public FlatLabel(StyledDocument doc) {
		super(doc);
		init();
	}

	public static void main(String[] args){
//		SwingUtilities.invokeLater(()-> {
//			FlatFrame flatFrame = new FlatFrame();
//			flatFrame.setWindowExit(true);
//			flatFrame.setTitle("FlatLabel Test");
//			flatFrame.setLocationOnScreenCenter();
//
//			FlatLabel flatLabel = new FlatLabel("A Beautiful Label!!\n This is automatically line jump if string width longer than label width. You just use it well!");
//			flatLabel.setVerticalCenteredTextAlignment();
//			flatLabel.setFont(FontManager.getNanumGothicFont(Font.BOLD, 50));
////		flatLabel.setFont();
//
////			flatLabel.setText("A Beautiful Label!!\n This is automatically line jump if string width longer than label width. You just use it well!");
////			flatLabel.setText("10");
//			flatLabel.setOnClickListener(component -> System.out.println(((FlatLabel) component).getText()));
////		flatLabel.setLineSpacing(0.7f);
//			int padding = 50;
//
////			flatLabel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
////			flatFrame.setSize(500,flatLabel.getFitHeightToWidth(500) + FlatFrame.getTitleBarHeight());
//			flatFrame.setSize(500, 1000);
////			flatFrame.getContainer().setLayout(new FlowLayout());
//			flatFrame.getContainer().setLayout(new LinearLayout(Orientation.HORIZONTAL));
//			flatFrame.getContainer().add(flatLabel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
//			flatLabel.setOpaque(true);
//			flatLabel.setBackground(Color.RED);
////			flatFrame.show();
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//		});

		FlatFrame mcFrame = new FlatFrame();

		ScreenManager mcSM = ScreenManager.getInstance();

		FlatPanel hpnumPanel = new FlatPanel();
		FlatPanel btnPanel = new FlatPanel();

		FlatLabel hpnumLabel = new FlatLabel("핸드폰 번호");
//		JTextPane hpnumLabel = new JTextPane();
		hpnumLabel.setText("핸드폰 번ㅇ호");
//		FlatLabel hpnumLabel = new FlatLabel();
		FlatTextField hpnumTField = new FlatTextField(false);

		FlatButton okBtn = new FlatButton("확인");
		FlatButton cancelBtn = new FlatButton("취소");

		mcFrame.setTitle("회원 확인");
		mcFrame.getContainer().setLayout(new LinearLayout(Orientation.VERTICAL, 0));
		mcFrame.setSize(mcSM.dip2px(250), mcSM.dip2px(100));
		mcFrame.setWindowExit(false);
		mcFrame.setLocationOnScreenCenter();

		mcFrame.getContainer().add(hpnumPanel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
		mcFrame.getContainer().add(btnPanel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));

		hpnumPanel.setLayout(new LinearLayout(0));
		btnPanel.setLayout(new LinearLayout(0));

		hpnumPanel.add(hpnumLabel, new LinearConstraints().setWeight(2).setLinearSpace(LinearSpace.MATCH_PARENT));
		hpnumPanel.add(hpnumTField, new LinearConstraints().setWeight(4).setLinearSpace(LinearSpace.MATCH_PARENT));

		hpnumLabel.setVerticalCenteredTextAlignment();
		hpnumLabel.setTextAlignment(FlatTextAlignment.ALIGN_CENTER);
		hpnumLabel.setFont(FontManager.getNanumGothicFont(Font.BOLD, 100));
		hpnumLabel.setBackground(new Color(190, 160, 120));
		btnPanel.add(okBtn, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
		btnPanel.add(cancelBtn, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
		okBtn.setThemeColor(new Color(135, 206, 250));
		mcFrame.show();

//		mcFrame.setVisible(true);

//		JFrame frame = new JFrame();
//		JTextPane pane = new JTextPane();
//		pane.setText("Test");
//		pane.setFont(pane.getFont().deriveFont(44.0f));
//		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//		frame.setSize(500,500);
//
//		frame.getContentPane().setLayout(new FlowLayout());
//		frame.getContentPane().add(pane);
//		frame.setVisible(true);

	}

	private void init(){
		setEditable(false);
		setFocusable(false);
		setFont(FontManager.getNanumGothicFont(Font.PLAIN, TEXT_FONT_SIZE));
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(-1,50);
	}
}
