package main.model;

import main.exceptions.NotImplementedException;

public class Command {

	private String command;
	
	// Parameter
	private Double commandParameter; // The command parameter, like the time period to maintain a button pressed, or any other parameter relative to the protocol documentation.
	
	private Command(String confCommand, Double actionPressTimeSeconds) {
		this.command = confCommand;
		this.commandParameter = actionPressTimeSeconds;
	}
	
	
	public static Command Create(String action, Integer actionPressTimeSeconds) {
		return new Command(action, new Double(actionPressTimeSeconds));
	}
	public static Command Create(String action, Double parameter) {
		return new Command(action, parameter);
	}
	public static Command Create(String action) {
		return new Command(action, null);
	}
	
	
	
	public boolean isParameterSet() throws NotImplementedException {
		return this.commandParameter != null;
	}
	
	
	/*
	 *  Getters
	 */
	
	public String getCommand() {
		return command;
	}
	
	public Double getParameter() {
		return this.commandParameter;
	}
}
