package pl.pwr.hiervis.dimensionReduction.distanceMeasures;

public interface DistanceMeasure
{
	public double getDistance(double[] a, double[] b);

	public String toString();
}
