package Compute;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener{
	Timer timer;
	Font big = new Font("Menlo", Font.BOLD, 72);
	Font small = new Font("Menlo", Font.PLAIN, 36);
	public static BufferedImage startscreen;
	GamePanel() {
		timer = new Timer(1000/60, this);
	}
	void startGame() {
		timer.start();
	}
	public void paintComponent(Graphics g) {
		g.setColor(new Color(0, 153, 85));
		g.fillRect(0, 0, 1000, 1600);
		g.setColor(Color.WHITE);
		g.setFont(big);
		g.drawString("Creative Title", 190, 100);
		g.setFont(small);
		g.drawString("Click to Start Game", 275, 300);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println("Key typed");
	}
	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Key pressed");
	}
	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("Key released");
	}
}
