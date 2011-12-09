package ch.frickler.biometrie.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import ch.frickler.biometrie.data.MinutiaPoint;
import ch.frickler.biometrie.data.Template;
import ch.frickler.biometrie.transformation.Homogeneouse2DMatrix;
import ch.frickler.biometrie.transformation.TransformationFactory;
import ch.frickler.biometrie.transformation.Vector;



public class FingerPrintFrame extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Color ORIGINAL_FINGERPRING_COLOR = Color.RED;
	private static final Color REFERENCE_FINGERPRINT_COLOR = Color.BLUE;
	private static final Color ROTATE_FINGERPRINT_COLOR = Color.GREEN;
	private Template template;
	private Template refTemplate;
	private Homogeneouse2DMatrix transformation;
	private double templateRotationAngle = 0.0;
	
	
	
	public FingerPrintFrame() {

	}
    /**
     * This is the method where the line is drawn.
     * the y coordinate is flipped, because we want the origin at the bottom left corner
     * @param g The graphics object
     */
    public void paint(Graphics g) {
    	super.paint(g);

    	paintTemplate(g, FingerPrintFrame.ORIGINAL_FINGERPRING_COLOR, template);
    	paintTemplate(g, FingerPrintFrame.REFERENCE_FINGERPRINT_COLOR, refTemplate);
    }
    
    private void paintTemplate(Graphics g, Color c, Template t){
    	
    	
    	
    	int h = getHeight();
    	if (t != null) {
	    	for (MinutiaPoint point : t.getMinutiaPoints()) {
	    		int x = point.getxCoord();
	    		int y = point.getyCoord();
	    		if (transformation != null) {
		    		Vector originalVector = new Vector(x,y);
		    		Vector result = transformation.multiply(originalVector);
		    		int xr = (int)result.getX();
		    		int yr = (int)result.getY();
		    		g.setColor(ROTATE_FINGERPRINT_COLOR);
		    		g.fillOval(xr-3, h - yr-3, 6, 6);
	    		}
	    		g.setColor(c);
	    		g.fillOval(x-3, h - y-3, 6, 6);
	    	}
    	}
    }

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
		templateRotationAngle = 0.0;
		calculateTransformation(templateRotationAngle);
		repaint();
	}
	
	public void rotateTemplate() {
		templateRotationAngle += Math.PI/6;
		if (templateRotationAngle >= 2*Math.PI) {
			templateRotationAngle = 0.0;
		}
		calculateTransformation(templateRotationAngle);
		repaint();
	}
	
	public void setReferenceTemplate(Template template){
		this.refTemplate = template;
		repaint();
	}
	public void calculateTransformation(double angle) {
		if (angle == 0.0) {
			transformation = null;
		} else {
			double x = (double) getWidth()/2;
			double y = (double) getHeight()/2;
			transformation = TransformationFactory.createTranslation(x,y);
			transformation = transformation.multiply(TransformationFactory.createRotation(angle));
			transformation = transformation.multiply(TransformationFactory.createTranslation(-x, -y));
		}
	}
}
