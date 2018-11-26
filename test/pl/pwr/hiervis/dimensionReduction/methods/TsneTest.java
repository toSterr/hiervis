package pl.pwr.hiervis.dimensionReduction.methods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.TestCommon;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class TsneTest {
	LoadedHierarchy loadedHierarchy;
	DimensionReduction dimensionReduction;
	Hierarchy hierarchy;

	@Before
	public void initialize() {
		dimensionReduction = new Tsne(false, 3, 2, 100, 1, false, 0.5, true, false);
		hierarchy = TestCommon.getTwoTwoGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
	}

	@Test
	public void testTsneParallel() {
		dimensionReduction = new Tsne(true, 3, 2, 100, 1, false, 0.5, true, false);
		assertNotEquals(null, dimensionReduction);
		assertEquals(4, loadedHierarchy.getMainHierarchy().getRoot().getNodeInstances().getFirst().getData().length);
		Hierarchy hierarchy = dimensionReduction.reduceHierarchy(loadedHierarchy);
		assertEquals(2, hierarchy.getRoot().getNodeInstances().getFirst().getData().length);
	}

	@Test
	public void testTsne() {
		dimensionReduction = null;
		assertEquals(null, dimensionReduction);
		dimensionReduction = new Tsne();
		assertNotEquals(null, dimensionReduction);
	}

	@Test
	public void testTsneBooleanIntIntIntDoubleBooleanDoubleBooleanBoolean() {
		assertNotEquals(null, dimensionReduction);
	}

	@Test
	public void testReduceHierarchy() {
		assertEquals(4, loadedHierarchy.getMainHierarchy().getRoot().getNodeInstances().getFirst().getData().length);
		Hierarchy hierarchy = dimensionReduction.reduceHierarchy(loadedHierarchy);
		assertEquals(2, hierarchy.getRoot().getNodeInstances().getFirst().getData().length);
	}

}
