package ch.frickler.biometrie.test;

import junit.framework.Assert;

import org.junit.Test;

import ch.frickler.biometrie.data.MinutiaNighbourPair;
import ch.frickler.biometrie.data.MinutiaPoint;
import ch.frickler.biometrie.transformation.Homogeneouse2DMatrix;
import ch.frickler.biometrie.transformation.TransformationFactory;
import ch.frickler.biometrie.transformation.Vector;

public class RotationTest {

	/*
	 * MinutiaNighbourPair reference) { Vector pair1point1 = new
	 * Vector(pair.getFirst().getxCoord(), pair .getFirst().getyCoord()); Vector
	 * pair1point2 = new Vector(pair.getSecond().getxCoord(), pair
	 * .getSecond().getyCoord());
	 * 
	 * Vector pair2point1 = new Vector(reference.getFirst().getxCoord(),
	 * reference.getFirst().getyCoord()); Vector pair2point2 = new
	 * Vector(reference.getSecond().getxCoord(),
	 * reference.getSecond().getyCoord()); double xtranslation =
	 * pair1point1.getX() - pair2point1.getX(); double ytranslation =
	 * pair1point1.getY() - pair2point1.getY();
	 * 
	 * // matrix for translation pair2 to pair1 Homogeneouse2DMatrix translation
	 * = TransformationFactory .createTranslation(xtranslation, ytranslation);
	 * 
	 * // translate them pair2point1 = translation.multiply(pair2point1);
	 * pair2point2 = translation.multiply(pair2point2);
	 * 
	 * // falls die punkte nun schon in der nähe von einander sind machen wir //
	 * keine rotation // TODO eventuell trozdem eine rotation machen, da es
	 * möglich wäre, // dass bei diesem paar der drehpunkt in der nähe ist und
	 * der // unroatated punkt nur kleine abweichung aufweist. if
	 * (isInRange(pair1point2, pair2point2)) { System.out
	 * .println("lucky bastard, they are already the same... no rotation needed"
	 * ); return translation; }
	 * 
	 * // die punkte von p1p1 und p2p1 sind nun am gleichen ort
	 * 
	 * // wir verschieben die punkte nun so, dass p1p1 (folglich auf p2p1) auf
	 * // den nullpunkt damit wir den winkel zwischen p1p2 und p2p2 //
	 * ausrechnen können.
	 * 
	 * double translatedp1p2x = pair1point2.x - pair1point1.x; double
	 * translatedp1p2y = pair1point2.y - pair1point1.y;
	 * 
	 * double translatedp2p2x = pair2point2.x - pair1point1.x; double
	 * translatedp2p2y = pair2point2.y - pair1point1.y;
	 * 
	 * double angleToTurn = new Vector(translatedp1p2x, translatedp1p2y)
	 * .getAngle(new Vector(translatedp2p2x, translatedp2p2y));
	 * 
	 * // nun da wir den winkel haben und unser minutiabild um p1p1 (folglich //
	 * auf p2p1) drehen wollen müssen wir zuerst // eine translation auf den
	 * null punkt machen, drehen, und zurück // translatieren. (umgekehrte
	 * reihenfolge da matirx multiplikation)
	 * 
	 * Homogeneouse2DMatrix transformation1 = TransformationFactory
	 * .createTranslation(pair1point1.getX(), pair1point1.getY());
	 * transformation1 = transformation1.multiply(TransformationFactory
	 * .createRotation(-angleToTurn)); transformation1 =
	 * transformation1.multiply(TransformationFactory
	 * .createTranslation(-pair1point1.getX(), -pair1point1.getY()));
	 * 
	 * // mit dieser multiplikation sollte nun p2p2 auf p1p2 liegen kommen.
	 * pair2point2 = transformation1.multiply(pair2point2);
	 */

