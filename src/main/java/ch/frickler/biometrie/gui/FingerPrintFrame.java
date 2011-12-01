package ch.frickler.biometrie.gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

import ch.frickler.biometrie.data.MinutiaPoint;
import ch.frickler.biometrie.data.Template;



public class FingerPrintFrame extends JFrame{
	
	private static final Color color = Color.RED;
	private Template template;
	
	/**
     * Creates a new instance of Java2DFrame
     */
    public FingerPrintFrame() {
    	super("Test");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setSize(600,600);
    	setVisible(true);        
    }
    
    
    
    /**
     * This is the method where the line is drawn.
     *
     * @param g The graphics object
     */
    public void paint(Graphics g) {
    	super.paint(g);

    	g.setColor(color);
    	if (template != null) {
	    	for (MinutiaPoint point : template.getMinutiaPoints()) {
	    		g.fillOval(point.getxCoord(), point.getyCoord(), 10, 10);
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
