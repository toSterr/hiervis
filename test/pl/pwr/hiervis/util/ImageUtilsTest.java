package pl.pwr.hiervis.util;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.junit.Test;

public class ImageUtilsTest {

	BufferedImage bimg;
	
	public ImageUtilsTest() {
		bimg = new BufferedImage( 100, 50, BufferedImage.TYPE_INT_ARGB );
	}
	@Test
	public void testRotate() {
		BufferedImage newBuffimg= ImageUtils.rotate(bimg, 180);
		assertEquals(100, newBuffimg.getWidth());
		assertEquals(50, newBuffimg.getHeight());
		newBuffimg= ImageUtils.rotate(bimg, 90);
		assertEquals(50, newBuffimg.getWidth());
		assertEquals(100, newBuffimg.getHeight());
	}

	@Test
	public void testTrimImg() {
		BufferedImage newBuffimg= ImageUtils.trimImg(bimg, Color.BLACK);
		assertEquals(98, newBuffimg.getWidth());
		assertEquals(50, newBuffimg.getHeight());
		newBuffimg= ImageUtils.addBorder(bimg, 10, 10, 10, 10, Color.BLACK);
		assertEquals(120, newBuffimg.getWidth());
		assertEquals(70, newBuffimg.getHeight());
		newBuffimg= ImageUtils.trimImg(bimg, Color.black);
		assertEquals(98, newBuffimg.getWidth());
		assertEquals(50, newBuffimg.getHeight());
	}

	@Test
	public void testAddBorder() {
		BufferedImage newBuffimg= ImageUtils.addBorder(bimg, 10, 10, 10, 10, Color.WHITE);
		assertEquals(120, newBuffimg.getWidth());
		assertEquals(70, newBuffimg.getHeight());
	}

	@Test (expected = IllegalArgumentException.class)
	public void testSetBackgroud() {
		BufferedImage newBuffimg=ImageUtils.setBackgroud(bimg, Color.BLACK);
		newBuffimg= ImageUtils.trimImg(newBuffimg, Color.BLACK);
	}

}