	@Test
	public void TestMinutiaTranslation() {

		MinutiaNighbourPair pair = GetPair(100, 0, 100, 50);
		MinutiaNighbourPair reference = GetPair(200, 0, 200, 50);

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

		Assert.assertEquals(pair2point1.x, pair1point1.x);
		Assert.assertEquals(pair2point1.y, pair1point1.y);
		Assert.assertEquals(pair2point2.x, pair1point2.x);
		Assert.assertEquals(pair2point2.y, pair1point2.y);

	}

	@Test
	public void TestMinutiaRotation() {

		MinutiaNighbourPair pair = GetPair(0, 0, 0, 50);
		MinutiaNighbourPair reference = GetPair(0, 0, 50, 0);

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

		// die punkte von p1p1 und p2p1 sind nun am gleichen ort

		// wir verschieben die punkte nun so, dass p1p1 (folglich auf p2p1) auf
		// den nullpunkt damit wir den winkel zwischen p1p2 und p2p2
		// ausrechnen können.

		double translatedp1p2x = pair1point2.x - pair1point1.x;
		double translatedp1p2y = pair1point2.y - pair1point1.y;

		double translatedp2p2x = pair2point2.x - pair1point1.x;
		double translatedp2p2y = pair2point2.y - pair1point1.y;

		double angleToTurn = new Vector(translatedp1p2x, translatedp1p2y)
				.getAngle(new Vector(translatedp2p2x, translatedp2p2y));

		double angleToTurnDegree = new Vector(translatedp1p2x, translatedp1p2y)
				.getAngleDegree(new Vector(translatedp2p2x, translatedp2p2y));

		Assert.assertEquals(90.0, angleToTurnDegree);

	}

	@Test
	public void TestMinutiaRotation2() {

		MinutiaNighbourPair pair = GetPair(0, 0, 0, 50);
		MinutiaNighbourPair reference = GetPair(0, 0, 50, 0);

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

		// die punkte von p1p1 und p2p1 sind nun am gleichen ort

		// wir verschieben die punkte nun so, dass p1p1 (folglich auf p2p1) auf
		// den nullpunkt damit wir den winkel zwischen p1p2 und p2p2
		// ausrechnen können.

		double translatedp1p2x = pair1point2.x - pair1point1.x;
		double translatedp1p2y = pair1point2.y - pair1point1.y;

		double translatedp2p2x = pair2point2.x - pair1point1.x;
		double translatedp2p2y = pair2point2.y - pair1point1.y;

		double angleToTurn = new Vector(translatedp1p2x, translatedp1p2y)
				.getAngle(new Vector(translatedp2p2x, translatedp2p2y));

		// nun da wir den winkel haben und unser minutiabild um p1p1 (folglich
		// auf p2p1) drehen wollen müssen wir zuerst
		// eine translation auf den null punkt machen, drehen, und zurück
		// translatieren. (umgekehrte reihenfolge da matirx multiplikation)

		Homogeneouse2DMatrix transformation1 = TransformationFactory
				.createTranslation(pair1point1.getX(), pair1point1.getY());
		transformation1 = transformation1.multiply(TransformationFactory
				.createRotation(-angleToTurn));
		transformation1 = transformation1.multiply(TransformationFactory
				.createTranslation(-pair1point1.getX(), -pair1point1.getY()));

		pair2point2 = transformation1.multiply(pair2point2);
		transformation1.print();
		Assert.assertEquals(pair2point1.x, pair1point1.x);
		Assert.assertEquals(pair2point1.y, pair1point1.y);
		Assert.assertEquals(pair2point2.x, pair1point2.x);
		Assert.assertEquals(pair2point2.y, pair1point2.y);

	}

