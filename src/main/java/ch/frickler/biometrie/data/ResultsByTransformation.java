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
	private double matches = 0;
	private boolean logging = false;


	private static double TOLERANCE = 3.0;
	
	public ResultsByTransformation(List<MinutiaNighbourPair> pairs,
			List<MinutiaNighbourPair> pairs2, boolean logging) {
		this(pairs,pairs2);
		this.logging = logging;
		
	}

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
				double currentMatches = checkMatches(matrix);
				if (bestTransformation ==null || matches < checkMatches(matrix)) {
					bestTransformation = matrix;
					matches = currentMatches;
				}
			}
		}
		//System.out.println(String.format("transformation with %d matches",matches));
		return bestTransformation;
	}
	
	public double checkMatches(Homogeneouse2DMatrix transformation) {
		double matches = 0;
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
			boolean thewebairway = false;
			if(thewebairway){
				if (isInRange(v1, rv1) && isInRange(v2, rv2)) {
					matches++;
				}
			}else{
				matches += getMatchScore(pair,referencePair);
			}
		}
		return matches;
	}
	
	private double getMatchScore(MinutiaNighbourPair pair,
			MinutiaNighbourPair referencePair) {
			
		double match1 = getMatchScore(pair.getFirst(),referencePair.getFirst());
		double match2 = getMatchScore(pair.getSecond(),referencePair.getSecond());
		
		double returnValue = (match1 + match2) / 2;
		
		return returnValue;
	}

	private double getMatchScore(MinutiaPoint first, MinutiaPoint second) {
		
		// every score should be between 0-1
		double maxAcceptedDistance = 10;
		double maxAcceptedAngle = 10;
		double typeScoreIfDoesntMatch = 0.2;
		
		double[] weight = { 0.4, 0.4 , 0.2 };
		
		double scoreAngle = Math.abs(first.getAngle() - second.getAngle());
		scoreAngle = scoreAngle > maxAcceptedAngle ? 0 : (maxAcceptedAngle -scoreAngle) /maxAcceptedAngle;
		double scoreDistance = first.getVector().getDistance(second.getVector());
		scoreDistance = scoreDistance > maxAcceptedDistance ? 0 : (maxAcceptedDistance -scoreDistance) /maxAcceptedDistance;
		double scoreType = first.getType() == first.getType() ? 1 : typeScoreIfDoesntMatch;
		
		double retValue = scoreAngle * weight[0] + scoreDistance * weight[1] + scoreType * weight[2];
		
		//todo what we do with the qualitity attribute first.getQuality()
		if(logging)
			System.out.println("match rate between "+first.getName()+" and "+second.getName()+" is:"+retValue);
		return retValue;
	}

	private boolean isInRange (Vector r1,Vector r2)  {
		
		if (r1.getDistance(r2) < TOLERANCE) {
				return true;
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
	
	public double getMatches() {
		return matches;
	}
}
