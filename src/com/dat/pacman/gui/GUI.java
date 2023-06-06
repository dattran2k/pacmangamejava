package com.dat.pacman.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.dat.pacman.gui.panel.CreateMapPanel;
import com.dat.pacman.gui.panel.MainPanel;
import com.dat.pacman.gui.panel.PlayGamePanel;

public class GUI extends JFrame implements Icommon {

	public static final String TITLE = "Báo cáo game Pacman";
	public static final int W_FRAME = 800;
	public static final int H_FRAME = 800;
	public static final String NAME_PANEL_PLAY = "HOME_PLAY";
	public static final String NAME_PANEL_CREATE = "HOME_CREATE";
	public static final String NAME_PANEL_HOME_FROM_CREATE = "CREATE_HOME";
	public static final String NAME_PANEL_HOME_FROM_PLAY = "CREATE_PLAY";
	public static final String REFRESH = "REFRESH";
	private CreateMapPanel createMap;
	private PlayGamePanel playGame;
	private MainPanel mainPanel;

	private PlayGamePanel play;
	ICallBackChangeMap callBack = new ICallBackChangeMap() {

		@Override
		public void changeMap(String name) {
			switch (name) {
			case NAME_PANEL_PLAY:
				play = new PlayGamePanel(callBack);
				mainPanel.setVisible(false);
				add(play);
				play.setVisible(true);
				play.transferFocus();
				break;
			case NAME_PANEL_CREATE:
				createMap = new CreateMapPanel(callBack);
				mainPanel.setVisible(false);
				add(createMap);
				createMap.setVisible(true);
				createMap.transferFocus();
				break;
			case NAME_PANEL_HOME_FROM_CREATE:
				mainPanel = new MainPanel(callBack);
				createMap.setVisible(false);
				add(mainPanel);
				mainPanel.setVisible(true);
				mainPanel.transferFocus();
				break;
			case NAME_PANEL_HOME_FROM_PLAY:
				mainPanel = new MainPanel(callBack);
				play.setVisible(false);
				add(mainPanel);
				mainPanel.setVisible(true);
				mainPanel.transferFocus();
				break;
			case REFRESH:
				play.setVisible(false);
				play = new PlayGamePanel(callBack);
				play.setVisible(false);
				add(play);
				play.setVisible(true);
				play.transferFocus();
				break;
			default:
				break;
			}

		}

		public void exit() {
			dispose();
		};
	};

	public GUI() {
		init();
		addEvent();
		addComp();
	}

	@Override
	public void init() {

		setTitle(TITLE);
		setSize(W_FRAME, H_FRAME);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		getContentPane().setBackground(Color.WHITE);
		setLayout(new CardLayout());

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addComp() {

		mainPanel = new MainPanel(callBack);
		add(mainPanel);
	}

	@Override
	public void addEvent() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int rs = JOptionPane.showConfirmDialog(null, "close ?", "Alert", JOptionPane.YES_NO_OPTION);
				if (rs == JOptionPane.YES_OPTION) {
					dispose();
				}
			}
		});

	}

}
