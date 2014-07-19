package solvers;

import Core.Equation;

/**
 * solves a system of equation using newton's matrix form
 * 
 */
public class NewtonMethodMultipleSolver
{
	/* member variables */
	Equation[] equations;

	public NewtonMethodMultipleSolver(Equation[] equations)
	{
		this.equations = equations;
	}

	/**
	 * Solves the equation to get the values of its variables that makes it
	 * evaluate to zero
	 * 
	 * @return an array of the values of the variables
	 */
	public double[] solve()
	{
		return new double[this.equations[0].getnVariables()];
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
