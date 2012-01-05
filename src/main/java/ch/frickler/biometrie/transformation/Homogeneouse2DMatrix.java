package ch.frickler.biometrie.transformation;

import javax.net.ssl.SSLContext;

/**
 * maybe add some comment for that?
 * @author kaeserst
 *
 */
public class Homogeneouse2DMatrix {
	private double a;
	private double b;
	private double c;
	private double d;
	private double tx;
	private double ty;
	
	public Homogeneouse2DMatrix() {
		super();
	}
	
	public Homogeneouse2DMatrix(double a, double b, double c, double d, double tx, double ty) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.tx = tx;
		this.ty = ty;
	}
	
	public Homogeneouse2DMatrix multiply(Homogeneouse2DMatrix htm) {
		Homogeneouse2DMatrix result = new Homogeneouse2DMatrix();
		result.setA(getA() * htm.getA() + getB() * htm.getC());
		result.setB(getA() * htm.getB() + getB() * htm.getD());
		result.setC(getC() * htm.getA() + getD() * htm.getC());
		result.setD(getC() * htm.getB() + getD() * htm.getD());
		result.setTx(getA() * htm.getTx() + getB() * htm.getTy() + getTx());
		result.setTy(getC() * htm.getTx() + getD() * htm.getTy() + getTy());
		return result;
	}
	
	public Vector multiply(Vector v) {
		Vector result = new Vector();
		result.x = getA() * v.getX() + getB() * v.getY() + getTx() * 1;
		result.y = getC() * v.getX() + getD() * v.getY() + getTy() * 1;
		
		return result;
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

	public double getTx() {
		return tx;
	}

	public void setTx(double tx) {
		this.tx = tx;
	}

	public double getTy() {
		return ty;
	}

	public void setTy(double ty) {
		this.ty = ty;
	}

	public void print() {
		System.out.println("|A:"+this.a+" , B:"+this.b+"|   x:"+this.tx);
		System.out.println("|C:"+this.c+" , D:"+this.d+"|   y:"+this.ty);
	}
}
