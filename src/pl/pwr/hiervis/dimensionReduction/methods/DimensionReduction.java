package pl.pwr.hiervis.dimensionReduction.methods;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public interface DimensionReduction
{
	public Hierarchy reduceHierarchy(LoadedHierarchy source);

}
