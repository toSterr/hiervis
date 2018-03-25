package pl.pwr.hiervis.util;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class HSVTest {

	HSV hsv;
	
	public HSVTest() {
		hsv = new HSV(0,1,1);
	}
	
	@Test
	public void testHSVFloatFloatFloat() {
		System.out.println( hsv.toColor().toString());
	}

	@Test
	public void testHSVHSV() {
		HSV newHsv= new HSV(hsv);
		assertEquals(0, newHsv.getHue(),0 );
		assertEquals(1, newHsv.getSaturation(),0 );
		assertEquals(1, newHsv.getBrightness(),0 );
	}

	@Test
	public void testHSVColor() {
		HSV newHsv= new HSV(Color.RED);
		assertEquals(0, newHsv.getHue(),0 );
		assertEquals(1, newHsv.getSaturation(),0 );
		assertEquals(1, newHsv.getBrightness(),0 );
		newHsv= new HSV(Color.GREEN);
		assertEquals(0.33, newHsv.getHue(),0.01 );
		assertEquals(1, newHsv.getSaturation(),0 );
		assertEquals(1, newHsv.getBrightness(),0 );
	}

	@Test
	public void testSetGet() {
		assertEquals(0, hsv.getHue(),0 );
		assertEquals(1, hsv.getSaturation(),0 );
		assertEquals(1, hsv.getBrightness(),0 );
		
		hsv.set(0.5f, 0.5f, 0.5f);
		assertEquals(0.5f, hsv.getHue(),0 );
		assertEquals(0.5f, hsv.getSaturation(),0 );
		assertEquals(0.5f, hsv.getBrightness(),0 );
	}

	@Test
	public void testSetHue() {
		assertEquals(0, hsv.getHue(),0 );
		hsv.setHue(0.5f);
		assertEquals(0.5f, hsv.getHue(),0 );
	}

	@Test
	public void testSetSaturation() {
		assertEquals(1, hsv.getSaturation(),0 );
		hsv.setSaturation(0.5f);
		assertEquals(0.5f, hsv.getSaturation(),0 );
	}

	@Test
	public void testSetValue() {
		assertEquals(1, hsv.getValue(),0);
		hsv.setValue(0.5f);
		assertEquals(0.5, hsv.getValue(),0);
	}

	@Test
	public void testSetBrightness() {
		assertEquals(1, hsv.getBrightness(),0 );
		hsv.setBrightness(0.5f);
		assertEquals(0.5f, hsv.getBrightness(),0 );
	}

	@Test
	public void testToColor() {
		assertEquals(Color.RED, hsv.toColor());
		assertEquals(Color.BLUE, new HSV(0.6665f,1,1).toColor()  );
	}

	@Test
	public void testToString() {
		assertEquals("HSV { 0.0, 1.0, 1.0 }", hsv.toString());
	}

}
