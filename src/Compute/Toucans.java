package Compute;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Toucans implements MouseListener, ActionListener {
	// Toucans is a strategy game where the goal is to win
	// You can win through DOMINATION (35 tiles), TECH (35 points), ECONOMY (35K
	// GDP + dependants, trade), or CIVILIZING
	// Domination Victory: Every tile on the board is conquered. Enemies are
	// vanquished.
	// Economical Victory: A combined sum of trade (per turn), subsidies to
	// other tribes (per turn), production per
	// turn, and GDP over 35K. On hard, if you near this all other countries
	// will sanction you, meaning inner country
	// must control majority of resources
	// You can die through being CONQUERED or CONCEDING (lit lit lit lit lit)
	// Death through Conquered: You are taken over by another tribe.
	// Death through Starvation: Not enough food! (Food is gained via
	// territory-population ratio. 10 food per square, and ratio must be at
	// least 5:1)
	// Death through Lack of Economy: Your economy and currency collapse -
	// anarchy happens.
	// Conceding/giving up - Button will be available for this if necessary
	JButton[] buttons = new JButton[36];
	int turnOf = 0;
	int turns = 0;
	int onepower = 0;
	int oneoutput  = 0;
	int twopower = 0;
	int twooutput = 0;
	int threepower = 0;
	int threeoutput = 0;
	int fourpower = 0;
	int fouroutput = 0;
	int[] status = new int[36];
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
		// (use JPanel and an image, then determine which block it is based off
		// that)
		// last 200 is bar with stuff like currency, and places to use troops
		// and cut deals
		gamePanel = new GamePanel();
		setup();
		gamePanel.startGame();
		turnOf = 1;
		turns++;
	}

	public void createBoard() {
		int locx = 0;
		int locy = 0;
		JFrame frame = new JFrame();
		frame.add(playpanel);
		frame.setSize(600, 600);
		frame.getContentPane().setBackground(new Color(0, 153, 85));
		playpanel.addMouseListener(this);
		playpanel.setLayout(new GridLayout(6, 6));
		for (int i = 0; i < 36; i++) {
			buttons[i] = new JButton();
			buttons[i].setPreferredSize(new Dimension(100, 100));
			buttons[i].setLocation(locx * 100, locy * 100 + 100);
			buttons[i].setVisible(true);
			buttons[i].addMouseListener(this);
			buttons[i].setBorder(BorderFactory.createLineBorder(new Color(0, 153, 87), 10));
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
		if (!boardInitialized) {
			createBoard();
			boardInitialized = true;
			status[0] = 1;
			status[5] = 2;
			status[30] = 3;
			status[35] = 4;
		} else {
			Object block = e.getSource();
			// Blitz through the following: use Object to get source of block,
			// then after Object do is the thing to do
			// Take object do and do it to object block, be sure to start stuff
			// like wars, etc.
			/**
			 * Squares go by:? 0,1,2,3,4,5 6,7,8,9,10,11 12,13,14,15,16,17
			 * 18,19,20,21,22,23 24,25,26,27,28,29 30,31,32,33,34,35
			 * 
			 * Toucan starts at 0 - Status is ONE Parrot starts at 5 - Status is
			 * TWO Macaw starts at 30 - Status is THREE Eclectus Parrot starts
			 * at 35 - Status is FOUR
			 */

			// This method could work, but I'm trying something else that's more
			// efficient

			/*
			 * if(block == buttons[0]) { String move =
			 * JOptionPane.showInputDialog(
			 * "Playing on selected square:\n- /claim\n- /attack\n- /develop <infrastructure, tech, education>\n- /train\n- /endmove\n- /concede"
			 * ); if(move.equals("/claim")) { if(status[0] != 0) {
			 * System.out.println(
			 * "This move is invalid - you cannot claim a claimed square."); } }
			 * if(move.equals("/attack")) { System.out.println(
			 * "Sending units to square"); if(turnOf == 2) { if(twopower >
			 * onepower + 1) { System.out.println("Square taken"); } else {
			 * System.out.println("Failed to take square"); } } } // TWO
			 * OPTIONS: // - Command based - sets a "Selected" variable to this.
			 * Waits for input from user via command // - GUI based - opens a
			 * frame that has all of the stuff } if(block == buttons[1]) {
			 * 
			 * }
			 */
			
			//Player, not AI
			if (turnOf == 1) {
				String move = JOptionPane.showInputDialog(
						"Action to Perform:\n- /claim - Claim a square\n- /attack - Attack a claimed square\n- /develop <infrastructure, tech, education> - Will boost output\n- /train - Train troops\n- /endmove - End move\n- /concede - Quit");
				if (move.equals("/concede")) {
					int confirm = JOptionPane.showConfirmDialog(null, "Are you sure?");
					if (confirm == JOptionPane.YES_OPTION) {
						System.exit(0);
					} else {
						System.out.println("Aborted game ending!");
						turnOf = 2;
						turns++;
					}
				}
				//Moves that DO NOT require usage of squares WHICH MAKES LIFE SO MUCH EASIER
				if (move.equals("/train")) {
					onepower++;
					System.out.println("Player trained troops");
					turnOf = 2;
					turns++;
				}
				if(move.equals("/develop tech")) {
					//Tech is a gamble. You can win big (+5) or waste a turn (0), or turn out somewhere in between.
					int rand = new Random().nextInt(5);
					oneoutput = oneoutput + rand;
					System.out.println("Player gained " + rand + " output");
					turnOf = 2;
					turns++;
				}
				if(move.equals("/develop education")) {
					//Education earns less on gambling but is always positive
					int rand = new Random().nextInt(2);
					oneoutput = oneoutput + rand + 1;
					System.out.println("Player gained " + (rand + 1) + " output");
					turnOf = 2;
					turns++;
				}
				if(move.equals("/develop infrastructure")) {
					//Infrastructure is a solid investment but only gains 1
					oneoutput++;
					System.out.println("Player gained 1 output");
					turnOf = 2;
					turns++;
				}
				if(move.equals("/endmove")) {
					System.out.println("Player checks action");
					turnOf = 2;
					turns++;
				}
				//Moves involving squares! OHH THE PAIN
				
				//AI Moves:
				
			}
			//AI Move: Parrot
			//Parrot will use rotating int - claim, train, dev, train, claim, dev...
			
			//AI Move: Macaw
			//Macaw will play agressively - claim, train, claim, train, dev, train, dev...
			
			//AI Move: Eclectus Parrot
			//Eclecty will play peacefully - dev, dev, claim, train...
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
