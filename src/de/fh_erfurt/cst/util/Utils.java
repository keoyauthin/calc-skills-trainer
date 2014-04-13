package de.fh_erfurt.cst.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

/**
 * A conveniece class which provides a number of static methods
 * which can be used accross the application.
 * 
 */
public class Utils 
{
	/** The one instance of {@link DecimalFormat} to be used across the application  */
	private static DecimalFormat df;
	
	// --------------------------------------------------------------------------
	
	/**
	 * Returns the configured instance of {@link DecimalFormat} to be used across the application. 
	 */
	public static DecimalFormat getDecimalFormatInstance()
	{
		if (df == null)
		{
			NumberFormat nf; 
			String lang = Locale.getDefault().getDisplayLanguage();
			if (lang.equals("Deutsch"))
				nf = NumberFormat.getNumberInstance(Locale.GERMAN);
			else
				nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
			
			df = (DecimalFormat) nf;
			df.setMaximumFractionDigits(99);
		}
		
		return df;
	}
	
	/**
	 * Formats a floating point number into a localized string
	 * 
	 * @param number the number to be formatted
	 * @return the number as localized string representation
	 */
	public static String toString(double number)
	{
		DecimalFormat df = getDecimalFormatInstance();
		return df.format(number);
	}
	
	/**
	 * Resets the the {@link DecimalFormat} instance for the application.
	 * 
	 * <p>This is necessay if the user changes the language in the
	 * system settings and then return into the application.
	 * 
	 * @see #getDecimalFormatInstance()
	 * 
	 */
	public static void invalidateDecimalFormat()
	{
		df = null;
	}
	
	/**
	 * Rounds a floating point number according to the desired number
	 * of decimal places
	 * 
	 * @param number the number to be rounded
	 * @param decimalPlaces the number of descimal places in the result
	 * @return the rounded number
	 */
	public static double round(double number, int decimalPlaces)
	{
		int integerPart = (int) number;
		double decimalPlacesFraction = 0;
	
		double expansionFactor = Math.pow(10, decimalPlaces);
		decimalPlacesFraction  = number % 1;
		decimalPlacesFraction *= expansionFactor;
		decimalPlacesFraction  = Math.round(decimalPlacesFraction);
		//decimalPlacesFraction /= Math.round(expansionFactor);
		//return integerPart + decimalPlacesFraction;
		
		StringBuilder sb = new StringBuilder();
		sb.append(Integer.toString(integerPart));
		sb.append(".");
		sb.append(Integer.toString((int)decimalPlacesFraction));
		return Double.parseDouble(sb.toString());
	}
	
	/**
	 * Renerates a pseudo random number with digits 1-9
	 * 
	 * @param numberOfDigits number of digits of the number to be generated, at least 1 is required
	 */
	public static Integer getRandomNumber(int numberOfDigits)
	{
		if (numberOfDigits <= 0)
			throw new IllegalArgumentException("numberOfDigits is '" + numberOfDigits + "' and shall be at least 1.");
		
		Random rand = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<numberOfDigits; i++)
		{
			int randInt = rand.nextInt(9) + 1;
			sb.append(randInt);
		}
		return new Integer(sb.toString());
	}
	
	/**
	 * Returns a timestamp with the format YYYY-MM-DD_HH.MM
	 */
	public static String getTimestamp()
	{
		// TODO refactoring - usage of 'java.text.DateFormat' would be more beautiful
		GregorianCalendar cal = new GregorianCalendar();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(year);
		sb.append("-");
		
		if (month < 10)
			sb.append("0");
		sb.append(month);
		sb.append("-");
		
		if (day < 10)
			sb.append("0");
		sb.append(day);
		sb.append("_");
		
		if (hour < 10)
			sb.append("0");
		sb.append(hour);
		sb.append(".");
		
		if (min < 10)
			sb.append("0");
		sb.append(min);
		
		return sb.toString();		
	}
	
	// --------------------------------------------------------------------------
	
	

	
}
