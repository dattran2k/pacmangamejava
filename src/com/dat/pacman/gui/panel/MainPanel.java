package com.dat.pacman.gui.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.dat.pacman.gui.GUI;
import com.dat.pacman.gui.ICallBackChangeMap;

public class MainPanel extends BasePanel {
	private static final String BT_PLAY = "BT_PLAY";
	private static final String BT_CREATE = "BT_CREATE";
	private static final String BT_EXIT = "BT_EXIT";
	private JButton btPlay,btCreate,btExit;
	private BufferedImage imgStart,imgCreate,imgExit;
	private ImageIcon icStart,icCreate,icExit;
	private MouseListener mouseListener;
	private ICallBackChangeMap callBack;
	@Override
	
	public void init() {
		setBackground(Color.WHITE);
		setLayout(null);
		imgStart = initImage("/MainButtonStart.png");
		icStart = new ImageIcon(imgStart);
		imgCreate = initImage("/MainButtonCreate.png");
		icCreate = new ImageIcon(imgCreate);
		imgExit = initImage("/MainButtonExit.png");
		icExit = new ImageIcon(imgExit);

	}

	public MainPanel(ICallBackChangeMap callBack) {
		super();
		this.callBack = callBack;
	}


	@Override
	public void addEvent() {
		mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Component c = (Component) e.getSource();
				String name = c.getName();
				doClick(name,c.getX(),c.getY());
			}
		};
		addMouseListener(mouseListener);
	}

	@Override
	public void addComp() {
		btPlay = new JButton(icStart);
		btPlay.setSize(200, 80);
		btPlay.setLocation(GUI.W_FRAME/2 - btPlay.getWidth()/2, GUI.H_FRAME/2);
		btPlay.addMouseListener(mouseListener);
		btPlay.setName(BT_PLAY);
		add(btPlay);
		
		btCreate = new JButton(icCreate);
		btCreate.setSize(200, 80);
		btCreate.setLocation(btPlay.getX(), btPlay.getY() + btPlay.getHeight() + 20);
		btCreate.addMouseListener(mouseListener);
		btCreate.setName(BT_CREATE);
		add(btCreate);
		
		btExit = new JButton(icExit);
		btExit.setSize(200, 80);
		btExit.setLocation(btPlay.getX(), btCreate.getY() + btCreate.getHeight() + 20);
		btExit.addMouseListener(mouseListener);
		btExit.setName(BT_EXIT);
		add(btExit);
	}

	@Override
	protected void doClick(String name, int x, int y) {
		if(name == null) {
			return;
		}
		switch (name) {
		case BT_PLAY:
			callBack.changeMap(GUI.NAME_PANEL_PLAY);
			break;
		case BT_CREATE:
			callBack.changeMap(GUI.NAME_PANEL_CREATE);
			break;
		case BT_EXIT:
			callBack.exit();
			break;
		default:
			break;
		}
	}
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(initImage("/MainBackGround.png"), 0, 0, null);
	}

}
