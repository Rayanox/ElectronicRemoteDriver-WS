package main.storage.types;

import java.time.LocalTime;

import main.exceptions.NotImplementedException;

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
		return (time.isAfter(this.timeFrom) && time.isBefore(this.timeTo))
				|| time.equals(timeFrom)
				|| time.equals(timeTo);
	}
	
	
	public String 	convertToString() {
		return null;
	}
	
	public static IntervalleTime buildFromString(String textValue) throws NotImplementedException {
		throw new NotImplementedException("Need to implement it now !!");
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
