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
	 * finds the partial derivative for the function when derived with respect
	 * to variable1 then with respect to variable 2
	 * 
	 * @param values
	 *            the values of the variables
	 * @param varialble1Index
	 *            the index of the first variable we'll differentiate to in the
	 *            array variables
	 * @param variable2Index
	 *            the index of the second variable we'll differentiate to in the
	 *            array variables
	 */
	public double evaluateSecondPartialDerivativeat(double[] values, int varialble1Index,
			int variable2Index)
	{
		double result = 0;
		double h = Math.sqrt(epsilon);

		// let variable1Index is the index of x, variable2Index is the index of
		// y
		// F(x+h, y+h)
		values[varialble1Index] += h;
		values[variable2Index] += h;
		result += evaluateAt(values);

		// F(x+h, y-h)
		values[variable2Index] -= 2 * h;
		result -= evaluateAt(values);

		// F(x-h, y-h)
		values[varialble1Index] -= 2 * h;
		result += evaluateAt(values);

		// F(x-h, y+h)
		values[variable2Index] += 2 * h;
		result -= evaluateAt(values);

		result /= 4 * h * h;
		return result;
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

		// Remove spaces and gets index for separators
		String tempString = equationStr.replace(" ", "");
		ArrayList<Integer> separatorsndex = new ArrayList<>();
		for (int i = 0; i < tempString.length(); i++)
		{
			String currentChar = tempString.charAt(i) + "";
			if (operatorsSet.contains(currentChar) || currentChar.equals("(")
					|| currentChar.equals(")"))
				separatorsndex.add(i);
		}

		// if there's no separators
		if (separatorsndex.size() == 0)
		{
			String onlyToken = tempString;
			if (variablesSet.contains(onlyToken) || isDouble(onlyToken))
			{
				ArrayList<String> result = new ArrayList<>();
				result.add(onlyToken);
				return result;
			} else
			{
				throw new Exception();
			}
		}

		// the first token
		if (separatorsndex.get(0) != 0)
		{
			String firstToken = tempString.substring(0, separatorsndex.get(0));
			if (variablesSet.contains(firstToken) || isDouble(firstToken))
			{
				tempResult.add(firstToken);
			} else
			{
				throw new Exception();
			}
		}

		// operate on each token
		int currentIndex = separatorsndex.get(0);
		int separatorIndex = 0;
		while (separatorIndex < separatorsndex.size())
		{
			if (separatorsndex.contains(currentIndex))
			{
				// if this index is a separator then add it
				tempResult.add(tempString.charAt(currentIndex) + "");
				separatorIndex++;
				currentIndex++;
			} else
			{
				// parse the next token
				String token = tempString.substring(currentIndex, separatorsndex
						.get(separatorIndex));
				if (variablesSet.contains(token) || isDouble(token))
				{
					tempResult.add(token);
					currentIndex += token.length();
				} else
				{
					throw new Exception();
				}

			}

		}

		// parse the last token
		if (separatorsndex.get(separatorsndex.size() - 1) != tempString.length() - 1)
		{
			String lastToken = tempString
					.substring(separatorsndex.get(separatorsndex.size() - 1) + 1);
			if (variablesSet.contains(lastToken) || isDouble(lastToken))
			{
				tempResult.add(lastToken);
			} else
			{
				throw new Exception();
			}
		}

		// form the result list
		ArrayList<String> result = new ArrayList<>();
		for (String token : tempResult)
			result.add(token);

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
			{ "x", "x2", "x3", "y" }, "(4*x^2 + 2*x*y + 3)/2");
			System.out.println(equation.evaluateSecondPartialDerivativeat(new double[]
			{ 1.0, 2.0, 3.0, 3.0 }, 0, 3));
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
