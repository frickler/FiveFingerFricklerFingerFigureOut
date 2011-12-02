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
	private Template template;
	
	
	public FingerPrintPanel() {


	}
    /**
     * This is the method where the line is drawn.
     *
     * @param g The graphics object
     */
    public void paint(Graphics g) {
    	super.paint(g);

    	g.setColor(ORIGINAL_FINGERPRING_COLOR);
    	if (template != null) {
	    	for (MinutiaPoint point : template.getMinutiaPoints()) {
	    		g.fillOval(point.getxCoord()-3, point.getyCoord()-3, 6, 6);
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
}
