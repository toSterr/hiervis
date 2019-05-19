package pl.pwr.hiervis.dimensionReduction.distanceMeasures;

public class Euclidean implements DistanceMeasure
{

	@Override
	public double getDistance(double[] a, double[] b)
	{
		assert a.length == b.length;

		double measure = 0.0;
		for (int i = 0; i < a.length; i++)
		{
			measure += Math.pow(a[i] - b[i], 2);
		}

		return Math.sqrt(measure);
	}

	public String toString()
	{
		return "Euclidean";
	}
}
