package pl.pwr.hiervis.dimensionReduction.distanceMeasures;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ManhattanTest {

    Manhattan distanceMeasure;
    double epsilon;

    @Before
    public void initialize() {
	distanceMeasure = new Manhattan();
	epsilon = 0;
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
	assertEquals(distanceMeasure.toString(), "Manhattan");
    }

}
