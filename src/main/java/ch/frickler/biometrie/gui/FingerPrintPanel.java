package ch.frickler.biometrie.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import ch.frickler.biometrie.data.MinutiaPoint;
import ch.frickler.biometrie.data.Template;
import ch.frickler.biometrie.transformation.Homogeneouse2DMatrix;
import ch.frickler.biometrie.transformation.TransformationFactory;
import ch.frickler.biometrie.transformation.Vector;

public class FingerPrintPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Color ORIGINAL_FINGERPRING_COLOR = Color.RED;
	private static final Color REFERENCE_FINGERPRINT_COLOR = Color.BLUE;
	private Template template;
	private Template refTemplate;
	private Homogeneouse2DMatrix transformation;

	public FingerPrintPanel() {

	}

	/**
	 * This is the method where the line is drawn. the y coordinate is flipped,
	 * because we want the origin at the bottom left corner
	 * 
	 * @param g
	 *            The graphics object
	 */
	public void paint(Graphics g) {
		super.paint(g);

		Font font = new Font("Serif", Font.PLAIN, 10);
		g.setFont(font);
		g.drawString("x = RIDGE_BIFURCATION; \u25A0 = RIDGE_ENDING", 10, 10);

		g.setColor(ORIGINAL_FINGERPRING_COLOR);
		paintTemplate(g, template,
				null);
		g.setColor(REFERENCE_FINGERPRINT_COLOR);
		paintTemplate(g,
				refTemplate, transformation);
	}

	
	private void paintTemplate(Graphics g, Template t,
			Homogeneouse2DMatrix transformation) {
		
		int h = getHeight();
		if (t != null) {
			for (MinutiaPoint point : t.getMinutiaPoints()) {
				int x = point.getxCoord();
				int y = point.getyCoord();
				if (transformation != null) {
					Vector originalVector = new Vector(x, y);
					Vector result = transformation.multiply(originalVector);
					x = (int) result.getX();
					y = (int) result.getY();
					g.setColor(REFERENCE_FINGERPRINT_COLOR);
				}
				
				// Display type
				switch (point.getType()) {
				case 0:// OTHER
						// g.fillCircle(x - 3, h - y - 3, 4, 4);
					g.fillOval(x - 2, h - y - 2, 4, 4);
					break;
				case 1: // RIDGE_ENDING
					g.fillRect(x - 2, h - y - 2, 4, 4);
					break;
				case 2: // RIDGE_BIFURCATION
					Graphics2D g2 = (Graphics2D) g;
					g2.setStroke(new BasicStroke(2));
					g2.drawLine(x - 6, h - y - 6, x, h - y);
					g2.drawLine(x, h - y - 6, x - 6, h - y);
					break;
				}
			}
		}
	}

	public Template getTemplate() {
		return template;	}

	public void setTemplate(Template template) {
		this.template = template;
		transformation = null;
		repaint();
	}

	public void setReferenceTemplate(Template template) {
		this.refTemplate = template;
		repaint();
	}

	public Template getRefTemplate() {
		return refTemplate;
	}

	public void calculateTransformation(double angle, int x, int y) {
		if (transformation == null) {
			transformation = TransformationFactory.createTranslation(0, 0);
		}
		if (angle == 0.0) {
			transformation = TransformationFactory.createTranslation(x, y).multiply(transformation);
		} else {

			double xPanel = (double) getWidth() / 2;
			double yPanel = (double) getHeight() / 2;
			
			transformation =  transformation.multiply(TransformationFactory.createTranslation(xPanel, yPanel));
			transformation = transformation.multiply(TransformationFactory
					.createRotation(angle));
			transformation = transformation.multiply(TransformationFactory
					.createTranslation(-xPanel, -yPanel));
			
			
			
		}
	}
}
