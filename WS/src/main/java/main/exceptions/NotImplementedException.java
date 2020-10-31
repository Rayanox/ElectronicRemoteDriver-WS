package main.exceptions;

public class NotImplementedException extends RuntimeException {
	
	private static final long serialVersionUID = 7485801012756521247L;

	public NotImplementedException(String message) {
		super(message);
	}
	
	public NotImplementedException() {
		super();
	}
}
