package ch.frickler.biometrie.data;

import java.nio.charset.Charset;

import ch.frickler.biometrie.transformation.Vector;

public class MinutiaPoint {
    private int type;
    private int xCoord;
    private int yCoord;
    private int angle;
    private int quality;
    private String name;
    
	public MinutiaPoint(int templateCount, int minutiaCount) {
		this.name = templateCount+"."+"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqurstuvwxyz".charAt(minutiaCount);
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getxCoord() {
		return xCoord;
	}
	public int getxCoord(double scale) {
		return (int)(xCoord * scale);
	}
	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}
	public int getyCoord() {
		return yCoord;
	}
	public int getyCoord(double scale) {
		return (int)(yCoord * scale);
	}
	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}
	public int getAngle() {
		return angle;
	}
	public void setAngle(int angle) {
		this.angle = angle;
	}
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + angle;
		result = prime * result + quality;
		result = prime * result + type;
		result = prime * result + xCoord;
		result = prime * result + yCoord;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MinutiaPoint other = (MinutiaPoint) obj;
		if (angle != other.angle)
			return false;
		if (quality != other.quality)
			return false;
		if (type != other.type)
			return false;
		if (xCoord != other.xCoord)
			return false;
		if (yCoord != other.yCoord)
			return false;
		return true;
	}
	
	public Vector getVector() {
		return new Vector((double)getxCoord(),(double) getyCoord());
	}
	public String getName() {
		return this.name;
	}
	
}
