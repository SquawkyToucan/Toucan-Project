package Compute;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {
	Timer timer;
	Font big = new Font("Menlo", Font.BOLD, 36);
	Font small = new Font("Menlo", Font.PLAIN, 24);

	GamePanel() {
		timer = new Timer(1000 / 60, this);
	}

	void startGame() {
		timer.start();
		repaint();
	}

	public void paintComponent(Graphics g) {
		g.setColor(new Color(0, 153, 85));
		g.fillRect(0, 0, 600, 900);
		g.setColor(Color.WHITE);
		g.setFont(big);
		g.drawString("Creative Title", 150, 100);
		g.setFont(small);
		g.drawString("Click to Start", 275, 300);
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

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// ¡Hola! ¿Como estas? Muy bien, gracias. De nada. Hallo! Wie geht es dir? Mir gehts gut, danke! Bitte schon.
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
// 김종은임니다