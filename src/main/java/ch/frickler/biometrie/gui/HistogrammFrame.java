package ch.frickler.biometrie.gui;

import java.awt.HeadlessException;
import java.util.List;

import javax.swing.JFrame;

import ch.frickler.biometrie.data.MinutiaNighbourPair;

public class HistogrammFrame extends JFrame{

	int separation = 36; // ten pillows for 360 degree
	private static final long serialVersionUID = 1L;

	public HistogrammFrame(String title) throws HeadlessException {
		super(title);
		setBounds(100, 100, 500, 200);
	}
	
	
	public void displayHistogramm(List<MinutiaNighbourPair> pairs){
	
		
		int[] histogramm = getAngleHistogramm(pairs);
		int c = 0;
		for (int i : histogramm) {
			//System.out.println("nr: "+c+" count: "+i);
			System.out.println("count: "+i);
			c++;
		}
		
	}
	
	
	public int[] getAngleHistogramm(List<MinutiaNighbourPair> pairs){
		
		
		
		int[] histogramm = new int [360/separation];
		
		for (int i : histogramm) {
			i = 0;
		}
			
		for(MinutiaNighbourPair p : pairs){
		
			histogramm[p.getAngle() / separation]++;
			
		}
			
		return histogramm;
		
		
	}

}
