package pl.pwr.hiervis.dimensionReduction.methods;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.TestCommon;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class StarCoordinatesTest {

    LoadedHierarchy loadedHierarchy;
    DimensionReduction dimensionReduction;
    Hierarchy hierarchy;

    @Before
    public void initialize() {
	dimensionReduction = new StarCoordinates();
	hierarchy = TestCommon.getTwoTwoGroupsHierarchy();
	loadedHierarchy = new LoadedHierarchy(hierarchy,
		new LoadedHierarchy.Options(false, false, false, false, false));
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
	assertEquals("t-Distributed Stochastic Neighbor Embedding", StarCoordinates.sGetName());
    }

    @Test
    public void testSGetSimpleName() {
	assertEquals("t-Distributed Stochastic Neighbor Embedding", StarCoordinates.sGetSimpleName());
    }

    @Test
    public void testSGetDescription() {
	assertEquals(" ", StarCoordinates.sGetDescription());
    }
}
