package com.dat.pacman.logic;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.dat.pacman.gui.ICallBackStop;
import com.dat.pacman.logic.AIAStar.Coordinate;
import com.dat.pacman.logic.Dynamic.ICallBackIntersect;

public class GameManager {

	public static final int SIZE = 20;
	public static final int HANG = 20;
	public static final int COT = 20;
	public static final int X_PANEL = 50;
	public static final int Y_PANEL = 220;
	public static final int TYPE_PACMAN = 0;
	public static final int TYPE_GHOST = 1;
	public static final int TYPE_BERRY = 2;
	public static final int TYPE_BRICK = 3;
	public static final int TYPE_FOOD = 4;
	public static final int TIME_BERRY = 5999;
	public static final String EFFECT_BERRY = "LET EAT GHOST";
	public static final int TIMEGAME = 120000;

	public HashMap<String, Integer> mapEffect;
	private List<GameObject> listGameobj;
	private Player player;
	private DoFile doFile;
	private AIAStar ai;
	private int maxEnemy;
	private int maxFood;
	private int realtime;
	private int score;
	private int countFood;
	private int countEnemy;
	private boolean isLosing = false;
	private boolean isWinning = false;

	private ICallBackIntersect callBack = new ICallBackIntersect() {

		@Override
		public void returnObject(GameObject obj) {
			if (obj instanceof Enemy) {
				if (!checkEffect(obj)) {
					isLosing = true;
				} else {
					countEnemy--;
					if (countEnemy <= 0 || countFood <= 0) {
						isWinning = true;
					}
				}
			} else if (obj instanceof Item) {
				countFood--;
				addEffect(obj);
				listGameobj.remove(obj);
				return;
			}
		}

		private void addEffect(GameObject obj) {
			if (((Item) obj).getEffect() == EFFECT_BERRY) {
				int timeEffect = 0;
				try {
					timeEffect = mapEffect.get(EFFECT_BERRY);
				} catch (NullPointerException e) {
				}
				mapEffect.put(EFFECT_BERRY, timeEffect + TIME_BERRY);
			}
		}

		private boolean checkEffect(GameObject obj) {
			for (String name : mapEffect.keySet()) {
				if (name.equals(EFFECT_BERRY) && mapEffect.get(name) > 0) {
					listGameobj.remove(obj);
					return true;
				}
			}
			return false;
		}
	};

	public GameManager() {
		mapEffect = new HashMap<String, Integer>();
		listGameobj = new ArrayList<GameObject>();
		doFile = new DoFile();
		ai = new AIAStar();
		initData(doFile.loadMap(0));
	}

	public GameManager(int currentMap, int score) {
		mapEffect = new HashMap<String, Integer>();
		listGameobj = new ArrayList<GameObject>();
		doFile = new DoFile();
		this.score = score;
		initData(doFile.loadMap(currentMap + 1));

	}

	public void initData(List<GameObject> listObj) {
		for (int i = 0; i < listObj.size(); i++) {
			GameObject temp = listObj.get(i);
			int x = temp.getX();
			int y = temp.getY();
			BufferedImage img = temp.getImg();
			BufferedImage origin = temp.getOrigin();
			int size = temp.getSize();
			int type = temp.getType();
			switch (temp.getType()) {
			case TYPE_PACMAN:
				player = new Player(x, y, img, temp.getOrigin(), temp.getType());
				break;
			case TYPE_BERRY:
				countFood++;
				listGameobj.add(new Item(x, y, img, type, TIME_BERRY, EFFECT_BERRY));
				break;
			case TYPE_FOOD:
				countFood++;
				listGameobj.add(new Item(x, y, img, type, size));
				break;
			case TYPE_GHOST:
				countEnemy++;
				listGameobj.add(new Enemy(x, y, img, origin, type));
				break;
			case TYPE_BRICK:
				listGameobj.add(new GameObject(x, y, img, type));
				break;
			default:
				break;
			}
		}
		maxEnemy = countEnemy;
		maxFood = countFood;
	}

	public void drawGameObject(Graphics2D g2d) {
		for (int i = 0; i < listGameobj.size(); i++) {
			if (!(listGameobj.get(i) instanceof Enemy)) {
				listGameobj.get(i).draw(g2d);
			}
		}
		for (int i = 0; i < listGameobj.size(); i++) {
			if (listGameobj.get(i) instanceof Enemy) {
				listGameobj.get(i).draw(g2d);
			}
		}
		player.draw(g2d);
	}

