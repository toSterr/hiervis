package pl.pwr.hiervis.dimensionReduction;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;

public class HierarchyWraper
{
	public Hierarchy hierarchy;
	private Hierarchy originalHierarchy;
	private Hierarchy[] reducedHierarchy;
	private DimensionReductionManager dimensionReductionManager;

	public HierarchyWraper(Hierarchy hierarchy)
	{
		this.originalHierarchy = hierarchy;
		this.hierarchy = hierarchy;
		this.dimensionReductionManager = new DimensionReductionManager();
		this.setReducedHierarchy(new Hierarchy[dimensionReductionManager.getNumber()]);
	}

	public HierarchyWraper()
	{
		hierarchy = null;
		originalHierarchy = null;
		dimensionReductionManager = new DimensionReductionManager();
		setReducedHierarchy(new Hierarchy[dimensionReductionManager.getNumber()]);
	}

	public Hierarchy getHierarchyWithoutChange(int index)
	{
		if (index == 0)
		{
			return originalHierarchy;
		}
		else if (index - 1 < dimensionReductionManager.getNumber())
		{
			return reducedHierarchy[index - 1];
		}
		else
			return null;
	}

	public void setHierarchy(int index)
	{
		if (index == 0)
		{
			hierarchy = originalHierarchy;
		}
		else if (index - 1 < dimensionReductionManager.getNumber())
		{
			hierarchy = reducedHierarchy[index - 1];
		}
	}

	public Hierarchy getOriginalHierarchy()
	{
		return originalHierarchy;
	}

	/**
	 * @return the reducedHierarchy
	 */
	public Hierarchy[] getReducedHierarchy()
	{
		return reducedHierarchy;
	}

	/**
	 * @param reducedHierarchy
	 *            the reducedHierarchy to set
	 */
	public void setReducedHierarchy(Hierarchy[] reducedHierarchy)
	{
		this.reducedHierarchy = reducedHierarchy;
	}

	public Hierarchy getReducedHierarchy(DimensionReduction dimensionReduction)
	{
		int index = dimensionReductionManager.getIndex(dimensionReduction);
		if (index != -1)
			return reducedHierarchy[index];
		else
			return null;
	}

	public Hierarchy getReducedHierarchy(int index)
	{
		if (index < 0 || index > dimensionReductionManager.getNumber())
			return null;
		return reducedHierarchy[index];
	}

	public void addReducedHierarchy(CalculatedDimensionReduction calculatedDimensionReduction)
	{
		int index = dimensionReductionManager.getIndex(calculatedDimensionReduction.dimensionReduction);
		if (index != -1)
		{
			reducedHierarchy[index] = calculatedDimensionReduction.outputHierarchy;
		}
	}

}
