package de.fh_erfurt.cst.view;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.widget.TextView;
import de.fh_erfurt.cst.R;
import de.fh_erfurt.cst.model.VarStore.ProblemType;
import de.fh_erfurt.cst.util.Utils;


public class HelpActivity extends Activity
{
	
	
	// --------------------------------------------------------------------------	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		setHelpText(getProblemType());
	}
	
	@Override
	protected void onResume()
	{
		Utils.invalidateDecimalFormat();
		super.onResume();
	}
	
	// --------------------------------------------------------------------------
	
	private void setHelpText(ProblemType problemType)
	{
		TextView lblHelp = (TextView) findViewById(R.id.help_text);
		
		Resources res = getResources();
		switch(problemType)
		{
			case ADDITION:
			{
				String helpText = res.getString(R.string.help_addition);
				lblHelp.setText(Html.fromHtml(helpText));
				break;
			}
			case SUBTRACTION:
			{	
				String helpText = res.getString(R.string.help_subtraction);
				lblHelp.setText(Html.fromHtml(helpText));
				break;
			}	
			case MULTIPLICATION:
			{	
				String helpText = res.getString(R.string.help_multiplication);
				lblHelp.setText(Html.fromHtml(helpText));
				break;
			}
			case DIVISION:
			{	
				String helpText = res.getString(R.string.help_division);
				lblHelp.setText(Html.fromHtml(helpText, imgGetter, null));
				break;
			}	
		}
		lblHelp.invalidate();
	}
	
	private ProblemType getProblemType()
	{
		ProblemType problemType = null;
		
		Bundle extras = getIntent().getExtras();
		if (extras == null)
			throw new IllegalStateException("No extras has been delivered to the HelpActivity.");

		String value = extras.getString("ProblemType");
		if (value == null)
			throw new IllegalStateException("Value for 'ProblemType' is missing in the extras.");
		
		problemType = ProblemType.valueOf(value);
		return problemType;
	}
	
	// --------------------------------------------------------------------------

	private ImageGetter imgGetter = new ImageGetter() 
	{
		public Drawable getDrawable(String source) 
		{
			Drawable drawable = null;
			if (source.equals("division_example_1.png"))
			{
				drawable = getResources().getDrawable(R.drawable.division_example_1);
			} 
			else if (source.equals("division_example_2.png"))
			{
				drawable = getResources().getDrawable(R.drawable.division_example_2);
			}
			
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
					.getIntrinsicHeight());

			return drawable;
		}
	};
	

	

}
