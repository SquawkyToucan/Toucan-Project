package Compute;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Toucans implements MouseListener, ActionListener {
	// Toucans is a strategy game where the goal is to win
	// You can win through DOMINATION (35 tiles), TECH (35 points), ECONOMY
	// (350,000 K GDP (sounds hard, but with 20 squares it won't be)
	// Domination Victory: Every tile on the board is conquered. Enemies are
	// vanquished.
	// Economical Victory: A combined sum of trade (per turn), subsidies to
	// other tribes (per turn), production per
	// turn, and GDP over 35K. On hard, if you near this all other countries
	// will sanction you, meaning inner country
	// must control majority of resources
	// You can die through being CONQUERED or CONCEDING (lit lit lit lit lit)
	// Death through Conquered: You are taken over by another tribe.
	// Conceding/giving up - This is the command '/concede'
	boolean toucanIsAlive = true;
	boolean parrotIsAlive = true;
	boolean macawIsAlive = true;
	boolean dodoIsAlive = true;
	JButton[] buttons = new JButton[36];
	int turnOf = -1;
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
	 * 1 - Toucan, Parrot 2 - Toucan, Macaw 3 - Toucan, Dodo 4 - Parrot, Macaw 5
	 * - Macaw, Dodo 6 - Parrot, Dodo
	 * 
	 */

	// Most likely, wars will mostly involve macaw and toucan
	// This is because one is the "I'm great!" player and the other is agreesive
	// AI
	// This hopefully explains the odd ordering of wars

	// There are currently no wars happening.
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

	JFrame frame = new JFrame();

	public void createBoard() {
		int locx = 0;
		int locy = 0;

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
			buttons[i].setOpaque(true);
			buttons[i].addMouseListener(this);
			buttons[i].setBorder(BorderFactory.createLineBorder(new Color(0, 153, 87), 2));
			locx++;
			if (locx == 6) {
				locy++;
				locx = 0;
			}
			playpanel.add(buttons[i]);
		}
		// frame.pack();
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

	public void playGame() {
		// Blitz through the following: use Object to get source of block,
		// then after Object do is the thing to do
		// Take object do and do it to object block, be sure to start stuff
		// like wars, etc.
		/**
		 * Toucan starts at 0 - Status is ONE Parrot starts at 5 - Status is TWO
		 * Macaw starts at 30 - Status is THREE Dodo starts at 35 - Status is
		 * FOUR
		 */

		// Player, not AI
		if (turnOf == 1 && toucanIsAlive) {
			loadImages();
			updateGraphics();
			String move = JOptionPane.showInputDialog(
					"Action to Perform:\n- /claim <row (letter)><column (number)> - Claim a square\n- /attack <row (letter)><column (number)> - Attack a claimed square\n- /develop <infrastructure, tech, education> - Will boost output\n- /train - Train troops\n- /endmove - End move\n- /help - See the rules and instructions\n- /concede - Quit");
			if (move.equals("/concede")) {
				concede();
			}
			if(move.equals("/help")) {
				new Instrucs();
			}
			// Moves that DO NOT require usage of squares WHICH MAKES LIFE
			// SO MUCH EASIER
			if (move.equals(null)) {
				System.out.println("Player pressed cancel. Quitting now...");
				System.exit(0);
			}
			if (move.equals("/train")) {
				train();
				check();
			}
			if (move.equals("/develop tech")) {
				devTech();
				check();
			}
			if (move.equals("/develop education")) {
				devEdu();
				check();
			}
			if (move.equals("/develop infrastructure")) {
				devInfrastructure();
				check();
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
				if(move.equalsIgnoreCase("/claim C6") && status[16] == 1) {
					//Exceptions for things that are throwing errors
					status[17] = 1;
				}
				else if (moveIsLegal(numToCheck, 1)) {
					if (status[numToCheck] == 0) {
						status[numToCheck] = 1;
						System.out.println("Square claimed successfully");
						check();
					} else {
						System.out.println("You can't claim an occupied square, silly!");
						check();
					}
				} else {
					System.out.println("That square is too far away, silly!");
					check();
				}
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
				if (moveIsLegal(numToCheck, 1)) {
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
							warOne = true;
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
							warTwo = true;
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
							warThree = true;
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
				} else {
					// THE MOVE IS ILLEGAL. ALL ACTION PASSED ONTO THE NEXT
					// PLAYER.
					System.err.println("Invalid move! You do not border this square.");
					check();
				}
			}
			// AI Moves:
		}
		// AI Move: Parrot
		// Parrot will use rotating int - claim, train, dev, dev, train, claim,
		// dev...
		else if (turnOf == 2&&parrotIsAlive) {
			loadImages();
			updateGraphics();
			if (twopattern % 6 == 1) {
				aiClaim();
				System.out.println("Parrot claimed a square");
				check();
			}
			if (twopattern % 6 == 2) {
				// Add in armies (random from 0 to 2);
				int armiesProduced = new Random().nextInt(3);
				twopower = twopower + armiesProduced;
				System.out.println("Parrot trained " + armiesProduced + " units.");
				check();
			}
			if (twopattern % 6 == 3) {
				// Develop infrastructure for Parrot
				int developmentAdded = (new Random().nextInt(4) + 1) * getNumberOfSquares(2);
				twooutput = twooutput + developmentAdded;
				System.out.println(
						"Parrot added " + (developmentAdded) + " development in infrastructure and the economy.");
				check();
			}
			if (twopattern % 6 == 4) {
				// Develop infrastructure for Parrot
				int developmentAdded = new Random().nextInt(4) + 1;
				twooutput = twooutput + developmentAdded * getNumberOfSquares(2);
				System.out.println(
						"Parrot added " + (developmentAdded * getNumberOfSquares(2)) + " development in infrastructure and the economy.");
				check();
			}
			if (twopattern % 6 == 5) {
				// Train some units for Parrot
				int armiesProduced = new Random().nextInt(3);
				twopower = twopower + armiesProduced;
				System.out.println("Parrot trained " + armiesProduced + " units.");
				check();
			}
			if (twopattern % 6 == 0) {
				aiClaim();
				System.out.println("Parrot claimed a square");
				check();
			}
			twopattern++;
		}
		// AI Move: Macaw
		// Macaw will play aggressively - train, claim, claim, train, dev,
		// train, dev... (7)
		else if (turnOf == 3&&macawIsAlive) {
			loadImages();
			updateGraphics();
			if (threepattern % 7 == 1) {
				// Train troops for Macaw
				int armiesProduced = new Random().nextInt(2) + 1;
				threepower = threepower + armiesProduced;
				System.out.println("Macaw trained " + armiesProduced + " troops");
				check();
			}
			if (threepattern % 7 == 2) {
				// Claim a square for Macaw
				aiClaim();
				check();
			}
			if (threepattern % 7 == 3) {
				// Claim a square for Macaw
				aiClaim();
				check();
			}
			if (threepattern % 7 == 4) {
				// Train troops for Macaw
				int armiesProduced = new Random().nextInt(2) + 1;
				threepower = threepower + armiesProduced;
				System.out.println("Macaw trained " + armiesProduced + " troops");
				check();
			}
			if (threepattern % 7 == 5) {
				// Develop Infrastructure for Macaw
				int infrastructureDeveloped = (new Random().nextInt(3) + 1) * getNumberOfSquares(3);
				threeoutput = threeoutput + infrastructureDeveloped;
				System.out.println("Macaw gained " + infrastructureDeveloped
						+ "K in economic boosts and infrastructure development.");
				check();
			}
			if (threepattern % 7 == 6) {
				// Train troops for Macaw
				int armiesProduced = new Random().nextInt(2) + 1;
				threepower = threepower + armiesProduced;
				System.out.println("Macaw trained " + armiesProduced + " troops");
				check();
			}
			if (threepattern % 7 == 0) {
				// Develop Infrastructure for Macaw
				int infrastructureDeveloped = (new Random().nextInt(3) + 1) * getNumberOfSquares(3);
				threeoutput = threeoutput + infrastructureDeveloped;
				System.out.println("Macaw gained " + infrastructureDeveloped
						+ "K in economic boosts and infrastructure development.");
				check();
			}
			threepattern++;
		}
		// AI Move: Dodo
		// Dodo will play peacefully - train, dev, dev, claim... (4)
		else if (turnOf == 4&&dodoIsAlive) {
			loadImages();
			updateGraphics();
			if (fourpattern % 4 == 1) {
				int armiesProduced = new Random().nextInt(2) + 1;
				fourpower = fourpower + armiesProduced;
				System.out.println("Dodo trained " + armiesProduced + " troops");
				check();
			}
			if (fourpattern % 4 == 2) {
				int infrastructureDeveloped = (new Random().nextInt(3) + 1) * getNumberOfSquares(4);
				fouroutput = fouroutput + infrastructureDeveloped;
				System.out.println("Dodo gained " + infrastructureDeveloped
						+ "K in economic boosts and infrastructure development.");
				check();
			}
			if (fourpattern % 4 == 3) {
				int infrastructureDeveloped = (new Random().nextInt(3) + 1) * getNumberOfSquares(4);
				fouroutput = fouroutput + infrastructureDeveloped;
				System.out.println("Dodo gained " + infrastructureDeveloped
						+ "K in economic boosts and infrastructure development.");
				check();
			}
			if (fourpattern % 4 == 0) {
				aiClaim();
				check();
			}
			fourpattern++;
		}
		// Only reason every group starts with train is to ensure that an
		// early assault is not successful

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
			playGame();
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
		int armiesTrained = new Random().nextInt(2) + 1;
		onepower = onepower + armiesTrained;
		System.out.println("Player trained troops");
	}

	public void devTech() {
		// Tech is a gamble. You can win big (+5) or waste a turn (0), or turn
		// out somewhere in between.
		int rand = new Random().nextInt(5);
		int add = rand * getNumberOfSquares(1);
		oneoutput = oneoutput + add;
		System.out.println("Player gained " + add + " output");
	}

	public void devEdu() {
		// Education earns less on gambling but is always positive
		int rand = new Random().nextInt(2) + 1;
		int add = rand * getNumberOfSquares(1);
		oneoutput = oneoutput + add;
		System.out.println("Player gained " + add + " output");
	}

	public void devInfrastructure() {
		// Infrastructure is a solid investment but only gains 1
		oneoutput = oneoutput * getNumberOfSquares(1);
		System.out.println("Player gained " + (2*getNumberOfSquares(1)) + " output");
	}

	public void check() {
		updateLife();
		checkForWinner();
		if (turnOf == 1) {
			// The player is checking the action
			System.out.println("Player checks action");
		} else {
			// An AI player is checking the action
			if (turnOf == 2) {
				// Parrot checking!
				System.out.println("Parrot checks action");
			} else if (turnOf == 3) {
				// Macaw is checking
				System.out.println("Macaw checks action");
			} else if (turnOf == 4) {
				// Dodo is checking the action
				System.out.println("Dodo checks action");
			}
		}
		if (turnOf == 1) {
			// The turn can just be given one here - examples: 1 to 2, 2 to 3, 3
			// to 4
			if(parrotIsAlive) {
				turnOf = 2;
			}
			else if(macawIsAlive) {
				turnOf = 3;
			}
			else {
				turnOf = 4;
			}
		} else if(turnOf == 2) {
			if(macawIsAlive) {
				turnOf = 3;
			}
			else if(dodoIsAlive) {
				turnOf = 4;
			}
			else {
				turnOf = 1;
			}
		} else if (turnOf == 3) {
			if(dodoIsAlive) {
				turnOf = 4;
			}
			else if(toucanIsAlive) {
				turnOf = 1;
			}
			else {
				turnOf = 2;
			}
		} else if (turnOf == 4) {
			// This is crucial. Without it, the game would transition from 4 to
			// 5 instead of 4 to 1, and because it could never call the method
			// the game would be over
			if(toucanIsAlive) {
				turnOf = 1;
			}
			else if(parrotIsAlive) {
				turnOf = 2;
			}
			else {
				turnOf = 3;
			}
		} else {
			// This is just for grammar purposes, but I added an Easter Egg just
			// for fun. It shouldn't ever be called.
			System.err.println("This program has encountered the following error: the logic operator has failed.");
		}
		turns++;
		System.out.println("\n\nAI playing...");
		// Backlash n FOUR TIMES on next console action
	}

	ImageIcon toucanImage;
	ImageIcon unclaimedImage;
	ImageIcon parrotImage;
	ImageIcon macawImage;
	ImageIcon dodoImage;

	public void loadImages() {
		try {
			Image toucanImg = ImageIO.read(getClass().getResource("Toucan.png"));
			Image parrotImg = ImageIO.read(getClass().getResource("Parrot.png"));
			Image macawImg = ImageIO.read(getClass().getResource("Macaw.png"));
			Image dodoImg = ImageIO.read(getClass().getResource("Dodo.png"));
			Image unclaimedImg = ImageIO.read(getClass().getResource("Unclaimed.png"));
			toucanImage = new ImageIcon(toucanImg);
			unclaimedImage = new ImageIcon(unclaimedImg);
			parrotImage = new ImageIcon(parrotImg);
			macawImage = new ImageIcon(macawImg);
			dodoImage = new ImageIcon(dodoImg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateGraphics() {
		for (int i = 0; i < 36; i++) {
			if (status[i] == 0) {
				buttons[i].setIcon(unclaimedImage);
			} else if (status[i] == 1) {
				buttons[i].setIcon(toucanImage);
			} else if (status[i] == 2) {
				buttons[i].setIcon(parrotImage);
			} else if (status[i] == 3) {
				buttons[i].setIcon(macawImage);
			} else if (status[i] == 4) {
				buttons[i].setIcon(dodoImage);
			} else {
				// This should never be called.
			}
			buttons[i].repaint();
		}

	}

	@SuppressWarnings("unchecked")
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
			if (warOne) {
				int squareToAttack;
				@SuppressWarnings("rawtypes")
				List squaresToClaim = new ArrayList();
				// We aren't at war with anyone.
				// Try to stay peaceful by only targeting unoccupied
				// squares.
				// (This move could be used for the purpose of finding a
				// square to attack with the exception that you'd find
				// a square that belonged to the enemy)
				for (int i = 0; i < 36; i++) {
					// Go through each square. If the value is zero, check
					// if it
					// has a neighbor of PARROT, and add it to the list.
					if (status[i] == 1) {
						// Square I is unoccupied. Now we'll check if it has
						// a
						// neighbor that is Parrot to see if the move is
						// legal.
						if (moveIsLegal(i, 2)) {
							// The move is legal, so it has two as a
							// neighbor.
							// Add it to the list of possible claims.
							squaresToClaim.add(i);
						} else {
							// The move is illegal, so this cannot be added
							// to
							// the posibilities.
							// Go through another iteration.
						}
					} else {
						// This square is occupied.
						// Go through another iteration.
					}
				}
				// The loop is over! The resulting list will be checked
				// through
				// to find a target.
				if (squaresToClaim.size() != 1) {
					// The size is one, so we can just claim that number.
					squareToAttack = (int) squaresToClaim.get(0);
				} else {
					// Pick a random square.
					int squareToClaim = new Random().nextInt(squaresToClaim.size());
					squareToAttack = (int) squaresToClaim.get(squareToClaim);
				}

				// We are at war with TOUCAN.
				// If our army is stronger than theirs, attack!
				if (twopower > onepower) {
					int luck = new Random().nextInt(6);
					int actualLuck = luck - 3;
					int oppoLuck = new Random().nextInt(2);
					// Gives options -3 through 3
					// Battle Numbers - Who Wins?
					int attackingForce = actualLuck + twopower;
					int defendingForce = oppoLuck + onepower;
					if (attackingForce > defendingForce) {
						System.out.println("Attackers took square");
						status[squareToAttack] = 2;
						twopower = twopower - (twopower - threepower);
						threepower = onepower - (twopower - onepower + 2);
						oneoutput = oneoutput - 1;
					} else if (attackingForce < defendingForce) {
						System.out.println("Defense remained in control");
						twopower = twopower - (onepower - twopower - 1);
						onepower = onepower - (onepower - twopower + 1);
						twooutput = twooutput - 1;
					} else {
						twopower = twopower - 1;
						onepower = onepower - 1;
						twooutput = twooutput - 1;
						oneoutput = oneoutput - 1;
					}
				}
				else {
					//We don't have more power. Traditionally, we'd claim another square.
					//However, because we're at war, we'll train troops.
					int armiesProduced = new Random().nextInt(3);
					twopower = twopower + armiesProduced;
					System.out.println("Parrot trained " + armiesProduced + " units.");
				}
			} else if (warFour) {
				// We are at war with MACAW.
				int squareToAttack;
				@SuppressWarnings("rawtypes")
				List squaresToClaim = new ArrayList();
				// We aren't at war with anyone.
				// Try to stay peaceful by only targeting unoccupied
				// squares.
				// (This move could be used for the purpose of finding a
				// square to attack with the exception that you'd find
				// a square that belonged to the enemy)
				for (int i = 0; i < 36; i++) {
					// Go through each square. If the value is zero, check
					// if it
					// has a neighbor of PARROT, and add it to the list.
					if (status[i] == 4) {
						// Square I is unoccupied. Now we'll check if it has
						// a
						// neighbor that is Parrot to see if the move is
						// legal.
						if (moveIsLegal(i, 2)) {
							// The move is legal, so it has two as a
							// neighbor.
							// Add it to the list of possible claims.
							squaresToClaim.add(i);
						} else {
							// The move is illegal, so this cannot be added
							// to
							// the posibilities.
							// Go through another iteration.
						}
					} else {
						// This square is occupied.
						// Go through another iteration.
					}
				}
				// The loop is over! The resulting list will be checked
				// through
				// to find a target.
				if (squaresToClaim.size() != 1) {
					// The size is one, so we can just claim that number.
					squareToAttack = (int) squaresToClaim.get(0);
				} else {
					// Pick a random square.
					int squareToClaim = new Random().nextInt(squaresToClaim.size());
					squareToAttack = (int) squaresToClaim.get(squareToClaim);
				}

				// We are at war with TOUCAN.
				// If our army is stronger than theirs, attack!
				if (twopower > threepower) {
					int luck = new Random().nextInt(6);
					int actualLuck = luck - 3;
					int oppoLuck = new Random().nextInt(2);
					// Gives options -3 through 3
					// Battle Numbers - Who Wins?
					int attackingForce = actualLuck + twopower;
					int defendingForce = oppoLuck + threepower;
					if (attackingForce > defendingForce) {
						System.out.println("Attackers took square");
						status[squareToAttack] = 2;
						twopower = twopower - (twopower - threepower);
						threepower = threepower - (twopower - threepower + 2);
						threeoutput = threeoutput - 1;
					} else if (attackingForce < defendingForce) {
						System.out.println("Defense remained in control");
						twopower = twopower - (threepower - twopower - 1);
						threepower = threepower - (threepower - twopower + 1);
						twooutput = twooutput - 1;
					} else {
						twopower = twopower - 1;
						threepower = threepower - 1;
						twooutput = twooutput - 1;
						threeoutput = threeoutput - 1;
					}
				}
				else {
					//We don't have more power. Traditionally, we'd claim another square.
					//However, because we're at war, we'll train troops.
					int armiesProduced = new Random().nextInt(3);
					twopower = twopower + armiesProduced;
					System.out.println("Parrot trained " + armiesProduced + " units.");
				}
			} else if (warSix) {
				// We are at war with DODO.
				// If our army is stronger than theirs, attack!
				if (twopower > fourpower) {
					// We have more power than them.
					// This makes attacking a better move and we should do so.

					// This is the attacking method - it needs a square
					// desperately.
					int squareToAttack;
					@SuppressWarnings("rawtypes")
					List squaresToClaim = new ArrayList();
					// We aren't at war with anyone.
					// Try to stay peaceful by only targeting unoccupied
					// squares.
					// (This move could be used for the purpose of finding a
					// square to attack with the exception that you'd find
					// a square that belonged to the enemy)
					for (int i = 0; i < 36; i++) {
						// Go through each square. If the value is zero, check
						// if it
						// has a neighbor of PARROT, and add it to the list.
						if (status[i] == 4) {
							// Square I is unoccupied. Now we'll check if it has
							// a
							// neighbor that is Parrot to see if the move is
							// legal.
							if (moveIsLegal(i, 2)) {
								// The move is legal, so it has two as a
								// neighbor.
								// Add it to the list of possible claims.
								squaresToClaim.add(i);
							} else {
								// The move is illegal, so this cannot be added
								// to
								// the posibilities.
								// Go through another iteration.
							}
						} else {
							// This square is occupied.
							// Go through another iteration.
						}
					}
					// The loop is over! The resulting list will be checked
					// through
					// to find a target.
					if (squaresToClaim.size() != 1) {
						// The size is one, so we can just claim that number.
						squareToAttack = (int) squaresToClaim.get(0);
					} else {
						// Pick a random square.
						int squareToClaim = new Random().nextInt(squaresToClaim.size());
						squareToAttack = (int) squaresToClaim.get(squareToClaim);
					}

					int luck = new Random().nextInt(6);
					int actualLuck = luck - 3;
					int oppoLuck = new Random().nextInt(2);
					// Gives options -3 through 3
					// Battle Numbers - Who Wins?
					int attackingForce = actualLuck + twopower;
					int defendingForce = oppoLuck + fourpower;
					if (attackingForce > defendingForce) {
						System.out.println("Attackers took square");
						status[squareToAttack] = 2;
						twopower = twopower - (twopower - fourpower);
						fourpower = fourpower - (twopower - fourpower + 2);
						fouroutput = fouroutput - 1;
					} else if (attackingForce < defendingForce) {
						System.out.println("Defense remained in control");
						twopower = twopower - (fourpower - twopower - 1);
						fourpower = fourpower - (fourpower - twopower + 1);
						oneoutput = oneoutput - 1;
					} else {
						twopower = twopower - 1;
						fourpower = fourpower - 1;
						twooutput = twooutput - 1;
						fouroutput = fouroutput - 1;
					}
				} else {
					// Traditionally, we'd claim a different square, but because
					// we're at wartime we should train troops.
					// Add in armies (random from 0 to 2);
					int armiesProduced = new Random().nextInt(3);
					twopower = twopower + armiesProduced;
					System.out.println("Parrot trained " + armiesProduced + " units.");
				}
			} else {
				@SuppressWarnings("rawtypes")
				List squaresToClaim = new ArrayList();
				// We aren't at war with anyone.
				// Try to stay peaceful by only targeting unoccupied squares.
				// (This move could be used for the purpose of finding a square
				// to attack with the exception that you'd find
				// a square that belonged to the enemy)
				for (int i = 0; i < 36; i++) {
					// Go through each square. If the value is zero, check if it
					// has a neighbor of PARROT, and add it to the list.
					if (status[i] == 0) {
						// Square I is unoccupied. Now we'll check if it has a
						// neighbor that is Parrot to see if the move is legal.
						if (moveIsLegal(i, 2)) {
							// The move is legal, so it has two as a neighbor.
							// Add it to the list of possible claims.
							squaresToClaim.add(i);
						} else {
							// The move is illegal, so this cannot be added to
							// the posibilities.
							// Go through another iteration.
						}
					} else {
						// This square is occupied.
						// Go through another iteration.
					}
				}
				// The loop is over! The resulting list will be checked through
				// to find a target.
				if(squaresToClaim.size() == 0) {
					//Pick a square next to me and go with it.
					for(int i = 0; i < 36; i++) {
						if(moveIsLegal(i, 2)) {
							if(status[i] == 1) {
								warOne = true;
								System.out.println("Parrot declared war on Toucan for infringing on its sovereign territory.");
								break;
							}
							else if(status[i] == 3) {
								warFour = true;
								System.out.println("Parrot declared war on Macaw for infringing on its sovereign territory.");
								break;
							}
							else if(status[i] == 4) {
								warSix = true;
								System.out.println("Parrot declared war on Dodo for infringing on its sovereign territory.");
								break;
							}
						}
					}
				}
				else if (squaresToClaim.size() != 1) {
					// The size is one, so we can just claim that number.
					status[(int) squaresToClaim.get(0)] = 2;
				} else {
					// Pick a random square.
					int squareToClaim = new Random().nextInt(squaresToClaim.size());
					status[(int) squaresToClaim.get(squareToClaim)] = 2;
				}
				// A square has been claimed. Hooray!
			}
		} else if (turnOf == 3) {
			if(warTwo) {
				//We are at war with TOUCAN.
				int squareToAttack;
				@SuppressWarnings("rawtypes")
				List squaresToClaim = new ArrayList();
				// We aren't at war with anyone.
				// Try to stay peaceful by only targeting unoccupied
				// squares.
				// (This move could be used for the purpose of finding a
				// square to attack with the exception that you'd find
				// a square that belonged to the enemy)
				for (int i = 0; i < 36; i++) {
					// Go through each square. If the value is zero, check
					// if it
					// has a neighbor of PARROT, and add it to the list.
					if (status[i] == 1) {
						// Square I is unoccupied. Now we'll check if it has
						// a
						// neighbor that is Parrot to see if the move is
						// legal.
						if (moveIsLegal(i, 3)) {
							// The move is legal, so it has two as a
							// neighbor.
							// Add it to the list of possible claims.
							squaresToClaim.add(i);
						} else {
							// The move is illegal, so this cannot be added
							// to
							// the posibilities.
							// Go through another iteration.
						}
					} else {
						// This square is occupied.
						// Go through another iteration.
					}
				}
				// The loop is over! The resulting list will be checked
				// through
				// to find a target.
				if (squaresToClaim.size() != 1) {
					// The size is one, so we can just claim that number.
					squareToAttack = (int) squaresToClaim.get(0);
				} else {
					// Pick a random square.
					int squareToClaim = new Random().nextInt(squaresToClaim.size());
					squareToAttack = (int) squaresToClaim.get(squareToClaim);
				}

				// We are at war with TOUCAN.
				// If our army is stronger than theirs, attack!
				if (threepower > onepower) {
					int luck = new Random().nextInt(6);
					int actualLuck = luck - 3;
					int oppoLuck = new Random().nextInt(2);
					// Gives options -3 through 3
					// Battle Numbers - Who Wins?
					int attackingForce = actualLuck + threepower;
					int defendingForce = oppoLuck + onepower;
					if (attackingForce > defendingForce) {
						System.out.println("Attackers took square");
						status[squareToAttack] = 3;
						threepower = threepower - (threepower - onepower);
						onepower = onepower - (threepower - onepower + 2);
						oneoutput = oneoutput - 1;
					} else if (attackingForce < defendingForce) {
						System.out.println("Defense remained in control");
						threepower = threepower - (threepower - onepower - 1);
						onepower = onepower - (onepower - threepower + 1);
						threeoutput = threeoutput - 1;
					} else {
						threepower = threepower - 1;
						onepower = onepower - 1;
						threeoutput = threeoutput - 1;
						oneoutput = oneoutput - 1;
					}
				}
				else {
					//We don't have more power. Traditionally, we'd claim another square.
					//However, because we're at war, we'll train troops.
					int armiesProduced = new Random().nextInt(3);
					threepower = threepower + armiesProduced;
					System.out.println("Macaw trained " + armiesProduced + " units.");
				}
			}
			else if(warFour) {
				//We are at war with TOUCAN.
				int squareToAttack;
				@SuppressWarnings("rawtypes")
				List squaresToClaim = new ArrayList();
				// We aren't at war with anyone.
				// Try to stay peaceful by only targeting unoccupied
				// squares.
				// (This move could be used for the purpose of finding a
				// square to attack with the exception that you'd find
				// a square that belonged to the enemy)
				for (int i = 0; i < 36; i++) {
					// Go through each square. If the value is zero, check
					// if it
					// has a neighbor of PARROT, and add it to the list.
					if (status[i] == 2) {
						// Square I is unoccupied. Now we'll check if it has
						// a
						// neighbor that is Parrot to see if the move is
						// legal.
						if (moveIsLegal(i, 3)) {
							// The move is legal, so it has two as a
							// neighbor.
							// Add it to the list of possible claims.
							squaresToClaim.add(i);
						} else {
							// The move is illegal, so this cannot be added
							// to
							// the posibilities.
							// Go through another iteration.
						}
					} else {
						// This square is occupied.
						// Go through another iteration.
					}
				}
				// The loop is over! The resulting list will be checked
				// through
				// to find a target.
				if (squaresToClaim.size() != 1) {
					// The size is one, so we can just claim that number.
					squareToAttack = (int) squaresToClaim.get(0);
				} else {
					// Pick a random square.
					int squareToClaim = new Random().nextInt(squaresToClaim.size());
					squareToAttack = (int) squaresToClaim.get(squareToClaim);
				}

				// We are at war with TOUCAN.
				// If our army is stronger than theirs, attack!
				if (threepower > onepower) {
					int luck = new Random().nextInt(6);
					int actualLuck = luck - 3;
					int oppoLuck = new Random().nextInt(2);
					// Gives options -3 through 3
					// Battle Numbers - Who Wins?
					int attackingForce = actualLuck + threepower;
					int defendingForce = oppoLuck + twopower;
					if (attackingForce > defendingForce) {
						System.out.println("Attackers took square");
						status[squareToAttack] = 3;
						threepower = threepower - (threepower - twopower);
						twopower = twopower - (threepower - twopower + 2);
						twooutput = twooutput - 1;
					} else if (attackingForce < defendingForce) {
						System.out.println("Defense remained in control");
						threepower = threepower - (threepower - twopower - 1);
						twopower = twopower - (twopower - threepower + 1);
						threeoutput = threeoutput - 1;
					} else {
						threepower = threepower - 1;
						twopower = twopower - 1;
						threeoutput = threeoutput - 1;
						twooutput = twooutput - 1;
					}
				}
				else {
					//We don't have more power. Traditionally, we'd claim another square.
					//However, because we're at war, we'll train troops.
					int armiesProduced = new Random().nextInt(3);
					threepower = threepower + armiesProduced;
					System.out.println("Macaw trained " + armiesProduced + " units.");
				}
			}
			else if(warFive) {
				//We are at war with DODO.
				//We are at war with TOUCAN.
				int squareToAttack;
				@SuppressWarnings("rawtypes")
				List squaresToClaim = new ArrayList();
				// We aren't at war with anyone.
				// Try to stay peaceful by only targeting unoccupied
				// squares.
				// (This move could be used for the purpose of finding a
				// square to attack with the exception that you'd find
				// a square that belonged to the enemy)
				for (int i = 0; i < 36; i++) {
					// Go through each square. If the value is zero, check
					// if it
					// has a neighbor of PARROT, and add it to the list.
					if (status[i] == 4) {
						// Square I is unoccupied. Now we'll check if it has
						// a
						// neighbor that is Parrot to see if the move is
						// legal.
						if (moveIsLegal(i, 3)) {
							// The move is legal, so it has two as a
							// neighbor.
							// Add it to the list of possible claims.
							squaresToClaim.add(i);
						} else {
							// The move is illegal, so this cannot be added
							// to
							// the posibilities.
							// Go through another iteration.
						}
					} else {
						// This square is occupied.
						// Go through another iteration.
					}
				}
				// The loop is over! The resulting list will be checked
				// through
				// to find a target.
				if (squaresToClaim.size() != 1) {
					// The size is one, so we can just claim that number.
					squareToAttack = (int) squaresToClaim.get(0);
				} else {
					// Pick a random square.
					int squareToClaim = new Random().nextInt(squaresToClaim.size());
					squareToAttack = (int) squaresToClaim.get(squareToClaim);
				}

				// We are at war with TOUCAN.
				// If our army is stronger than theirs, attack!
				if (threepower > fourpower) {
					int luck = new Random().nextInt(6);
					int actualLuck = luck - 3;
					int oppoLuck = new Random().nextInt(2);
					// Gives options -3 through 3
					// Battle Numbers - Who Wins?
					int attackingForce = actualLuck + threepower;
					int defendingForce = oppoLuck + fourpower;
					if (attackingForce > defendingForce) {
						System.out.println("Attackers took square");
						status[squareToAttack] = 3;
						threepower = threepower - (threepower - fourpower);
						fourpower = fourpower - (threepower - fourpower + 2);
						fouroutput = fouroutput - 1;
					} else if (attackingForce < defendingForce) {
						System.out.println("Defense remained in control");
						threepower = threepower - (threepower - fourpower - 1);
						fourpower = fourpower - (fourpower - threepower + 1);
						threeoutput = threeoutput - 1;
					} else {
						threepower = threepower - 1;
						fourpower = fourpower - 1;
						threeoutput = threeoutput - 1;
						fouroutput = fouroutput - 1;
					}
				}
				else {
					//We don't have more power. Traditionally, we'd claim another square.
					//However, because we're at war, we'll train troops.
					int armiesProduced = new Random().nextInt(3);
					threepower = threepower + armiesProduced;
					System.out.println("Macaw trained " + armiesProduced + " units.");
				}
			}
			else {
				@SuppressWarnings("rawtypes")
				List squaresToClaim = new ArrayList();
				// We aren't at war with anyone.
				// Try to stay peaceful by only targeting unoccupied squares.
				// (This move could be used for the purpose of finding a square
				// to attack with the exception that you'd find
				// a square that belonged to the enemy)
				for (int i = 0; i < 36; i++) {
					// Go through each square. If the value is zero, check if it
					// has a neighbor of PARROT, and add it to the list.
					if (status[i] == 0) {
						// Square I is unoccupied. Now we'll check if it has a
						// neighbor that is Parrot to see if the move is legal.
						if (moveIsLegal(i, 3)) {
							// The move is legal, so it has two as a neighbor.
							// Add it to the list of possible claims.
							squaresToClaim.add(i);
						} else {
							// The move is illegal, so this cannot be added to
							// the posibilities.
							// Go through another iteration.
						}
					} else {
						// This square is occupied.
						// Go through another iteration.
					}
				}
				// The loop is over! The resulting list will be checked through
				// to find a target.
				if (squaresToClaim.size() == 0) {
					//Declare war on the first tribe.
					for(int i = 0; i < 36; i++) {
						if(moveIsLegal(i, 3)) {
							if(status[i] == 1) {
								warTwo = true;
								System.out.println("Macaw has decided to take up arms to defend their sovereign territory from Toucan.");
								break;
							}
							else if(status[i] == 2) {
								warFour = true;
								System.out.println("Macaw has decided to take up arms to defend their sovereign territory from Parrot.");
								break;
							}
							else if(status[i] == 4) {
								warFive = true;
								System.out.println("Macaw has decided to take up arms to defend their sovereign territory from Dodo");
								break;
							}
						}
					}
				}
				if (squaresToClaim.size() != 1) {
					// The size is one, so we can just claim that number.
					status[(int) squaresToClaim.get(0)] = 3;
				} else {
					// Pick a random square.
					int squareToClaim = new Random().nextInt(squaresToClaim.size());
					status[(int) squaresToClaim.get(squareToClaim)] = 3;
				}
				// A square has been claimed. Hooray!
			}
		} else if (turnOf == 4) {
			if(warThree) {
				//We are at war with TOUCAN.
				int squareToAttack;
				@SuppressWarnings("rawtypes")
				List squaresToClaim = new ArrayList();
				// We aren't at war with anyone.
				// Try to stay peaceful by only targeting unoccupied
				// squares.
				// (This move could be used for the purpose of finding a
				// square to attack with the exception that you'd find
				// a square that belonged to the enemy)
				for (int i = 0; i < 36; i++) {
					// Go through each square. If the value is zero, check
					// if it
					// has a neighbor of PARROT, and add it to the list.
					if (status[i] == 1) {
						// Square I is unoccupied. Now we'll check if it has
						// a
						// neighbor that is Parrot to see if the move is
						// legal.
						if (moveIsLegal(i, 4)) {
							// The move is legal, so it has two as a
							// neighbor.
							// Add it to the list of possible claims.
							squaresToClaim.add(i);
						} else {
							// The move is illegal, so this cannot be added
							// to
							// the posibilities.
							// Go through another iteration.
						}
					} else {
						// This square is occupied.
						// Go through another iteration.
					}
				}
				// The loop is over! The resulting list will be checked
				// through
				// to find a target.
				if (squaresToClaim.size() != 1) {
					// The size is one, so we can just claim that number.
					squareToAttack = (int) squaresToClaim.get(0);
				} else {
					// Pick a random square.
					int squareToClaim = new Random().nextInt(squaresToClaim.size());
					squareToAttack = (int) squaresToClaim.get(squareToClaim);
				}

				// We are at war with TOUCAN.
				// If our army is stronger than theirs, attack!
				if (fourpower > onepower) {
					int luck = new Random().nextInt(6);
					int actualLuck = luck - 3;
					int oppoLuck = new Random().nextInt(2);
					// Gives options -3 through 3
					// Battle Numbers - Who Wins?
					int attackingForce = actualLuck + fourpower;
					int defendingForce = oppoLuck + onepower;
					if (attackingForce > defendingForce) {
						System.out.println("Attackers took square");
						status[squareToAttack] = 4;
						fourpower = fourpower - (fourpower - onepower);
						onepower = onepower - (fourpower - onepower + 2);
						oneoutput = oneoutput - 1;
					} else if (attackingForce < defendingForce) {
						System.out.println("Defense remained in control");
						fourpower = fourpower - (fourpower - onepower - 1);
						onepower = onepower - (onepower - fourpower + 1);
						fouroutput = fouroutput - 1;
					} else {
						fourpower = fourpower - 1;
						onepower = onepower - 1;
						fouroutput = fouroutput - 1;
						oneoutput = oneoutput - 1;
					}
				}
				else {
					//We don't have more power. Traditionally, we'd claim another square.
					//However, because we're at war, we'll train troops.
					int armiesProduced = new Random().nextInt(3);
					fourpower = fourpower + armiesProduced;
					System.out.println("Dodo trained " + armiesProduced + " units.");
				}
			}
			else if(warFive) {
				//We are at war with MACAW.
				int squareToAttack;
				@SuppressWarnings("rawtypes")
				List squaresToClaim = new ArrayList();
				// We aren't at war with anyone.
				// Try to stay peaceful by only targeting unoccupied
				// squares.
				// (This move could be used for the purpose of finding a
				// square to attack with the exception that you'd find
				// a square that belonged to the enemy)
				for (int i = 0; i < 36; i++) {
					// Go through each square. If the value is zero, check
					// if it
					// has a neighbor of PARROT, and add it to the list.
					if (status[i] == 3) {
						// Square I is unoccupied. Now we'll check if it has
						// a
						// neighbor that is Parrot to see if the move is
						// legal.
						if (moveIsLegal(i, 4)) {
							// The move is legal, so it has two as a
							// neighbor.
							// Add it to the list of possible claims.
							squaresToClaim.add(i);
						} else {
							// The move is illegal, so this cannot be added
							// to
							// the posibilities.
							// Go through another iteration.
						}
					} else {
						// This square is occupied.
						// Go through another iteration.
					}
				}
				// The loop is over! The resulting list will be checked
				// through
				// to find a target.
				if (squaresToClaim.size() != 1) {
					// The size is one, so we can just claim that number.
					squareToAttack = (int) squaresToClaim.get(0);
				} else {
					// Pick a random square.
					int squareToClaim = new Random().nextInt(squaresToClaim.size());
					squareToAttack = (int) squaresToClaim.get(squareToClaim);
				}

				// We are at war with TOUCAN.
				// If our army is stronger than theirs, attack!
				if (fourpower > threepower) {
					int luck = new Random().nextInt(6);
					int actualLuck = luck - 3;
					int oppoLuck = new Random().nextInt(2);
					// Gives options -3 through 3
					// Battle Numbers - Who Wins?
					int attackingForce = actualLuck + fourpower;
					int defendingForce = oppoLuck + threepower;
					if (attackingForce > defendingForce) {
						System.out.println("Attackers took square");
						status[squareToAttack] = 4;
						fourpower = fourpower - (fourpower - threepower);
						threepower = threepower - (fourpower - threepower + 2);
						threeoutput = threeoutput - 1;
					} else if (attackingForce < defendingForce) {
						System.out.println("Defense remained in control");
						fourpower = fourpower - (fourpower - threepower - 1);
						threepower = threepower - (threepower - fourpower + 1);
						fouroutput = fouroutput - 1;
					} else {
						fourpower = fourpower - 1;
						threepower = threepower - 1;
						fouroutput = fouroutput - 1;
						threeoutput = threeoutput - 1;
					}
				}
				else {
					//We don't have more power. Traditionally, we'd claim another square.
					//However, because we're at war, we'll train troops.
					int armiesProduced = new Random().nextInt(3);
					fourpower = fourpower + armiesProduced;
					System.out.println("Dodo trained " + armiesProduced + " units.");
				}
			}
			else if(warSix) {
				//We are at war with PARROT.
				int squareToAttack;
				@SuppressWarnings("rawtypes")
				List squaresToClaim = new ArrayList();
				// We aren't at war with anyone.
				// Try to stay peaceful by only targeting unoccupied
				// squares.
				// (This move could be used for the purpose of finding a
				// square to attack with the exception that you'd find
				// a square that belonged to the enemy)
				for (int i = 0; i < 36; i++) {
					// Go through each square. If the value is zero, check
					// if it
					// has a neighbor of PARROT, and add it to the list.
					if (status[i] == 2) {
						// Square I is unoccupied. Now we'll check if it has
						// a
						// neighbor that is Parrot to see if the move is
						// legal.
						if (moveIsLegal(i, 4)) {
							// The move is legal, so it has two as a
							// neighbor.
							// Add it to the list of possible claims.
							squaresToClaim.add(i);
						} else {
							// The move is illegal, so this cannot be added
							// to
							// the posibilities.
							// Go through another iteration.
						}
					} else {
						// This square is occupied.
						// Go through another iteration.
					}
				}
				// The loop is over! The resulting list will be checked
				// through
				// to find a target.
				if (squaresToClaim.size() != 1) {
					// The size is one, so we can just claim that number.
					squareToAttack = (int) squaresToClaim.get(0);
				} else {
					// Pick a random square.
					int squareToClaim = new Random().nextInt(squaresToClaim.size());
					squareToAttack = (int) squaresToClaim.get(squareToClaim);
				}

				// We are at war with TOUCAN.
				// If our army is stronger than theirs, attack!
				if (fourpower > threepower) {
					int luck = new Random().nextInt(6);
					int actualLuck = luck - 3;
					int oppoLuck = new Random().nextInt(2);
					// Gives options -3 through 3
					// Battle Numbers - Who Wins?
					int attackingForce = actualLuck + fourpower;
					int defendingForce = oppoLuck + twopower;
					if (attackingForce > defendingForce) {
						System.out.println("Attackers took square");
						status[squareToAttack] = 4;
						fourpower = fourpower - (fourpower - twopower);
						twopower = twopower - (fourpower - twopower + 2);
						twooutput = twooutput - 1;
					} else if (attackingForce < defendingForce) {
						System.out.println("Defense remained in control");
						fourpower = fourpower - (fourpower - twopower - 1);
						twopower = twopower - (twopower - fourpower + 1);
						fouroutput = fouroutput - 1;
					} else {
						fourpower = fourpower - 1;
						twopower = twopower - 1;
						fouroutput = fouroutput - 1;
						twooutput = twooutput - 1;
					}
				}
				else {
					//We don't have more power. Traditionally, we'd claim another square.
					//However, because we're at war, we'll train troops.
					int armiesProduced = new Random().nextInt(3);
					fourpower = fourpower + armiesProduced;
					System.out.println("Dodo trained " + armiesProduced + " units.");
				}
			}
			else {
				@SuppressWarnings("rawtypes")
				List squaresToClaim = new ArrayList();
				// We aren't at war with anyone.
				// Try to stay peaceful by only targeting unoccupied squares.
				// (This move could be used for the purpose of finding a square
				// to attack with the exception that you'd find
				// a square that belonged to the enemy)
				for (int i = 0; i < 36; i++) {
					// Go through each square. If the value is zero, check if it
					// has a neighbor of PARROT, and add it to the list.
					if (status[i] == 0) {
						// Square I is unoccupied. Now we'll check if it has a
						// neighbor that is Parrot to see if the move is legal.
						if (moveIsLegal(i, 4)) {
							// The move is legal, so it has two as a neighbor.
							// Add it to the list of possible claims.
							squaresToClaim.add(i);
						} else {
							// The move is illegal, so this cannot be added to
							// the posibilities.
							// Go through another iteration.
						}
					} else {
						// This square is occupied.
						// Go through another iteration.
					}
				}
				// The loop is over! The resulting list will be checked through
				// to find a target.
				if (squaresToClaim.size() == 0) {
					//Declare war on the first tribe.
					for(int i = 0; i < 36; i++) {
						if(moveIsLegal(i, 4)) {
							if(status[i] == 1) {
								warThree = true;
								System.out.println("Dodo reluctantly goes to war with Toucan after they refuse to stand down on territorial claims.");
								break;
							}
							else if(status[i] == 2) {
								warFive = true;
								System.out.println("Dodo reluctantly goes to war with Parrot after they refuse to stand down on territorial claims.");
								break;
							}
							else if(status[i] == 3) {
								warSix = true;
								System.out.println("Dodo reluctantly goes to war with Macaw after they refuse to stand down on territorial claims.");
								break;
							}
						}
					}
				}
				if (squaresToClaim.size() != 1) {
					// The size is one, so we can just claim that number.
					status[(int) squaresToClaim.get(0)] = 4;
				} else {
					// Pick a random square.
					int squareToClaim = new Random().nextInt(squaresToClaim.size());
					status[(int) squaresToClaim.get(squareToClaim)] = 4;
				}
			}
		} else {
			// This should never be called. This is just here for grammar
			// purposes.
			// Unless I mistakenly used ai
			System.err.println("The logic operator failed me.");
		}
	}
	public boolean checkIfIsAlive(int team) {
		for(int i = 0; i < 36; i++) {
			if(status[i] == team) {
				return true;
			}
		}
		return false;
	}
	public void updateLife() {
		toucanIsAlive = checkIfIsAlive(1);
		parrotIsAlive = checkIfIsAlive(2);
		macawIsAlive = checkIfIsAlive(3);
		dodoIsAlive = checkIfIsAlive(4);
	}
	public void checkForWinner() {
		if((toucanIsAlive&&!parrotIsAlive&&!macawIsAlive&&!dodoIsAlive)||oneoutput > 3500) {
			JOptionPane.showMessageDialog(null, "Toucan wins!");
			System.exit(0);
		}
		else if((!toucanIsAlive&&parrotIsAlive&&!macawIsAlive&&!dodoIsAlive)||twooutput > 3500) {
			JOptionPane.showMessageDialog(null, "Parrot wins!");
			System.exit(0);
		}
		else if((!toucanIsAlive&&!parrotIsAlive&&macawIsAlive&&!dodoIsAlive)||threeoutput > 3500) {
			JOptionPane.showMessageDialog(null, "Macaw wins!");
			System.exit(0);
		}
		else if((!toucanIsAlive&&!parrotIsAlive&&!macawIsAlive&&dodoIsAlive)||fouroutput > 3500) {
			JOptionPane.showMessageDialog(null, "Dodo wins!");
			System.exit(0);
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
	public boolean moveIsLegal(int square, int team) {

		// !!!!! DO NOT EDIT THIS CODE! IT HAS BEEN PROVEN TO WORK. !!!!

		// Find Options for attacking - go through for() loop to determine if
		// the move is legal
		// Go backwards! Rework the unit to find all of the neighbors, then take
		// those and work it out
		int[] validMovesForAttack = determineNeighbors(square);
		for (int i = 0; i < validMovesForAttack.length; i++) {
			if (status[validMovesForAttack[i]] == team) {
				// Move was legal!!! Attacker came from square that borders it
				return true;
			}
		}
		// The method has been ran through with no success.
		// The move is invalidated
		// Player will check action in the real life
		return false;
	}
	public int getNumberOfSquares(int team) {
		int squaresOfTeam = 0;
		for(int i = 0; i < 36; i++) {
			if(team == 1) {
				if(status[i] == 1) {
					squaresOfTeam++;
				}
				else {
					//Don't do anything here - the int doesn't need to change.
				}
			}
			else if(team == 2) {
				if(status[i] == 2) {
					squaresOfTeam++;
				}
				else {
					//Don't do anything here - the int doesn't need to change.
				}
			}
			else if(team == 3) {
				if(status[i] == 3) {
					squaresOfTeam++;
				}
				else {
					//Don't do anything here - the int doesn't need to change.
				}
			}
			else if(team == 4) {
				if(status[i] == 4) {
					squaresOfTeam++;
				}
				else {
					//Don't do anything here - the int doesn't need to change.
				}
			}
			else {
				//Only for grammar
			}
		}
		return squaresOfTeam;
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