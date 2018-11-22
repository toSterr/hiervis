package pl.pwr.hiervis.dimensionReduction;

import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class CalculatedDimensionReduction 
{
	public LoadedHierarchy inputLoadedHierarchy;
	public LoadedHierarchy outputLoadedHierarchy;
	public DimensionReduction dimensionReduction;
	
	public CalculatedDimensionReduction(LoadedHierarchy inputLoadedHierarchy, DimensionReduction dimensionReduction, LoadedHierarchy outputLoadedHierarchy) 
	{
		this.inputLoadedHierarchy=inputLoadedHierarchy;
		this.dimensionReduction=dimensionReduction;
		this.outputLoadedHierarchy=outputLoadedHierarchy;
	}
	

}
