package pl.pwr.hiervis.dimensionReduction;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.dimensionReduction.methods.StarCoordinates;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class CalculatedDimensionReductionTest {

    LoadedHierarchy loadedHierarchy;
    DimensionReduction dimensionReduction;
    Hierarchy hierarchy;

    @Before
    public void initialize() {
	hierarchy = TestCommon.getFourGroupsHierarchy();
	loadedHierarchy = new LoadedHierarchy(hierarchy,
		new LoadedHierarchy.Options(false, false, false, false, false));
	dimensionReduction = new StarCoordinates();
    }

    @Test
    public void testCalculatedDimensionReduction() {
	CalculatedDimensionReduction calculatedDimensionReduction = new CalculatedDimensionReduction(loadedHierarchy,
		dimensionReduction, hierarchy);
	assertNotSame(calculatedDimensionReduction, null);
	assertSame(calculatedDimensionReduction.dimensionReduction, dimensionReduction);
	assertSame(calculatedDimensionReduction.inputLoadedHierarchy, loadedHierarchy);
	assertSame(calculatedDimensionReduction.outputHierarchy, hierarchy);

    }

}
