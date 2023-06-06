package com.dat.pacman.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.dat.pacman.logic.AIAStar.Coordinate;

public class AIAStar {

	public List<Coordinate> findWay(Enemy enemy, Player player, List<GameObject> listGameobj) {
		Coordinate firstNode = new Coordinate(enemy.getX() / GameManager.SIZE, enemy.getY() / GameManager.SIZE);
		Coordinate goal = new Coordinate(player.getX() / GameManager.SIZE, player.getY() / GameManager.SIZE);
		firstNode.h = calculateH(firstNode.x, firstNode.y, goal.x, goal.y);
		// System.out.println(firstNode.h);
		List<Coordinate> way = null;
		// set là 1 collection chỉ lưu những giá trị khác nhau ( giống như key trong
		// hash map)
		// lưu những toạ độ
		Set<Coordinate> explored = new HashSet<Coordinate>();
		// Sử dụng priorityQueqe để khai báo kiểu comparator ( override giữa 2 phần tử
		// trong PriorityQueue) Khi lấy phần tử ra luôn lấy phần tử nhỏ nhất
		PriorityQueue<Coordinate> queue = new PriorityQueue<Coordinate>(20, new Comparator<Coordinate>() {
			public int compare(Coordinate i, Coordinate j) {
				if (i.f > j.f) {
					return 1;
				}

				else if (i.f < j.f) {
					return -1;
				}

				else {
					return 0;
				}
			}

		});
		boolean found = false;
		queue.add(firstNode);
		// Khi hàng đợi tồn tại phần tử và chưa tìm thấy đích thì lặp lại vòng lặp
		while ((!queue.isEmpty()) && (!found)) {
			Coordinate current = queue.poll();
			explored.add(current);
			if (current.x == goal.x && current.y == goal.y) {
				found = true;
				
				return getWay(current);
			}
			initAdjacencies(current, goal, listGameobj);
			System.out.println("===");
			for (Coordinate node : current.adjacencies) {
				Coordinate child = node;

				double temp_g = current.g + 1;
				double temp_f = temp_g + child.h;
				if ((explored.contains(child)) && (temp_f >= child.f)) {
				} else if ((!queue.contains(child)) || (temp_f < child.f)) {
					child.parent = current;
					// lưu khoảng cách từ điểm xuất phát hành phố đã thoả mãn
					child.g = temp_g;
					// lưu khoảng cách từ thành phố hiện tại tới thành phố đã thoả mãn
					child.f = temp_f;
					queue.add(child);
				}
				System.out.println(explored.size());
				System.out.println("x =" +node.x + " y = "+node.y + " f ="+node.f);
			}
			
		}
		return null;
	}

	private List<Coordinate> getWay(Coordinate current) {
		List<Coordinate> path = new ArrayList<Coordinate>();
		for (Coordinate node = current; node != null; node = node.parent) {
			path.add(node);
		}

		Collections.reverse(path);
		return path;
	}

	private void initAdjacencies(Coordinate current, Coordinate goal, List<GameObject> listGameobj) {
		int x = current.x;
		int y = current.y;
		for (int i = 0; i < 4; i++) {
			Coordinate temp = null;
			switch (i) {
			case 0:
				temp = new Coordinate(x - 1, y);
				break;
			case 1:
				temp = new Coordinate(x + 1, y);
				break;
			case 2:
				temp = new Coordinate(x, y - 1);
				break;
			case 3:
				temp = new Coordinate(x, y + 1);
				break;
			}
			;
			for (GameObject gameObject : listGameobj) {
				if (temp.x == gameObject.x / 20 && temp.y == gameObject.y / 20
						&& gameObject.type == GameManager.TYPE_BRICK) {
					temp = null;
					break;
				}
			}
			if (temp != null) {
				temp.h = calculateH(temp.x, temp.y, goal.x, goal.y);
				current.adjacencies.add(temp);
			}
		}
	}

	private double calculateH(int x1, int y1, int x2, int y2) {
		double x = Math.pow((x2 - x1), 2);
		double y = Math.pow((y2 - y1), 2);

		return Math.sqrt(x + y);
	}

	class Coordinate {
		public int x, y;
		// G là khoảng cách di chuyển từ điểm bắt đầu tới vị trí hiện tại.
		public double g;
		// Chiều dài đường chim bay tính từ node hiện tại tới thành phố cần tìm
		public double h;

		// khoảng cách từ vị trí hiện tại đang xét mà tới điểm đích
		// Nó chỉ là phỏng đoán tại vì chúng ta không thể biết được giá trị của nó. Chỉ
		// là ước lượng.
		public double f = 0;
		// Danh sách điểm mà node hiện tại có thể đi tới
		public List<Coordinate> adjacencies;

		public Coordinate parent;

		public Coordinate(int x, int y) {
			super();
			this.x = x;
			this.y = y;
			adjacencies = new ArrayList<AIAStar.Coordinate>();
		}

		
		@Override
		public boolean equals(Object obj) {
			
			return this.x == ((Coordinate)obj).x&& this.y == ((Coordinate)obj).y;
		}

	}

}
