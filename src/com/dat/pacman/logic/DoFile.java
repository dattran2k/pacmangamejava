package com.dat.pacman.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class DoFile {
	private static final String MAP1 = "map1.txt";
	private static final String MAP2 = "map2.txt";
	private BufferedImage imWall, imGhost, imBerry, imPacman, imFood, imGhost1, imGhost2;
	public int currentMap;
	public int maxMap;
	public List<String> listMap;
	public DoFile() {
		listMap = new ArrayList<String>();
		imWall = initImage("wall.png");
		imGhost = initImage("ghost.png");
		imBerry = initImage("berry.png");
		imPacman = initImage("Pacman.png");
		imFood = initImage("food0.png");
		imGhost1 = initImage("ghost1.png");
		imGhost2 = initImage("ghost2.png");
		listMap.add(MAP1);
		listMap.add(MAP2);
		//listMap.add(MAP3);
		maxMap = listMap.size();
	}

	public boolean export(String fileName, List<GameObject> listObj) {
		try {
			int countPlayer = 0;
			for (int i = 0; i < listObj.size(); i++) {
				if (listObj.get(i).getType() == GameManager.TYPE_PACMAN) {
					countPlayer++;
				}
			}
			if (countPlayer == 0) {
				JOptionPane.showMessageDialog(null, "Chưa tạo Pacman mà đòi save à ?");
				return false;
			}
			if (listObj.size() < GameManager.COT * GameManager.HANG) {
				JOptionPane.showMessageDialog(null, "Hình như thiếu gì đó , check lại đi");
				return false;
			}
			RandomAccessFile rf = new RandomAccessFile(
					getClass().getResource("/resource/map/").getPath() + fileName + ".txt", "rw");

			for (GameObject gameObject : listObj) {
				rf.writeBytes(gameObject.getType() + "," + gameObject.getSize() + "," + gameObject.getX() + ","
						+ gameObject.getY() + "\n");
			}
			rf.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public List<GameObject> loadMap(int index) {
		currentMap = index ;
		List<GameObject> listObj = new ArrayList<GameObject>();
		String getMap = listMap.get(currentMap);
		File file = new File(getClass().getResource("/resource/map/" + getMap).getPath());
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			while (raf.getFilePointer() < raf.length()) {
				String line = raf.readLine();
				String[] words = line.split(",");
				if (words.length < 4) {
					continue;
				}

				String type = words[0];
				int size = Integer.parseInt(words[1]);
				int x = Integer.parseInt(words[2]);
				int y = Integer.parseInt(words[3]);
				BufferedImage img = getImageByType(type);
				if (Integer.parseInt(type) == GameManager.TYPE_GHOST) {
					int random = new Random().nextInt(3);
					if (random == 0) {
						img = imGhost;
					} else if (random == 1) {
						img = imGhost1;
					} else {
						img = imGhost2;
					}
				}
				GameObject gameObj = new GameObject(x, y, img, img, Integer.parseInt(type), size);
				if (listObj.indexOf(gameObj) == -1) {
					listObj.add(gameObj);
				}
			}
			raf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listObj;
	}

	public BufferedImage initImage(String name) {
		try {
			return ImageIO.read(getClass().getResource("/resource/image/" + name));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private BufferedImage getImageByType(String type) {
		int typeI = Integer.parseInt(type);
		if (typeI == GameManager.TYPE_PACMAN) {
			return imPacman;
		}
		if (typeI == GameManager.TYPE_BERRY) {
			return imBerry;
		}
		if (typeI == GameManager.TYPE_BRICK) {
			return imWall;
		}
		if (typeI == GameManager.TYPE_FOOD) {
			return imFood;
		}
		if (typeI == GameManager.TYPE_GHOST) {
			return imGhost;
		}
		return null;

	}
}
