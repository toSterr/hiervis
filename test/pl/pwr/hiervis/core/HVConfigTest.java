package pl.pwr.hiervis.core;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import org.junit.Test;

public class HVConfigTest {

	HVConfig hvConfig;
	
	public HVConfigTest() {
		hvConfig= new HVConfig();
	}
	
	//osobne test casy
	@Test
	public void testGet()
	{
		assertEquals(Color.red, hvConfig.getCurrentGroupColor());
		assertEquals(Color.green, hvConfig.getChildGroupColor ());
		assertEquals(Color.blue.brighter(), hvConfig.getAncestorGroupColor() );
		assertEquals(Color.lightGray, hvConfig.getOtherGroupColor() );
		assertEquals(Color.magenta, hvConfig.getHistogramColor() );
		assertEquals(new Color( -1 ), hvConfig.getBackgroundColor() );
		assertEquals(100, hvConfig.getNumberOfHistogramBins() );
		assertEquals(3, hvConfig.getPointSize());
		assertEquals(3, hvConfig.getDoubleFormatPrecision());
		assertEquals(false, hvConfig.isMeasuresUseSubtree());
		assertEquals("", hvConfig.getPreferredLookAndFeel());
		assertEquals(false, hvConfig.isStopXfceLafChange());
		assertEquals(2, hvConfig.getHkClusters());
		assertEquals(10, hvConfig.getHkIterations());
		assertEquals(10, hvConfig.getHkRepetitions());
		assertEquals(2, hvConfig.getHkDendrogramHeight());
		assertEquals(-1, hvConfig.getHkMaxNodes());
		assertEquals(10, hvConfig.getHkEpsilon());
		assertEquals(5, hvConfig.getHkLittleValue());
		assertEquals(true, hvConfig.isHkWithTrueClass());
		assertEquals(false, hvConfig.isHkWithInstanceNames());
		assertEquals(true, hvConfig.isHkWithDiagonalMatrix());
		assertEquals(false, hvConfig.isHkNoStaticCenter());
		assertEquals(false, hvConfig.isHkGenerateImages());
		assertEquals(Color.black, hvConfig.getParentGroupColor());
	}
	
	@Test
	public void testFromHVConfig() {
		HVConfig o = HVConfig.from( hvConfig );
		assertNotEquals(o, hvConfig);
	}

	@Test
	public void testFromFile() throws IOException {
		hvConfig= HVConfig.from( new File("out/newFileHVConfigLoadConf.txt") );
		assertEquals(Color.red, hvConfig.getChildGroupColor());
	}

	//dlaczego nie robi kopi?
	@Test
	public void testCopy() {
		hvConfig.setAncestorGroupColor(Color.black);
		HVConfig o = hvConfig.copy();

		assertNotEquals(o, hvConfig);
	}

	@Test
	public void testTo() throws IOException {
		Files.deleteIfExists( Paths.get("out/newFileHVConfig.txt") );
		hvConfig.to( new File("out/newFileHVConfig.txt"));
		assertEquals(true, new File("out/newFileHVConfig.txt").exists() );
	}

	@Test
	public void testSetCurrentLevelColor() {
		assertEquals(Color.red, hvConfig.getCurrentGroupColor());
		hvConfig.setCurrentLevelColor(Color.black);
		assertEquals(Color.black, hvConfig.getCurrentGroupColor());
	}

	@Test
	public void testSetChildGroupColor() {
		hvConfig.setChildGroupColor(Color.black);
		assertEquals(Color.black, hvConfig.getChildGroupColor());
	}

	@Test
	public void testSetParentGroupColor() {
		hvConfig.setParentGroupColor(Color.black);
		assertEquals(Color.black, hvConfig.getParentGroupColor() );
	}

	@Test
	public void testSetOtherGroupColor() {
		hvConfig.setOtherGroupColor(Color.black);
		assertEquals(Color.black, hvConfig.getOtherGroupColor()  );
	}


	@Test
	public void testSetAncestorGroupColor() {
		hvConfig.setAncestorGroupColor(Color.black);
		assertEquals(Color.black, hvConfig.getAncestorGroupColor()  );
	}

	@Test
	public void testSetBackgroundColor() {
		hvConfig.setBackgroundColor(Color.black);
		assertEquals(Color.black, hvConfig.getBackgroundColor()  );
	}


