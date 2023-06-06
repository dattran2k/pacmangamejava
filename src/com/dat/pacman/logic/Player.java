package com.dat.pacman.logic;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class Player extends Dynamic {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	private BufferedImage left, up, righ, down, close;

	public Player(int x, int y, BufferedImage img, BufferedImage origin, int type) {
		super(x, y, img, origin, type);
		left = initImage("PacmanLeft.png");
		up = initImage("PacmanUp.png");
		righ = initImage("Pacman.png");
		down = initImage("PacmanDown.png");
		close = initImage("PacmanClose.png");
		orient = RIGHT;
		speed = SPEED_SUPER_FAST;
	}

	public void changeOrient(int newOrient) {

		if (orient == newOrient) {
			return;
		}
		orient = newOrient;
		if (orient == LEFT) {
			img = left;
			origin = left;
		} else if (orient == UP) {
			img = up;
			origin = up;
		} else if (orient == DOWN) {
			img = down;
			origin = down;
		} else {
			img = origin;
			origin = righ;
		}
	}

	public boolean move(List<GameObject> listObj, ICallBackIntersect callBack, int count) {
		if (count % 200 <= 100) {
			img = close;
		} else {
			img = origin;
		}
		for (int i = 0; i < listObj.size(); i++) {
			GameObject temp;
			if (listObj.get(i) instanceof Enemy) {
				temp = (Enemy) listObj.get(i);
			} else if (listObj.get(i) instanceof Item) {
				temp = (Item) listObj.get(i);
			} else {
				temp = (GameObject) listObj.get(i);
			}
			if (isIntesect(x, y, temp)) {
				callBack.returnObject(temp);
				return false;
			}
		}
		if (!super.move(listObj, callBack)) {
			return false;
		}

		return true;
	}

	public BufferedImage initImage(String name) {
		try {
			return ImageIO.read(getClass().getResource("/resource/image/" + name));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getSpeed() {
		return speed;
	}
}
