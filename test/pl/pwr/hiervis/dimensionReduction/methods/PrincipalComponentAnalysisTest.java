package pl.pwr.hiervis.dimensionReduction.methods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.TestCommon;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class PrincipalComponentAnalysisTest {
    LoadedHierarchy loadedHierarchy;
    DimensionReduction dimensionReduction;
    Hierarchy hierarchy;

    @Before
    public void initialize() {
	dimensionReduction = new PrincipalComponentAnalysis();
	hierarchy = TestCommon.getTwoTwoGroupsHierarchy();
	loadedHierarchy = new LoadedHierarchy(hierarchy,
		new LoadedHierarchy.Options(false, false, false, false, false));
    }

    @Test
    public void testPrincipalComponentAnalysisInt() {
	dimensionReduction = new PrincipalComponentAnalysis(3);
	assertEquals(4, loadedHierarchy.getMainHierarchy().getRoot().getNodeInstances().getFirst().getData().length);
	Hierarchy hierarchy = dimensionReduction.reduceHierarchy(loadedHierarchy);
	assertEquals(3, hierarchy.getRoot().getNodeInstances().getFirst().getData().length);
    }

    @Test
    public void testPrincipalComponentAnalysis() {
	assertNotEquals(null, dimensionReduction);
	;
    }

    @Test
    public void testReduceHierarchy() {
	assertEquals(4, loadedHierarchy.getMainHierarchy().getRoot().getNodeInstances().getFirst().getData().length);
	Hierarchy hierarchy = dimensionReduction.reduceHierarchy(loadedHierarchy);
	assertEquals(2, hierarchy.getRoot().getNodeInstances().getFirst().getData().length);
    }

}
