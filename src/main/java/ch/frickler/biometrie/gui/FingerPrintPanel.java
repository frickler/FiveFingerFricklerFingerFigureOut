package ch.frickler.biometrie.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import ch.frickler.biometrie.data.MinutiaPoint;
import ch.frickler.biometrie.data.Template;



public class FingerPrintPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Color ORIGINAL_FINGERPRING_COLOR = Color.RED;
	private static final Color REFERENCE_FINGERPRINT_COLOR = Color.BLUE;
	private Template template;
	private Template refTemplate;
	
	
	public FingerPrintPanel() {


	}
    /**
     * This is the method where the line is drawn.
     * the y coordinate is flipped, because we want the origin at the bottom left corner
     * @param g The graphics object
     */
    public void paint(Graphics g) {
    	super.paint(g);

    	paintTemplate(g, FingerPrintPanel.ORIGINAL_FINGERPRING_COLOR, template);
    	paintTemplate(g, FingerPrintPanel.REFERENCE_FINGERPRINT_COLOR, refTemplate);
    }
    
    private void paintTemplate(Graphics g, Color c, Template t){
    	g.setColor(c);
    	int h = getHeight();
    	if (t != null) {
	    	for (MinutiaPoint point : t.getMinutiaPoints()) {
	    		g.fillOval(point.getxCoord()-3, h - point.getyCoord()-3, 6, 6);
	    	}
    	}
    }

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
		repaint();
	}
	
	public void setReferenceTemplate(Template template){
		this.refTemplate = template;
		repaint();
	}
}
