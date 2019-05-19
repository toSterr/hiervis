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

    @Test
    public void testGetName() {
	assertEquals("t-Distributed Stochastic Neighbor Embedding", dimensionReduction.getName());
    }

    @Test
    public void testGetSimpleName() {
	assertEquals("t-Distributed Stochastic Neighbor Embedding", dimensionReduction.getSimpleName());
    }

    @Test
    public void testGetDescription() {
	assertEquals(" ", dimensionReduction.getDescription());
    }

    @Test
    public void testSGetName() {
	assertEquals("t-Distributed Stochastic Neighbor Embedding", PrincipalComponentAnalysis.sGetName());
    }

    @Test
    public void testSGetSimpleName() {
	assertEquals("t-Distributed Stochastic Neighbor Embedding", PrincipalComponentAnalysis.sGetSimpleName());
    }

    @Test
    public void testSGetDescription() {
	assertEquals(" ", PrincipalComponentAnalysis.sGetDescription());
    }
}
