package pl.pwr.hiervis.dimensionReduction.methods;

import basic_hierarchy.interfaces.Hierarchy;
import mdsj.MDSJ;
import pl.pwr.hiervis.dimensionReduction.MatrixUtils;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.DistanceMeasure;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.Euclidean;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.HierarchyUtils;

public class MultidimensionalScaling implements DimensionReduction
{
	private DistanceMeasure distanceMeasure;

	public MultidimensionalScaling()
	{
		distanceMeasure = new Euclidean();
	}

	public MultidimensionalScaling(DistanceMeasure distanceMeasure)
	{
		this.distanceMeasure = distanceMeasure;
	}

	@Override
	public LoadedHierarchy reduceHierarchy(LoadedHierarchy source)
	{
		double[][] input = generateDissimilarityMatrix(HierarchyUtils.toMatrix(source.getMainHierarchy()));

		System.out.println("Calculating MDS");
		double[][] output = MDSJ.classicalScaling(input); // apply MDS
		System.out.println("Finished Calculating MDS");

		output = MatrixUtils.TransposeMatrix(output);

		Hierarchy newHier = HierarchyUtils.clone(source.getMainHierarchy(), true, null);

		for (int i = 0; i < newHier.getOverallNumberOfInstances(); i++)
		{
			newHier.getRoot().getSubtreeInstances().get(i).setData(output[i]);
		}
		newHier.deleteDataNames();

		return new LoadedHierarchy(newHier, source.options);

	}

	private double[][] generateDissimilarityMatrix(double[][] matrix)
	{
		System.out.println("Calculating Dissimilarity Matrix");

		double[][] output = new double[matrix.length][matrix.length];

		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = i + 1; j < matrix.length; j++)
			{
				output[i][j] = distanceMeasure.getDistance(matrix[i], matrix[j]);
				output[j][i] = output[i][j];
			}
		}
		System.out.println("Finishing Calculating Dissimilarity Matrix");
		return output;
	}
}
