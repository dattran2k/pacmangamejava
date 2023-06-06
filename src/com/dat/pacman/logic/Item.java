package com.dat.pacman.logic;

import java.awt.image.BufferedImage;

public class Item extends GameObject {
	private int timeItem;
	private String effect;

	public Item(int x, int y, BufferedImage img, int type, int timeItem, String effect) {
		super(x, y, img, img, type);
		this.timeItem = timeItem;
		this.effect = effect;
	}

	public Item(int x, int y, BufferedImage img, int type, int size) {
		super(x, y, img, img, type);
		this.size = size;
	}

	public int getTimeItem() {
		return timeItem;
	}

	public String getEffect() {
		return effect;
	}

}
