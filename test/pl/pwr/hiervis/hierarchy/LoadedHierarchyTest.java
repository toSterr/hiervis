package pl.pwr.hiervis.hierarchy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import basic_hierarchy.implementation.BasicNode;
import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.interfaces.Node;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.core.HVConfig;

public class LoadedHierarchyTest {

	Hierarchy h;
	LoadedHierarchy loadedHierarchy;
	LoadedHierarchy.Options o;
	
	public LoadedHierarchyTest() {
		h= TestCommon.getFourGroupsHierarchy();
		o = new LoadedHierarchy.Options(false, false, false, false, false);
		loadedHierarchy = new LoadedHierarchy(h, o);
	}

	@Test
	public void testIsProcessed() {
		assertEquals(false, loadedHierarchy.isProcessed());
		loadedHierarchy.processHierarchy(new HVConfig());
		assertEquals(true, loadedHierarchy.isProcessed());
	}

	@Test
	public void testGetMainHierarchy() {
		assertEquals(h, loadedHierarchy.getMainHierarchy());
	}

	@Test(expected =IllegalArgumentException.class)
	public void testGetNodeHierarchyThrowsErr() {
		assertEquals(false, loadedHierarchy.getNodeHierarchy( new BasicNode("id", null, false), false));
	}

	
	@Test
	public void testGetNodeHierarchy() {
		Node n = h.getRoot();
		assertEquals(11, h.getOverallNumberOfInstances() );
		assertEquals(2, loadedHierarchy.getNodeHierarchy(n, false).getOverallNumberOfInstances() );
	}

	@Test
	public void testIsOwnerOf() {
		assertEquals(true, loadedHierarchy.isOwnerOf(h));
		assertEquals(false, loadedHierarchy.isOwnerOf(TestCommon.getFourGroupsHierarchy()));
	}

	@Test
	public void testGetVisualizationStateForIntInt() {
		VisualizationState state= loadedHierarchy.getVisualizationStateFor(100, 100);
		assertEquals (-1, state.getResolutionWidth(), 0);
		assertEquals (-1, state.getResolutionHeight(), 0);
	}

	@Test
	public void testGetVisualizationStateForPairOfIntegerInteger() {
		VisualizationState state= loadedHierarchy.getVisualizationStateFor( Pair.of( 100, 100 ));
		assertEquals (-1, state.getResolutionWidth(), 0);
		assertEquals (-1, state.getResolutionHeight(), 0);
	}

	@Test
	public void testGetTree() {
		assertEquals(null, loadedHierarchy.getTree());
		loadedHierarchy.processHierarchy(new HVConfig());
		assertNotEquals(null, loadedHierarchy.getTree());
	}

	@Test
	public void testGetTreeLayoutData() {
		assertEquals(null, loadedHierarchy.getTreeLayoutData());
		loadedHierarchy.processHierarchy(new HVConfig());
		assertNotEquals(null, loadedHierarchy.getTreeLayoutData());
	}

	@Test
	public void testGetInstanceTable() {
		assertEquals(null, loadedHierarchy.getInstanceTable());
		loadedHierarchy.processHierarchy(new HVConfig());
		assertNotEquals(null, loadedHierarchy.getInstanceTable());
	}

	@Test
	public void testSetSelectedRow() {
		assertEquals(0, loadedHierarchy.getSelectedRow());
		loadedHierarchy.setSelectedRow(10);
		assertEquals(10, loadedHierarchy.getSelectedRow());
	}

	@Test
	public void testGetSelectedRow() {
		assertEquals(0, loadedHierarchy.getSelectedRow());
	}

	@Test
	public void testDispose() {
		loadedHierarchy.processHierarchy(new HVConfig());
		assertNotEquals(null, loadedHierarchy.getInstanceTable());
		assertNotEquals(null, loadedHierarchy.getTree());
		assertNotEquals(null, loadedHierarchy.getTreeLayoutData());
		loadedHierarchy.dispose();
		assertEquals(null, loadedHierarchy.getInstanceTable());
		assertEquals(null, loadedHierarchy.getTree());
		assertEquals(null, loadedHierarchy.getTreeLayoutData());
	}

}