	@Test
	public void testSetHistogramColor() {
		hvConfig.setHistogramColor(Color.black);
		assertEquals(Color.black, hvConfig.getHistogramColor()  );
	}

	@Test
	public void testSetPointSize() {
		hvConfig.setPointSize(150);
		assertEquals(150 , hvConfig.getPointSize()  );
	}

	@Test
	public void testSetNumberOfHistogramBins() {
		hvConfig.setNumberOfHistogramBins(150);
		assertEquals(150 , hvConfig.getNumberOfHistogramBins()  );
	}

	@Test
	public void testSetDoubleFormatPrecision() {
		hvConfig.setDoubleFormatPrecision(150);
		assertEquals(150 , hvConfig.getDoubleFormatPrecision()  );
	}
	
	@Test
	public void testSetMeasuresUseSubtree() {
		hvConfig.setMeasuresUseSubtree(false);
		assertEquals(true , hvConfig.isMeasuresUseSubtree()  );
	}

	@Test
	public void testSetPreferredLookAndFeel() {
		hvConfig.setPreferredLookAndFeel("look");
		assertEquals("look" , hvConfig.getPreferredLookAndFeel()  );
	}

	@Test
	public void testSetStopXfceLafChange() {
		hvConfig.setStopXfceLafChange(false);
		assertEquals(false , hvConfig.isStopXfceLafChange()  );
	}

	@Test
	public void testSetHkClusters() {
		hvConfig.setHkClusters(150);
		assertEquals(150 , hvConfig.getHkClusters()  );
	}

	@Test
	public void testSetHkIterations() {
		hvConfig.setHkIterations(150);
		assertEquals(150 , hvConfig.getHkIterations()  );
	}

	@Test
	public void testSetHkRepetitions() {
		hvConfig.setHkRepetitions(150);
		assertEquals(150 , hvConfig.getHkRepetitions()  );		
	}

	@Test
	public void testSetHkDendrogramHeight() {
		hvConfig.setHkDendrogramHeight(150);
		assertEquals(150 , hvConfig.getHkDendrogramHeight()  );
	}

	@Test
	public void testSetHkMaxNodes() {
	hvConfig.setHkMaxNodes(150);
	assertEquals(150 , hvConfig.getHkMaxNodes()  );
	}

	@Test
	public void testSetHkEpsilon() {
		hvConfig.setHkEpsilon(150);
		assertEquals(150 , hvConfig.getHkEpsilon()  );
	}

	@Test
	public void testSetHkLittleValue() {
		hvConfig.setHkLittleValue(150);
		assertEquals(150 , hvConfig.getHkLittleValue()  );
	}

	@Test
	public void testSetHkWithTrueClass() {
		hvConfig.setHkWithTrueClass(false);
		assertEquals(false , hvConfig.isHkWithTrueClass()  );
	}

	@Test
	public void testSetHkWithInstanceNames() {
		hvConfig.setHkWithInstanceNames(false);
		assertEquals(false , hvConfig.isHkWithInstanceNames()  );
	}

	@Test
	public void testSetHkWithDiagonalMatrix() {
		hvConfig.setHkWithDiagonalMatrix(false);
		assertEquals(false , hvConfig.isHkWithDiagonalMatrix()  );
	}
	@Test
	public void testSetHkNoStaticCenter() {
		hvConfig.setHkNoStaticCenter(false);
		assertEquals(false , hvConfig.isHkNoStaticCenter()  );
	}

	@Test
	public void testSetHkGenerateImages() {
		hvConfig.setHkGenerateImages(false);
		assertEquals(false , hvConfig.isHkGenerateImages()  );
	}

	@Test
	public void testEqualsObject() {
		assertEquals(false, hvConfig.equals((Object)null));
		assertEquals(false, hvConfig.equals( new double [] {} ));
		Object o = new HVConfig();
		assertEquals(true, hvConfig.equals( o ));
	}

	@Test
	public void testEqualsHVConfig() {
		HVConfig o = new HVConfig();
		o.setAncestorGroupColor(Color.black);
		assertEquals(false, hvConfig.equals( o ));
		o = new HVConfig();
		assertEquals(true, hvConfig.equals( o ));
	}

}
