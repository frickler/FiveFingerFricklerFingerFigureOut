package ch.frickler.biometrie.test.transformation;

import org.junit.Test;

import ch.frickler.biometrie.transformation.Homogeneouse2DMatrix;
import ch.frickler.biometrie.transformation.TransformationFactory;
import ch.frickler.biometrie.transformation.Vector;

import junit.framework.TestCase;

public class VectorTest extends TestCase {

	@Test
	public void testGetAngle() {
		
		Vector v1 = new Vector(0,10);
		Vector v2 = new Vector(10,0);
		//-90 grad
		assertEquals(Math.PI/2 + Math.PI, v2.getAngle(v1));
		//90 grad
		assertEquals(Math.PI/2, v1.getAngle(v2));
		
		double angle = v1.getAngle(v2);
		
		
		Homogeneouse2DMatrix t = TransformationFactory.createRotation(-angle);
		v1 = t.multiply(v1);
		
		assertEquals(v2.getX(), v1.getX());
		//assertEquals(v2.getY(), v1.getY());
	}
	
	
	
}
