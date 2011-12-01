package ch.frickler.biometrie.data;

public class MinutiaPoint {
    private int type;
    private int xCoord;
    private int yCoord;
    private int angle;
    private int quality;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getxCoord() {
		return xCoord;
	}
	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}
	public int getyCoord() {
		return yCoord;
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
}
