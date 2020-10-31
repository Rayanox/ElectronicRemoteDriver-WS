package main.storage.types;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import main.exceptions.BadFormatPropertyException;

public class IntervalleTime implements IStorageType {

	private final LocalTime timeFrom;
	private final LocalTime timeTo;
	
	public IntervalleTime(LocalTime timeFrom, LocalTime timeTo) {
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
	}
	
	public static IntervalleTime Of(LocalTime timeFrom, LocalTime timeTo) {
		return new IntervalleTime(timeFrom, timeTo);
	}
	
	public boolean containsTime(LocalTime time) {
		long shiftMinutesNormalization = LocalTime.of(0, 0).until(timeFrom, ChronoUnit.MINUTES);
		
		LocalTime fromNormalized = this.timeFrom.minus(shiftMinutesNormalization, ChronoUnit.MINUTES);
		LocalTime toNormalized = this.timeTo.minus(shiftMinutesNormalization, ChronoUnit.MINUTES);
		LocalTime timeNormalized = time.minus(shiftMinutesNormalization, ChronoUnit.MINUTES);
		
		return fromNormalized.until(timeNormalized, ChronoUnit.MINUTES) 
				<= 
			   fromNormalized.until(toNormalized, ChronoUnit.MINUTES);
	}
	
	
	public String 	convertToString() {
		String hourFromPattern = "hourFrom", hourToPattern = "hourTo", minuteFromPattern = "hourTo", minuteToPattern = "minuteTo";
		StringBuilder builder = new StringBuilder();
		builder.append("[").append(hourFromPattern).append(":").append(minuteFromPattern).append("-").append(hourToPattern).append(":").append(minuteToPattern).append("]");
		String pattern = builder.toString();
		
		return pattern.replace(hourFromPattern, String.valueOf(this.timeFrom.getHour()))
					  .replace(minuteFromPattern, String.valueOf(this.timeFrom.getMinute()))
					  .replace(hourToPattern, String.valueOf(this.timeTo.getHour()))
					  .replace(minuteToPattern, String.valueOf(this.timeTo.getMinute()));
	}
	
	public static IntervalleTime buildFromString(String textValue) throws Exception {
		Pattern pattern = Pattern.compile("(\\d{2})(:|h)(\\d{2})-(\\d{2})(:|h)(\\d{2})");
		Matcher regex = pattern.matcher(textValue);
		
		if(!regex.find())
			throw new BadFormatPropertyException(String.join("", "The intervalle time is bad formatted. Value is \"", textValue, "\" and pattern is \"", pattern.pattern(), "\""));
		
		int hourFrom = Integer.parseInt(regex.group(1));
		int minuteFrom = Integer.parseInt(regex.group(3));
		
		int hourTo = Integer.parseInt(regex.group(4));
		int minuteTo = Integer.parseInt(regex.group(6));
		
		return new IntervalleTime(
				LocalTime.of(hourFrom, minuteFrom), 
				LocalTime.of(hourTo, minuteTo)
				);
	}
	
	public LocalTime getTimeFrom() {
		return timeFrom;
	}
	public LocalTime getTimeTo() {
		return timeTo;
	}
	
	@Override
	public Object getValue() { // Should never be called for this class
		return this;
	}
}
