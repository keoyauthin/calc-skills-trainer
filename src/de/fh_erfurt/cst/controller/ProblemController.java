package de.fh_erfurt.cst.controller;

import java.util.LinkedList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;

import de.fh_erfurt.cst.model.Problem;
import de.fh_erfurt.cst.model.StatisticModel;
import de.fh_erfurt.cst.model.VarStore;
import de.fh_erfurt.cst.model.VarStore.ProblemType;
import de.fh_erfurt.cst.util.DialogUtils;
import de.fh_erfurt.cst.util.Utils;
import de.fh_erfurt.cst.view.ProblemActivity;
import de.fh_erfurt.cst.R;

/**
 * The ProblemController class is responsible for the logical
 * tasks, e.g. creating new training tasks and triggering the
 * display update of the mapped view.
 * 
 * It is intended to be instantiated by {@link ProblemActivity}.
 *
 */
public class ProblemController 
{
	/** The interface where the user sees his/her training tasks */
	private ProblemActivity view;
	
	/** The class which handels the data for the statistic */
	private StatisticModel statistic;
	
	/** An enum value for the different types of training problems like addition or subtraction */
	private ProblemType problemType;
	
	/** A queue to make it possible to repeat training problems */
	private LinkedList<Problem> problemQueue;
	
	/** Stores an object with infos about the currently displayed training problem */
	private Problem currentProblem;
	
	/** The amount of displayed problems before a rescheduled problem occurs again */
	final private int RESCHEDULE_INDEX = 1;
	
	// --------------------------------------------------------------------------
	
	/**
	 * Creates an new instance of the class which controlles the logic of
	 * the training sessions.
	 * 
	 * @param view the view with the GUI for the training sessions
	 * @param problemType the problem type the user is training
	 */
 	public ProblemController(ProblemActivity view, ProblemType problemType)
	{
		this.view = view;
		this.problemType = problemType;
		
		problemQueue = new LinkedList<Problem>();
		statistic = new StatisticModel(view, problemType);
	}
	
	// --------------------------------------------------------------------------

 	/**
 	 * Triggers the display of a new training problem in the UI
 	 */
	public void nextProblem()
	{
		int currentLevel = getCurrentLevel();
		
		if (problemQueue.isEmpty())
			currentProblem = Problem.createNewProblem(problemType, currentLevel, view);
		else
			currentProblem = problemQueue.pollFirst();
		
		view.clearAnswerField();
		view.setProblemText(currentProblem.getText());
		view.setLevelDisplay(currentLevel);
	}
 	
	/**
	 * Notify the logic class about the answer of the user.
	 * 
	 * After evaluation the method triggers {@link #nextProblem()}
	 * from a listener registered on an alert dialog.
	 * 
	 * @param answer the number presentation of the user's input 
	 */
	public void receiveAnswer(Double answer) 
	{
		int currentLevel = getCurrentLevel();
		Resources res = view.getResources();

		if (answer.equals(currentProblem.getRightAnswer()))
		{
			
			if (VarStore.getBooleanProperty(
					view, 
					"general_level_modulation", 
					res.getBoolean(R.bool.default_general_level_modulation)))
			{
				setCurrentLevel(currentLevel + 1);
			}
			showAlertDialogAndContinue(res.getString(R.string.dialog_right_answer), getRightAnswerMessage());
			statistic.addResult(true, currentLevel);
		}
		else
		{
			if (VarStore.getBooleanProperty(
					view, 
					"general_level_modulation",  
					res.getBoolean(R.bool.default_general_level_modulation)))
			{
				if (currentLevel > 1)
					setCurrentLevel(currentLevel - 1);
			}
			if (VarStore.getBooleanProperty(
					view, 
					"general_reschedule_problems", 
					res.getBoolean(R.bool.default_general_reschedule_problems)))
			{
				resheduleProblem(RESCHEDULE_INDEX, currentProblem);
			}
			showAlertDialogAndContinue(res.getString(R.string.dialog_wrong_answer), getFalseAnswerMessage(answer));
			statistic.addResult(false, currentLevel);
		} 
	}

	/** Returns the current type of training problem 
	 * @return an enum value for the problem type 
	 */
	public ProblemType getProblemType()
	{
		return problemType;
	}
	
