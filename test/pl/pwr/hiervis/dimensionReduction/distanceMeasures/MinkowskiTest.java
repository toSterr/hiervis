package pl.pwr.hiervis.dimensionReduction.distanceMeasures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class MinkowskiTest {

    Minkowski distanceMeasure;
    double epsilon;

    @Before
    public void initialize() {
	distanceMeasure = new Minkowski();
	epsilon = 0;
    }

    @Test
    public void testMinkowski() {
	assertNotEquals(distanceMeasure, null);
    }

    @Test
    public void testMinkowskiDouble() {
	distanceMeasure = null;
	assertEquals(distanceMeasure, null);
	distanceMeasure = new Minkowski(3.2);
	assertNotEquals(distanceMeasure, null);
    }

    @Test
    public void testGetDistance() {
	double distance = distanceMeasure.getDistance(new double[] { 1, 1 }, new double[] { 1, 2 });
	assertEquals(distance, 1, epsilon);
    }

    @Test(expected = java.lang.AssertionError.class)
    public void testGetDistanceThrowsAssertionError() {
	distanceMeasure.getDistance(new double[] { 1, 1, 2 }, new double[] { 1, 2 });
    }

    @Test
    public void testToString() {
	assertEquals(distanceMeasure.toString(), "Minkowski");
    }

}
