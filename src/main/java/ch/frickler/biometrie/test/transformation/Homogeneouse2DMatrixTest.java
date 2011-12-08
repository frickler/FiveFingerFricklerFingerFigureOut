package ch.frickler.biometrie.test.transformation;

import org.junit.Test;

import ch.frickler.biometrie.transformation.Homogeneouse2DMatrix;

import junit.framework.TestCase;

public class Homogeneouse2DMatrixTest extends TestCase {

	@Test
	public void testMultiply() {
		Homogeneouse2DMatrix m1 = new Homogeneouse2DMatrix(3.0, 2.0, 4.0, 5.0, 6.0, 7.0);
		Homogeneouse2DMatrix m2 = new Homogeneouse2DMatrix(7.0,5.0,4.0,2.0,3.0,6.0);
		
		Homogeneouse2DMatrix result = m1.multiply(m2);
		
		assertEquals(29.0, result.getA(), 0.0);
		assertEquals(19.0f,result.getB(),0.0);
		assertEquals(48.0,result.getC(),0.0);
		assertEquals(30.0, result.getD(),0.0);
		assertEquals(27.0, result.getTx(),0.0);
		assertEquals(49.0, result.getTy(),0.0);
		
		
	}
}
