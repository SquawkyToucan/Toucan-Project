package Stuff;

import java.awt.Frame;

import javax.swing.JFrame;

public class Toucans {
	// Toucans is a strategy game where the goal is to win
	// You can win through DOMINATION (35 tiles), TECH (35 points), ECONOMY (35K GDP + dependants, trade), or CIVILIZING
	// Domination Victory: Every tile on the board is conquered. Enemies are vanquished.
	// Technological Victory: 35 points worth of breakthrough, such as rockets, boats, etc. Can benefit military points
	// Economical Victory: A combined sum of trade (per turn), subsidies to other tribes (per turn), production per turn, and GDP over 35K. On hard, if you near this all other countries will sanction you.
	// Civilizing Victory: Your society has a combination of currency (5p), trade relations (5p), healthcare (5p), internet (10p - from Tech), education (5p), a university (5p), literature (5p), language (10p), and a city all combined to over 35 points
	// You can die through being CONQUERED, STARVING, LACK OF ECON, or CONCEDING
	// Death through Conquered: You are taken over by another tribe.
	// Death through Starvation: Not enough food!
	// Death through Lack of Economy: Your interior and exterior economies are both at 0.
	// Conceding/giving up - Button will be available for this if necessary
	JFrame frame = new JFrame();
	GamePanel gamePanel;
	Toucans() {
		frame.setSize(1000, 1600);
		frame.setVisible(true);
		gamePanel = new GamePanel();
		setup();
	}
	void setup() {
		frame.add(gamePanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gamePanel.startGame();
		frame.addKeyListener(gamePanel);
	}
	public static void main(String[] args) {
		Toucans runner = new Toucans();
	}
}