	/**
	 * Returns the level on which the user is training
	 * @return an integer level code for the current level
	 */
	public int getCurrentLevel()
	{
		Integer currentLevel = null;
		Resources res = view.getResources();
		
		switch (problemType)
		{
			case ADDITION:
			{
				String levelAsString = VarStore.getStringProperty(
						view, 
						"addition_level", 
						res.getString(R.string.default_addition_level));
				currentLevel = Integer.parseInt(levelAsString);
				break;
			}
			case SUBTRACTION:
			{
				String levelAsString = VarStore.getStringProperty(
						view, 
						"subtraction_level", 
						res.getString(R.string.default_subtraction_level));
				currentLevel = Integer.parseInt(levelAsString);
				break;
			}
			case MULTIPLICATION:
			{
				String levelAsString = VarStore.getStringProperty(
						view, 
						"multiplication_level", 
						res.getString(R.string.default_multiplication_level));
				currentLevel = Integer.parseInt(levelAsString);
				break;
			}
			case DIVISION:
			{
				String levelAsString = VarStore.getStringProperty(
						view, 
						"division_level", 
						res.getString(R.string.default_division_level));
				currentLevel = Integer.parseInt(levelAsString);
				break;
			}
		}
		
		return currentLevel;
	}
	
	/**
	 * Sets the level in the settings and the display
	 * @param level the desired training level
	 */
	public void setCurrentLevel(int level)
	{
		VarStore.setStringProperty(view, getLevelKey(), Integer.toString(level));
		view.setLevelDisplay(level);
	}
	
	/**
	 * Removes all previously scheduled training problemes 
	 */
	public void clearProblemQueue()
	{
		problemQueue.clear();
	}
	
	// --------------------------------------------------------------------------
	
	/**
	 * Shows an alert dialog with answer evaluation and triggers the creation
	 * of a new problem <b>after</b> closing the dialog.
	 * @param title statement whether answer was right or wrong
	 * @param message solution of the problem
	 */
	private void showAlertDialogAndContinue(String title, String message)
	{		
		OnClickListener listener = new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int id) 
			{
				nextProblem();
			}
		};
		
		AlertDialog alert = DialogUtils.createAlertDialog(view, title, message);
		alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", listener);
		alert.show();
	}
	
	/**
	 * Convenience method to encapsulate the creation of the false answer method.
	 * 
	 * @param answer the wrong answer of the user as number presentation
	 * @return the message to be displayed in the alert dialog
	 */
	private String getFalseAnswerMessage(Double answer)
	{
		StringBuilder message = new StringBuilder();
		message.append(currentProblem.getText());
		message.append(" != ");
		message.append(Utils.toString(answer));
		message.append("\n");
		message.append(currentProblem.getText());
		message.append("  = ");
		message.append(Utils.toString(currentProblem.getRightAnswer()));
		return message.toString();
	}
	
	/**
	 * Convenience method to encapsulate the creation of the right answer method.
	 * 
	 * @param answer the right answer of the user as number presentation
	 * @return the message to be displayed in the alert dialog
	 */
	private String getRightAnswerMessage()
	{
		StringBuilder message = new StringBuilder();
		message.append(currentProblem.getText());
		message.append(" = ");
		message.append(Utils.toString(currentProblem.getRightAnswer()));
		return message.toString();
	}
	
	/**
	 * Get the key which is used in {@link SharedPreferences} to store the current level.
	 * 
	 * @return the value of 'android:key' for the level preference
	 * according to the current problem type.
	 */
	private String getLevelKey()
	{
		// TODO refactoring - maybe it would be better to use xml constants for this purpose
		String key = null;
		switch(problemType)
		{
			case ADDITION:
				key = "addition_level";
				break;
			case SUBTRACTION:
				key = "subtraction_level";
				break;
			case MULTIPLICATION:
				key = "multiplication_level";
				break;
			case DIVISION:
				key = "division_level";
				break;				
		}
		return key;
	}

	/**
	 * Insert a problem into the problem queue at the specified position
	 * @param problem the arithmetic problem to be resheduled
	 * @param targetIndex the index at which the resheduled problem shall reside in the problem queue list
	 */
	private void resheduleProblem(int targetIndex, Problem problem)
	{
		while(problemQueue.size() < targetIndex)
			problemQueue.add(Problem.createNewProblem(problemType, getCurrentLevel(), view));	
		problemQueue.add(problem);
	}

	// --------------------------------------------------------------------------
	
	/**
	 * Getter for the view which has created this class
	 * @return the UI where the user is training
	 */
	protected ProblemActivity getView() 
	{
		return view;
	}



}
