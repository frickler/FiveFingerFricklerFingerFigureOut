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

	private static final long serialVersionUID = 1L;
	private static final int SEPARATION = 36; // ten pillows for 360 degree
	private static final int FONT_SIZE = 10;
	private static final int BOARDER_SIZE = 35;
	private static final Color[] barColors = new Color[] { Color.BLUE,
			new Color(0, 0, 200), Color.RED, new Color(200, 0, 0) };

	private List<List<MinutiaNighbourPair>> neighbourPairs;
	private int windowWidh;
	private int windowHeight;
	private int horiontalPaintGap;

	public HistogrammPanel() {
		neighbourPairs = new ArrayList<List<MinutiaNighbourPair>>();

		setBackground(Color.WHITE);
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

		if (neighbourPairs.isEmpty()) {
			return;
		}

		windowWidh = getWidth() - 2 * BOARDER_SIZE;
		windowHeight = getHeight() - 2 * BOARDER_SIZE;

		paintHorizontalCaption(g);

		// Get maximal occurence of angles
		int maxOccurence = getMaxOccurence();

		if (maxOccurence == 0) {
			return;
		}

		paintVerticalCaption(g, maxOccurence);

		paintBars(g, maxOccurence);

	}

	private void paintBars(Graphics g, int maxOccurence) {
		// Calc height of bar for one occurence
		int barHeightPerOccurence = (windowHeight - BOARDER_SIZE)
				/ maxOccurence;

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
							* i + BOARDER_SIZE;
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
		g.drawLine(BOARDER_SIZE - 10, windowHeight, BOARDER_SIZE - 10,
				BOARDER_SIZE);
		g.drawLine(BOARDER_SIZE - 12, BOARDER_SIZE, BOARDER_SIZE - 8,
				BOARDER_SIZE);
		g.drawLine(BOARDER_SIZE - 12, windowHeight, BOARDER_SIZE - 8,
				windowHeight);

		g.drawString(String.valueOf(maxOccurence), 10, BOARDER_SIZE + FONT_SIZE
				/ 2);
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
		g.drawLine(BOARDER_SIZE, windowHeight, lineWidth + BOARDER_SIZE,
				windowHeight);
		horiontalPaintGap = (int) ((lineWidth) / (SEPARATION));
		for (int i = 0; i <= SEPARATION; i++) {
			int coordX = i * horiontalPaintGap + BOARDER_SIZE;
			g.drawLine(coordX, windowHeight + 2, coordX, windowHeight - 2);
		}

		// Write degrees
		Font font = new Font("Serif", Font.PLAIN, FONT_SIZE);
		g.setFont(font);
		drawRotatedText(g, BOARDER_SIZE - FONT_SIZE / 2, windowHeight
				+ FONT_SIZE, Math.PI / 2, "0");
		drawRotatedText(g, horiontalPaintGap * (SEPARATION / 4) + BOARDER_SIZE
				- FONT_SIZE / 2, windowHeight + FONT_SIZE / 2, Math.PI / 2,
				"90");
		drawRotatedText(g, horiontalPaintGap * (SEPARATION / 2) + BOARDER_SIZE
				- FONT_SIZE / 2, windowHeight + FONT_SIZE / 2, Math.PI / 2,
				"180");
		drawRotatedText(g, horiontalPaintGap * (3 * SEPARATION / 4)
				+ BOARDER_SIZE - FONT_SIZE / 2, windowHeight + FONT_SIZE / 2,
				Math.PI / 2, "270");
		drawRotatedText(g, horiontalPaintGap * (SEPARATION) + BOARDER_SIZE
				- FONT_SIZE / 2, windowHeight + FONT_SIZE / 2, Math.PI / 2,
				"360");
	}

	public int[] getAngleHistogramm(List<MinutiaNighbourPair> pairs) {

		int[] histogramm = new int[SEPARATION];
		int gap = 360 / SEPARATION;

		for (MinutiaNighbourPair p : pairs) {
			histogramm[p.getAngle() / gap]++;
		}

		return histogramm;

	}

}
