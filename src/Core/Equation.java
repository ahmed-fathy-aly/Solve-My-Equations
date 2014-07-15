package Core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Equation models a multi-variable non-linear mathematical equations example :
 * (3x)*(2^y) - 3/z The parsing algorithm used is Disjkstra shunting-yard
 * algorithm which gives the reverse polish notation
 * http://en.wikipedia.org/wiki/Shunting-yard_algorithm
 * http://en.wikipedia.org/wiki/Reverse_Polish_notation
 * 
 * @author Ahmed Fathy Aly
 * 
 */
public class Equation
{
	/*
	 * The precedence defines which operators are evaluated first The
	 * associativity is either true if right or false if left
	 * http://en.wikipedia.org/wiki/Operator_associativity
	 * http://en.wikipedia.org/wiki/Order_of_operations
	 */
	public static HashMap<String, Integer> operatorToPrecedence;
	public static HashMap<String, Boolean> operatorToAssociativity;
	static
	{
		operatorToPrecedence = new HashMap<>();
		operatorToPrecedence.put("+", 2);
		operatorToPrecedence.put("-", 2);
		operatorToPrecedence.put("*", 3);
		operatorToPrecedence.put("/", 3);
		operatorToPrecedence.put("^", 4);

		operatorToAssociativity = new HashMap<>();
		operatorToAssociativity.put("+", false);
		operatorToAssociativity.put("-", false);
		operatorToAssociativity.put("*", false);
		operatorToAssociativity.put("/", false);
		operatorToAssociativity.put("^", true);
	}

	/* member variables */
	private int nVariables;
	private String[] variables;
	private String equationStr;
	private double epsilon;
	private HashSet<String> variablesSet;
	private HashSet<String> operatorsSet;
	public Queue<String> reversePolishNotation;

	/**
	 * @param nVariables
	 *            number of variables
	 * @param variables
	 *            names of variables in the equation
	 * @param equationStr
	 *            ex: "(3x)*(2^y) - 3"
	 * @throws Exception
	 */
	public Equation(int nVariables, String[] variables, String equationStr) throws Exception
	{
		// member variables
		this.nVariables = nVariables;
		this.variables = variables;
		this.equationStr = equationStr;
		this.epsilon = 0.000000001;

		// Initialize sets of variables and operators
		this.variablesSet = new HashSet<>();
		for (String var : variables)
			this.variablesSet.add(var);

		this.operatorsSet = new HashSet<>();
		for (String operator : operatorToPrecedence.keySet())
			this.operatorsSet.add(operator);

		// Parse the string
		reversePolishNotation = parseEquation();
	}

	/**
	 * @return the number of variables in the equation
	 */
	public int getnVariables()
	{
		return nVariables;
	}

	/**
	 * @return a list of variables names
	 */
	public String[] getVariables()
	{
		return variables;
	}

	/**
	 * @return the equation as a string
	 */
	public String getEquationStr()
	{
		return equationStr;
	}

	/**
	 * @return epsilon, the small value used to get derivative
	 */
	public double getEpsilon()
	{
		return epsilon;
	}

	/**
	 * @param epsilon
	 *            the new epsion used to get the derivative
	 */
	public void setEpsilon(double epsilon)
	{
		this.epsilon = epsilon;
	}

	/**
	 * @param values
	 *            the values of the variables
	 * @return the value the equation yields at the values provided
	 */
	public double evaluateAt(double[] values)
	{
		// Clone the reverse polish notation
		Queue<String> tokens = new LinkedList<>(reversePolishNotation);
		Stack<Double> operandsStack = new Stack<>();
		HashMap<String, Double> variableToValue = new HashMap<>();
		for (int i = 0; i < values.length; i++)
			variableToValue.put(this.variables[i], values[i]);

		// operate on each token
		while (tokens.size() != 0)
		{
			// check the type of the token
			String token = tokens.poll();
			boolean isNumber = isDouble(token);
			boolean isVariable = variablesSet.contains(token);
			boolean isOperator = operatorsSet.contains(token);

			if (isNumber)
			{
				operandsStack.push(Double.parseDouble(token));
			} else if (isVariable)
			{
				operandsStack.push(variableToValue.get(token));
			} else
			{
				// operate on the two values
				double value2 = operandsStack.pop();
				double value1 = operandsStack.pop();
				operandsStack.push(doOperation(value1, value2, token));
			}
		}

		return operandsStack.pop();
	}

