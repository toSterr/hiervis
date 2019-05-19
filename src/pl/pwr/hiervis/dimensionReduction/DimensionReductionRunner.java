package pl.pwr.hiervis.dimensionReduction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.Event;

public class DimensionReductionRunner extends Thread {
    private static final Logger log = LogManager.getLogger(DimensionReductionRunner.class);
    private DimensionReduction dimensionReduction;
    private LoadedHierarchy inputLoadedHierarchy;
    private Event<CalculatedDimensionReduction> brodcastEvent;

    public DimensionReductionRunner(LoadedHierarchy loadedHierarchy, DimensionReduction dimensionReduction,
	    Event<CalculatedDimensionReduction> brodcastEvent) {
	inputLoadedHierarchy = loadedHierarchy;
	this.dimensionReduction = dimensionReduction;
	this.brodcastEvent = brodcastEvent;
	setName("DimensionReductionComputeThread: " + dimensionReduction.getSimpleName());
	setDaemon(true);
    }

    public boolean isTheSame(LoadedHierarchy loadedHierarchy,
	    Class<? extends DimensionReduction> dimensionReductionClass) {
	return (inputLoadedHierarchy == loadedHierarchy
		&& this.dimensionReduction.getClass() == dimensionReductionClass);
    }

    public void myInterrupt() {
	// System.out.println("interupt?");
	// this.interrupt();
	this.stop();
    }

    public void run() {
	Hierarchy outputHierarchy = null;
	try {
	    long start = System.currentTimeMillis();

	    outputHierarchy = dimensionReduction.reduceHierarchy(inputLoadedHierarchy);

	    long elapsedTime = System.currentTimeMillis() - start;
	    log.trace(dimensionReduction.getSimpleName() + " calculated in: " + elapsedTime / (1000F) + " sec");
	}
	catch (java.lang.ThreadDeath e) {
	    log.trace("Stoped calculation of: " + this.getName());
	}
	catch (Exception e) {
	    log.trace(e);
	}
	catch (Error e) {
	    log.trace(e);
	}
	finally {
	    brodcastEvent.broadcast(
		    new CalculatedDimensionReduction(inputLoadedHierarchy, dimensionReduction, outputHierarchy));
	}
    }

}
