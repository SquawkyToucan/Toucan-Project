package Compute;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener{
	Timer timer;
	JPanel playpanel;
	JButton[] buttons = new JButton[35];
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
	public void paintgame(Graphics g) {
		g.setColor(new Color(0, 153, 85));
		g.fillRect(0, 0, 1000, 1600); 
	}
	public void createBoard() {
		int locx = 0;
		int locy = 0;
		playpanel = new JPanel();
		this.setLayout(new GridLayout(6, 6));
		this.add(playpanel);
		playpanel.addMouseListener(this);
		for(int i = 0; i < 35; i++) {
			buttons[i].setSize(100, 100);
			buttons[i].setLocation(locx * 100, locy * 100 + 100);
			locx++;
			if(locx == 6) {
				locy++;
				locx = 0;
			}
			this.add(buttons[i]);
		}
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
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
