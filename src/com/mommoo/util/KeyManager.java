package com.mommoo.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyManager {
	private static final String ENTER_KEY = "enter";

	public interface KeyEventListener{
		public void execute();
	}


	public static void addEnterKeyListener(Component comp, final KeyEventListener listener){
		comp.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyChar()==KeyEvent.VK_ENTER){
					listener.execute();
				}
			}
		});
	}

	public static void addEnterKeyListener(JFrame rootFrame, final KeyEventListener listener){
		final JRootPane ROOT_PANE = rootFrame.getRootPane();
		ROOT_PANE.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char)KeyEvent.VK_ENTER),ENTER_KEY);
		ROOT_PANE.getActionMap().put(ENTER_KEY, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (listener != null) listener.execute();
			}
		});
	}
}
