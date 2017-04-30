package Compute;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.imageio.ImageIO;

import java.io.IOException;

public class GamePanel extends JPanel implements ActionListener, KeyListener{
	Timer timer;
	public static BufferedImage startscreen;
	GamePanel() {
		try {
			startscreen = ImageIO.read(this.getCLass().getResourceAsStream("startscreen"));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		timer = new Timer(1000/60, this);
	}
	void startGame() {
		timer.start();
	}
	public void paintComponent(Graphics g) {
		g.drawImage(GamePanel.startscreen, 0, 0,1000, 1600, null);
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
