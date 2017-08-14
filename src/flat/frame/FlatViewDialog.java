package flat.frame;

import flat.component.OnClickListener;

import javax.swing.*;
import java.awt.*;

public final class FlatViewDialog {
	private final FlatDialog FLAT_DIALOG;
	protected FlatViewDialog(final FlatDialog FLAT_DIALOG){
		this.FLAT_DIALOG = FLAT_DIALOG;
	}
	
	public void show(){
		FLAT_DIALOG.show();
	}
	
	public static class Builder{
		private final FlatDialog FLAT_DIALOG = new FlatDialog();
		public Builder(){
			FLAT_DIALOG.COMMON_JFRAME.getCustomizablePanel().remove(FLAT_DIALOG.CONTENT);
		}
		
		public Builder setTitle(String title){
			FLAT_DIALOG.setTitle(title);
			return this;
		}
		
		public Builder setTitleFont(Font font){
			FLAT_DIALOG.setTitleFont(font.deriveFont(font.getSize2D()));
			return this;
		}
		
		public Builder setView(JPanel panel){
			final JPanel WRAP_PANEL = new JPanel();
			WRAP_PANEL.setLayout(new BorderLayout());
			WRAP_PANEL.setBackground(new Color(1.0f,1.0f,1.0f,0f));
			WRAP_PANEL.setPreferredSize(new Dimension(panel.getPreferredSize().width,panel.getPreferredSize().height+FlatDialog.PADDING));
			WRAP_PANEL.setBorder(BorderFactory.createEmptyBorder(0,0,FlatDialog.PADDING,0));
			WRAP_PANEL.add(panel,BorderLayout.CENTER);
			FLAT_DIALOG.COMMON_JFRAME.getCustomizablePanel().add(WRAP_PANEL,BorderLayout.CENTER);
			return this;
		}
		
		public Builder setButtonFont(Font font){
			FLAT_DIALOG.setButtonFont(font.deriveFont(font.getSize2D()));
			return this;
		}
		
		public Builder setButtonTextColor(Color color){
			FLAT_DIALOG.setButtonTextColor(color);
			return this;
		}
		
		public Builder setBackgroundColor(Color color){
			FLAT_DIALOG.setBackgroundColor(color);
			return this;
		}
		
		public Builder setButtonBackgroundColor(Color color){
			FLAT_DIALOG.setButtonBackgroundColor(color);
			return this;
		}
		
		public Builder setLocationRelativeTo(Component c){
			FLAT_DIALOG.setLocationRelativeTo(c);
			return this;
		}
		
		public Builder setLocationCenterTo(Component c){
			FLAT_DIALOG.setLocationCenterTo(c);
			return this;
		}
		
		public Builder setOnClickEvent(OnClickListener onClickListener){
			FLAT_DIALOG.setOnClickEvent(onClickListener);
			return this;
		}
	
		public Builder applyPreferredWidth(int width){
			FLAT_DIALOG.applyPreferredWidth(width);
			return this;
		}
		
		public FlatViewDialog build(){
			return new FlatViewDialog(FLAT_DIALOG);
		}
	}
	
	public static void main(String[] args){
		JPanel panel = new JPanel();
		panel.setBackground(Color.RED);
		panel.setPreferredSize(new Dimension(300,300));
		new Builder()
				.setTitle("Test")
				.setView(panel)
				.build()
				.show();
	}
}
