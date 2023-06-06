package com.dat.pacman.gui.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.dat.pacman.gui.ICallBackStop;
import com.dat.pacman.logic.GameManager;
import com.dat.pacman.logic.Player;

public class PlayGameMapGame extends BasePanel {
	private GameManager gameMng;
	public boolean isMove;
	private BufferedImage imgPlayGameReady;
	private Image imgPlayGameWin;
	private Image imgPlayGameLose;

	public ICallBackStop callback;

	public PlayGameMapGame(ICallBackStop callback) {
		super();
		this.callback = callback;
	}

	@Override
	public void init() {
		setSize(GameManager.COT * GameManager.SIZE, GameManager.HANG * GameManager.SIZE);
		setLocation(GameManager.X_PANEL, GameManager.Y_PANEL);
		imgPlayGameReady = initImage("PlayGameReady.png");
		imgPlayGameWin = initImage("PlayGameWin.png");
		imgPlayGameLose = initImage("PlayGameLose.png");
		setLayout(null);
		setBackground(Color.black);
		gameMng = new GameManager();

	}

	@Override
	public void addEvent() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				doPress(e.getKeyCode());
			}
		});
		setFocusable(true);
	}

	protected void doPress(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
			gameMng.changeOrientPlayer(Player.LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			gameMng.changeOrientPlayer(Player.RIGHT);
			break;
		case KeyEvent.VK_UP:
			gameMng.changeOrientPlayer(Player.UP);
			break;
		case KeyEvent.VK_DOWN:
			gameMng.changeOrientPlayer(Player.DOWN);
			break;

		default:
			break;
		}
		isMove = true;
		repaint();
	}

	@Override
	public void addComp() {
	}

	@Override
	protected void doClick(String name, int x, int y) {

	}

	protected void execTask(int count, ICallBackStop callBack) {
		if (isMove) {
			gameMng.doSTask(count, callBack);
			repaint();
		}

	}

	public void nextMap(int currentMap, int score) {

		isMove = false;
		gameMng = new GameManager(currentMap, score);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gameMng.drawGameObject(g2d);
		if (!isMove) {
			g2d.drawImage(imgPlayGameReady, 0, 0, null);
		}
		if (gameMng.isWinning()) {
			g2d.drawImage(imgPlayGameWin, 0, 0, null);
		}
		if (gameMng.isLosing()) {
			g2d.drawImage(imgPlayGameLose, 0, 0, null);
		}
	}

	public GameManager getGameMng() {
		return gameMng;
	}

	public boolean isMove() {
		return isMove;
	}

	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}

	public BufferedImage getImgPlayGameReady() {
		return imgPlayGameReady;
	}

	public void setImgPlayGameReady(BufferedImage imgPlayGameReady) {
		this.imgPlayGameReady = imgPlayGameReady;
	}

	public Image getImgPlayGameWin() {
		return imgPlayGameWin;
	}

	public void setImgPlayGameWin(Image imgPlayGameWin) {
		this.imgPlayGameWin = imgPlayGameWin;
	}

	public Image getImgPlayGameLose() {
		return imgPlayGameLose;
	}

	public void setImgPlayGameLose(Image imgPlayGameLose) {
		this.imgPlayGameLose = imgPlayGameLose;
	}


	public void setGameMng(GameManager gameMng) {
		this.gameMng = gameMng;
	}

}
