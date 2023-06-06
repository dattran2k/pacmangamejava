package com.dat.pacman.logic;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy extends Dynamic {
	List<AIAStar.Coordinate> way;
	public Enemy(int x, int y, BufferedImage img, BufferedImage origin, int type) {
		super(x, y, img, origin, type);
		Random rd = new Random();
		orient = rd.nextInt(3);
		speed = LISTSPEED[rd.nextInt(3)];
		way = new ArrayList<AIAStar.Coordinate>();
	}

	public void changeOrient(int nextInt) {
		orient = new Random().nextInt(4);
	}

}
