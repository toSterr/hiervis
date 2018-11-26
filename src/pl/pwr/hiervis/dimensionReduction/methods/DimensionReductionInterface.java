package pl.pwr.hiervis.dimensionReduction.methods;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public interface DimensionReductionInterface {
    public Hierarchy reduceHierarchy(LoadedHierarchy source);

}
