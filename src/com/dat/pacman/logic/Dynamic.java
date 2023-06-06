package com.dat.pacman.logic;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dynamic extends GameObject {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	protected int orient;
	protected int speed;
	public final static int SPEED_SUPER_FAST = 6;
	public final static int SPEED_LOW = 10;
	public final static int SPEED_NOMAL = 7;
	public final static int SPEED_FAST = 8;
	public final static int[] LISTSPEED = new int[] { SPEED_SUPER_FAST, SPEED_FAST, SPEED_NOMAL, SPEED_LOW };

	public Dynamic(int x, int y, BufferedImage img, BufferedImage origin, int type) {
		super(x, y, img, origin, type);

	}

	public boolean move(List<GameObject> listObj, ICallBackIntersect callBack) {
		int xTmp = x;
		int yTmp = y;
		switch (orient) {
		case LEFT:
			xTmp --;
			break;
		case RIGHT:
			xTmp++;
			break;
		case UP:
			yTmp--;
			break;
		case DOWN:
			yTmp++;
			break;
		default:
			break;
		}

		if (xTmp < 1 || yTmp < 1 || xTmp > (GameManager.HANG - 1) * GameManager.SIZE
				|| yTmp > (GameManager.COT - 1) * GameManager.SIZE) {
			return false;
		}

		boolean isCanMove = true;
		for (int i = 0; i < listObj.size(); i++) {
			GameObject obj = listObj.get(i);
			if (isIntesect(xTmp, yTmp, obj) && obj.getType() == GameManager.TYPE_BRICK) {
				isCanMove = false;
				if (callBack != null) {
					callBack.returnObject(obj);
				}
				break;
			}
		}

		if (isCanMove) {
			x = xTmp;
			y = yTmp;
		}

		return isCanMove;
	}

	public interface ICallBackIntersect {
		void returnObject(GameObject obj);
	}

	public interface ICallBackStart {
		void start();
	}

	public int getOrient() {
		return orient;
	}

	public int getSpeed() {
		return speed;
	}

}