	@Test
	public void TestMinutiaRotation3() {

		MinutiaNighbourPair pair = GetPair(20, 20, 20, 40);
		MinutiaNighbourPair reference = GetPair(60, 40, 40, 40);

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

		System.out.println("xtranslation "+xtranslation);
		System.out.println("ytranslation "+ytranslation);
		
		// matrix for translation pair2 to pair1
		Homogeneouse2DMatrix translation = TransformationFactory
				.createTranslation(xtranslation, ytranslation);

		// translate them
		pair2point1 = translation.multiply(pair2point1);
		pair2point2 = translation.multiply(pair2point2);

		// die punkte von p1p1 und p2p1 sind nun am gleichen ort

		// wir verschieben die punkte nun so, dass p1p1 (folglich auf p2p1) auf
		// den nullpunkt damit wir den winkel zwischen p1p2 und p2p2
		// ausrechnen können.

		double translatedp1p2x = pair1point2.x - pair1point1.x;
		double translatedp1p2y = pair1point2.y - pair1point1.y;

		double translatedp2p2x = pair2point2.x - pair1point1.x;
		double translatedp2p2y = pair2point2.y - pair1point1.y;

		double angleToTurn = new Vector(translatedp1p2x, translatedp1p2y)
				.getAngle(new Vector(translatedp2p2x, translatedp2p2y));

		double angleToTurnDegree = new Vector(translatedp1p2x, translatedp1p2y)
				.getAngleDegree(new Vector(translatedp2p2x, translatedp2p2y));

		System.out.println("angleToTurnDegree: " + angleToTurnDegree
				+ " angleToTurn: " + angleToTurn);

		// nun da wir den winkel haben und unser minutiabild um p1p1 (folglich
		// auf p2p1) drehen wollen müssen wir zuerst
		// eine translation auf den null punkt machen, drehen, und zurück
		// translatieren. (umgekehrte reihenfolge da matirx multiplikation)

		Homogeneouse2DMatrix transformation1 = TransformationFactory
				.createTranslation(pair1point1.getX(), pair1point1.getY());
		transformation1 = transformation1.multiply(TransformationFactory
				.createRotation(angleToTurn));
		transformation1 = transformation1.multiply(TransformationFactory
				.createTranslation(-pair1point1.getX(), -pair1point1.getY()));

		System.out.println("pair1point1: "+pair1point1);
		System.out.println("pair2point1: "+pair2point1);
		System.out.println("\npair1point2: "+pair1point2);
		System.out.println("pair2point2: "+pair2point2);
		
		pair2point2 = transformation1.multiply(pair2point2);
		pair2point1 = transformation1.multiply(pair2point1);
		
		transformation1.print();
		System.out.println("pair1point1: "+pair1point1);
		System.out.println("pair2point1: "+pair2point1);
		System.out.println("\npair1point2: "+pair1point2);
		System.out.println("pair2point2: "+pair2point2);
		Assert.assertEquals(pair2point1.x, pair1point1.x);
		Assert.assertEquals(pair2point1.y, pair1point1.y);
		Assert.assertEquals(pair2point2.x, pair1point2.x);
		Assert.assertEquals(pair2point2.y, pair1point2.y);

	}

	@Test
	public void RotationTestForward() {
		Homogeneouse2DMatrix transformation = TransformationFactory
				.createRotation(Math.PI / 2);
		Vector v = new Vector(50, 0);
		Vector rotated = transformation.multiply(v);

		Assert.assertEquals(rotated.x, 0.0);
		Assert.assertEquals(rotated.y, -50.0);
	}

	@Test
	public void RotationTestBackward() {
		double radian = 90.0 * Math.PI / 180;
		Homogeneouse2DMatrix transformation = TransformationFactory
				.createRotation(radian);
		Vector v = new Vector(50, 0);
		Vector rotated = transformation.multiply(v);
		transformation.print();
		Assert.assertEquals(rotated.x, 0.0);
		Assert.assertEquals(rotated.y, -50.0);
	}

	private MinutiaNighbourPair GetPair(int x, int y, int x2, int y2) {
		MinutiaPoint first = new MinutiaPoint(-1, 1);
		first.setxCoord(x);
		first.setyCoord(y);
		MinutiaPoint second = new MinutiaPoint(-1, 1);
		second.setxCoord(x2);
		second.setyCoord(y2);

		return new MinutiaNighbourPair(first, second);
	}
}
