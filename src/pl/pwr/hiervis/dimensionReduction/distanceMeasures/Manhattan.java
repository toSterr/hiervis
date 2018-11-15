package pl.pwr.hiervis.dimensionReduction.distanceMeasures;

public class Manhattan implements DistanceMeasure
{

	@Override
	public double getDistance(double[] a, double[] b)
	{
		assert a.length == b.length;

		double measure = 0.0;
		for (int i = 0; i < a.length; i++)
		{
			measure += Math.abs(a[i] - b[i]);
		}

		return measure;
	}

	public String toString()
	{
		return "Manhattan";
	}

}
