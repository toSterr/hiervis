package pl.pwr.hiervis.dimensionReduction.methods;

import basic_hierarchy.interfaces.Hierarchy;
import mdsj.MDSJ;
import pl.pwr.hiervis.dimensionReduction.MatrixUtils;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.DistanceMeasure;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.Euclidean;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.HierarchyUtils;

public class MultidimensionalScaling extends DimensionReduction {
    private DistanceMeasure distanceMeasure;

    public MultidimensionalScaling() {
	distanceMeasure = new Euclidean();
    }

    public MultidimensionalScaling(DistanceMeasure distanceMeasure) {
	this.distanceMeasure = distanceMeasure;
    }

    @Override
    public Hierarchy reduceHierarchy(LoadedHierarchy source) {

	double[][] output = generateDissimilarityMatrix(
		HierarchyUtils.toMatrix(source.getHierarchyWraper().getOriginalHierarchy()));

	System.out.println("Calculating MDS");
	output = MDSJ.classicalScaling(output); // apply MDS
	System.out.println("Finished Calculating MDS");

	output = MatrixUtils.TransposeMatrix(output);

	Hierarchy newHier = HierarchyUtils.clone(source.getMainHierarchy(), true, null);

	for (int i = 0; i < newHier.getOverallNumberOfInstances(); i++) {
	    newHier.getRoot().getSubtreeInstances().get(i).setData(output[i]);
	}
	newHier.deleteDataNames();

	return newHier;

    }

    private double[][] generateDissimilarityMatrix(double[][] matrix) {
	System.out.println("Calculating Dissimilarity Matrix");

	double[][] output = new double[matrix.length][matrix.length];

	for (int i = 0; i < matrix.length; i++) {
	    for (int j = i + 1; j < matrix.length; j++) {
		output[i][j] = distanceMeasure.getDistance(matrix[i], matrix[j]);
		output[j][i] = output[i][j];
	    }
	}
	System.out.println("Finishing Calculating Dissimilarity Matrix");
	return output;
    }

    @Override
    public String getName() {
	return "Multidimensional Scaling";
    }

    @Override
    public String getSimpleName() {
	return "MDS";
    }

    @Override
    public String getDescription() {
	return "";
    }

    public static String sGetName() {
	return "Multidimensional Scaling";
    }

    public static String sGetSimpleName() {
	return "MDS";
    }

    public static String sGetDescription() {
	return "";
    }
}
