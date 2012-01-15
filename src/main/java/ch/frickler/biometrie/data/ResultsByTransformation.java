package ch.frickler.biometrie.data;

import java.util.ArrayList;
import java.util.List;

import ch.frickler.biometrie.transformation.Homogeneouse2DMatrix;
import ch.frickler.biometrie.transformation.TransformationFactory;
import ch.frickler.biometrie.transformation.Vector;

public class ResultsByTransformation {
	private List<MinutiaNighbourPair> lstTempalatePairs;
	private List<MinutiaNighbourPair> lstReferencePairs;
	private List<MinutiaNighbourPair> mPairs;
	private List<MinutiaNighbourPair> mReferencePairs;
	private double matches = 0;
	private boolean logging = false;
	private Template template2;
	private Template template1;
	private Homogeneouse2DMatrix bestTransformation;

	private static double TOLERANCE = 3.0;
	private static double ANGLE_TOLERANCE = 10.0;

	private static double FUZZYTOLERANCE = 0.9; // 0 = bad, 1.0 very equal

	public ResultsByTransformation(List<MinutiaNighbourPair> pairs,
			List<MinutiaNighbourPair> pairs2, Template t, Template t2,
			boolean logging) {
		this(pairs, pairs2, t, t2);
		this.logging = logging;

	}

	public ResultsByTransformation(List<MinutiaNighbourPair> pairs,
			List<MinutiaNighbourPair> referencePairs, Template t, Template t2) {

		lstTempalatePairs = pairs;
		lstReferencePairs = referencePairs;

		this.template1 = t;
		this.template2 = t2;

		// System.out.println(String.format("pairs: %d, referencePairs: %d",pairs.size(),referencePairs.size()));

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

	}

	public Homogeneouse2DMatrix getTransformation() {
		// take the first for now...
		if (bestTransformation != null)
			return bestTransformation;
		
		matches = 0;
		int ibest = -1;
		int jbest = -1;
		for (int i = 0; i < lstTempalatePairs.size(); i++) {
			for (int j = 0; j < lstReferencePairs.size(); j++) {
				double fuzzyValue = lstTempalatePairs.get(i).compareFuzzy(
						lstReferencePairs.get(j), false);
				// System.out.println("FuzzyValue was "+fuzzyValue);
				// skip if the fuzzyValue is bad
				if (fuzzyValue < FUZZYTOLERANCE) {
					continue;
				}

				Homogeneouse2DMatrix matrix = analystePairs(
						lstTempalatePairs.get(i), lstReferencePairs.get(j));

				if (matrix != null) {
					double currentMatches = checkMatches(matrix);
					if (bestTransformation == null || matches < currentMatches) {
						bestTransformation = matrix;
						matches = currentMatches;
						ibest = i;
						jbest = j;
					}
				}
			}
		}
		if (ibest >= 0) {
			System.out.println("best match comparing pair: "
					+ lstTempalatePairs.get(ibest).getFirst().getName() + ":"
					+ lstTempalatePairs.get(ibest).getSecond().getName());
			System.out.println(" to :"
					+ lstReferencePairs.get(jbest).getFirst().getName() + ":"
					+ lstReferencePairs.get(jbest).getSecond().getName());

			System.out.println("angle diff "
					+ lstTempalatePairs.get(ibest).getAngleInDegree() + " vs. "
					+ lstReferencePairs.get(jbest).getAngleInDegree());
			System.out.println(" matchrate: " + matches);
		}

		return bestTransformation;
	}

	/**
	 * Compares all points of two templates
	 * 
	 * @param transformation
	 * @return
	 */
	public double checkTemplates(Homogeneouse2DMatrix transformation) {

		return 0;
	}

