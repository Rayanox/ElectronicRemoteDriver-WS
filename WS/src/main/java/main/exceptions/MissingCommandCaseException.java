package main.exceptions;

import java.time.LocalTime;

public class MissingCommandCaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MissingCommandCaseException(LocalTime timeCase, String programId) {
		super(new StringBuilder(
				"A case is not cover in the command file config for program Id = ")
				.append(programId)
				.append(". Case is following time = ").append(timeCase.toString())
				.toString());
	}
	
}
