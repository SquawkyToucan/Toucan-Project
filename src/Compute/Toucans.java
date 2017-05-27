package Compute;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Toucans implements MouseListener {
	// Toucans is a strategy game where the goal is to win
	// You can win through DOMINATION (35 tiles), TECH (35 points), ECONOMY (35K GDP + dependants, trade), or CIVILIZING
	// Domination Victory: Every tile on the board is conquered. Enemies are vanquished.
	// Technological Victory: 35 points worth of breakthrough, such as rockets, boats, etc. Can benefit military points
	// Economical Victory: A combined sum of trade (per turn), subsidies to other tribes (per turn), production per
	// turn, and GDP over 35K. On hard, if you near this all other countries will sanction you, meaning inner country
	// must control majority of resources
	// Civilizing Victory: Your society has a combination of currency (5p), trade relations (5p), healthcare (5p),
	// internet (10p - from Tech), education (5p), mining (5p), a university (5p), literature (5p), language (10p), and
	// a city all combined to over 35 points + a population over 1,000
	// You can die through being CONQUERED or CONCEDING (lit lit lit lit lit)
	// Death through Conquered: You are taken over by another tribe.
	// Death through Starvation: Not enough food!
	// Death through Lack of Economy: Your economy and currency collapse - anarchy happens.
	// Conceding/giving up - Button will be available for this if necessary
	JButton[] buttons = new JButton[36];
	int[] status = new int[35];
	JFrame startframe = new JFrame();
	JPanel startpanel = new JPanel();
	JFrame options = new JFrame();
	JPanel moves = new JPanel();
	JPanel playpanel = new JPanel();
	JFrame game = new JFrame();
	GamePanel gamePanel;
	public static final int width = 600;
	public static final int height = 900;
	boolean boardInitialized = false;

	Toucans() {
		startframe.setSize(width, height);
		startframe.setVisible(true);
		game.setSize(600, 900);
		// Use like this - top 100 = Players, points
		// next 600 down is game board (100x100) per piece
		// (use JPanel and an image, then determine which block it is based off that)
		// last 200 is bar with stuff like currency, and places to use troops and cut deals
		gamePanel = new GamePanel();
		setup();
	}

	public void createBoard() {
		int locx = 0;
		int locy = 0;
		JFrame frame = new JFrame();
		frame.add(playpanel);
		frame.setSize(600, 600);
		playpanel.addMouseListener(this);
		playpanel.setLayout(new GridLayout(6, 6));
		for (int i = 0; i < 36; i++) {
			buttons[i] = new JButton();
			buttons[i].setPreferredSize(new Dimension(100, 100));
			buttons[i].setLocation(locx * 100, locy * 100 + 100);
			buttons[i].setVisible(true);
			locx++;
			if (locx == 6) {
				locy++;
				locx = 0;
			}
			playpanel.add(buttons[i]);
		}
		frame.setVisible(true);
	}

	void setup() {
		startframe.add(gamePanel);
		startframe.addMouseListener(this);
		startframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gamePanel.startGame();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startframe.addKeyListener(gamePanel);
	}

	public static void main(String[] args) {
		Toucans runner = new Toucans();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(!boardInitialized) {
			createBoard();
			boardInitialized = true;
		}
		else {
			Object block = e.getSource();
			//Blitz through the following: use Object to get source of block, then after Object do is the thing to do
			//Take object do and do it to object block, be sure to start stuff like wars, etc.
			System.out.println("Square accessed: " + block);
			//Get the number - Iʻm not sure how I can put them all through a little fancy loop so Iʻm going to hardcode it
		}
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
