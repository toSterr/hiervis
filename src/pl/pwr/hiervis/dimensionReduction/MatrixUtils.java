package pl.pwr.hiervis.dimensionReduction;

public class MatrixUtils
{
	/**
	 * Checks if given matrix is real a matrix
	 * @param matrix
	 * @throws IllegalArgumentException
	 */
	private static void checkIfMatrixCorrect( double  matrix [][]) throws IllegalArgumentException
	{
		if (matrix==null)
			throw new  IllegalArgumentException("Matrix is null");
		if( matrix.length==0 )
			throw new  IllegalArgumentException("First input matrix dimension is 0");
		if (matrix[0]==null || matrix[0].length == 0)
			throw new  IllegalArgumentException("Second input matrix dimension is 0");
	}
	
	 /**
     * Transposes given matrix and returns it transposition
     * @param data matrix
     * @return the transposed matrix
     */
	public static double [][] TransposeMatrix(double  matrix [][]) throws IllegalArgumentException
	{
		checkIfMatrixCorrect(matrix);
		
		int n = matrix.length;
		int m = matrix[0].length;
		
		double [][] newmatrix = new double [m][n];
		
		for (int i=0; i< n;i++)
		{
			for (int j=0; j< m; j++)
			{
				newmatrix[j][i]=matrix[i][j];
			}
		}

		return newmatrix;
	}
	
	/**
	 * Performs standardization on given matrix, changes given matrix
	 * @param matrix
	 * @throws IllegalArgumentException
	 */
	public static void StandaryzeMatrix(double [][] matrix) throws IllegalArgumentException
	{
		checkIfMatrixCorrect(matrix);
		
		for (int i=0; i< matrix[0].length; i++)
		{
			StandaryzeColum(matrix , i);
		}
	}
	
	private static void StandaryzeColum(double [][] matrix, int columnIndex)
	{
		double mean=0;
		double deviation=0;
		double variance=0;
		
		int pointsAmount=matrix.length;
		
		for (int i=0; i<pointsAmount ; i++)
		{
			mean += matrix[i][columnIndex] / pointsAmount;
		}
		for (int i=0; i<pointsAmount ; i++)
		{
			variance+= (matrix[i][columnIndex]-mean) * (matrix[i][columnIndex]-mean) / pointsAmount;
		}
		
		deviation = Math.pow(variance, 0.5);
		
		for (int i=0; i<pointsAmount ; i++)
		{
			matrix[i][columnIndex] = (matrix[i][columnIndex] - mean)/deviation;
		}
	}
	
	/**
	 * Performs linearization on given matrix, changes given matrix
	 * @param matrix
	 * @throws IllegalArgumentException
	 */
	public static void linearlyTransformMatrix(double [][]matrix) throws IllegalArgumentException
	{
		checkIfMatrixCorrect(matrix);
		
		for (int i=0; i< matrix[0].length; i++)
		{
			linearlyTransformColum(matrix, i);
		}
	}
	
	private static void linearlyTransformColum(double [][] matrix, int index)
	{
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
			
		int pointsAmount= matrix.length;
		
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
	
	/**
	 * Multiplicates matrixes A*B
	 * @param A matrix
	 * @param B matrix
	 * @return A*B
	 */
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
	 
	 
}
