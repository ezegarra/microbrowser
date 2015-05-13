package microbrowser.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateLib {
	final static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
	
	/**
	 * Remove time information from date
	 */
	public static long removeTime(long date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(date));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	/**
	 * converts the given date in long format to int by reducing accuracy, such
	 * as removing milliseconds.
	 * 
	 * @param date the date to convert
	 * @return the smaller date
	 */
	public static int getDateAsInt(long date) {
		return (int)(date / 1000);
	}
	
	/**
	 * Formats a given date in int format to default date format
	 * @param date in int
	 * @return formatted string date
	 */
	public static String formatDate(int date) {
		
		return dateFormat.format(new Date(((long)date)*1000L));
	}
	
	/**
	 * Formats a given date in int format to default date format
	 * @param date in int
	 * @return formatted string date
	 */
	public static String formatDate(long date) {
		
		return dateFormat.format(new Date(date));
	}
	
	/**
	 * Formats a given date in Calendar format to default date format
	 * @param date as Calendar
	 */
	public static String formatDate(Calendar date) {
		return dateFormat.format(date.getTime());
	}
	
	/**
	 * Converts an int date to its long representation
	 * 
	 * @param date
	 * @return
	 */
	public static long getDateAsLong(int date) {
		return ((long)date)*1000L;
	}
}
