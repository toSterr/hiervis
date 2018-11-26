package pl.pwr.hiervis.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class UtilsTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }
    
	@Test
	public void testWaitUntilActivitiesAreFinished() {
		fail("Not yet implemented");
	}

	@Test
	public void testFitToBounds() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnzoom() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTransform() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDisplaySnapshot() {
		fail("Not yet implemented");
	}

	@Test
	public void testTimed() {
		class HelloRunnable implements Runnable {

		    public void run() {
		        System.out.println("Runnable stuff");
		    }
		}
		Runnable helloRunnable= new HelloRunnable();
		Logger log= LogManager.getLogger();
		Utils.timed( log , "prefix",  helloRunnable );
		assertEquals("Runnable stuff\r\n" + 
				"TRACE UtilsTest - prefix: 0ms", outContent.toString().substring(0, 45) );
	}

	@Test
	public void testRgba() {
		assertEquals (-16777216, Utils.rgba(Color.black));
		assertEquals (-1, Utils.rgba(Color.white));
		assertEquals (-65536, Utils.rgba(Color.red));
	}

	@Test
	public void testCalculateBoundingRectForCluster() {
		fail("Not yet implemented");
	}

	@Test
	public void testNormalize() {
		assertEquals( 0.5, Utils.normalize(0, -5, 5, 0, 1),0);
	}

	@Test
	public void testClampIntIntInt() {
		assertEquals(5, Utils.clamp(0, 5, 10));
	}

	@Test
	public void testClampLongLongLong() {
		assertEquals(5l, Utils.clamp(0l, 5l, 10l));
	}

	@Test
	public void testClampFloatFloatFloat() {
		assertEquals(5.0f, Utils.clamp(0.0f, 5.0f, 10.0f), 0);
	}

	@Test
	public void testClampDoubleDoubleDouble() {
		assertEquals(5.0, Utils.clamp(0.0, 5.0, 10.0), 0);
	}

	@Test
	public void testCreateArray() {
		Double [] test= Utils.createArray(Double.class, 5);
		assertEquals(5, test.length);
	}

	@Test
	public void testMerge() {
		Double [] test= Utils.createArray(Double.class, 5);
		Double [] test2= Utils.createArray(Double.class, 5);
		Double [] test3=Utils.merge(test, test2);
		assertEquals(10, test3.length);

	}

}
