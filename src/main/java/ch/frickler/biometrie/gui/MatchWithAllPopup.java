package ch.frickler.biometrie.gui;

import java.awt.Color;

import javax.swing.JFrame;

public class MatchWithAllPopup extends JFrame {
	
	private int[] scores;
	
	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;
	
	public MatchWithAllPopup(int comparedWith, int[] scores, int t){
		this.scores = scores;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Template #"+comparedWith+" in comparision to all others");
		setSize(WIDTH, HEIGHT);
		setBackground(Color.white);
		add(new HistogrammPanel(scores, t));
		setResizable(false);
		setVisible(true);
	}
	
}
