package ch.frickler.biometrie.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ch.frickler.biometrie.data.MinutiaNighbourPair;

public class HistogrammPanel extends JPanel {

	public enum Mode {
		MINUTIA_PAIR, MATCH_WITH_ALL,
	}

	private static final long serialVersionUID = 1L;
	private int SEPARATION;
	private int FONT_SIZE;
	private int BORDER_SIZE;
	String[] hBar;

	private static final Color[] barColors = new Color[] { Color.RED,
			new Color(200, 0, 0), Color.BLUE, new Color(0, 0, 200) };

	private List<List<MinutiaNighbourPair>> neighbourPairs;
	private int windowWidh;
	private int windowHeight;
	private int horiontalPaintGap;

	private Mode mode;
	private int[] scores;
	private int threshold;

	public HistogrammPanel(Mode mode, int[] scores) {
		setBackground(Color.WHITE);

		this.mode = mode;
		this.scores = scores;

		if (Mode.MINUTIA_PAIR.equals(mode)) {
			// set up a minutia histogram
			setUpMinutia();
		} else {
			// setup the match with all
			setUpMatchWithAll();
		}
	}

	public HistogrammPanel() {
		// default to minutia_pair
		this(Mode.MINUTIA_PAIR, null);
	}

	public HistogrammPanel(int[] scores, int threshold) {
		this(Mode.MATCH_WITH_ALL, scores);
		this.threshold = threshold;
	}

	private void setUpMinutia() {
		neighbourPairs = new ArrayList<List<MinutiaNighbourPair>>();

		// let's parametrize the gui
		SEPARATION = 36; // ten pillows for 360 degree
		FONT_SIZE = 10;
		BORDER_SIZE = 35;
		hBar = new String[] { "0", "90", "180", "270", "360" };
	}

	private void setUpMatchWithAll() {
		// let's parametrize this gui too
		SEPARATION = scores.length;
		FONT_SIZE = 10;
		BORDER_SIZE = 35;
		hBar = new String[] { "0", Integer.toString(scores.length / 4),
				Integer.toString(scores.length / 2),
				Integer.toString(scores.length * 3 / 4),
				Integer.toString(scores.length) };
	}

	private void drawRotatedText(Graphics g, double x, double y, double theta,
			String label) {

		// Köt & Paste from
		// http://greybeardedgeek.net/2009/05/15/rotated-text-in-java-swing-2d/
		Graphics2D g2D = (Graphics2D) g;
		AffineTransform fontAT = new AffineTransform();
		Font theFont = g2D.getFont();
		fontAT.rotate(theta);
		Font theDerivedFont = theFont.deriveFont(fontAT);
		g2D.setFont(theDerivedFont);
		g2D.drawString(label, (int) x, (int) y);
		g2D.setFont(theFont);
	}

	public void plotPairs(List<MinutiaNighbourPair> pairsOfSelectedTemplate,
			List<MinutiaNighbourPair> pairsOfReferenceTemplate) {
		neighbourPairs.clear();
		neighbourPairs.add(pairsOfSelectedTemplate);
		if (pairsOfReferenceTemplate != null) {
			neighbourPairs.add(pairsOfReferenceTemplate);
		}
		repaint();
	}

	public void plotPairs(List<MinutiaNighbourPair> selectedTemplate) {
		plotPairs(selectedTemplate, null);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		windowWidh = getWidth() - 2 * BORDER_SIZE;
		windowHeight = getHeight() - 2 * BORDER_SIZE;

		paintHorizontalCaption(g);

		if (Mode.MINUTIA_PAIR.equals(mode)) {
			paintMinutia(g);
		} else {
			paintMatchWithAll(g);
		}

	}

	private void paintMinutia(Graphics g) {
		if (neighbourPairs.isEmpty()) {
			return;
		}

		// Get maximal occurence of angles
		int maxOccurence = getMaxOccurence();

		if (maxOccurence == 0) {
			return;
		}

		paintVerticalCaption(g, maxOccurence);

		paintBars(g, maxOccurence);
	}

	private void paintMatchWithAll(Graphics g) {
		paintVerticalCaption(g, 100);

		// Calc height of bar for one occurence
		int barHeightPerOccurence = (windowHeight - BORDER_SIZE) / 100;

		// Paint the scores
		for (int i = 0; i < scores.length; i++) {
			// Take alternating color
			Color color = barColors[i % 2];

			g.setColor(color);
			int neightbourPairOffset = horiontalPaintGap / scores.length;
			int coordX = i * horiontalPaintGap + neightbourPairOffset * i
					+ BORDER_SIZE;
			g.fillRect(coordX,
					windowHeight - scores[i] * barHeightPerOccurence,
					// horiontalPaintGap / scores.length
					10, scores[i] * barHeightPerOccurence);
		}

		if (threshold > 0) {
			g.setColor(Color.BLUE);
			g.drawLine(0, windowHeight - threshold * barHeightPerOccurence,
					getWidth(), windowHeight - threshold
							* barHeightPerOccurence);
			g.drawString("t=" + threshold, 0, windowHeight - threshold
					* barHeightPerOccurence);
		}
	}

