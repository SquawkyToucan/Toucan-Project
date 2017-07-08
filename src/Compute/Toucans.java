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
	int oneoutput = 0;
	int twopower = 0;
	int twooutput = 0;
	int twopattern = 1;
	int threepower = 0;
	int threeoutput = 0;
	int threepattern = 1;
	int fourpower = 0;
	int fourpattern = 1;
	int fouroutput = 0;
	int[] status = new int[36];
	// Wars available:
	/**
	 * 1 - Toucan, Parrot 2 - Toucan, Macaw 3 - Toucan, Eclectus 4 - Parrot,
	 * Macaw 5 - Macaw, Eclectus 6 - Parrot, Eclectus
	 * 
	 */

	// Most likely, wars will mostly involve macaw and toucan
	// This is because one is the "I'm great!" player and the other is agreesive
	// AI
	// This hopefully explains the odd ordering of wars

	boolean warOne = false;
	boolean warTwo = false;
	boolean warThree = false;
	boolean warFour = false;
	boolean warFive = false;
	boolean warSix = false;

	JFrame startframe = new JFrame();
	JPanel startpanel = new JPanel();
	JPanel moves = new JPanel();
	JPanel playpanel = new JPanel();

	GamePanel gamePanel;
	public static final int width = 600;
	public static final int height = 900;
	boolean boardInitialized = false;

	Toucans() {
		startframe.setSize(width, height);
		startframe.setVisible(true);
		gamePanel = new GamePanel();
		gamePanel.setSize(600, 900);
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
			buttons[i].setBorder(BorderFactory.createLineBorder(new Color(0, 153, 87), 2));
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
		startframe.addKeyListener(gamePanel);
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
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
			// Blitz through the following: use Object to get source of block,
			// then after Object do is the thing to do
			// Take object do and do it to object block, be sure to start stuff
			// like wars, etc.
			/**
			 * Toucan starts at 0 - Status is ONE Parrot starts at 5 - Status is
			 * TWO Macaw starts at 30 - Status is THREE Eclectus Parrot starts
			 * at 35 - Status is FOUR
			 */

			// Player, not AI
			if (turnOf == 1) {
				String move = JOptionPane.showInputDialog(
						"Action to Perform:\n- /claim - Claim a square\n- /attack - Attack a claimed square\n- /develop <infrastructure, tech, education> - Will boost output\n- /train - Train troops\n- /endmove - End move\n- /concede - Quit");
				if (move.equals("/concede")) {
					concede();
				}
				// Moves that DO NOT require usage of squares WHICH MAKES LIFE
				// SO MUCH EASIER
				if (move.equals("/train")) {
					train();
				}
				if (move.equals("/develop tech")) {
					devTech();
				}
				if (move.equals("/develop education")) {
					devEdu();
				}
				if (move.equals("/develop infrastructure")) {
					devInfrastructure();
				}
				if (move.equals("/endmove")) {
					check();
				}
				if (move.contains("/claim")) {
					char[] findSquare = move.toCharArray();
					char column = findSquare[findSquare.length - 2];
					char row = findSquare[findSquare.length - 1];
					System.out.println(column + " " + row);
					int notChar = Integer.parseInt(row + "");
					int numToCheck = 0;
					// Find numToCheck
					if (column == 'A' || column == 'a') {
						numToCheck = -1 + notChar;
					}
					if (column == 'B' || column == 'b') {
						numToCheck = 5 + notChar;
					}
					if (column == 'C' || column == 'c') {
						numToCheck = 11 + notChar;
					}
					if (column == 'D' || column == 'd') {
						numToCheck = 17 + notChar;
					}
					if (column == 'E' || column == 'e') {
						numToCheck = 23 + notChar;
					}
					if (column == 'F' || column == 'f') {
						numToCheck = 29 + notChar;
					}
					// Claim or check on
					if(moveIsLegal(numToCheck)) {
						if (status[numToCheck] == 0) {
							status[numToCheck] = 1;
							System.out.println("Square claimed successfully");
						} else {
							System.out.println("You can't claim an occupied square, silly!");
						}
					}
					else {
						System.out.println("That square is too far away, silly!");
					}
					check();
				}
				if (move.contains("/attack")) {
					char[] findSquare = move.toCharArray();
					char column = findSquare[findSquare.length - 2];
					char row = findSquare[findSquare.length - 1];
					System.out.println(column + " " + row);
					int notChar = Integer.parseInt(row + "");
					int numToCheck = 0;
					// Find numToCheck
					if (column == 'A' || column == 'a') {
						numToCheck = -1 + notChar;
					}
					if (column == 'B' || column == 'b') {
						numToCheck = 5 + notChar;
					}
					if (column == 'C' || column == 'c') {
						numToCheck = 11 + notChar;
					}
					if (column == 'D' || column == 'd') {
						numToCheck = 17 + notChar;
					}
					if (column == 'E' || column == 'e') {
						numToCheck = 23 + notChar;
					}
					if (column == 'F' || column == 'f') {
						numToCheck = 29 + notChar;
					}
					if (moveIsLegal(numToCheck)) {
						// The move is legal.
						// Is square occupied?
						if (status[numToCheck] == 0 || status[numToCheck] == 1) {
							System.out.println(
									"Error attacking square: No occupying army to attack, or this square belongs to you");
							if (status[numToCheck] != 1) {
								int claimInstead = JOptionPane.showConfirmDialog(null,
										"Do you want to claim this square instead?");
								if (claimInstead == JOptionPane.YES_OPTION) {
									status[numToCheck] = 1;
									System.out.println("Square claimed!");
								} else {
									System.out.println("Square not taken!");
									check();
								}
							} else {
								System.out.println(
										"This square is yours! If you want to end the game, play /concede on your next move");
							}
						} else {
							// Attacking the square! 
							if (status[numToCheck] == 2) {
								int luck = new Random().nextInt(6);
								int actualLuck = luck - 3;
								// Gives options -3 through 3
								int oppoLuck = new Random().nextInt(3);
								// Battle Numbers - Who Wins?
								int attackingForce = actualLuck + onepower;
								int defendingForce = oppoLuck + twopower;
								if (attackingForce > defendingForce) {
									System.out.println("Attackers took square");
									status[numToCheck] = 1;
									onepower = onepower - (onepower - twopower);
									twopower = twopower - (onepower - twopower + 2);
									twooutput = twooutput - 1;
								} else if (attackingForce < defendingForce) {
									System.out.println("Defense remained in control");
									onepower = onepower - (twopower - onepower - 1);
									twopower = twopower - (twopower - onepower + 1);
									oneoutput = oneoutput - 1;
								} else {
									onepower = onepower - 1;
									twopower = twopower - 1;
									oneoutput = oneoutput - 1;
									twooutput = twooutput - 1;
								}
							}
							if (status[numToCheck] == 3) {
								int luck = new Random().nextInt(6);
								int actualLuck = luck - 3;
								// Gives options -3 through 3
								int oppoLuck = new Random().nextInt(2);
								// Battle Numbers - Who Wins?
								int attackingForce = actualLuck + onepower;
								int defendingForce = oppoLuck + threepower;
								if (attackingForce > defendingForce) {
									System.out.println("Attackers took square");
									status[numToCheck] = 1;
									onepower = onepower - (onepower - threepower);
									threepower = threepower - (onepower - threepower + 2);
									threeoutput = threeoutput - 1;
								} else if (attackingForce < defendingForce) {
									System.out.println("Defense remained in control");
									onepower = onepower - (threepower - onepower - 1);
									threepower = threepower - (threepower - onepower + 1);
									oneoutput = oneoutput - 1;
								} else {
									onepower = onepower - 1;
									threepower = threepower - 1;
									oneoutput = oneoutput - 1;
									threeoutput = threeoutput - 1;
								}
							}
							if (status[numToCheck] == 4) {
								int luck = new Random().nextInt(6);
								int actualLuck = luck - 3;
								// Gives options -3 through 3
								int oppoLuck = new Random().nextInt(2);
								// Battle Numbers - Who Wins?
								int attackingForce = actualLuck + onepower;
								int defendingForce = oppoLuck + fourpower;
								if (attackingForce > defendingForce) {
									System.out.println("Attackers took square");
									status[numToCheck] = 1;
									onepower = onepower - (onepower - fourpower);
									fourpower = fourpower - (onepower - fourpower + 2);
									fouroutput = fouroutput - 1;
								} else if (attackingForce < defendingForce) {
									System.out.println("Defense remained in control");
									onepower = onepower - (fourpower - onepower - 1);
									fourpower = fourpower - (fourpower - onepower + 1);
									oneoutput = oneoutput - 1;
								} else {
									onepower = onepower - 1;
									fourpower = fourpower - 1;
									oneoutput = oneoutput - 1;
									fouroutput = fouroutput - 1;
								}
							}
						}
						check();
					}
					else {
						//THE MOVE IS ILLEGAL. ALL ACTION PASSED ONTO THE NEXT PLAYER.
						System.err.println("Invalid move! You do not border this square.");
						check();
					}
				}
				// AI Moves:

			}
			// AI Move: Parrot
			// Parrot will use rotating int - train, claim, dev, train, claim,
			// dev...
			if (turnOf == 2) {
				if (twopattern % 6 == 1 / 6) {
					aiClaim();
				}
				if (twopattern % 6 == 1 / 3) {

				}
				if (twopattern % 6 == 1 / 2) {

				}
				if (twopattern % 6 == 2 / 3) {

				}
				if (twopattern % 6 == 5 / 6) {
					aiClaim();
				}
				if (twopattern % 6 == 1) {

				}
			}

			// AI Move: Macaw
			// Macaw will play aggressively - train, claim, claim, train, dev,
			// train, dev...

			// AI Move: Eclectus Parrot
			// Eclecty will play peacefully - train, dev, dev, claim...

			// Only reason every group starts with train is to ensure that an
			// early assault is not successful

		}
	}

	public void concede() {
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure?");
		if (confirm == JOptionPane.YES_OPTION) {
			System.exit(0);
		} else {
			System.out.println("Aborted game ending!");
			turnOf = 2;
			turns++;
		}
	}

	public void train() {
		onepower++;
		System.out.println("Player trained troops");
		turnOf = 2;
		turns++;
	}

	public void devTech() {
		// Tech is a gamble. You can win big (+5) or waste a turn (0), or turn
		// out somewhere in between.
		int rand = new Random().nextInt(5);
		oneoutput = oneoutput + rand;
		System.out.println("Player gained " + rand + " output");
		turnOf = 2;
		turns++;
	}

	public void devEdu() {
		// Education earns less on gambling but is always positive
		int rand = new Random().nextInt(2);
		oneoutput = oneoutput + rand + 1;
		System.out.println("Player gained " + (rand + 1) + " output");
		turnOf = 2;
		turns++;
	}

	public void devInfrastructure() {
		// Infrastructure is a solid investment but only gains 1
		oneoutput++;
		oneoutput++;
		System.out.println("Player gained 2 output");
		turnOf = 2;
		turns++;
	}

	public void check() {
		System.out.println("Player checks action");
		turnOf = 2;
		turns++;
	}

	public void aiClaim() {
		// Step One: At war? If yes, see 1, if no, see 2
		// 1. Target the enemy
		// 2. Create list of next squares. If at least one is unnocupied, see 3,
		// else, see 4
		// 3. Pick random number. Claim that square.
		// 4. Pick someone to be at war with! See 5
		// 5. Use random number of the enemy's squares to generate target

		// Step One!
		if (turnOf == 2) {

		}
	}

	public int[] determineNeighbors(int square) {
		int[] nums;
		if (square != 0 && square != 1 && square != 2 && square != 3 && square != 4 && square != 5 && square != 6
				&& square != 11 && square != 12 && square != 17 && square != 18 && square != 23 && square != 24
				&& square != 29 && square != 30 && square != 31 && square != 32 && square != 33 && square != 34
				&& square != 35) {
			// Internal squares with all corners
			nums = new int[8];
			nums[0] = square - 7;
			nums[1] = square - 6;
			nums[2] = square - 5;
			nums[3] = square--;
			nums[4] = square++;
			nums[5] = square + 5;
			nums[6] = square + 6;
			nums[7] = square + 7;
			// It's symetrical!
			return nums;
		} else {
			// Checking non-internal squares to see if they are corner or line
			if (square != 0 && square != 5 && square != 30 && square != 35) {
				// Line squares, non-internal - now which side
				if (square > 0 && square < 5) {
					nums = new int[5];
					nums[0] = square--;
					nums[1] = square++;
					nums[2] = square + 5;
					nums[3] = square + 6;
					nums[4] = square + 7;
					return nums;
				} else if (square == 11 || square == 17 || square == 23 || square == 29) {
					nums = new int[5];
					nums[0] = square - 7;
					nums[1] = square - 6;
					nums[2] = square--;
					nums[3] = square + 5;
					nums[4] = square + 6;
					// Yeet - also semisymetrical
					return nums;
				} else if (square == 6 || square == 12 || square == 18 || square == 24) {
					nums = new int[5];
					nums[0] = square - 6;
					nums[1] = square - 5;
					nums[2] = square++;
					nums[3] = square + 6;
					nums[4] = square + 7;
					// Yeet - also semisymetrical
					return nums;
				} else {
					nums = new int[5];
					nums[0] = square--;
					nums[1] = square++;
					nums[2] = square - 5;
					nums[3] = square - 6;
					nums[4] = square - 7;
					return nums;
				}
			} else {
				// Corner squares! Yay!
				if (square == 0) {
					nums = new int[3];
					nums[0] = 1;
					nums[1] = 6;
					nums[2] = 7;
					return nums;
				} else if (square == 5) {
					nums = new int[3];
					nums[0] = 4;
					nums[1] = 10;
					nums[2] = 11;
					return nums;
				} else if (square == 30) {
					nums = new int[3];
					nums[0] = 24;
					nums[1] = 25;
					nums[2] = 31;
					return nums;
				} else {
					nums = new int[3];
					nums[0] = 28;
					nums[1] = 29;
					nums[2] = 34;
					return nums;
				}
			}
		}
	}

	// int i < x.length; so on
	public boolean moveIsLegal(int square) {
		
		
		//    !!!!! DO NOT EDIT THIS CODE! IT HAS BEEN PROVEN TO WORK. !!!!
		
		
		// Find Options for attacking - go through for() loop to determine if
		// the move is legal
		// Go backwards! Rework the unit to find all of the neighbors, then take
		// those and work it out
		int[] validMovesForAttack = determineNeighbors(square);
		for (int i = 0; i < validMovesForAttack.length; i++) {
			if (status[validMovesForAttack[i]] == 1) {
				// Move was legal!!! Attacker came from square that borders it
				return true;
			}
		}
		// The method has been ran through with no success.
		// The move is invalidated
		//Player will check action in the real life
		return false;
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}