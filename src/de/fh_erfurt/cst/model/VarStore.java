package de.fh_erfurt.cst.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;


/**
 * A convience class to provide access on properties and other
 * information required arross the application.
 */
public class VarStore 
{	
	/**
	 * Convience method for access on a boolean value in the {@link SharedPreferences}}
	 * 
	 * @param context the current context
	 * @param key the key of the examined property
	 * @param defaultValue the default value for this property 
	 * @return the value for this property
	 */
	public static boolean getBooleanProperty(Context context, String key, boolean defaultValue)
	{
		SharedPreferences sharedPref = 
				PreferenceManager.getDefaultSharedPreferences(context);
		Boolean result = sharedPref.getBoolean(key, defaultValue);
		return result;
	}

	/**
	 * Convience method for access on a string value in the {@link SharedPreferences}
	 * 
	 * @param context the current context
	 * @param key the key of the examined property
	 * @param defaultValue the default value for this property 
	 * @return the value for this property
	 */
	public static String getStringProperty(Context context, String key, String defaultValue)
	{
		SharedPreferences sharedPref = 
				PreferenceManager.getDefaultSharedPreferences(context);
		String result = sharedPref.getString(key, defaultValue);
		return result;
	}
	
	/**
	 * Convience method to set a string key-value pair in the {@link SharedPreferences}
	 * 
	 * @param context the current context
	 * @param key the key of the examined property
	 * @param value the value for the key
	 */
	public static void setStringProperty(Context context, String key, String value)
	{
		SharedPreferences sharedPref = 
				PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	// --------------------------------------------------------------------------
	
	/**
	 * The different possible types for the training tasks.
	 */
	public enum ProblemType
	{
		ADDITION,
		SUBTRACTION,
		MULTIPLICATION,
		DIVISION
	}



}
