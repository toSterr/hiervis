package pl.pwr.hiervis.util;

import static org.junit.Assert.*;


import java.util.Map;

import org.junit.Test;

import basic_hierarchy.implementation.BasicNode;
import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class HierarchyUtilsTest2 {

	Hierarchy h;
	LoadedHierarchy l;
	
	public HierarchyUtilsTest2() {
		h= TestCommon.getFourGroupsHierarchy();
		l=new LoadedHierarchy(h, new LoadedHierarchy.Options(false, false, false, false, false));
	}
	@Test
	public void testMergeLoadedHierarchyLoadedHierarchyString() {
		fail("Not yet implemented");
	}

	@Test
	public void testMergeHierarchyHierarchyString() {
		fail("Not yet implemented");
	}

	@Test
	public void testFlattenHierarchy() {
		assertEquals(4, l.getMainHierarchy().getNumberOfGroups());
		LoadedHierarchy newL=  HierarchyUtils.flattenHierarchy(l);
		assertEquals(1, newL.getMainHierarchy().getNumberOfGroups());
	}

	@Test
	public void testGetClassCountMap() {
		Map<String, Integer> map = HierarchyUtils.getClassCountMap(h);
		assertEquals(4, (int) map.get("gen.0") );
		assertEquals(2, (int) map.get("gen.0.0") );
		assertEquals(3, (int) map.get("gen.0.0.0") );
		assertEquals(2, (int) map.get("gen.0.1") );
	}

	@Test
	public void testComputeClassCountMap() {
		Map<String, Integer> map = HierarchyUtils.computeClassCountMap(h.getRoot());
		assertEquals(4, (int) map.get("gen.0") );
		assertEquals(2, (int) map.get("gen.0.0") );
		assertEquals(3, (int) map.get("gen.0.0.0") );
		assertEquals(2, (int) map.get("gen.0.1") );
	}

	@Test
	public void testRemove() {
		Hierarchy newH = HierarchyUtils.remove(h, "gen.0.1");
		assertEquals(11, h.getOverallNumberOfInstances());
		assertEquals(8, newH.getOverallNumberOfInstances());
	}

	@Test
	public void testWrapNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testContains() {
		assertEquals( true, HierarchyUtils.contains(h, h.getRoot()));
		assertEquals( false, HierarchyUtils.contains(h,  new BasicNode("nowe", null, false)   ));
	}

	@Test
	public void testSubHierarchy() {
		fail("Not yet implemented");
	}

	@Test
	public void testRebase() {
		fail("Not yet implemented");
	}

	@Test
	public void testCloneHierarchyBooleanPredicateOfNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testCloneNodeBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testCloneInstance() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindGroupLoadedHierarchyInt() {
		assertEquals("gen.0", HierarchyUtils.findGroup(l, 0).getId());
		assertEquals("gen.0.0", HierarchyUtils.findGroup(l, 1).getId());
		assertEquals("gen.0.1", HierarchyUtils.findGroup(l, 2).getId());
		assertEquals("gen.0.0.0", HierarchyUtils.findGroup(l, 3).getId());
		assertEquals(null, HierarchyUtils.findGroup(l, 4));
	}

	@Test
	public void testFindGroupLoadedHierarchyString() {
		assertEquals("gen.0", HierarchyUtils.findGroup(l, "gen.0").getId());
		assertEquals("gen.0.0", HierarchyUtils.findGroup(l, "gen.0.0").getId());
		assertEquals("gen.0.1", HierarchyUtils.findGroup(l, "gen.0.1").getId());
		assertEquals("gen.0.0.0", HierarchyUtils.findGroup(l, "gen.0.0.0").getId());
		assertEquals(null, HierarchyUtils.findGroup(l, "gen.0.2"));
	}

	@Test
	public void testBuildHierarchy() {
		fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

	@Test
	public void testToCSV() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFirstInstance() {
		assertEquals (h.getRoot().getNodeInstances().get(0), HierarchyUtils.getFirstInstance(h));
	}

	@Test
	public void testGetFeatureCount() {
		assertEquals(2, HierarchyUtils.getFeatureCount(h));
	}

}
