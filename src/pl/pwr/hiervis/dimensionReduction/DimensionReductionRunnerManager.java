package pl.pwr.hiervis.dimensionReduction;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class DimensionReductionRunnerManager {
    private static final Logger log = LogManager.getLogger(DimensionReductionRunnerManager.class);
    private HVContext context;
    private List<DimensionReductionRunner> taskList;

    public DimensionReductionRunnerManager(HVContext context) {
	this.context = context;
	taskList = new ArrayList<DimensionReductionRunner>();
	context.dimensionReductionCalculated.addListener(this::onDimensionReductionCalculated);
    }

    public void addTask(LoadedHierarchy loadedHierarchy, DimensionReduction dimensionReduction) {
	DimensionReductionRunner dimensionReductionRunner = new DimensionReductionRunner(loadedHierarchy,
		dimensionReduction, context.dimensionReductionCalculated);
	dimensionReductionRunner.start();
	taskList.add(dimensionReductionRunner);
    }

    public boolean removeTask(LoadedHierarchy loadedHierarchy,
	    Class<? extends DimensionReduction> dimensionReductionClass) {
	for (DimensionReductionRunner reductionRunner : taskList) {
	    if (reductionRunner.isTheSame(loadedHierarchy, dimensionReductionClass)) {
		taskList.remove(reductionRunner);
		return true;
	    }
	}
	return false;
    }

    public void onDimensionReductionCalculated(CalculatedDimensionReduction calculatedDimensionReduction) {
	removeTask(calculatedDimensionReduction.inputLoadedHierarchy,
		calculatedDimensionReduction.dimensionReduction.getClass());
    }

    public boolean interuptTask(LoadedHierarchy loadedHierarchy,
	    Class<? extends DimensionReduction> dimensionReductionClass) {
	for (DimensionReductionRunner reductionRunner : taskList) {
	    if (reductionRunner.isTheSame(loadedHierarchy, dimensionReductionClass)) {
		reductionRunner.myInterrupt();
		taskList.remove(reductionRunner);
		return true;
	    }
	}
	return false;
    }

}
