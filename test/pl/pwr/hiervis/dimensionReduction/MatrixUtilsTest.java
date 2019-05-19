package pl.pwr.hiervis.dimensionReduction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class MatrixUtilsTest {

	double[][] matrix_1;
	double[][] matrix_2;
	double[][] matrix_3;
	double epsilon;

	@Before
	public void initialize() {
		matrix_1 = new double[][] { { 1 }, { 2 } };
		matrix_2 = new double[][] { { 1, 1 }, { 1, 1 } };
		matrix_3 = new double[][] { { 1, 2 }, { 0, 0 }, { 2, 4 } };
		epsilon = 0;
	}

	@Test
	public void testTransposeMatrix() {
		matrix_2 = MatrixUtils.TransposeMatrix(matrix_1);
		assertEquals(matrix_2.length, 1);
		assertEquals(matrix_2[0].length, 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMatrixNull() {
		MatrixUtils.TransposeMatrix(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMatrix0Length() {
		MatrixUtils.TransposeMatrix(new double[0][1]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMatrixSecondNull() {
		double[][] mat = new double[1][0];
		mat[0] = null;
		MatrixUtils.TransposeMatrix(mat);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMatrixSecond0Length() {
		MatrixUtils.TransposeMatrix(new double[1][0]);
	}

	@Test
	public void testStandaryzeMatrix() {
		MatrixUtils.StandaryzeMatrix(matrix_2);
		MatrixUtils.StandaryzeMatrix(matrix_3);
		assertEquals(matrix_2[0][0], 0, epsilon);
		assertEquals(matrix_2[0][1], 0, epsilon);
		assertEquals(matrix_3[0][0], 0, epsilon);
		assertEquals(matrix_3[0][1], 0, epsilon);
	}

	@Test
	public void testLinearlyTransformMatrix() {
		MatrixUtils.linearlyTransformMatrix(matrix_2);
		MatrixUtils.linearlyTransformMatrix(matrix_3);
		assertEquals(matrix_2[0][0], 0, epsilon);
		assertEquals(matrix_2[0][1], 0, epsilon);
		assertEquals(matrix_3[0][0], 0.5, epsilon);
		assertEquals(matrix_3[0][1], 0.5, epsilon);
	}

	@Test
	public void testDeepCopy() {
		matrix_2 = matrix_1;
		matrix_3 = MatrixUtils.deepCopy(matrix_1);

		assertSame(matrix_1, matrix_2);
		assertNotSame(matrix_1, matrix_3);
	}

	@Test
	public void testMultiplicateMatrix() {
		matrix_3 = MatrixUtils.multiplicateMatrix(matrix_2, matrix_1);
		assertEquals(matrix_3.length, 2, epsilon);
		assertEquals(matrix_3[0].length, 1, epsilon);

		assertEquals(matrix_3[0][0], 3, epsilon);
		assertEquals(matrix_3[1][0], 3, epsilon);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMultiplicateMatrixThrowException() {
		MatrixUtils.multiplicateMatrix(matrix_1, matrix_2);
	}
}
