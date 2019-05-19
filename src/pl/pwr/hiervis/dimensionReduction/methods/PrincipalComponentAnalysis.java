package pl.pwr.hiervis.dimensionReduction.methods;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.HierarchyUtils;

public class PrincipalComponentAnalysis extends DimensionReduction {

    private int targerDimension;

    public PrincipalComponentAnalysis(int targetDimension) {
	this.targerDimension = targetDimension;
    }

    public PrincipalComponentAnalysis() {
	targerDimension = 2;
    }

    @Override
    public Hierarchy reduceHierarchy(LoadedHierarchy source) {
	double[][] matrix = HierarchyUtils.toMatrix(source.getHierarchyWraper().getOriginalHierarchy());

	com.jujutsu.tsne.PrincipalComponentAnalysis pca = new com.jujutsu.tsne.PrincipalComponentAnalysis();
	double[][] output = pca.pca(matrix, targerDimension);

	Hierarchy newHier = HierarchyUtils.clone(source.getMainHierarchy(), true, null);

	for (int i = 0; i < newHier.getOverallNumberOfInstances(); i++) {
	    newHier.getRoot().getSubtreeInstances().get(i).setData(output[i]);
	}
	newHier.deleteDataNames();

	return newHier;

    }

    @Override
    public String getName() {
	return "Principal Component Analysis";
    }

    @Override
    public String getSimpleName() {
	return "PCA";
    }

    @Override
    public String getDescription() {
	return "";
    }

    public static String sGetName() {
	return "Principal Component Analysis";
    }

    public static String sGetSimpleName() {
	return "PCA";
    }

    public static String sGetDescription() {
	return "";
    }
}
