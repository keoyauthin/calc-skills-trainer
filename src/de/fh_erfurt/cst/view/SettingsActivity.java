package de.fh_erfurt.cst.view;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import de.fh_erfurt.cst.R;
import de.fh_erfurt.cst.model.VarStore.ProblemType;
import de.fh_erfurt.cst.util.Utils;


public class SettingsActivity extends PreferenceActivity
{
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		/*
		switch ( getProblemType() )
		{
			case ADDITION:
				addPreferencesFromResource(R.xml.preferences_addition);
				break;
			case SUBTRACTION:
				addPreferencesFromResource(R.xml.preferences_subtraction);
				break;
			case MULTIPLICATION:
				addPreferencesFromResource(R.xml.preferences_multiplication);
				break;
			case DIVISION:
				addPreferencesFromResource(R.xml.preferences_division);
				break;	
		}
		*/
		
		addPreferencesFromResource(R.xml.preferences_general);
	}
	
	@Override
	protected void onResume()
	{
		Utils.invalidateDecimalFormat();
		super.onResume();
	}
	
	// --------------------------------------------------------------------------
	
	private ProblemType getProblemType()
	{
		ProblemType problemType = null;
		
		Bundle extras = getIntent().getExtras();
		if (extras == null)
			throw new IllegalStateException("No extras has been delivered to the SettingsActivity.");

		String value = extras.getString("ProblemType");
		if (value == null)
			throw new IllegalStateException("Value for 'ProblemType' is missing in the extras.");
		
		problemType = ProblemType.valueOf(value);
		return problemType;
	}
	

}
