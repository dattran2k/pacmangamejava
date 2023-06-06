package com.dat.pacman.gui.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.dat.pacman.gui.GUI;
import com.dat.pacman.gui.ICallBackChangeMap;
import com.dat.pacman.logic.DoFile;
import com.dat.pacman.logic.GameManager;
import com.dat.pacman.logic.GameObject;

public class CreateMapPanel extends BasePanel implements MouseMotionListener {
	private Rectangle rectSelected;

	private static final String LB_BRICK = "LB_BRICK";
	private static final String LB_GHOST = "LB_GHOST";
	private static final String BT_DELETE = "BT_DELETE";
	private static final String LB_BERRY = "LB_BERRY";
	private static final String LB_PACMAN = "LB_PACMAN";
	private static final String BT_ADD_FOOD = "BT_ADD_FOOD";
	private static final String BT_REMOVE_ALL = "BT_REMOVE_ALL";
	private static final String BT_SAVE = "BT_SAVE";

	private static final String BT_Load = "BT_LOAD";

	private static final String LB_HOME = "LB_HOME";
	private BufferedImage imIcSelected, imBackground;
	private ImageIcon icWall, icGhost, icBerry, icPacman;
	private JLabel lbBrick, lbGhost, lbBerry, lbPacman, lbHome;
	private JButton btDelete, btAddFood, btRemoveAll, btSave, btLoad;
	private MouseAdapter mouseListener;
	private boolean isDelete = false;
	private ICallBackChangeMap callBack;
	private CreateMapMapGame mapGame;
	private ImageIcon icHome;

	public CreateMapPanel(ICallBackChangeMap callBack) {
		super();
		this.callBack = callBack;
	}

	@Override
	public void init() {
		mapGame = new CreateMapMapGame(false);
		setBackground(Color.WHITE);
		setLayout(null);
		icWall = new ImageIcon(mapGame.getImWall());

		icGhost = new ImageIcon(mapGame.getImGhost());

		icBerry = new ImageIcon(mapGame.getImBerry());

		icPacman = new ImageIcon(mapGame.getImPacman());
		imBackground = initImage("/BackGroundCreateMap.png");
		icHome = new ImageIcon(initImage("home.png"));
	}

