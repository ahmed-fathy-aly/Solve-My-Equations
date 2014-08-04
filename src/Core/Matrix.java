package Core;

/**
 * a set of operations (addition - subtraction - multiplication - inverse ) for
 * a 2D matrix of doubles
 * 
 * @author Mohamed Ahmed
 * 
 */
public class Matrix
{

	/**
	 * return the multiplication of the two matrices
	 */
	public static double[][] mulMatrix(double[][] x, double[][] y) throws Exception
	{
		// check for errors in the dimensions of the matrices
		if (x[0].length != y.length)
		{
			throw new Exception(
					"Error: The number of columns in the first matrix must equal to the number of rows in the second one");
		}

		double[][] z = new double[x.length][y[0].length];
		for (int i = 0; i < x.length; i++)
		{
			for (int k = 0; k < y[0].length; k++)
			{
				for (int j = 0; j < x[0].length; j++)
				{

					z[i][k] += x[i][j] * y[j][k];

				}
			}
		}

		return z;
	}

	/**
	 * Add 2 matrices of one row
	 */
	public static double[][] addMatrix(double[] x, double[] y) throws Exception
	{
		// check for errors in the dimensions
		if (x.length != y.length)
		{
			throw new Exception("Error: The two matrices must have the same dimensions");
		}
		// make x & y a 2D arrays to pass them
		double k[][] =
		{ x };
		double l[][] =
		{ y };
		double[][] p = addMatrix(k, l);
		return p;
	}

	/**
	 * add 2 matrices with any number of rows and columns
	 */
	public static double[][] addMatrix(double[][] x, double[][] y) throws Exception
	{
		// check for the errors in the matrices dimensions
		if ((x.length != y.length) || (x[0].length != y[0].length))
		{
			throw new Exception("Error: The two matrices must have the same dimensions");
		}
		// result matrix
		double z[][] = new double[x.length][x[0].length];

		for (int i = 0; i < y.length; i++)
		{
			for (int j = 0; j < y[0].length; j++)
			{
				z[i][j] = x[i][j] + y[i][j];
			}
		}
		return z;
	}

	/**
	 * Add 2 matrices of one row
	 */
	public static double[][] subMatrix(double[] x, double[] y) throws Exception
	{
		// check for errors in the dimensions
		if (x.length != y.length)
		{
			throw new Exception("Error: The two matrices must have the same dimensions");
		}
		// make x & y a 2D arrays to pass them
		double k[][] =
		{ x };
		double l[][] =
		{ y };
		double[][] p = subMatrix(k, l);
		return p;
	}

	/**
	 * add 2 matrices with any number of rows and columns
	 */
	public static double[][] subMatrix(double[][] x, double[][] y) throws Exception
	{
		// check for the errors in the matrices dimensions
		if ((x.length != y.length) || (x[0].length != y[0].length))
		{
			throw new Exception("Error: The two matrices must have the same dimensions");
		}
		// result matrix
		double z[][] = new double[x.length][x[0].length];

		for (int i = 0; i < y.length; i++)
		{
			for (int j = 0; j < y[0].length; j++)
			{
				z[i][j] = x[i][j] - y[i][j];
			}
		}
		return z;
	}

	/**
	 * Get the inverse of the matrix
	 */
	public static double[][] invMatrix(double[][] x) throws Exception
	{
		// check the matrix is a square one
		if (x.length != x[0].length)
		{
			throw new Exception("Error: The matrix must be a sqaure matrix");
		}
		// result matrix z
		double[][] z = new double[x.length][x[0].length];

		// Initialize an identity matrix with the same dimensions of x
		for (int i = 0; i < x.length; i++)
		{
			for (int j = 0; j < x.length; j++)
			{
				if (i == j)
					z[i][j] = 1;
				else
					z[i][j] = 0;
			}
		}

		// ////////////////////////////////////////////////////////////

		for (int i = 0; i < x.length; i++)
		{
			// the pivot element
			double pivot = x[i][i];
			// dividing the row by the pivot element
			for (int j = 0; j < x.length; j++)
			{
				x[i][j] = x[i][j] / (double) pivot;
				z[i][j] = z[i][j] / (double) pivot;
			}
			// zero the elements above and below the pivot element
			for (int j = 0; j < x.length; j++)
			{
				if (i != j)
				{
					double mult = x[j][i];
					for (int k = 0; k < x.length; k++)
					{

						x[j][k] = (-mult * x[i][k]) + x[j][k];
						z[j][k] = (-mult * z[i][k]) + z[j][k];

					}
				}
			}
		}
		return z;
	}

	/**
	 * @param matrix
	 *            a 2D array of doubles
	 * @return a readable string representation of the 2D array
	 */
	public static String matrixString(double[][] matrix)
	{
		StringBuilder result = new StringBuilder();
		for (double[] row : matrix)
		{
			for (double item : row)
			{
				result.append(item + " ");
			}
			result.append("\n");
		}
		return result.toString();
	}

	/**
	 * a testing client
	 */
	public static void main(String... args)
	{

		// testing operations on matrices
		try
		{
			double[][] m1 = new double[][]
			{
			{ 1, 2 },
			{ 3, 4 } };

			double[][] m2 = new double[][]
			{
			{ 2, 3 },
			{ 4, 5 } };

			System.out.println(matrixString(addMatrix(m1, m2)));
			System.out.println(matrixString(subMatrix(m1, m2)));
			System.out.println(matrixString(mulMatrix(m1, m2)));
			System.out.println(matrixString(invMatrix(m1)));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
