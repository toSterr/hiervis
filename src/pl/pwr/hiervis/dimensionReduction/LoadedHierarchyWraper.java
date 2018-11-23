package pl.pwr.hiervis.dimensionReduction;

import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class LoadedHierarchyWraper {
    public LoadedHierarchy loadedHierarchy;
    private LoadedHierarchy originalLoadedHierarchy;
    private LoadedHierarchy[] reducedLoadedHierarchy;
    private DimensionReductionManager dimensionReductionManager;

    public LoadedHierarchyWraper(LoadedHierarchy loadedHierarchy) {
	this.originalLoadedHierarchy = loadedHierarchy;
	this.loadedHierarchy = loadedHierarchy;
	this.dimensionReductionManager = new DimensionReductionManager();
	this.setReducedLoadedHierarchy(new LoadedHierarchy[dimensionReductionManager.getNumber()]);
    }

    public LoadedHierarchyWraper() {
	loadedHierarchy = null;
	dimensionReductionManager = new DimensionReductionManager();
	this.setReducedLoadedHierarchy(new LoadedHierarchy[dimensionReductionManager.getNumber()]);
    }

    public LoadedHierarchy getHierarchyWithoutChange(int index) {
	if (index == 0) {
	    return originalLoadedHierarchy;
	}
	else if (index - 1 < dimensionReductionManager.getNumber()) {
	    return reducedLoadedHierarchy[index - 1];
	}
	else
	    return null;
    }

    public void setHierarchy(int index) {
	if (index == 0) {
	    loadedHierarchy = originalLoadedHierarchy;
	}
	else if (index - 1 < dimensionReductionManager.getNumber()) {
	    loadedHierarchy = reducedLoadedHierarchy[index - 1];
	}
    }

    public LoadedHierarchy getOriginalLoadedHierarchy() {
	return originalLoadedHierarchy;
    }

    /**
     * @return the reducedLoadedHierarchy
     */
    public LoadedHierarchy[] getReducedLoadedHierarchy() {
	return reducedLoadedHierarchy;
    }

    /**
     * @param reducedLoadedHierarchy the reducedLoadedHierarchy to set
     */
    public void setReducedLoadedHierarchy(LoadedHierarchy[] reducedLoadedHierarchy) {
	this.reducedLoadedHierarchy = reducedLoadedHierarchy;
    }

    public LoadedHierarchy getReducedHierarchy(DimensionReduction dimensionReduction) {
	int index = dimensionReductionManager.getIndex(dimensionReduction);
	if (index != -1)
	    return reducedLoadedHierarchy[index];
	else
	    return null;
    }

    public LoadedHierarchy getReducedHierarchy(int index) {
	if (index < 0 || index > dimensionReductionManager.getNumber())
	    return null;
	return reducedLoadedHierarchy[index];
    }

    public void addReducedHierarchy(CalculatedDimensionReduction calculatedDimensionReduction) {
	int index = dimensionReductionManager.getIndex(calculatedDimensionReduction.dimensionReduction);
	if (index != -1) {
	    reducedLoadedHierarchy[index] = calculatedDimensionReduction.outputLoadedHierarchy;
	}
    }
}