	@Override
	public void addEvent() {
		mouseListener = new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Component a = ((Component) e.getSource());
					String name = a.getName().toString();
					rectSelected = a.getBounds();
					switch (name) {
					case LB_BRICK:

						imIcSelected = mapGame.getImWall();

						isDelete = false;
						JOptionPane.showMessageDialog(null, "Ấn và kéo chuột để vẽ tường");
						break;

					case LB_GHOST:
						JOptionPane.showMessageDialog(null, "Tạo ra đổi thủ , tạo ít ít thôi nha ");
						imIcSelected = mapGame.getImGhost();

						isDelete = false;
						break;
					case LB_BERRY:
						JOptionPane.showMessageDialog(null, "Tạo item Cherry , khi ăn trái này có khả năng giết Ghost");
						imIcSelected = mapGame.getImBerry();

						isDelete = false;
						break;
					case LB_PACMAN:
						JOptionPane.showMessageDialog(null,
								"Tạo ra pacman , tạo 1 thằng thôi nha , tạo nhiều coi trừng");
						imIcSelected = mapGame.getImPacman();

						isDelete = false;
						break;
					case LB_HOME:
						callBack.changeMap(GUI.NAME_PANEL_HOME_FROM_CREATE);
						break;
					default:
						break;
					}

				} catch (NullPointerException e2) {
				}
				doDraw(e.getX() - GameManager.X_PANEL, e.getY() - GameManager.Y_PANEL);
				repaint();
			}
		};
		addMouseListener(mouseListener);
		addMouseMotionListener(this);
	}

	@Override
	public void addComp() {
		lbHome = new JLabel(icHome);
		add(lbHome);
		lbHome.setSize(50, 50);
		lbHome.setLocation(GUI.W_FRAME - 50 - lbHome.getWidth(), 10);
		lbHome.addMouseListener(mouseListener);
		lbHome.setName(LB_HOME);

		add(mapGame);
		Font fBig = new Font("Arial", Font.BOLD, 30);
		lbBrick = new JLabel(icWall);
		lbBrick.setLocation(mapGame.getX() + mapGame.getWidth() + 40, mapGame.getY() + 50);
		lbBrick.setSize(50, 50);
		lbBrick.setName(LB_BRICK);
		add(lbBrick);
		lbBrick.addMouseListener(mouseListener);

		lbGhost = new JLabel(icGhost);
		lbGhost.setSize(50, 50);
		lbGhost.setName(LB_GHOST);
		lbGhost.setLocation(lbBrick.getX() + lbBrick.getWidth() + 40, mapGame.getY() + 50);
		add(lbGhost);
		lbGhost.addMouseListener(mouseListener);

		lbBerry = new JLabel(icBerry);
		lbBerry.setSize(50, 50);
		lbBerry.setName(LB_BERRY);
		lbBerry.setLocation(lbBrick.getX(), mapGame.getY() + 140);
		add(lbBerry);
		lbBerry.addMouseListener(mouseListener);

		lbPacman = new JLabel(icPacman);
		lbPacman.setSize(50, 50);
		lbPacman.setName(LB_PACMAN);
		lbPacman.setLocation(lbBerry.getX() + lbBerry.getWidth() + 20, lbBerry.getY());
		add(lbPacman);
		lbPacman.addMouseListener(mouseListener);

		btAddFood = createButton("ADD FOOD", fBig, lbBrick.getX(), lbPacman.getY() + lbPacman.getHeight() + 50,
				Color.YELLOW, BT_ADD_FOOD);
		add(btAddFood);

		btDelete = createButton("DELETE", fBig, btAddFood.getX(), btAddFood.getY() + btAddFood.getHeight() + 20,
				Color.RED, BT_DELETE);
		add(btDelete);

		btRemoveAll = createButton("REMAKE", fBig, btDelete.getX(), btDelete.getY() + btDelete.getHeight() + 20,
				Color.RED, BT_REMOVE_ALL);
		add(btRemoveAll);

		btSave = createButton("SAVE", fBig, mapGame.getX(), mapGame.getY() + mapGame.getHeight() + 70, Color.BLUE,
				BT_SAVE);
		add(btSave);
		btLoad = createButton("LOAD", fBig, btSave.getX() + btSave.getWidth() + 20, btSave.getY(), Color.BLUE, BT_Load);
		add(btLoad);
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(imBackground, 0, 0, null);
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(1));
		if (rectSelected != null) {
			g2d.drawRect((int) rectSelected.getX() - 5, (int) rectSelected.getY() - 5,
					(int) rectSelected.getWidth() + 10, (int) rectSelected.getHeight() + 10);
		}

	}

	@Override
	protected void doClick(String name, int x, int y) {
		rectSelected = null;
		switch (name) {
		case BT_DELETE:
			JOptionPane.showMessageDialog(null, "Ấn và kéo chuột để xoá tất mọi thứ mà nó đi qua trong map");
			isDelete = true;
			imIcSelected = null;
			break;
		case BT_ADD_FOOD:

			autoAddFood();
			break;
		case BT_REMOVE_ALL:
			JOptionPane.showMessageDialog(null, "Bạn đã ấn vào nút xoá tất cả , không có chuyện rollback đâu nhé");
			mapGame.setListObj(new ArrayList<GameObject>());
			repaint();
			break;
		case BT_SAVE:
			String fileName = JOptionPane.showInputDialog("Nhập tên file");
			if (new DoFile().export(fileName, mapGame.getListObj())) {
				JOptionPane.showMessageDialog(this, "Thành công");
			} else {
				JOptionPane.showMessageDialog(this, "Thất bại");
			}

			break;
		case BT_Load:
			mapGame.loadMap();
			break;
		default:
			break;
		}
	}

	private void autoAddFood() {
		mapGame.autoAddFood();
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		try {
			if (isDelete || imIcSelected.equals(mapGame.getImWall())) {
				doDraw(e.getX() - GameManager.X_PANEL, e.getY() - GameManager.Y_PANEL);
			}
		} catch (NullPointerException e2) {
		}
		repaint();
	}

	protected void doDraw(int x, int y) {
		mapGame.doDraw(x, y, imIcSelected, isDelete);
		mapGame.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

}
