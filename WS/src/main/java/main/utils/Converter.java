package main.utils;

import java.time.DateTimeException;
import java.time.LocalTime;

import main.exceptions.BadFormatPropertyException;

public class Converter {
	
	public static String convertStacktraceToString(StackTraceElement [] stack) {
		String result = "";
		for (StackTraceElement stackTraceElement : stack) {
			result += stackTraceElement.toString() + "       ---------      " ;
		}
		return result;
	}
	
	/**
	 * Convert a date according to the french date format
	 * 
	 * @param dateString
	 * @return
	 * @throws Exception 
	 */
	public static LocalTime convertStringToTime(String dateString) throws Exception {
		String errorMessage = "The string date does not have the correct synthax (hh:mm:ss) --> (dateString = " + dateString + ")";
		try {
			LocalTime time = LocalTime.parse(dateString); // TODO Tester qu'une exception se throw bien si le format est mauvais, ou l'heure incorrecte.
			return time;
		}catch(DateTimeException e) {
			throw new Exception(errorMessage);
		}
	}
	
	public static String convertTimeToString(LocalTime time) {
		return String.format("%d:%d:%d", time.getHour(),time.getMinute(), time.getSecond());
	}
	
	public static int convertStringToVolum(String param) throws Exception {
		int volum = Integer.parseInt(param);
		if(volum > 100)
			throw new Exception(String.format("The volum level cannot be greater than 100 (value = %s)", volum));
		if(volum < 0)
			throw new Exception(String.format("The volum level cannot be less than 0 (value = %s)", volum));
		return volum;
	}

	public static double convertSecondsToMinutes(long seconds) {
		return ((double) seconds) / 60;
	}
}
