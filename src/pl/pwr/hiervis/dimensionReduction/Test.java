package pl.pwr.hiervis.dimensionReduction;

import java.io.File;
import java.io.IOException;

import basic_hierarchy.implementation.BasicHierarchy;
import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.reader.GeneratedCSVReader;
import pl.pwr.hiervis.util.HierarchyUtils;

public class Test
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		GeneratedCSVReader reader = new GeneratedCSVReader();
	
		File file= new File("tes.csv");
		Hierarchy source;
		try
		{
			source = reader.load( 
				file.getAbsolutePath(),
				false,
				true,
				false,
				false,
				false);
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

	}

}
