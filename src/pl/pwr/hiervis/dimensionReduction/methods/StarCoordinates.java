package pl.pwr.hiervis.dimensionReduction.methods;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.MatrixUtils;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.HierarchyUtils;

public class StarCoordinates implements DimensionReduction
{
	@Override
	public Hierarchy reduceHierarchy(LoadedHierarchy source)
	{
		double[][] matrix = MatrixUtils
				.deepCopy(HierarchyUtils.toMatrix(source.getHierarchyWraper().getOriginalHierarchy()));

		MatrixUtils.linearlyTransformMatrix(matrix);
		int dimensions = matrix[0].length;

		double projectionVector[][] = new double[dimensions][2];

		for (int i = 1; i <= dimensions; i++)
		{
			projectionVector[i - 1][0] = Math.cos(2 * Math.PI * i / dimensions);
			projectionVector[i - 1][1] = Math.sin(2 * Math.PI * i / dimensions);
		}

		double newMatrix[][] = MatrixUtils.multiplicateMatrix(matrix, projectionVector);

		Hierarchy newHier = HierarchyUtils.clone(source.getMainHierarchy(), true, null);

		for (int i = 0; i < newHier.getOverallNumberOfInstances(); i++)
		{
			newHier.getRoot().getSubtreeInstances().get(i).setData(newMatrix[i]);
		}
		newHier.deleteDataNames();
		return newHier;
	}

}