	/**

	 * 
	 * @param transformation
	 * @return
	 */
	public double checkMatches(Homogeneouse2DMatrix transformation) {
		double matches = 0;

		System.out.println(transformation.toString());

		// Get template and match with reference template
		for (MinutiaPoint p : template2.getMinutiaPoints()) {
			// Clone and rotate minutia point
			MinutiaPoint rotatedPoint = transformation
					.multiply(new MinutiaPoint(p));
			MinutiaPoint nearestPoint = MinutiaUtils.getNearestPoint(template1,
					rotatedPoint);

			if (isInRange(rotatedPoint.getVector(), nearestPoint.getVector())
			// && isSameAngle(rotatedPoint.getAngle(),
			// nearestPoint.getAngle())
					&& rotatedPoint.getType() == nearestPoint.getType()) {
				matches++;
				if (logging)
					System.out.println("match between points " + rotatedPoint
							+ " and " + nearestPoint);
			} else {
				if (logging)
					System.out.println("NO match between points "
							+ rotatedPoint + " and " + nearestPoint);
			}

		}

		// for (int i = 0; i < mPairs.size(); i++) {
		// Vector v1, v2, rv1, rv2;
		//
		// MinutiaNighbourPair pair = mPairs.get(i);
		// MinutiaNighbourPair referencePair = mReferencePairs.get(i);
		// v1 = pair.getFirst().getVector();
		// v2 = pair.getSecond().getVector();
		// rv1 = referencePair.getFirst().getVector();
		// rv2 = referencePair.getSecond().getVector();
		//
		// rv1 = transformation.multiply(rv1);
		// rv2 = transformation.multiply(rv2);
		// boolean thewebairway = false;
		// if (thewebairway) {
		// if (isInRange(v1, rv1) && isInRange(v2, rv2)) {
		// matches++;
		// }
		// } else {
		// matches += getMatchScore(pair, referencePair);
		// }
		// }
		return matches;
	}

	private boolean isSameAngle(int angle, int angle2) {

		return Math.abs(angle2 - angle) <= ANGLE_TOLERANCE;
	}

