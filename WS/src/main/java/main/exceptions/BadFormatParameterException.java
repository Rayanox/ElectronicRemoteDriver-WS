package main.exceptions;

public class BadFormatParameterException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 427612479128959L;

	public BadFormatParameterException(String message) {
		super(message);
	}
	
	public BadFormatParameterException(Exception exception) {
		super(exception);
	}
	
	public BadFormatParameterException(String message, Exception exception) {
		super(exception);
	}
}
