package pl.pwr.hiervis.dimensionReduction;

import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;

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

		String tabTitle = "[" + dimensionReduction.getClass().getSimpleName() + "] "
				+ context.getHierarchyFrame().getSelectedTabTitle();

		context.loadHierarchy(tabTitle, dimensionReduction.reduceHierarchy(context.getHierarchy()));

		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println("Elapsed Time: " + elapsedTime / (1000F) + " sec");
	}

}
