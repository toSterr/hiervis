package pl.pwr.hiervis.dimensionReduction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.reader.GeneratedCSVReader;
import common.CommonQualityMeasure;
import distance_measures.Euclidean;
import interfaces.DistanceMeasure;
import internal_measures.FlatCalinskiHarabasz;
import internal_measures.FlatDaviesBouldin;
import internal_measures.FlatDunn1;
import internal_measures.FlatDunn2;
import internal_measures.FlatDunn3;
import internal_measures.FlatDunn4;
import internal_measures.FlatReversedDunn2;
import internal_measures.FlatReversedDunn3;
import internal_measures.FlatReversedDunn4;
import internal_measures.FlatWithinBetweenIndex;
import internal_measures.HierarchicalInternalMeasure;
import internal_measures.VarianceDeviation;

public class DimReductionTest
{

	public static void main(String[] args)
	{
		//String[] folderNames = { "BadanieNoze3", "BadanieNoze50", "BadanieNoze100", "BadanieNoze200", "BadanieFigury3",
			//	"BadanieFigury50", "BadanieFigury100", "BadanieFigury200" };
		String[] folderNames = {  "NozePo10Redukcja","NozePo10","NozePo30Redukcja","NozePo30","FiguryFullRedukcja","FiguryFull" };
		for (int m = 0; m <  2 ; m++)
		{
			String folderDir = "D:\\Studia\\Praca Inzynierska\\imagenet\\FV-Sift\\" + folderNames[m];
			//String folderDir = "D:\\Studia\\Praca Inzynierska\\imagenet\\test\\BadanieNozePelne20";
			ArrayList<String> filesNameArray = listFilesForFolder(new File(folderDir));
			System.out.println(filesNameArray);
			Hierarchy h = null;
			GeneratedCSVReader CSVReader = new GeneratedCSVReader();
			CommonQualityMeasure[] measures = generateMeasures(new Euclidean());
			double[][] calculatedMetricks = new double[filesNameArray.size()][measures.length];
			for (int i = 0; i < filesNameArray.size(); i++)
			{
				System.out.println(i);
				try
				{
					h = CSVReader.load(folderDir + "\\" + filesNameArray.get(i), false, true, false, false, true);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

				for (int j = 0; j < measures.length; j++)
				{
					System.out.println("j " +j);
					calculatedMetricks[i][j] = measures[j].getMeasure(h);
					// System.out.println(calculatedMetricks[i][j]);
				}
			}
			System.out.println("Done");

			String path = folderDir + ".csv";
			try
			{
				FileWriter fileWriter = new FileWriter(new File(path));
				fileWriter.write("VocabularySize;StrongestFeatures;;");
				for (int j = 0; j < measures.length; j++)
				{
					if (j < 11)
						fileWriter.write(measures[j].getClass().getSimpleName() + ";");
					else
						fileWriter.write("HIM" + "(" + measures[j - 11].getClass().getSimpleName() + ");");
				}
				fileWriter.write("\n");
				for (int i = 0; i < filesNameArray.size(); i++)
				{
					String[] helper = filesNameArray.get(i).replaceAll(".csv", "").split("_");

					for (int k = 0; k < helper.length; k++)
					{
						fileWriter.write(helper[k] + ";");
					}

					for (int j = 0; j < measures.length; j++)
					{
						fileWriter.write(calculatedMetricks[i][j] + ";");
					}
					fileWriter.write("\n");
				}
				fileWriter.close();
				System.out.println("File saved to " + path);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		System.out.println("Done Done");
	}

	public static ArrayList<String> listFilesForFolder(final File folder)
	{
		ArrayList<String> output = new ArrayList<String>();

		for (final File fileEntry : folder.listFiles())
		{
			if (fileEntry.isDirectory())
			{
				output.addAll(listFilesForFolder(fileEntry));
			}
			else
			{
				output.add(fileEntry.getName());
			}
		}
		return output;
	}

	public static CommonQualityMeasure[] generateMeasures(DistanceMeasure dist)
	{
		CommonQualityMeasure[] measures = new CommonQualityMeasure[21];
		measures[0] = new FlatCalinskiHarabasz(dist);
		measures[1] = new FlatDaviesBouldin(dist);
		measures[2] = new FlatDunn1(dist);
		measures[3] = new FlatDunn2(dist);
		measures[4] = new FlatDunn3(dist);
		measures[5] = new FlatDunn4(dist);
		measures[6] = new FlatReversedDunn2(dist);
		measures[7] = new FlatReversedDunn3(dist);
		measures[8] = new FlatReversedDunn4(dist);
		measures[9] = new FlatWithinBetweenIndex(dist);
		measures[10] = new VarianceDeviation(1.0);

		measures[11] = new HierarchicalInternalMeasure(new FlatCalinskiHarabasz(dist));
		measures[12] = new HierarchicalInternalMeasure(new FlatDaviesBouldin(dist));
		measures[13] = new HierarchicalInternalMeasure(new FlatDunn1(dist));
		measures[14] = new HierarchicalInternalMeasure(new FlatDunn2(dist));
		measures[15] = new HierarchicalInternalMeasure(new FlatDunn3(dist));
		measures[16] = new HierarchicalInternalMeasure(new FlatDunn4(dist));
		measures[17] = new HierarchicalInternalMeasure(new FlatReversedDunn2(dist));
		measures[18] = new HierarchicalInternalMeasure(new FlatReversedDunn3(dist));
		measures[19] = new HierarchicalInternalMeasure(new FlatReversedDunn4(dist));
		measures[20] = new HierarchicalInternalMeasure(new FlatWithinBetweenIndex(dist));

		return measures;
	}
}
