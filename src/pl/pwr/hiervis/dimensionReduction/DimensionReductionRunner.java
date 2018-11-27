package pl.pwr.hiervis.dimensionReduction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.ui.VisualizerFrame;

public class DimensionReductionRunner extends Thread {
    private static final Logger log = LogManager.getLogger(VisualizerFrame.class);
    private HVContext context;
    private DimensionReduction dimensionReduction;

    public DimensionReductionRunner(HVContext context, DimensionReduction dimensionReduction) {
	this.context = context;
	this.dimensionReduction = dimensionReduction;
	setName("DimensionReductionComputeThread");
	setDaemon(true);
    }

    public void run() {
	LoadedHierarchy inputLoadedHierarchy = context.getHierarchy();
	Hierarchy outputHierarchy = null;
	try {
	    long start = System.currentTimeMillis();

	    outputHierarchy = dimensionReduction.reduceHierarchy(inputLoadedHierarchy);

	    long elapsedTime = System.currentTimeMillis() - start;
	    System.out.println("Elapsed Time: " + elapsedTime / (1000F) + " sec");
	}
	catch (Exception e) {
	    log.trace(e);
	}
	catch (Error e) {
	    log.trace(e);
	}
	finally {
	    context.dimensionReductionCalculated.broadcast(
		    new CalculatedDimensionReduction(inputLoadedHierarchy, dimensionReduction, outputHierarchy));
	}
    }

}
