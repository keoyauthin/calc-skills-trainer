package de.fh_erfurt.cst.view;

import java.text.DecimalFormat;
import java.text.ParseException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import de.fh_erfurt.cst.R;
import de.fh_erfurt.cst.controller.ProblemController;
import de.fh_erfurt.cst.model.VarStore.ProblemType;
import de.fh_erfurt.cst.util.DialogUtils;
import de.fh_erfurt.cst.util.Utils;


public class ProblemActivity extends Activity
implements OnClickListener
{
	private ProblemController ctrl;
	private TextView lblProblem;
	
	private Button btnOkay;
	private Spinner lstLevel;
	
	private EditText txtAnswer;
	private DecimalFormat df;
	
	/** The answer of the user as <b>unformatted</b> number string presentation.
	 * 
	 * <p>This is like a standard floating point number but with additional 
	 * special cases: {"-", "-.", ".", "N."}
	 */
	private String answerString;
	
	// --------------------------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_problem);
		ctrl = new ProblemController(this, ProblemType.ADDITION);
		
		lblProblem = (TextView) findViewById(R.id.problem_task);
		txtAnswer = (EditText) findViewById(R.id.problem_input_answer);
		txtAnswer.setInputType(InputType.TYPE_NULL);
		btnOkay = (Button) findViewById(R.id.problem_button_submit);
		lstLevel = (Spinner) findViewById(R.id.problem_level_spinner);
		
		btnOkay.setOnClickListener(this);
		answerString = txtAnswer.getText().toString();
		
		initializeSpinners();
		ctrl.nextProblem();
	}
	
	@Override
	protected void onResume()
	{
		invalidate();
		Utils.invalidateDecimalFormat();
		ctrl.nextProblem();
		super.onResume();
	}
	
	// --------------------------------------------------------------------------

	@Override
	public void onClick(View v) 
	{
		Resources res = getResources();
		switch (v.getId())
		{
			case R.id.problem_button_submit:
			{
				Double answer = getLogicalNumber(answerString);
				if (answer == null)
				{
					DialogUtils.showMessageDialog(this, res.getString(R.string.dialog_error_invalid_number));
					clearAnswerField();
				}
				else
				{
					ctrl.receiveAnswer( answer );	
				}
				break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    MenuInflater inflater = getMenuInflater();
	    switch(ctrl.getProblemType())
	    {
		    case ADDITION:
		    	inflater.inflate(R.menu.menu_addition, menu);
		    	break;
		    case SUBTRACTION:
		    	inflater.inflate(R.menu.menu_subtraction, menu);
		    	break;
		    case MULTIPLICATION:
		    	inflater.inflate(R.menu.menu_multiplication, menu);
		    	break;
		    case DIVISION:
		    	inflater.inflate(R.menu.menu_division, menu);
		    	break;		    	
	    }
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) 
	    {
	        case R.id.addition_configuration:
	        {
	        	Intent i = new Intent( getApplicationContext(), SettingsActivity.class );
	        	i.putExtra("ProblemType", ProblemType.ADDITION.toString());
	        	startActivity( i );
	            return true;	        	
	        }
	        
	        case R.id.addition_statistic:
	        {
	        	Intent i = new Intent( getApplicationContext(), StatisticActivity.class );
	        	i.putExtra("ProblemType", ProblemType.ADDITION.toString());
	        	startActivity( i );
	        	return true;
	        }
	        
	        case R.id.addition_help:
	        {
	        	Intent i = new Intent( getApplicationContext(), HelpActivity.class );
	        	i.putExtra("ProblemType", ProblemType.ADDITION.toString());
	        	startActivity( i );
	        	return true;
	        }
	        
	        /////
	        
	        case R.id.subtraction_configuration:
	        {
	        	Intent i = new Intent( getApplicationContext(), SettingsActivity.class );
	        	i.putExtra("ProblemType", ProblemType.SUBTRACTION.toString());
	        	startActivity( i );
	            return true;	        	
	        }
	        
	        case R.id.subtraction_statistic:
	        {
	        	Intent i = new Intent( getApplicationContext(), StatisticActivity.class );
	        	i.putExtra("ProblemType", ProblemType.SUBTRACTION.toString());
	        	startActivity( i );
	        	return true;
	        }
	        
	        case R.id.subtraction_help:
	        {
	        	Intent i = new Intent( getApplicationContext(), HelpActivity.class );
	        	i.putExtra("ProblemType", ProblemType.SUBTRACTION.toString());
	        	startActivity( i );
	        	return true;
	        }
	        
	        ////
	        
	        case R.id.multiplication_configuration:
	        {
	        	Intent i = new Intent( getApplicationContext(), SettingsActivity.class );
	        	i.putExtra("ProblemType", ProblemType.MULTIPLICATION.toString());
	        	startActivity( i );
	            return true;	        	
	        }
	        
	        case R.id.multiplication_statistic:
	        {
	        	Intent i = new Intent( getApplicationContext(), StatisticActivity.class );
	        	i.putExtra("ProblemType", ProblemType.MULTIPLICATION.toString());
	        	startActivity( i );
	        	return true;
	        }
	        
	        case R.id.multiplication_help:
	        {
	        	Intent i = new Intent( getApplicationContext(), HelpActivity.class );
	        	i.putExtra("ProblemType", ProblemType.MULTIPLICATION.toString());
	        	startActivity( i );
	        	return true;
	        }
	        
	        ////
	        
	        case R.id.division_configuration:
	        {
	        	Intent i = new Intent( getApplicationContext(), SettingsActivity.class );
	        	i.putExtra("ProblemType", ProblemType.DIVISION.toString());
	        	startActivity( i );
	            return true;	        	
	        }
	        
	        case R.id.division_statistic:
	        {
	        	Intent i = new Intent( getApplicationContext(), StatisticActivity.class );
	        	i.putExtra("ProblemType", ProblemType.DIVISION.toString());
	        	startActivity( i );
	        	return true;
	        }
	        
	        case R.id.division_help:
	        {
	        	Intent i = new Intent( getApplicationContext(), HelpActivity.class );
	        	i.putExtra("ProblemType", ProblemType.DIVISION.toString());
	        	startActivity( i );
	        	return true;
	        }
	        
	        ////
	        
	        default:
	        {
	            return super.onOptionsItemSelected(item);
	        }
	    }
	}

	public void onNumberPadAction(View view)
	{
		switch (view.getId())
		{
			case R.id.problem_button_backspace:
			{
				int length = answerString.length();
				if (length > 0)
				{
					answerString = answerString.substring(0, length - 1);
				}
				break;
			}
			case R.id.problem_button_point:
			{
				if (! answerString.contains("."))
					answerString += ".";
				break;
			}			
			case R.id.problem_button_minus:
			{
				if (answerString.isEmpty())
				{
					answerString = "-";
				}
				else if (answerString.equals("-"))
				{
					answerString = "";
				}
				else if (answerString.equals("."))
				{
					answerString = "-.";
				}
				else if (answerString.equals("-."))
				{
					answerString = ".";
				}
				else
				{
					Double d = Double.parseDouble(answerString);
					d *= -1;
					answerString = d.toString();
				}
				break;
			}
			
			////
			
			case R.id.problem_button_zero:
			{
				if (! answerString.equals("0"))
					answerString += "0";
				break;
			}			
			case R.id.problem_button_one:
			{
				answerString += "1";
				break;
			}
			case R.id.problem_button_two:
			{
				answerString += "2";
				break;
			}
			case R.id.problem_button_three:
			{
				answerString += "3";
				break;
			}
			case R.id.problem_button_four:
			{
				answerString += "4";
				break;
			}
			case R.id.problem_button_five:
			{
				answerString += "5";
				break;
			}
			case R.id.problem_button_six:
			{
				answerString += "6";
				break;
			}
			case R.id.problem_button_seven:
			{
				answerString += "7";
				break;
			}
			case R.id.problem_button_eight:
			{
				answerString += "8";
				break;
			}
			case R.id.problem_button_nine:
			{
				answerString += "9";
				break;
			}
		}

		txtAnswer.setText(getFormattedNumber(answerString));
		txtAnswer.setSelection(txtAnswer.getText().length());
	}
	
	// --------------------------------------------------------------------------	
	
	public void setProblemText(String text)
	{
		lblProblem.setText(text);
	}
	
	public void setLevelDisplay(int level)
	{
		lstLevel.setSelection(level - 1);
	}
	
	public void clearAnswerField()
	{
		txtAnswer.setText("");
		answerString = "";
	}
	
	// --------------------------------------------------------------------------
	
	private void initializeSpinners()
	{
		// Initialize Problem type spinner		
		Spinner lstProblemType = (Spinner) findViewById(R.id.problem_type_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.problem_types, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		lstProblemType.setAdapter(adapter);
		lstProblemType.setOnItemSelectedListener(getTypeSpinnerListener());

		// Initiailize Level choice spinner
		Spinner lstProblemLevel = (Spinner) findViewById(R.id.problem_level_spinner);
		adapter = ArrayAdapter.createFromResource(this,
		        R.array.problem_levels, android.R.layout.simple_spinner_item); 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		lstProblemLevel.setAdapter(adapter);
		lstProblemLevel.setOnItemSelectedListener(getLevelSpinnerListener());
	}
	
	private Double getLogicalNumber(String answerString)
	{
		if (answerString.isEmpty() || answerString.equals("-"))
		{
			return null;
		}
		else
		{
			try 
			{
				return Double.parseDouble(answerString);
			} 
			catch (Exception e) 
			{
				return null;
			}
		}
	}
	
	private String getFormattedNumber(String answerString)
	{
		String result = null;
		
		if (answerString.isEmpty() || answerString.equals("-") || answerString.equals(".") || answerString.equals("-."))
		{
			return answerString;
		}
	
		try 
		{
			Double d = Double.parseDouble(answerString);
			DecimalFormat df = Utils.getDecimalFormatInstance();
			result = df.format(d);
		} 
		catch (Exception e) 
		{
			return null;
		}

		if (answerString.endsWith(".") )
		{
			Resources res = getResources();
			result += res.getString(R.string.decimal_point);;
		}
		else if (answerString.endsWith(".0"))
		{
			result += ".0";
		}
		else if (answerString.contains(".") && 
				answerString.endsWith("0") && answerString.length() > 1)
		{
			// Are there any numbers except zero after the decimal point?
			if (answerString.matches("\\d*[.][0]*[^0]\\d*"))
			{
				// Append zeros from the tail !
				result += answerString.split("[.][0-9]*[1-9]*[^0]")[1];	
			}
			else
			{
				// Append the tail plus leading decimal point
				result += "." + answerString.split("[.]")[1];
			}
			
		}
		return result; 
	}
	
	private ProblemType getProblemType()
	{
		ProblemType problemType = null;
		
		Bundle extras = getIntent().getExtras();
		if (extras == null)
			throw new IllegalStateException("No extras has been delivered to the Problem View.");

		String value = extras.getString("ProblemType");
		if (value == null)
			throw new IllegalStateException("Value for 'ProblemType' is missing in the extras.");
		
		problemType = ProblemType.valueOf(value);
		return problemType;
	}

	private OnItemSelectedListener getTypeSpinnerListener()
	{
		final ProblemActivity v = this;

		OnItemSelectedListener listenerProblemType = new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(
					AdapterView<?> parent, 
					View view, 
					int pos,
					long id) 
			{
				switch(parent.getSelectedItemPosition())
				{
					case 0:
						ctrl = new ProblemController(v, ProblemType.ADDITION);
						break;
					case 1:
						ctrl = new ProblemController(v, ProblemType.SUBTRACTION);
						break;
					case 2:
						ctrl = new ProblemController(v, ProblemType.MULTIPLICATION);
						break;
					case 3:
						ctrl = new ProblemController(v, ProblemType.DIVISION);
						break;				
				}
				invalidate();
				ctrl.clearProblemQueue();
				ctrl.nextProblem();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		};
		
		
		return listenerProblemType;
	}
	
	private OnItemSelectedListener getLevelSpinnerListener()
	{
		
		OnItemSelectedListener levelSpinnerListener = new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(
					AdapterView<?> parent, 
					View view, 
					int pos,
					long id) 
			{
				ctrl.clearProblemQueue();
				ctrl.setCurrentLevel(pos + 1);
				ctrl.nextProblem();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		};
		
		return levelSpinnerListener;
	}
	
	private void invalidate()
	{
		if (Build.VERSION.SDK_INT >= 11)
		{
			invalidateOptionsMenu();
		}
	}
	
	
}
