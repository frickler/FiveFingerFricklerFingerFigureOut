package ch.frickler.biometrie.data;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;

import ch.frickler.biometrie.transformation.Homogeneouse2DMatrix;
import ch.frickler.biometrie.transformation.TransformationFactory;
import ch.frickler.biometrie.transformation.Vector;

public class ResultsByTransformation {
	private List<MinutiaNighbourPair> mPairs;
	private List<MinutiaNighbourPair> mReferencePairs;
	private int matches = 0;
	


	private static double TOLERANCE = 3.0;

	public ResultsByTransformation(List<MinutiaNighbourPair> pairs,
			List<MinutiaNighbourPair> referencePairs) {
		
		//System.out.println(String.format("pairs: %d, referencePairs: %d",pairs.size(),referencePairs.size()));
		
		mPairs = new ArrayList<MinutiaNighbourPair>();
		mReferencePairs = new ArrayList<MinutiaNighbourPair>();
		
		for (MinutiaNighbourPair pair : pairs) {
			for (MinutiaNighbourPair referencePair : referencePairs) {
				if (pair.equals(referencePair)) {
					mPairs.add(pair);
					mReferencePairs.add(referencePair);
				}
			}
			
		}
		
		//System.out.println(String.format("matchedPairs: %d, matchedReferencePairs: %d",mPairs.size(),mReferencePairs.size()));
	}
	
	public Homogeneouse2DMatrix getTransformation() {
		//take the first for now...
		Homogeneouse2DMatrix bestTransformation = null;
		matches = 0;
		for (int i=0; i<mPairs.size(); i++) {
			Homogeneouse2DMatrix matrix = analystePairs(mPairs.get(i), mReferencePairs.get(i));
			if (matrix != null) {
				int currentMatches = checkMatches(matrix);
				if (bestTransformation ==null || matches < checkMatches(matrix)) {
					bestTransformation = matrix;
					matches = currentMatches;
				}
			}
		}
		//System.out.println(String.format("transformation with %d matches",matches));
		return bestTransformation;
	}
	
	public int checkMatches(Homogeneouse2DMatrix transformation) {
		int matches = 0;
		for (int i = 0; i<mPairs.size(); i++) {
			Vector v1,v2,rv1,rv2;
			
			MinutiaNighbourPair pair = mPairs.get(i);
			MinutiaNighbourPair referencePair = mReferencePairs.get(i);
			v1 = pair.getFirst().getVector();
			v2 = pair.getSecond().getVector();
			rv1 = referencePair.getFirst().getVector();
			rv2 = referencePair.getSecond().getVector();
			
			rv1 = transformation.multiply(rv1);
			rv2 = transformation.multiply(rv2);
			
			if (isInRange(v1, rv1) && isInRange(v2, rv2)) {
				matches++;
			}
			
		}
		return matches;
	}
	
	private boolean isInRange (Vector r1,Vector r2)  {
		
		if (r1.getX() - TOLERANCE <= r2.getX() && r1.getX() + TOLERANCE >= r2.getX()) {
			if (r1.getY() - TOLERANCE <= r2.getY() && r1.getY() + TOLERANCE >= r2.getY()) {
				return true;
			}
		}
		return false;
	}
	
	private Homogeneouse2DMatrix analystePairs(MinutiaNighbourPair pair, MinutiaNighbourPair reference) {
		Vector v1 = new Vector(pair.getFirst().getxCoord(), pair.getFirst().getyCoord());
		Vector v2 = new Vector(pair.getSecond().getxCoord(), pair.getSecond().getyCoord());
		
		Vector rv1 = new Vector(reference.getFirst().getxCoord(),reference.getFirst().getyCoord());
		Vector rv2 = new Vector(reference.getSecond().getxCoord(),reference.getSecond().getyCoord());
		double xt = v1.getX() - rv1.getX();
		double yt = v1.getY()- rv1.getY();
		
		//System.out.println("xt: " + xt + " yt: " + yt);
		Homogeneouse2DMatrix transformation = TransformationFactory.createTranslation(xt, yt);
		
		rv1 = transformation.multiply(rv1);
		rv2 = transformation.multiply(rv2);
		
		if (isInRange(v2, rv2)) {
			//System.out.println("lucky bastard, they are allready the same...");
			return transformation;
		}
		/* rotation not working yet
		//richtungsvektor
		Vector rv = new Vector(v2.getX() - v1.getX(),v2.getY()-v1.getY());
		Vector rrv = new Vector(rv2.getX() - rv1.getX(), rv2.getY() - rv1.getY());
		
		double angle = rrv.getAngle(rv);
		
		Homogeneouse2DMatrix rotation = TransformationFactory.createTranslation(rv1.getX(), rv1.getY());
		rotation = rotation.multiply(TransformationFactory.createRotation(angle));
		rotation = rotation.multiply(TransformationFactory.createTranslation(-rv1.getX(), -rv1.getY()));
		
		if (isInRange(v2, rv2)) {
			System.out.println("uiui last change, but you got the rotation baby!");
			return transformation;
		}
		*/
		return null;
	}
	
	public int getMatches() {
		return matches;
	}
}
