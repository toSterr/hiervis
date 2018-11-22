package pl.pwr.hiervis.dimensionReduction;

import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class DimensionReductionRunner extends Thread
{
	HVContext context;
	DimensionReduction dimensionReduction;

	public DimensionReductionRunner(HVContext context, DimensionReduction dimensionReduction)
	{
		this.context = context;
		this.dimensionReduction = dimensionReduction;
		setName("DimensionReductionComputeThread");
		setDaemon(true);
	}

	public void run()
	{
		long start = System.currentTimeMillis();
		LoadedHierarchy inputLoadedHierarchy= context.getHierarchy();
		LoadedHierarchy outputLoadedHierarchy = dimensionReduction.reduceHierarchy(inputLoadedHierarchy);

		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println("Elapsed Time: " + elapsedTime / (1000F) + " sec");

		context.dimensionReductionCalculated.broadcast(new CalculatedDimensionReduction( inputLoadedHierarchy,dimensionReduction,outputLoadedHierarchy));
	}

}
