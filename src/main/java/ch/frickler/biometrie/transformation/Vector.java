package ch.frickler.biometrie.transformation;

public class Vector {

	public double x;
	public double y;
	
	public Vector() {
		super();
	}
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public double getX(double scale) {
		return (int)(x * scale);
	}
	public double getY(double scale) {
		return (int)(y * scale);
	}
	
	public double getAngle(Vector other) {
		double tmp1 = this.x * other.x + this.y * other.y;
		double tmp2 = Math.sqrt(this.x*this.x + this.y*this.y) * Math.sqrt(other.x*other.x + other.y*other.y);
		double angle = Math.acos(tmp1/tmp2);
		//drehrichtung
		double orientation = this.x * other.y - this.y * other.x;
		
		return angle;
	}
	
	public double getAngleDegree(Vector other){
		return Math.round(getAngle(other)/(2*Math.PI)*360*100)/100;
	}
	

	public double getDistance(Vector r2) {
		double x = Math.abs(getX()-r2.getX());
		double y = Math.abs(getY()-r2.getY());
	    return Math.sqrt(Math.pow(x, 2)+Math.pow(y,2)); // pütagoras
	}
}
