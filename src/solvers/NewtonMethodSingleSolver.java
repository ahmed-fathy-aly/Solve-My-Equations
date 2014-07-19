package solvers;

import Core.Equation;

/**
 * solves one equation using newton's method
 * 
 */
public class NewtonMethodSingleSolver
{
	/* member variables */
	private Equation equation;

	public NewtonMethodSingleSolver(Equation equation)
	{
		this.equation = equation;
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Solves the equation to get the values of its variables that makes it evaluate to zero
	 * @return an array of the values of the variables
	 */
	public double[] solve()
	{
		return new double[this.equation.getnVariables()];
	}

}
