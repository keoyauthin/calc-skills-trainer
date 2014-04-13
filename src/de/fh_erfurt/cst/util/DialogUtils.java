package de.fh_erfurt.cst.util;

import android.app.AlertDialog;
import android.content.Context;


/**
 * A convenience class to facilitate the creation of dialogs.
 *
 */
public class DialogUtils 
{

	/**
	 * Show a simple dialog with a message and OK button
	 * 
	 * @param context the context to be used for the dialog
	 * @param message the message to be displayed within the dialog
	 */
	public static void showMessageDialog(Context context, String message)
	{
		showMessageDialog(context, null, message);
	}
	
	/**
	 * Show a dialog with a title bar, message and OK button
	 * 
	 * @param context the context to be used for the dialog
	 * @param title the title text for the dialog
	 * @param message the title text for the dialog
	 */
	public static void showMessageDialog(Context context, String title, String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		if (title != null)
			builder.setTitle(title);
		builder.setPositiveButton("OK", null); 
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	/**
	 * Prepares a raw dialog which can be extended with buttons, listeners etc.
	 * 
	 * @param context the context to be used for the dialog
	 * @param title the title text for the dialog
	 * @param message the title text for the dialog
	 * @return a dialog created with {@link AlertDialog.Builder(Context)}}
	 */
	public static AlertDialog createAlertDialog(Context context, String title, String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
	    builder.setTitle(title);
		return builder.create();
	}
	
	
}
