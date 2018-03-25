package pl.pwr.hiervis.hierarchy;

import static org.junit.Assert.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.junit.Test;

public class VisualizationStateTest {

	VisualizationState state;
	
	public VisualizationStateTest() {
		state = new VisualizationState();
	}
	
	@Test
	public void testVisualizationState() {
		assertEquals(-1, state.getResolutionWidth(), 0);
		assertEquals(-1, state.getResolutionHeight(), 0);
		assertEquals(new AffineTransform(), state.getTransform());
	}

	@Test
	public void testStore() {
		fail("Not yet implemented");
	}

	@Test
	public void testApplyTo() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetResolutionWidth() {
		state.setResolutionWidth(100);
		assertEquals(100, state.getResolutionWidth(), 0);
	}

	@Test
	public void testSetResolutionHeight() {
		state.setResolutionHeight(100);
		assertEquals(100, state.getResolutionHeight(), 0);
	}

	@Test
	public void testSetResolution() {
		state.setResolution(100, 100);
		assertEquals(100, state.getResolutionHeight(), 0);
		assertEquals(100, state.getResolutionWidth(), 0);
	}

	@Test
	public void testResetResolution() {
		state.setResolution(100, 100);
		assertEquals(100, state.getResolutionHeight(), 0);
		assertEquals(100, state.getResolutionWidth(), 0);
		state.resetResolution();
		assertEquals(-1, state.getResolutionWidth(), 0);
		assertEquals(-1, state.getResolutionHeight(), 0);
	}

	@Test (expected = IllegalArgumentException.class )
	public void testSetTransformThrowsError() {
		state.setTransform(null);
	}
	
	@Test
	public void testSetTransform() {
		AffineTransform af= new AffineTransform(1, 1, 1, 1, 2, 2);
		assertNotEquals(af, state.getTransform());
		state.setTransform(af);
		assertEquals(af, state.getTransform());
	}
	
	@Test
	public void testGetResolutionWidth() {
		assertEquals(-1, state.getResolutionWidth(), 0);
	}

	@Test
	public void testGetResolutionHeight() {
		assertEquals(-1, state.getResolutionHeight(), 0);
	}

	@Test
	public void testGetResolutionRect() {
		Rectangle2D.Double rect= new Rectangle2D.Double( 0, 0, 100, 100 );
		assertEquals( null, state.getResolutionRect());
		state.setResolution(100, 100);
		assertEquals( rect, state.getResolutionRect());
		
		
		
	}

	@Test
	public void testGetTransform() {
		assertEquals(new AffineTransform(), state.getTransform());
	}

}
