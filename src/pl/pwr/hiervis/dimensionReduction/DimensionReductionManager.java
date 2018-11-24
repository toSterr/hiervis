package pl.pwr.hiervis.dimensionReduction;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimensionReduction.methods.DimensionReduction;
import pl.pwr.hiervis.dimensionReduction.ui.DimensionReductionDialog;
import pl.pwr.hiervis.dimensionReduction.ui.MdsDialog;
import pl.pwr.hiervis.dimensionReduction.ui.PcaDialog;
import pl.pwr.hiervis.dimensionReduction.ui.StarCoordsDialog;
import pl.pwr.hiervis.dimensionReduction.ui.TsneDialog;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

//TODO implement
/**
 * Provides all dimension methods, number, and so on
 */
public class DimensionReductionManager
{
	/*
	 * Holds all dialogs for dimension reduction methods Elements need to be
	 * manually added after implementation
	 */
	private ArrayList<DimensionReductionDialog> dimensionReductionDialogs;
	private ArrayList<Pair<LoadedHierarchy, Class<? extends DimensionReduction>>> calculationQue;

	public DimensionReductionManager()
	{
		calculationQue = new ArrayList<Pair<LoadedHierarchy, Class<? extends DimensionReduction>>>();
		dimensionReductionDialogs = new ArrayList<DimensionReductionDialog>();
		dimensionReductionDialogs.add(new MdsDialog());
		dimensionReductionDialogs.add(new PcaDialog());
		dimensionReductionDialogs.add(new TsneDialog());
		dimensionReductionDialogs.add(new StarCoordsDialog());
	}

	public int getIndex(DimensionReduction dimensionReduction)
	{
		int index = -1;
		for (int i = 0; i < getNumber(); i++)
		{
			if (dimensionReductionDialogs.get(i).getResultClass().isAssignableFrom(dimensionReduction.getClass()))
				index = i;
		}
		return index;
	}

	public boolean addToQue(LoadedHierarchy hierarchy, Class<? extends DimensionReduction> reductionClass)
	{
		return calculationQue.add(Pair.of(hierarchy, reductionClass));
	}

	public boolean isInQue(LoadedHierarchy hierarchy, Class<? extends DimensionReduction> reductionClass)
	{

		for (Pair<LoadedHierarchy, Class<? extends DimensionReduction>> pair : calculationQue)
		{
			if (pair.getLeft() == hierarchy && pair.getRight() == reductionClass)
			{
				return true;
			}
		}
		return false;
	}

	public boolean removeFromQue(LoadedHierarchy hierarchy, Class<? extends DimensionReduction> reductionClass)
	{
		for (Pair<LoadedHierarchy, Class<? extends DimensionReduction>> pair : calculationQue)
		{
			if (pair.getLeft() == hierarchy && pair.getRight() == reductionClass)
			{
				calculationQue.remove(pair);
				return true;
			}
		}
		return false;
	}

	public ArrayList<DimensionReductionDialog> getList()
	{
		return dimensionReductionDialogs;
	}

	public int getNumber()
	{
		return dimensionReductionDialogs.size();
	}

	public String[] getNames()
	{
		return dimensionReductionDialogs.stream().map(e -> e.getName()).toArray(String[]::new);
	}

	public String[] getSimpleNames()
	{
		return dimensionReductionDialogs.stream().map(e -> e.getSimpleName()).toArray(String[]::new);
	}

	public DimensionReductionDialog[] getDialogs()
	{
		return dimensionReductionDialogs.toArray(new DimensionReductionDialog[0]);
	}

	public DimensionReduction showDialog(int index, int maxOutputDimensions, int pointsAmount)
	{
		DimensionReduction dimensionReduction = dimensionReductionDialogs.get(index).showDialog(maxOutputDimensions,
				pointsAmount);
		return dimensionReduction;
	}

	public DimensionReduction showDialog(int index, HVContext context, int x, int y)
	{
		DimensionReduction dimensionReduction = dimensionReductionDialogs.get(index).showDialog(context, x, y);
		return dimensionReduction;
	}

	public DimensionReduction showDialog(int index, HVContext context)
	{
		return showDialog(index, context, 100, 100);
	}

	public static void main(String[] args)
	{
		DimensionReductionManager dimensionReductionMenager = new DimensionReductionManager();

		System.out.println(dimensionReductionMenager.getDialogs().length);

		for (int i = 0; i < dimensionReductionMenager.getDialogs().length; i++)
		{
			String s = dimensionReductionMenager.getDialogs()[i].getResultClass().toString();
			System.out.println(s);
		}

	}
}
