package ch.frickler.biometrie.transformation;

public class TransformationFactory {
	public static Homogeneouse2DMatrix createRotation(double angle) {
		Homogeneouse2DMatrix rotation = new Homogeneouse2DMatrix(Math.cos(angle), Math.sin(angle), -Math.sin(angle), Math.cos(angle), 0.0, 0.0);
		return rotation;
	}
	
	public static Homogeneouse2DMatrix createTranslation(double x,double y) {
		Homogeneouse2DMatrix translation = new Homogeneouse2DMatrix(1.0,0.0,0.0,1.0,x,y);
		return translation;
	}
}
