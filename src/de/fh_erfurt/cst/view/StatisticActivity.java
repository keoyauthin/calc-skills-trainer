package de.fh_erfurt.cst.view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.fh_erfurt.cst.R;
import de.fh_erfurt.cst.model.StatisticModel;
import de.fh_erfurt.cst.model.VarStore.ProblemType;
import de.fh_erfurt.cst.util.DialogUtils;
import de.fh_erfurt.cst.util.Utils;


public class StatisticActivity extends Activity
{
	private StatisticModel model;

	// --------------------------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		model = new StatisticModel(this, getProblemType());
		setContentView(R.layout.activity_statistic);
		setTitle(getTitleText());
		
		fillStatisticTable();
	}
	
	@Override
	protected void onResume()
	{
		Utils.invalidateDecimalFormat();
		super.onResume();
	}
	
	// --------------------------------------------------------------------------
	
	public void onClick(View view)
	{
		switch(view.getId())
		{
			case R.id.statistic_button_export_csv:
			{
				exportToCSV();
				break;
			}
			case R.id.statistic_button_reset:
			{
				requestResetConfirmation();
				break;
			}
		}
	}
	
	// --------------------------------------------------------------------------
	
	private ProblemType getProblemType()
	{
		ProblemType problemType = null;
		
		Bundle extras = getIntent().getExtras();
		if (extras == null)
			throw new IllegalStateException("No extras has been delivered to the Statistic View.");

		String value = extras.getString("ProblemType");
		if (value == null)
			throw new IllegalStateException("Value for 'ProblemType' is missing in the extras.");
		
		problemType = ProblemType.valueOf(value);
		return problemType;
	}
	
	private void exportToCSV()
	{
		Resources res = getResources();
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			boolean result = model.exportCSV();
			if (result == true)
				DialogUtils.showMessageDialog(this, res.getString(R.string.dialog_success_export));
			else
				DialogUtils.showMessageDialog(this, res.getString(R.string.dialog_error_export));
		}
		else
		{
			DialogUtils.showMessageDialog(this, res.getString(R.string.dialog_error_storage));
		}
		
	}
	
	private void requestResetConfirmation()
	{
		OnClickListener listener = new OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int id) 
			{
				if (id == DialogInterface.BUTTON_POSITIVE)
				{
					resetStatistic();	
				}
			}
		};
		
		Resources res = getResources();
		AlertDialog alert = DialogUtils.createAlertDialog(this, res.getString(R.string.dialog_confirm), res.getString(R.string.dialog_confirm_reset_statistic));
		alert.setButton(DialogInterface.BUTTON_POSITIVE, res.getString(R.string.dialog_yes), listener); 
		alert.setButton(DialogInterface.BUTTON_NEGATIVE, res.getString(R.string.dialog_no), listener);
		alert.show();
	}
	
	private void resetStatistic()
	{
		model.reset();
		
		TableLayout tbl = (TableLayout)findViewById(R.id.statistic_table);
		int childCount = tbl.getChildCount();
		for (int i=childCount-1; i > 0; i--)
		{
			tbl.removeViewAt(i);
		}
	}
	
	private String getTitleText()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getString(R.string.menu_statistic));
		sb.append(" ");
		switch(getProblemType())
		{
			case ADDITION:
				sb.append( getString(R.string.addition) );
				break;
			case SUBTRACTION:
				sb.append( getString(R.string.subtraction) );
				break;
			case MULTIPLICATION:
				sb.append( getString(R.string.multiplication) );
				break;
			case DIVISION:
				sb.append( getString(R.string.division) );
				break;				
		}
		return sb.toString();
	}
	
	private void fillStatisticTable()
	{
		TableLayout tbl = (TableLayout)findViewById(R.id.statistic_table);
		StatisticModel model = new StatisticModel(this, getProblemType() ); 
		ArrayList<String[]> statisticEntries = model.getStatistic();
		for (int i=0; i < statisticEntries.size(); i++) 
		{
			TableRow newRow = new TableRow(this);
			for (int j=0; j < statisticEntries.get(i).length; j++) {
				TextView txt = new TextView(this);
				txt.setText(statisticEntries.get(i)[j]);
				txt.setPadding(7, 7, 7, 7);
				newRow.addView(txt);
			}
			tbl.addView(newRow);
		}
	}

}
