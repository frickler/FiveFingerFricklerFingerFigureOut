package ch.frickler.biometrie.data;

import java.util.ArrayList;
import java.util.List;



public class Template {
	private int imageWidth;
	private int imageHeight;
	
	private int xResolution;
	private int yResolution;
	
	private int center;
	
	private List<MinutiaPoint> minutiaPoints;
	
	public Template() {
		minutiaPoints = new ArrayList<MinutiaPoint>();
	}
	
	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public int getxResolution() {
		return xResolution;
	}

	public void setxResolution(int xResolution) {
		this.xResolution = xResolution;
	}

	public int getyResolution() {
		return yResolution;
	}

	public void setyResolution(int yResolution) {
		this.yResolution = yResolution;
	}

	public List<MinutiaPoint> getMinutiaPoints() {
		return minutiaPoints;
	}

	public void addMinutiaPoint(MinutiaPoint p) {
		minutiaPoints.add(p);
	}

	public int getCenter() {
		return center;
	}

	public void setCenter(int center) {
		this.center = center;
	}
	
	
}
