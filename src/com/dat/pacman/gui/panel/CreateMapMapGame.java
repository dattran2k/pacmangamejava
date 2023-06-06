package com.dat.pacman.gui.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.dat.pacman.logic.DoFile;
import com.dat.pacman.logic.GameManager;
import com.dat.pacman.logic.GameObject;

public class CreateMapMapGame extends BasePanel {

	public boolean isPlay;
	private List<GameObject> listObj;
	private BufferedImage imWall, imGhost, imBerry, imPacman, imFood;

	public CreateMapMapGame(boolean isPlay) {
		super();
		this.isPlay = isPlay;
	}

	@Override
	public void init() {
		listObj = new ArrayList<GameObject>();
		imWall = initImage("wall.png");
		imGhost = initImage("ghost.png");

		imBerry = initImage("Berry.png");

		imPacman = initImage("Pacman.png");
		imFood = initImage("food0.png");
		setBackground(Color.BLACK);
		setLayout(null);
		setSize(GameManager.COT * GameManager.SIZE, GameManager.HANG * GameManager.SIZE);
		setLocation(GameManager.X_PANEL, GameManager.Y_PANEL);
	}

	@Override
	public void addEvent() {

	}

	@Override
	public void addComp() {

	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(1));
		if (!isPlay) {
			drawMap(g2d);
		}
		for (int i = 0; i < listObj.size(); i++) {
			listObj.get(i).draw(g2d);

		}
	}

	private void drawMap(Graphics2D g2d) {
		g2d.setColor(Color.RED);
		for (int i = 0; i <= GameManager.HANG; i++) {
			g2d.drawLine(i * GameManager.SIZE, 0, i * GameManager.SIZE, GameManager.HANG * GameManager.SIZE);
			g2d.drawLine(0, i * GameManager.SIZE, GameManager.COT * GameManager.SIZE, i * GameManager.SIZE);
		}

	}

	@Override
	protected void doClick(String name, int x, int y) {
	}

	public void autoAddFood() {
		for (int i = 0; i < GameManager.HANG; i++) {
			for (int j = 0; j < GameManager.COT; j++) {
				GameObject add = new GameObject(i * GameManager.SIZE, j * GameManager.SIZE, imFood, imFood,
						GameManager.TYPE_FOOD, 10);
				if (listObj.indexOf(add) < 0) {
					listObj.add(add);
				}
			}
		}
		repaint();
	}

	protected void doDraw(int x, int y, BufferedImage imIcSelected, boolean isDelete) {

		int col = x - (x % GameManager.SIZE);
		int row = y - (y % GameManager.SIZE);
		if (!isDelete) {
			if (imIcSelected == null || col < 0 || col >= GameManager.COT * GameManager.SIZE || row < 0
					|| row >= GameManager.HANG * GameManager.SIZE) {
				return;
			}
			GameObject add = new GameObject(col, row, imIcSelected, imIcSelected, returnType(imIcSelected));
			if (imIcSelected.equals(imPacman)) {
				int count = 0;
				for (GameObject gameObject : listObj) {

					if (gameObject.getType() == GameManager.TYPE_PACMAN) {
						count++;
					}
				}
				if (count > 0) {
					JOptionPane.showMessageDialog(this, "Chỉ được 1 thằng Pacman thôi , nhiều thằng ai chơi với");
					return;
				}
			}
			if (listObj.indexOf(add) > -1) {
				return;
			}
			System.out.println(add.getType());
			listObj.add(add);
			repaint();
		} else {
			GameObject remove = new GameObject(col, row, null, null, -1);
			if (listObj.indexOf(remove) < -1) {
				return;
			}
			System.out.println("remove");
			listObj.remove(remove);
			repaint();
		}
	}

	private int returnType(BufferedImage ic) {
		if (ic == imGhost) {
			return GameManager.TYPE_GHOST;
		}
		if (ic.equals(imWall)) {
			return GameManager.TYPE_BRICK;
		}
		if (ic.equals(imBerry)) {
			return GameManager.TYPE_BERRY;
		}
		if (ic.equals(imPacman)) {
			return GameManager.TYPE_PACMAN;
		}
		if (ic.equals(imFood)) {
			return GameManager.TYPE_FOOD;
		}
		return -1;
	}

	public List<GameObject> loadMap() {
		listObj = new DoFile().loadMap(0);
		repaint();
		return listObj;
	}

	public BufferedImage getImWall() {
		return imWall;
	}

	public BufferedImage getImGhost() {
		return imGhost;
	}

	public BufferedImage getImBerry() {
		return imBerry;
	}

	public BufferedImage getImPacman() {
		return imPacman;
	}

	public List<GameObject> getListObj() {
		return listObj;
	}

	public void setListObj(List<GameObject> listObj) {
		this.listObj = listObj;
	}
}
