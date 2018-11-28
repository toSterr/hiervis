package pl.pwr.hiervis.dimensionReduction.methods;

import com.jujutsu.tsne.TSneConfiguration;
import com.jujutsu.tsne.barneshut.BHTSne;
import com.jujutsu.tsne.barneshut.BarnesHutTSne;
import com.jujutsu.tsne.barneshut.ParallelBHTsne;
import com.jujutsu.utils.TSneUtils;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.HierarchyUtils;

public class Tsne extends DimensionReduction {
    BarnesHutTSne tsne;
    TSneConfiguration config;
    boolean parallel;
    int initialDims;
    int outputDims;
    int maxIter;
    double perplexity;
    boolean usePCA;
    double tetha;
    boolean silent;
    boolean printError;

    public Tsne() {
	this(false, 5, 2, 1000, 20.0, true, 0.5, true, true);
    }

    public Tsne(boolean parallel, int initialDims, int outputDims, int maxIter, double perplexity, boolean usePCa,
	    double tetha, boolean silent, boolean printError) {
	this.parallel = parallel;
	this.initialDims = initialDims;
	this.outputDims = outputDims;
	this.maxIter = maxIter;
	this.perplexity = perplexity;
	this.usePCA = usePCa;
	this.tetha = tetha;
	this.silent = silent;
	this.printError = printError;
	if (parallel) {
	    tsne = new ParallelBHTsne();
	}
	else {
	    tsne = new BHTSne();
	}
    }

    @Override
    public Hierarchy reduceHierarchy(LoadedHierarchy source) {
	double[][] matrix = HierarchyUtils.toMatrix(source.getHierarchyWraper().getOriginalHierarchy());

	TSneConfiguration config = TSneUtils.buildConfig(matrix, outputDims, initialDims, perplexity, maxIter, usePCA,
		tetha, silent, printError);

	// double[][] outputMatrix = new
	// double[source.getMainHierarchy().getOverallNumberOfInstances()][1];
	double[][] outputMatrix = tsne.tsne(config);

	Hierarchy newHier = HierarchyUtils.clone(source.getMainHierarchy(), true, null);

	for (int i = 0; i < newHier.getOverallNumberOfInstances(); i++) {
	    newHier.getRoot().getSubtreeInstances().get(i).setData(outputMatrix[i]);
	}
	newHier.deleteDataNames();

	return newHier;
    }

    @Override
    public String getName() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getSimpleName() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getDescription() {
	// TODO Auto-generated method stub
	return null;
    }

    public static String sGetName() {
	// TODO Auto-generated method stub
	return null;
    }

    public static String sGetSimpleName() {
	// TODO Auto-generated method stub
	return null;
    }

    public static String sGetDescription() {
	// TODO Auto-generated method stub
	return null;
    }
}
