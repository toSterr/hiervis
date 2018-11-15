package pl.pwr.hiervis.dimensionReduction;
/*
public class SimpleMatrix {
	
	public double matrix[][];
	public int pointsAmount;
	public int dimensions;
	public String labels[][];
	
	public SimpleMatrix()
	{
		this.pointsAmount= 0;
		this.dimensions=0;
		this.matrix =  null;
		labels=null;
	}
	public SimpleMatrix(int pointsAmount, int dimensions)
	{
		this.pointsAmount= pointsAmount;
		this.dimensions=dimensions;
		this.matrix =  new double[pointsAmount][dimensions];
		this.labels = new String [pointsAmount][3];
	}
	
	public SimpleMatrix( double [][] matrix, String [][] labels)
	{
		setMatrix(matrix);
		this.labels=labels.clone();
	}
	public SimpleMatrix(SimpleMatrix simpleMatrix)
	{
		this(simpleMatrix.getMatrix(), simpleMatrix.labels);
	}
	public void setMatrix( double [][] newMatrix)
	{
		this.matrix= null;
		if (newMatrix != null)
		{
			this.pointsAmount=newMatrix.length;
			if (newMatrix.length>0)
			{
				this.dimensions=newMatrix[0].length;
			}
			this.matrix= new double [newMatrix.length][];
			
			for (int i=0; i<pointsAmount;i++)
			{
				this.matrix[i]=newMatrix[i].clone();
			}
		}
		else
		{
			this.pointsAmount=-1;
			this.dimensions=-1;
		}
		
	}
	
	public double [][] GenerateFullMatrix (double [] values, int targetdDimensions )
	{
		dimensions=targetdDimensions;
		pointsAmount = (int) Math.pow( values.length , dimensions);
		matrix= new double [pointsAmount][dimensions];
		
		int i;
		for (int j=0; j< dimensions; j++)
		{
			i=0;
			for(int k=0; k<pointsAmount ;k++)
			{
				if( k + 1 > Math.pow( values.length ,  j) * (i +1 )  )
				{
					i=i+1;
				}
				matrix[k][j] = values[ i%values.length ];
			}
		}
		return matrix;
	}
	public void TransposeMatrix()
	{
		double [][] newmatrix= new double [dimensions][pointsAmount];
		for (int i=0; i< pointsAmount;i++)
		{
			for (int j=0; j< dimensions; j++)
			{
				newmatrix[j][i]=matrix[i][j];
			}
		}
		matrix=newmatrix;
		int helper=dimensions;
		dimensions=pointsAmount;
		pointsAmount=helper;
	}
	
	public void StandaryzeMatrix()
	{
		for (int i=0; i< dimensions; i++)
		{
			StandaryzeColum(i);
		}
	}
	
	void StandaryzeColum(int index)
	{
		double mean=0;
		double deviation=0;
		double variance=0;
		
		for (int i=0; i<pointsAmount ; i++)
		{
			mean += matrix[i][index] / pointsAmount;
		}
		for (int i=0; i<pointsAmount ; i++)
		{
			variance+= (matrix[i][index]-mean) * (matrix[i][index]-mean) / pointsAmount;
		}
		
		deviation = Math.pow(variance, 0.5);
		
		for (int i=0; i<pointsAmount ; i++)
		{
			matrix[i][index] = (matrix[i][index] - mean)/deviation;
		}
	}
	
	public void linearlyTransformMatrix()
	{
		for (int i=0; i< dimensions; i++)
		{
			linearlyTransformColum(i);
		}
	}
	
	void linearlyTransformColum(int index)
	{
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
			
		for (int i=0; i<pointsAmount ; i++)
		{
			if (min> matrix[i][index] )
			{ 
				min = matrix[i][index];
			}
			if (max< matrix[i][index] )
			{
				max = matrix[i][index];
			}
		}
		
		double range = max - min;
		if (range!=0)
			for (int i=0; i<pointsAmount ; i++)
			{
				matrix[i][index] =  (matrix[i][index]- min ) / range;
			}
		else
		{
			for (int i=0; i<pointsAmount ; i++)
			{
				matrix[i][index] = 0;
			}
		}
	}
	
	public double[][] getMatrix()
	{
		return matrix;
	}
	public int getPointsAmount()
	{
		return pointsAmount;
	}
	public int getDimenstions()
	{
		return dimensions;
	}
	
	 public static double[][] multiplicateMatrix(double[][] A, double[][] B) 
	 {
	        int aRows = A.length;
	        int aColumns = A[0].length;
	        int bRows = B.length;
	        int bColumns = B[0].length;

	        if (aColumns != bRows) {
	            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
	        }

	        double[][] C = new double[aRows][bColumns];
	        for (int i = 0; i < aRows; i++) {
	            for (int j = 0; j < bColumns; j++) {
	                C[i][j] = 0.00000;
	            }
	        }

	        for (int i = 0; i < aRows; i++) { // aRow
	            for (int j = 0; j < bColumns; j++) { // bColumn
	                for (int k = 0; k < aColumns; k++) { // aColumn
	                    C[i][j] += A[i][k] * B[k][j];
	                }
	            }
	        }

	        return C;
	}
	
	public String toString()
	{
		String str="";
		for (int i=0; i< pointsAmount; i++)
		{
			for (int j=0; j< dimensions ; j++)
			{
				str+=matrix[i][j]+" ;";
			}
			str+="\n";
		}
		
		return str;
	}
}
*/