package solvers;

import Core.Equation;

/**
 * solves a single equation using bisection method (binary search)
 *
 */
public class BisectionMethodSolver
{

	/* member variables */
	private Equation equation;
	public BisectionMethodSolver(Equation equation)
	{
		this.equation = equation;
	}
	/**
	 * Solves the equation to get the values of its variables that makes it evaluate to zero
	 * @return an array of the values of the variables
	 */
	public double[] solve()
	{
		return new double[this.equation.getnVariables()];
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
