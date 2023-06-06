package com.dat.pacman.logic;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class GameObject {

	protected int x, y;
	protected int type;
	protected int size;
	protected BufferedImage img;
	protected BufferedImage origin;

	public GameObject(int x, int y, BufferedImage img, BufferedImage origin, int type) {
		super();
		this.type = type;
		this.x = x;
		this.y = y;
		this.img = img;
		this.origin = origin;
		this.size = 20;
	}

	public GameObject(int x, int y, BufferedImage img, BufferedImage origin, int type, int size) {
		this(x, y, img, origin, type);
		this.size = size;
	}

	public GameObject(int x, int y, BufferedImage img, int type) {
		this(x, y, img, img, type);
	}

	public void draw(Graphics2D g2d) {
		g2d.drawImage(img, x + (GameManager.SIZE - size) / 2, y + (GameManager.SIZE - size) / 2, size, size, null);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GameObject) {
			GameObject g2 = (GameObject) obj;
			return g2.x == x && g2.y == y;
		}
		return super.equals(obj);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public BufferedImage getImg() {
		return img;
	}

	public int getType() {
		return type;
	}

	public int getSize() {
		return size;
	}

	public BufferedImage getOrigin() {
		return origin;
	}

	@Override
	public String toString() {
		return "type :" + type;
	}

	public boolean isIntesect(int x, int y, GameObject obj2) {
		Rectangle2D rect1 = new Rectangle(x, y, size, size);
		Rectangle2D rect2 = new Rectangle(obj2.x, obj2.y, obj2.size, obj2.size);
		Rectangle2D dest = new Rectangle();
		Rectangle2D.intersect(rect1, rect2, dest);
		if (dest.getWidth() > 0 && dest.getHeight() > 0) {
			return true;
		}
		return false;
	}

}
