package de.fh_erfurt.cst.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;
import de.fh_erfurt.cst.R;
import de.fh_erfurt.cst.model.VarStore.ProblemType;
import de.fh_erfurt.cst.util.Utils;


/**
 * A class which encapsules actions with the data connected
 * with the creation of statistics for the training
 *
 */
public class StatisticModel 
{
	
	private DatabaseHelper db;
	private ProblemType problemType;
	private Context context;
	
	// --------------------------------------------------------------------------
	
	/**
	 * Creates an instance of the statistic model for the specified problem type.
	 * 
	 * @param context the current context
	 * @param problemType the problem type this instance shall handle
	 */
	public StatisticModel(Context context, ProblemType problemType)
	{
		this.context = context;
		this.problemType = problemType;
		
		db = new DatabaseHelper(context);
	}

	// --------------------------------------------------------------------------
	
	/**
	 * Report the data of an user answer which is relevant for the statistic
	 * 
	 * @param result Was the answer right or wrong?
	 * @param level What was the level of the calculated problem?
	 */
	public void addResult(boolean result, int level)
	{
		ContentValues values = new ContentValues(4);
		
		long time = new Date().getTime() / 1000;
		int resultCode = 0;
		if (result == true)
			resultCode = 1;
		
		values.put(DatabaseHelper.TIME, Long.toString(time));
		values.put(DatabaseHelper.RESULT, Integer.toString(resultCode));
		values.put(DatabaseHelper.PROBLEM_TYPE, problemType.toString());
		values.put(DatabaseHelper.LEVEL, Integer.toString(level));

		db.getWritableDatabase().insert("statistic", null, values);
	}
	
	
	/**
	 * Reads out the database and returns the data relevant for the
	 * creation of the statistic table.
	 * 
	 * @return a list of string arrays with date (0), number of problems 
	 * calculated this day (1), the average level (2) and the 
	 * percentage of right answers (3)
	 */
	public ArrayList<String[]> getStatistic()
	{
		ArrayList<String[]> result = new ArrayList<String[]>();
		
		Cursor data = getStatisticData();
		
		while (data.moveToNext())
		{
			String date = data.getString(0);
			String problemCount = data.getString(1);
			String avgLevel = Utils.toString(Double.parseDouble(data.getString(2)));
			String precentageRight = data.getString(3) + " %";

			result.add(new String[]{date, problemCount, avgLevel, precentageRight});
		}
		data.close();

		return result; 
	}
	
	/**
	 * Exports the statistic into an CSV file on the external storage.
	 * 
	 * @return true if operation was successful, otherwise false
	 */
	public boolean exportCSV()
	{
		ArrayList<String[]> statistic = getStatistic();
		if (statistic.size() == 0)
			return false;
		try {
			File sdCard = Environment.getExternalStorageDirectory();
			File outputFile = new File(sdCard, getCSVFilename());
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			bw.write(getCSVHeader() + "\n");
			for(String[] entry : statistic)
			{
				bw.write(
						entry[0] + ";" + 
						entry[1] + ";" + 
						entry[2] + ";" +
						entry[3] + "\n" );
			}
			bw.close();
		} catch (IOException e) {
			Log.e("ERROR", e.getMessage(), e);
			return false;
		};
		return true;
	}
	
	/**
	 * Deletes the entries in the database for the current problem type.
	 */
	public void reset()
	{
		String[] args = new String[]{problemType.toString()};
		db.getWritableDatabase().delete("statistic", "problemType=?", args);
	}
	
	// --------------------------------------------------------------------------

	private Cursor getStatisticData()
	{
		String[] columns = {
				// DD.MM.YY
				"strftime('%d.%m.%Y', time , 'unixepoch')",
				// Number of results for that day
				"count(*)",
				// Average level
				"round(avg(level),1)",
				// pecentage of right answers
				"round((100.0 * sum(result)) / count(*))"
		};
		
		String[] selectionArgs = {
				problemType.toString()
		};
		
		Cursor result = db.getReadableDatabase().query (
				"statistic", 				// table
				columns, 					// columns
				"problemType=?", 			// selection
				selectionArgs, 				// selection args
				"date(time, 'unixepoch')",	// group by
				null,						// having
				"time DESC"					// order by
		);
		
		return result;
	}

	private String getCSVFilename()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(problemType.toString());
		sb.append("_");
		sb.append(Utils.getTimestamp());
		sb.append(".CSV");
		return sb.toString();
	}
	
	private String getCSVHeader()
	{
		Resources res = context.getResources();
		
		StringBuilder sb = new StringBuilder();		
		sb.append(res.getString(R.string.stats_date) + ";");
		sb.append(res.getString(R.string.stats_tasks) + ";");
		sb.append(res.getString(R.string.stats_level) + ";");
		sb.append(res.getString(R.string.stats_right));
		
		return sb.toString();
	}
	
	
	
}