	/**
	 * @param values
	 *            values of the variables
	 * @param variableIndex
	 *            the index of the variable we'll differentiate to in the array
	 *            variables
	 * @return the value of the derivative with respect to the variable
	 *         indicated by variableIndex
	 */
	public double evaluateDerivativeAt(double[] values, int variableIndex)
	{
		// computer (f(x + epsilon) - f(x-epsilon))/2epsilon
		values[variableIndex] -= epsilon;
		double leftPoint = evaluateAt(values);
		values[variableIndex] += 2 * epsilon;
		double rightPoint = evaluateAt(values);

		return (rightPoint - leftPoint) / (2 * epsilon);
	}

	/**
	 * @param values
	 *            values of the variables
	 * @param varialbeIndex
	 *            the index of the variable we'll differentiate to in the array
	 *            variables
	 * @return value of the second derivative with respect to the variable
	 *         indicated by variableIndex
	 */
	public double evaluateSecondDerivativeAt(double[] values, int variableIndex)
	{
		// find f(x+h) - 2*f(x) + f(x-h) and divilide by h^2
		double result = 0;
		double h = Math.sqrt(epsilon);
		values[variableIndex] += h;
		result += evaluateAt(values);

		values[variableIndex] -= h;
		result -= 2 * evaluateAt(values);

		values[variableIndex] -= h;
		result += evaluateAt(values);

		return (result / (h * h));

	}

	/**
	 * @param value1
	 *            left value
	 * @param value2
	 *            right value
	 * @param token
	 *            the operator
	 * @return value1 operator value2
	 */
	private Double doOperation(double value1, double value2, String operator)
	{
		if (operator.equals("+"))
			return value1 + value2;
		else if (operator.equals("-"))
			return value1 - value2;
		else if (operator.equals("*"))
			return value1 * value2;
		else if (operator.equals("/"))
			return value1 / value2;
		else if (operator.equals("^"))
			return Math.pow(value1, value2);

		return null;
	}

	/**
	 * @return a list with the operators, numbers, variables in the equation
	 * @throws Exception
	 */
	private ArrayList<String> tokenize() throws Exception
	{
		Queue<String> tempResult = new LinkedList<String>();

		// Remove spaces
		String tempString = equationStr.replace(" ", "");
		int originalLength = tempString.length();

		// test each substring of the equation
		for (int startIndex = 0; startIndex < tempString.length(); startIndex++)
		{
			// Check if it's a number or a dot or a parenthesis
			char c = tempString.charAt(startIndex);
			if (c == '.' || c == '(' || c == ')' || !(c <= '/' || c >= ':'))
			{
				tempResult.add(c + "");
				continue;
			}

			// get every substring to find which operator or variable it is
			for (int endIndex = startIndex + 1; endIndex <= tempString.length(); endIndex++)
			{
				String currentString = tempString.substring(startIndex, endIndex);
				if (operatorsSet.contains(currentString) || variablesSet.contains(currentString))
				{
					tempResult.add(currentString);
					startIndex = endIndex - 1;
					break;
				}
			}
		}

		// concatenate numbers
		ArrayList<String> result = concatenateTokens(tempResult);

		// check the length of the result
		int resultLength = 0;
		for (String string : result)
			resultLength += string.length();

		if (resultLength != originalLength)
			throw new Exception();
		return result;
	}

