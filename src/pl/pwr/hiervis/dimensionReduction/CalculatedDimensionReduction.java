package pl.pwr.hiervis.dimensionReduction;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class CalculatedDimensionReduction
{
	public LoadedHierarchy inputLoadedHierarchy;
	public Hierarchy outputHierarchy;
	public DimensionReduction dimensionReduction;

	public CalculatedDimensionReduction(LoadedHierarchy inputLoadedHierarchy, DimensionReduction dimensionReduction,
			Hierarchy outputHierarchy)
	{
		this.inputLoadedHierarchy = inputLoadedHierarchy;
		this.dimensionReduction = dimensionReduction;
		this.outputHierarchy = outputHierarchy;
	}

}
