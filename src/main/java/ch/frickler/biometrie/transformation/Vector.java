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
		if(orientation > 0){ 
			angle += Math.PI;
		}
		
		return angle;
	}
}
