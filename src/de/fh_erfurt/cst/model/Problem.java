package de.fh_erfurt.cst.model;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;

import de.fh_erfurt.cst.R;
import de.fh_erfurt.cst.model.VarStore.ProblemType;
import de.fh_erfurt.cst.util.Utils;


public class Problem 
{
	private ProblemType problemType;
	private int level;
	private Context context;
	
	private double number1;
	private double number2;
	
	private double rightAnswer;
	private String problemText;
	
	// --------------------------------------------------------------------------
	
	/**
	 * @param problemType
	 * @param level
	 * @param context the current context. This is neccessary to read out properties.
	 */
	private Problem(ProblemType problemType, int level, Context context)
	{
		this.problemType = problemType;
		this.level = level;	
		this.context = context;
		
		String dp = VarStore.getStringProperty(
				context, 
				"general_decimal_places_answer", 
				context.getResources().getString(R.string.default_general_decimal_places_answer));
		int decimalPlaces = Integer.parseInt(dp);
		
		ArrayList<Integer> field = createProblemField(level);
		number1 = field.get(0);
		number2 = field.get(1);

		switch (problemType)
		{
			case ADDITION:
				problemText = Utils.toString(number1) + " + " + Utils.toString(number2);
				rightAnswer = Utils.round(number1 + number2, decimalPlaces);
				break;
			case SUBTRACTION:
				problemText = Utils.toString(number1) + " - " + Utils.toString(number2);
				rightAnswer = Utils.round(number1 - number2, decimalPlaces);
				break;
			case MULTIPLICATION:
				problemText = Utils.toString(number1) + " * " + Utils.toString(number2);
				rightAnswer = Utils.round(number1 * number2, decimalPlaces);
				break;
			case DIVISION:
				problemText = Utils.toString(number1) + " / " + Utils.toString(number2);
				rightAnswer = Utils.round(number1 / number2, decimalPlaces);
				break;				
		}
	}
	
	/**
	 * A factory method to create a new training problem
	 * 
	 * @param problemType the desired type of problem
	 * @param level the level of difficulty for the problem
	 * @param context the current context
	 * @return
	 */
	public static Problem createNewProblem(ProblemType problemType, int level, Context context)
	{
		if (level < 0)
			throw new IllegalArgumentException("The level must not be a negative number.");
		
		return new Problem(problemType, level, context);
	}	
	
	// --------------------------------------------------------------------------
	
	public double getRightAnswer()
	{
		return rightAnswer;
	}
	
	public String getText()
	{
		return problemText;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public Context getContext()
	{
		return context;
	}
	
	public ProblemType getProblemType()
	{
		return problemType;
	}
	
	// --------------------------------------------------------------------------
	
	/**
	 * Get a list with the numbers for the arithmetic problem  
	 * 
	 * <p>"Any set of objects upon which all four arithmetic operations 
	 * (except division by zero) can be performed, and where these 
	 * four operations obey the usual laws, is called a field."
	 * http://en.wikipedia.org/wiki/Arithmetic#Arithmetic_operations
	 */
	private ArrayList<Integer> createProblemField(int level)
	{
		int digitCountTotal = level + 1;
		int digitCountElement1 = (int) Math.ceil(digitCountTotal / 2.0);
		int digitCountElement2 = (int) Math.floor(digitCountTotal / 2.0);
		
		ArrayList<Integer> field = new ArrayList<Integer>();
		field.add(Utils.getRandomNumber(digitCountElement1));
		field.add(Utils.getRandomNumber(digitCountElement2));
		Collections.shuffle(field);
		
		return field;
	}
	

}
