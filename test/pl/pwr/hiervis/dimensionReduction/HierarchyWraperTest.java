package pl.pwr.hiervis.dimensionReduction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.dimensionReduction.methods.MultidimensionalScaling;
import pl.pwr.hiervis.dimensionReduction.methods.StarCoordinates;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class HierarchyWraperTest {

    Hierarchy hierarchy;
    HierarchyWraper hierachyWraper;

    @Before
    public void initialize() {
	hierarchy = TestCommon.getFourGroupsHierarchy();
	hierachyWraper = new HierarchyWraper(hierarchy);
    }

    @Test
    public void testHierarchyWraperHierarchy() {
	assertNotSame(hierachyWraper, null);
	assertEquals(hierachyWraper.hierarchy, hierarchy);
    }

    @Test
    public void testHierarchyWraper() {
	hierachyWraper = null;
	assertEquals(hierachyWraper, null);
	hierachyWraper = new HierarchyWraper();
	assertNotSame(hierachyWraper, null);
	assertEquals(hierachyWraper.hierarchy, null);
    }

    @Test
    public void testGetHierarchyWithoutChange() {
	assertEquals(hierarchy, hierachyWraper.getHierarchyWithoutChange(0));
	assertEquals(null, hierachyWraper.getHierarchyWithoutChange(1));
	assertEquals(null, hierachyWraper.getHierarchyWithoutChange(10));
    }

    @Test
    public void testSetHierarchy() {
	assertEquals(hierarchy, hierachyWraper.hierarchy);
	hierachyWraper.hierarchy = null;
	assertEquals(null, hierachyWraper.hierarchy);
	hierachyWraper.getReducedHierarchy()[0] = hierarchy;
	hierachyWraper.setHierarchy(1);
	assertEquals(hierarchy, hierachyWraper.hierarchy);

	hierachyWraper.hierarchy = null;
	assertEquals(null, hierachyWraper.hierarchy);
	hierachyWraper.setHierarchy(0);
	assertEquals(hierarchy, hierachyWraper.hierarchy);
	hierachyWraper.setHierarchy(-1);
	assertEquals(hierarchy, hierachyWraper.hierarchy);

	hierachyWraper.setHierarchy(10);
	assertEquals(hierarchy, hierachyWraper.hierarchy);
    }

    @Test
    public void testGetOriginalHierarchy() {
	assertEquals(hierarchy, hierachyWraper.getOriginalHierarchy());
    }

    @Test
    public void testGetReducedHierarchy() {
	assertNotSame(null, hierachyWraper.getReducedHierarchy());
    }

    @Test
    public void testSetReducedHierarchy() {
	hierachyWraper.setReducedHierarchy(null);
	assertSame(null, hierachyWraper.getReducedHierarchy());

    }

    @Test
    public void testGetReducedHierarchyDimensionReduction() {

	hierachyWraper.getReducedHierarchy()[0] = hierarchy;
	assertEquals(hierarchy, hierachyWraper.getReducedHierarchy(new MultidimensionalScaling()));
	// assertEquals(null, hierachyWraper.getReducedHierarchy(null));
    }

    @Test
    public void testGetReducedHierarchyInt() {
	Hierarchy hier = hierachyWraper.getReducedHierarchy(1);
	assertEquals(null, hier);
    }

    @Test
    public void testAddReducedHierarchy() {
	LoadedHierarchy loadedHierarchy = new LoadedHierarchy(hierarchy,
		new LoadedHierarchy.Options(false, false, false, false, false));
	DimensionReduction dimensionReduction = new StarCoordinates();
	CalculatedDimensionReduction calculatedDimensionReduction = new CalculatedDimensionReduction(loadedHierarchy,
		dimensionReduction, hierarchy);
	hierachyWraper.addReducedHierarchy(calculatedDimensionReduction);
	assertNotSame(null, hierachyWraper.getHierarchyWithoutChange(4));
    }

}