	/**
	 * @param tempResult
	 *            a queue with the tokens but numbers not concatenated
	 * @return concatenates list of tokenes: [x,1 , 2, ., 3] -> [x, 12.3]
	 */
	private ArrayList<String> concatenateTokens(Queue<String> tempResult)
	{
		ArrayList<String> result = new ArrayList<>();
		while (tempResult.size() != 0)
		{
			String currentToken = tempResult.poll();

			// if it is a number then keep pulling
			if (isDouble(currentToken))
			{
				String number = "" + currentToken;

				// get the rest of the number
				while (tempResult.size() > 0
						&& !(tempResult.peek().charAt(0) <= '/' || tempResult.peek().charAt(0) >= ':'))
					number += tempResult.poll();

				// check the decimal place
				if (tempResult.size() > 0 && tempResult.peek().charAt(0) == '.')
				{
					number += tempResult.poll();
					while (tempResult.size() > 0
							&& !(tempResult.peek().charAt(0) <= '/' || tempResult.peek().charAt(0) >= ':'))
						number += tempResult.poll();
				}
				result.add(number);
			} else
			{
				result.add(currentToken);
			}
		}
		return result;
	}

	/**
	 * parses the equation string
	 * http://en.wikipedia.org/wiki/Reverse_Polish_notation
	 * 
	 * @return the reverse polish notation of the parsed equation
	 * @throws Exception
	 */
	private Queue<String> parseEquation() throws Exception
	{
		ArrayList<String> result = new ArrayList<>();

		// Get the list of tokens and intialize the stack, queue
		ArrayList<String> tokens = tokenize();
		Stack<String> operatorStack = new Stack<>();
		Queue<String> outputQueue = new LinkedList<String>();

		// operate on each token
		for (String token : tokens)
		{
			// Check the token's type
			boolean isNumber = isDouble(token);
			boolean isVariable = variablesSet.contains(token);
			boolean isOperator = operatorsSet.contains(token);

			if (isNumber || isVariable)
			{
				outputQueue.add(token);
			} else if (isOperator)
			{
				// keep popping operators with higher precedence or left
				// associativity with higher or equal precedence

				// if the stack is empty or that's not an operator
				if (operatorStack.size() == 0
						|| operatorsSet.contains(operatorStack.peek()) == false)
				{
					operatorStack.push(token);
					continue;
				}

				// check popping condition and keep popping
				String o2 = operatorStack.peek();
				boolean shouldPop = operatorToPrecedence.get(token) < operatorToPrecedence.get(o2);
				shouldPop |= (operatorToPrecedence.get(token) <= operatorToPrecedence.get(o2))
						&& operatorToAssociativity.get(token) == false;
				while (shouldPop)
				{
					outputQueue.add(operatorStack.pop());
					if (operatorStack.size() == 0
							|| operatorsSet.contains(operatorStack.peek()) == false)
					{
						break;
					}
					o2 = operatorStack.peek();
					shouldPop = operatorToPrecedence.get(token) < operatorToPrecedence.get(o2);
					shouldPop |= (operatorToPrecedence.get(token) <= operatorToPrecedence.get(o2))
							&& operatorToAssociativity.get(token) == false;
				}
				operatorStack.push(token);
			}

			else if (token.charAt(0) == '(')
			{
				operatorStack.push(token);
			} else if (token.charAt(0) == ')')
			{
				// Keep popping till a ( appears
				while (operatorStack.peek().charAt(0) != '(')
				{
					outputQueue.add(operatorStack.pop());
				}
				operatorStack.pop();

			}

		}

		// pop the remaining operator stack to the output queue
		while (operatorStack.size() != 0)
			outputQueue.add(operatorStack.pop());

		return outputQueue;
	}

	/**
	 * @param s
	 *            a string to be tested
	 * @return true if that string can be converted to a double
	 */
	private static boolean isDouble(String s)
	{
		try
		{
			Double.parseDouble(s);
		} catch (NumberFormatException e)
		{
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	/**
	 * For debugging, testing
	 */
	public static void main(String[] args)
	{
		Equation equation;
		try
		{
			equation = new Equation(3, new String[]
			{ "x", "y", "z" }, "(1*(x^3) + 4*y + 3^z) / 0.5 ");
			System.out.println(equation.evaluateAt(new double[]
					{ 2, 2, 3 }));
					System.out.println(equation.evaluateDerivativeAt(new double[]
					{ 3, 2, 3 }, 0));
					System.out.println(equation.evaluateSecondDerivativeAt(new double[]
					{ 3, 2, 3 }, 0));		} catch (Exception e)
		{
			e.printStackTrace();
		}


	}

}