	private void paintBars(Graphics g, int maxOccurence) {
		// Calc height of bar for one occurence
		int barHeightPerOccurence = (windowHeight - BORDER_SIZE) / maxOccurence;

		// Paint bars
		for (int i = 0; i < neighbourPairs.size(); i++) {

			List<MinutiaNighbourPair> mp = neighbourPairs.get(i);
			int[] histogramm = getAngleHistogramm(mp);
			for (int j = 0; j < histogramm.length; j++) {
				int amount = histogramm[j];
				if (amount > 0) {
					// Take alternating color
					Color color = barColors[i * 2 + j % 2];

					g.setColor(color);
					int neightbourPairOffset = horiontalPaintGap
							/ neighbourPairs.size();
					int coordX = j * horiontalPaintGap + neightbourPairOffset
							* i + BORDER_SIZE;
					g.fillRect(coordX, windowHeight - amount
							* barHeightPerOccurence, horiontalPaintGap
							/ neighbourPairs.size(), amount
							* barHeightPerOccurence);
				}
			}
		}
	}

	private void paintVerticalCaption(Graphics g, int maxOccurence) {

		// Paint vertical help lines
		g.drawLine(BORDER_SIZE - 10, windowHeight, BORDER_SIZE - 10,
				BORDER_SIZE);
		g.drawLine(BORDER_SIZE - 12, BORDER_SIZE, BORDER_SIZE - 8, BORDER_SIZE);
		g.drawLine(BORDER_SIZE - 12, windowHeight, BORDER_SIZE - 8,
				windowHeight);

		if (Mode.MINUTIA_PAIR.equals(mode)) {
			g.drawString(String.valueOf(maxOccurence), 10, BORDER_SIZE
					+ FONT_SIZE / 2);
		}
	}

	/**
	 * 
	 * @return The highest amount of all (grouped) angles
	 */
	private int getMaxOccurence() {
		int maxOccurence = 0;
		for (List<MinutiaNighbourPair> minutiaPair : neighbourPairs) {
			int[] histogramm = getAngleHistogramm(minutiaPair);
			for (int i = 0; i < histogramm.length; i++) {
				int occurence = histogramm[i];
				if (occurence > maxOccurence) {
					maxOccurence = occurence;
				}
			}
		}
		return maxOccurence;
	}

	private void paintHorizontalCaption(Graphics g) {
		// Avoid rounding errors
		int lineWidth = ((int) (windowWidh / SEPARATION)) * SEPARATION;

		// Paint horizontal help lines
		g.drawLine(BORDER_SIZE, windowHeight, lineWidth + BORDER_SIZE,
				windowHeight);
		horiontalPaintGap = (int) ((lineWidth) / (SEPARATION));
		for (int i = 0; i <= SEPARATION; i++) {
			int coordX = i * horiontalPaintGap + BORDER_SIZE;
			g.drawLine(coordX, windowHeight + 2, coordX, windowHeight - 2);
		}

		// Write degrees
		Font font = new Font("Serif", Font.PLAIN, FONT_SIZE);
		g.setFont(font);
		drawRotatedText(g, BORDER_SIZE - FONT_SIZE / 2, windowHeight
				+ FONT_SIZE, Math.PI / 2, hBar[0]);
		drawRotatedText(g, horiontalPaintGap * (SEPARATION / 4) + BORDER_SIZE
				- FONT_SIZE / 2, windowHeight + FONT_SIZE / 2, Math.PI / 2,
				hBar[1]);
		drawRotatedText(g, horiontalPaintGap * (SEPARATION / 2) + BORDER_SIZE
				- FONT_SIZE / 2, windowHeight + FONT_SIZE / 2, Math.PI / 2,
				hBar[2]);
		drawRotatedText(g, horiontalPaintGap * (3 * SEPARATION / 4)
				+ BORDER_SIZE - FONT_SIZE / 2, windowHeight + FONT_SIZE / 2,
				Math.PI / 2, hBar[3]);
		drawRotatedText(g, horiontalPaintGap * (SEPARATION) + BORDER_SIZE
				- FONT_SIZE / 2, windowHeight + FONT_SIZE / 2, Math.PI / 2,
				hBar[4]);
	}

	public int[] getAngleHistogramm(List<MinutiaNighbourPair> pairs) {

		int[] histogramm = new int[SEPARATION];
		int gap = 360 / SEPARATION;

		for (MinutiaNighbourPair p : pairs) {
			histogramm[p.getAngleInDegree() / gap]++;
		}

		return histogramm;

	}

}
