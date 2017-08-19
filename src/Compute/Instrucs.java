package Compute;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JLabel;

public class Instrucs {
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JLabel rule = new JLabel();
	public Instrucs() {
		rule.setText("Welcome to my game.\n\nThe goal of the game\nis to take over the square...\n\nor gain a strong economy\nwith a GDP of over 350,000.\n\nYour economy grows with /develop\n\nTech gives you a gain between 0 and 5K\n\nEducation, between 1 and 3\n\nand infrastructure gives a solid two - \n\nwhich is then multiplied by your amount of squares.\n\nTrain using /train\n\nEvery time you click, the AI will play.\nClick in between your turns.\nYou can see what they're doing in the console.\n\nGood luck!");
		panel.add(rule);
		frame.add(panel);
		frame.setVisible(true);
	}
}