	/**
	 * 
	 * @param pair
	 * @param referencePair
	 * @return a score between 0 and 1, witch tells us how much similar these
	 *         pairs are.
	 */
	private double getMatchScore(MinutiaNighbourPair pair,
			MinutiaNighbourPair referencePair) {
		double returnValue1 = 0;
		double returnValue2 = 0;
		try {
			double match1 = getMatchScore(pair.getFirst(),
					referencePair.getFirst());

			double match2 = getMatchScore(pair.getSecond(),
					referencePair.getSecond());
			// we do not know how the constelation is
			// can be p1p1 = p2p1 and p1p2 = p2p2 OR
			// can be p1p2 = p2p1 and p1p1 = p2p2
			// so we return the better result ;-)
			double reverse1 = getMatchScore(pair.getSecond(),
					referencePair.getFirst());

			double reverse2 = getMatchScore(pair.getFirst(),
					referencePair.getSecond());

			returnValue1 = (match1 + match2) / 2;
			returnValue2 = (reverse1 + reverse2) / 2;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue1 > returnValue2 ? returnValue1 : returnValue2;
	}

	private double getMatchScore(MinutiaPoint first, MinutiaPoint second)
			throws Exception {

		// every score should be between 0-1
		double maxAcceptedDistance = 10;
		double maxAcceptedAngle = 10;
		double typeScoreIfDoesntMatch = 0.2;

		double[] weight = { 0.4, 0.4, 0.2 };

		double scoreAngle = Math.abs(first.getAngle() - second.getAngle());
		scoreAngle = scoreAngle > maxAcceptedAngle ? 0
				: (maxAcceptedAngle - scoreAngle) / maxAcceptedAngle;
		double scoreDistance = first.getVector()
				.getDistance(second.getVector());
		scoreDistance = scoreDistance > maxAcceptedDistance ? 0
				: (maxAcceptedDistance - scoreDistance) / maxAcceptedDistance;
		double scoreType = first.getType() == first.getType() ? 1
				: typeScoreIfDoesntMatch;

		double retValue = FuzzyMachine.geneateFuzzyValue(weight, new double[] {
				scoreAngle, scoreDistance, scoreType });

		// todo what we do with the qualitity attribute first.getQuality()
		if (logging)
			System.out.println("match rate between " + first.getName()
					+ " and " + second.getName() + " is:" + retValue);
		return retValue;
	}

	private boolean isInRange(Vector r1, Vector r2) {

		if (r1.getDistance(r2) < TOLERANCE) {
			return true;
		}
		return false;
	}

	/**
	 * Wir analyisieren die beiden paare indem wir eine translation und falls
	 * n�tig eine rotation machen und schauen ob die punkte der beiden paare nun
	 * in der n�he sind. TODO noch nicht gehandelter fall: p(air)1p(oint)1 ist
	 * gleicher punkt wie p(air)2p(oint)2 (momentan wird nur verglichen p1p1 =
	 * p2p1
	 * 
	 * @param pair
	 * @param reference
	 * @return
	 */
	private Homogeneouse2DMatrix analystePairs(MinutiaNighbourPair pair,
			MinutiaNighbourPair reference) {
		Vector pair1point1 = new Vector(pair.getFirst().getxCoord(), pair
				.getFirst().getyCoord());
		Vector pair1point2 = new Vector(pair.getSecond().getxCoord(), pair
				.getSecond().getyCoord());

		Vector pair2point1 = new Vector(reference.getFirst().getxCoord(),
				reference.getFirst().getyCoord());
		Vector pair2point2 = new Vector(reference.getSecond().getxCoord(),
				reference.getSecond().getyCoord());
		double xtranslation = pair1point1.getX() - pair2point1.getX();
		double ytranslation = pair1point1.getY() - pair2point1.getY();

		// matrix for translation pair2 to pair1
		Homogeneouse2DMatrix translation = TransformationFactory
				.createTranslation(xtranslation, ytranslation);

		// translate them
		pair2point1 = translation.multiply(pair2point1);
		pair2point2 = translation.multiply(pair2point2);

		// falls die punkte nun schon in der n�he von einander sind machen wir
		// keine rotation
		// TODO eventuell trozdem eine rotation machen, da es m�glich w�re,
		// dass bei diesem paar der drehpunkt in der n�he ist und der
		// unroatated punkt nur kleine abweichung aufweist.
		if (isInRange(pair1point2, pair2point2)) {
			System.out
					.println("lucky bastard, they are already the same... no rotation needed");
			return translation;
		}

		// die punkte von p1p1 und p2p1 sind nun am gleichen ort

		// wir verschieben die punkte nun so, dass p1p1 (folglich auf p2p1) auf
		// den nullpunkt damit wir den winkel zwischen p1p2 und p2p2
		// ausrechnen k�nnen.

		double translatedp1p2x = pair1point2.x - pair1point1.x;
		double translatedp1p2y = pair1point2.y - pair1point1.y;

		double translatedp2p2x = pair2point2.x - pair1point1.x;
		double translatedp2p2y = pair2point2.y - pair1point1.y;

		double angleToTurn = new Vector(translatedp1p2x, translatedp1p2y)
				.getAngle(new Vector(translatedp2p2x, translatedp2p2y));

		// nun da wir den winkel haben und unser minutiabild um p1p1 (folglich
		// auf p2p1) drehen wollen m�ssen wir zuerst
		// eine translation auf den null punkt machen, drehen, und zur�ck
		// translatieren. (umgekehrte reihenfolge da matirx multiplikation)

		Homogeneouse2DMatrix transformation1 = TransformationFactory
				.createTranslation(pair1point1.getX(), pair1point1.getY());
		transformation1 = transformation1.multiply(TransformationFactory
				.createRotation(-angleToTurn));
		transformation1 = transformation1.multiply(TransformationFactory
				.createTranslation(-pair1point1.getX(), -pair1point1.getY()));

		// mit dieser multiplikation sollte nun p2p2 auf p1p2 liegen kommen.
		pair2point2 = transformation1.multiply(pair2point2);
		// was wier hier pr�fen
		if (isInRange(pair1point2, pair2point2)) {
			System.out
					.println("uiui last change, but you got the rotation baby!");
			// before we return we must add the translation, witch we done fist.
			return transformation1.multiply(translation);
		}

		return null;
	}

	public double getMatches() {
		return matches;
	}

	public int getMatchRate() {
		int matchs = (int) getMatches();
		int min = Math.min(mPairs.size(), lstReferencePairs.size());
		int percent = (int) Math.round((double) matchs / (double) min * 100);
		return percent;
	}

	public void printTransformation() {
		if (getTransformation() != null) {
			System.out.println(getTransformation());
		} else {
			System.out.println("no transformation");
		}

	}
}