	public void doSTask(int count, ICallBackStop callBack) {
		if (TIMEGAME < realtime) {
			isLosing = true;
		}
		if (isLosing || (isWinning && doFile.currentMap >= doFile.maxMap - 1)) {
			callBack.stop();
			return;
		}
		if (isWinning() && doFile.currentMap < doFile.maxMap - 1) {
			callBack.nextMap(doFile.currentMap, score);
		}

		for (String name : mapEffect.keySet()) {
			if (mapEffect.get(name) > 0) {
				mapEffect.replace(name, mapEffect.get(name) - 1);
			}
		}
		realtime++;
		if (count % getPlayer().getSpeed() == 0) {
			move(count);
		}
		moveObject(count);

		score = (maxFood - countFood) * 50 + (maxEnemy - countEnemy) * 200;
	}

	public void move(int count) {
		player.move(listGameobj, callBack, count);

	}

	public void moveObject(int time) {

		for (int i = 0; i < listGameobj.size(); i++) {
			GameObject temp = listGameobj.get(i);
			if (temp instanceof Enemy) {
				Enemy enemy = (Enemy) temp;
				moveAI(enemy, player, time);
				
			}
		}
	}


	private void moveAI(Enemy enemy, Player player, int time) {
		
		if (time % enemy.speed == 0) {
			if(enemy.way.isEmpty()) {
				enemy.way = ai.findWay(enemy, player, listGameobj);
			} else {
				double x = enemy.getX() - enemy.way.get(0).x * 20;
				double y = enemy.getY() - enemy.way.get(0).y * 20;
				if (y == 0) {
					if (x > 0) {
						enemy.orient = Enemy.LEFT;
					} else {
						enemy.orient = Enemy.RIGHT;
					}
				} else if (x == 0) {
					if (y > 0) {
						enemy.orient = Enemy.UP;
					} else {
						enemy.orient = Enemy.DOWN;
					}
				}
				enemy.move(listGameobj, callBack);
				if (enemy.getX() == enemy.way.get(0).x * 20 && enemy.getY() == enemy.way.get(0).y * 20) {
					enemy.way.remove(0);
					if (enemy.way.isEmpty()) {
						
					}
				}
			}

		}
	}

	public void changeOrientPlayer(int newOrient) {
		player.changeOrient(newOrient);
	}

	public List<GameObject> getListGameobj() {
		return listGameobj;
	}

	public void setListGameobj(List<GameObject> listGameobj) {
		this.listGameobj = listGameobj;
	}

	public Player getPlayer() {
		return player;
	}

	public HashMap<String, Integer> getMapEffect() {
		return mapEffect;
	}

	public static int getSize() {
		return SIZE;
	}

	public static int getHang() {
		return HANG;
	}

	public static int getCot() {
		return COT;
	}

	public static int getxPanel() {
		return X_PANEL;
	}

	public static int getyPanel() {
		return Y_PANEL;
	}

	public static int getTypePacman() {
		return TYPE_PACMAN;
	}

	public static int getTypeGhost() {
		return TYPE_GHOST;
	}

	public static int getTypeBerry() {
		return TYPE_BERRY;
	}

	public static int getTypeBrick() {
		return TYPE_BRICK;
	}

	public static int getTypeFood() {
		return TYPE_FOOD;
	}

	public static int getTimeBerry() {
		return TIME_BERRY;
	}

	public static String getEffectBerry() {
		return EFFECT_BERRY;
	}

	public static int getTimegame() {
		return TIMEGAME;
	}

	public int getMaxEnemy() {
		return maxEnemy;
	}

	public int getMaxFood() {
		return maxFood;
	}

	public int getRealtime() {
		return realtime;
	}

	public int getScore() {
		return score;
	}

	public int getCountFood() {
		return countFood;
	}

	public int getCountEnemy() {
		return countEnemy;
	}

	public boolean isLosing() {
		return isLosing;
	}

	public boolean isWinning() {
		return isWinning;
	}

	public ICallBackIntersect getCallBack() {
		return callBack;
	}

}
