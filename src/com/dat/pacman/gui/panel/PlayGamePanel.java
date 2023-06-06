package com.dat.pacman.gui.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import com.dat.pacman.gui.GUI;
import com.dat.pacman.gui.ICallBackChangeMap;
import com.dat.pacman.gui.ICallBackStop;
import com.dat.pacman.gui.Icommon;
import com.dat.pacman.logic.GameManager;

public class PlayGamePanel extends BasePanel implements Icommon {
	private static final String LB_HOME = "LB_HOME";

	private BufferedImage imWall, imGhost, imBerry, imPacman, imFood, imgBackGround;
	private ImageIcon icHome;
	private JProgressBar progressBar, progressBarEffect, pBKillEnemy, progressBarFood;
	private JLabel lbHome;

	private MouseListener mouseListener;
	private ICallBackChangeMap callBack;

	private PlayGameMapGame playGameMapGame;
	private Thread thMap;
	ICallBackStop Callbackstop = new ICallBackStop() {

		public void stop() {
			repaint();
			thMap.stop();
			setFocusable(true);
		}

		public void nextMap(int currentMap, int score) {
			repaint();
			playGameMapGame.nextMap(currentMap, score);

		};
	};

	public PlayGamePanel(ICallBackChangeMap callBack) {
		super();
		this.callBack = callBack;
	}

	@Override
	public void init() {
		playGameMapGame = new PlayGameMapGame(Callbackstop);
		add(playGameMapGame);

		imgBackGround = initImage("/BackGroundPlayGame.png");
		imWall = initImage("wall.png");
		imGhost = initImage("ghost.png");
		imBerry = initImage("Berry.png");
		imPacman = initImage("Pacman.png");
		imFood = initImage("food0.png");
		icHome = new ImageIcon(initImage("home.png"));

		setLayout(null);
		setBackground(Color.WHITE);

	}

	@Override
	public void addEvent() {
		mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Component c = (Component) e.getSource();
				String name = c.getName();
				doClick(name, c.getX(), c.getY());
			}
		};
		addMouseListener(mouseListener);
	}

	@Override
	public void addComp() {
		lbHome = new JLabel(icHome);
		add(lbHome);
		lbHome.setSize(50, 50);
		lbHome.setLocation(GUI.W_FRAME - 50 - lbHome.getWidth(), 10);
		lbHome.addMouseListener(mouseListener);
		lbHome.setName(LB_HOME);

		progressBar = new JProgressBar();
		add(progressBar);
		progressBar.setLocation(10, 700);
		progressBar.setSize(770, 50);
		progressBar.setValue(100);
		progressBar.setStringPainted(true);
		progressBar.setString("time left :" + GameManager.TIMEGAME / 1000 + " Giây");

		progressBarEffect = new JProgressBar();
		add(progressBarEffect);
		progressBarEffect.setLocation(playGameMapGame.getX() + playGameMapGame.getWidth() + 70, 400);
		progressBarEffect.setSize(210, 50);
		progressBarEffect.setValue(0);
		progressBarEffect.setStringPainted(true);
		progressBarEffect.setVisible(false);

		pBKillEnemy = new JProgressBar();
		add(pBKillEnemy);
		pBKillEnemy.setLocation(playGameMapGame.getX() + playGameMapGame.getWidth() + 70, playGameMapGame.getY());
		pBKillEnemy.setSize(210, 50);
		pBKillEnemy.setValue(0);
		pBKillEnemy.setStringPainted(true);

		progressBarFood = new JProgressBar();
		add(progressBarFood);
		progressBarFood.setLocation(playGameMapGame.getX() + playGameMapGame.getWidth() + 10, 300);
		progressBarFood.setSize(270, 50);
		progressBarFood.setValue(0);
		GameManager gameManager = playGameMapGame.getGameMng();
		progressBarFood.setString("Đã đớp : " + "0 / " + gameManager.getMaxFood() + " Chỗ thức ăn");
		progressBarFood.setStringPainted(true);

		thMap = new Thread(new Runnable() {
			@Override
			public void run() {
				execTaskMap();
			}
		});
		thMap.setDaemon(true);
		thMap.start();
	}

	protected void execTaskMap() {
		int time = 0;

		while (true) {
			try {
				time++;
				Thread.sleep(1);
				playGameMapGame.execTask(time, Callbackstop);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {

		GameManager gameManager = playGameMapGame.getGameMng();
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(imgBackGround, 0, 0, null);

		// vẽ enemy
		g2d.drawImage(imGhost, playGameMapGame.getX() + playGameMapGame.getWidth() + 10, playGameMapGame.getY(), null);

		// vẽ effect
		for (String name : gameManager.getMapEffect().keySet()) {
			g2d.drawImage(imBerry, playGameMapGame.getX() + playGameMapGame.getWidth() + 10, 400, null);
			progressBarEffect.setValue((gameManager.getMapEffect().get(name) * 100 / GameManager.TIME_BERRY));
			progressBarEffect.setString(gameManager.getMapEffect().get(name) / 1000 + " Giây");
			progressBarEffect.setVisible(true);
		}
		// Vẽ giết ghosh

		pBKillEnemy
				.setValue((gameManager.getMaxEnemy() - gameManager.getCountEnemy()) * 100 / gameManager.getMaxEnemy());
		progressBarFood
				.setValue((gameManager.getMaxFood() - gameManager.getCountFood()) * 100 / gameManager.getMaxFood());

		pBKillEnemy.setString("Giết ma để win , ăn food chi cho mệt :"
				+ (gameManager.getMaxEnemy() - gameManager.getCountEnemy()) + "/" + gameManager.getMaxEnemy());
		// Vẽ ăn food

		progressBarFood.setString("Đã đớp : " + (gameManager.getMaxFood() - gameManager.getCountFood()) + " / "
				+ gameManager.getMaxFood() + " Chỗ thức ăn");

		// vẽ time
		progressBar.setValue((GameManager.TIMEGAME - gameManager.getRealtime()) * 100 / GameManager.TIMEGAME);
		progressBar.setString("Time left : " + (GameManager.TIMEGAME - gameManager.getRealtime()) / 1000 + "s");

		// Vẽ điểm
		g2d.setFont(new Font("NewellsHand", Font.BOLD, 30));
		g2d.setColor(Color.RED);
		g2d.drawString("SCORE : " + playGameMapGame.getGameMng().getScore(), 500, 500);

	}

	@Override
	protected void doClick(String name, int x, int y) {
		if (playGameMapGame.getGameMng().isLosing() || playGameMapGame.getGameMng().isWinning()) {
			callBack.changeMap(GUI.REFRESH);
		}
		if (name == null) {
			return;
		}
		if (name.equals(LB_HOME)) {
			if (JOptionPane.showConfirmDialog(this, "Trở về main menu ?", "Confirm",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				thMap.stop();
				callBack.changeMap(GUI.NAME_PANEL_HOME_FROM_PLAY);
			}

		}
	}

}
