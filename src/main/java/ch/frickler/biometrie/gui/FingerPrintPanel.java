package ch.frickler.biometrie.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

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
    		
    		List<MinutiaPoint> ps =  t.getMinutiaPoints();
    		int max = ps.size();
	    	for (MinutiaPoint current : t.getMinutiaPoints()) {

	    		g.fillOval(current.getxCoord()-3, h - current.getyCoord()-3, 6, 6);
	    		
	    		MinutiaPoint nearEst = getNearestPoint(t,current);
	    		
	    		g.drawLine(current.getxCoord(),h -  current.getyCoord(),nearEst.getxCoord(),h -  nearEst.getyCoord());
	    		//super.repaint();
	    	}
    	}
    	
    }

    public MinutiaPoint getNearestPoint(Template t,MinutiaPoint current){
    	
		double minDistance = Integer.MAX_VALUE;
		MinutiaPoint nearEst = null;
				
		for (MinutiaPoint comp :  t.getMinutiaPoints()) {
			//System.out.println("j "+j+" i "+i+" max "+max);
			double dist = Math.sqrt(Math.pow(current.getxCoord()-comp.getxCoord(),2)+Math.pow(current.getyCoord()-comp.getyCoord(),2));
			System.out.println(dist);
			
			if(dist < minDistance && !current.equals(comp)){
				minDistance = dist;
				nearEst = comp;
				System.out.println("new nearest dist "+dist);
			}
		}
		return nearEst;
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
