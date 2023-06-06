package com.dat.pacman.gui.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.dat.pacman.gui.Icommon;

abstract class BasePanel extends JPanel implements Icommon, ActionListener {
	public BasePanel() {
		init();
		addEvent();
		addComp();
	}

	public JLabel createLable(String text, Font f, int x, int y, Color c) {
		JLabel lb = new JLabel(text);
		lb.setForeground(c);
		// sử dụng đối tượng FontMetríc để đo chiều dài chiều cao của chuỗi text theo
		// font chữ
		FontMetrics fontMetrics = getFontMetrics(f);
		int wText = fontMetrics.stringWidth(lb.getText());
		int hText = fontMetrics.getHeight();
		lb.setSize(wText + 10, hText);
		lb.setOpaque(true);
		lb.setFont(f);
		lb.setBackground(Color.WHITE);
		lb.setLocation(x, y);
		return (lb);
	}

	public BufferedImage initImage(String name) {
		try {
			return ImageIO.read(getClass().getResource("/resource/image/" + name));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public JButton createButton(String text, Font f, int x, int y, Color c, String name) {
		JButton bt = new JButton(text);
		// sử dụng đối tượng FontMetríc để đo chiều dài chiều cao của chuỗi text theo
		// font chữ
		FontMetrics fontMetrics = getFontMetrics(f);
		int wText = bt.getInsets().left * 2 + fontMetrics.stringWidth(bt.getText());
		int hText = fontMetrics.getHeight() + bt.getInsets().top * 2;
		bt.setSize(wText + 10, hText + 10);
		bt.setForeground(c);
		bt.setBackground(Color.WHITE);
		bt.setLocation(x, y);
		bt.setName(name);
		bt.addActionListener(this);
		bt.setFont(f);
		return (bt);
	}

	@Override
	public final void actionPerformed(ActionEvent e) {
		Component c = (Component) e.getSource();
		String name = c.getName();
		doClick(name, c.getX(), c.getY());
	}

	protected abstract void doClick(String name, int x, int y);
}
