package pl.pwr.hiervis.dimensionReduction.distanceMeasures;

public class Minkowski implements DistanceMeasure
{
	double p;

	public Minkowski()
	{
		p = 1.5;
	}

	public Minkowski(double p)
	{
		this.p = p;
	}

	@Override
	public double getDistance(double[] a, double[] b)
	{
		assert a.length == b.length;

		double measure = 0.0;
		for (int i = 0; i < a.length; i++)
		{
			measure += Math.pow(Math.abs(a[i] - b[i]), p);
		}

		return Math.pow(measure, 1.0 / p);
	}

	public String toString()
	{
		return "Minkowski";
	}
}
