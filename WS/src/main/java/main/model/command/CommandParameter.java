package main.model.command;

import java.time.LocalTime;

public class CommandParameter {

	private Double 					commandDoubleParameter; // The command parameter, like the time period to maintain a button pressed, or any other parameter relative to the protocol documentation.
	private LocalTime				commandTimeParameter;
	private CommandParameterType	parameterType;
	
	private CommandParameter(Double commanDoubleParameter, LocalTime commandTimeParameter, CommandParameterType parameterType) {
		this.commandDoubleParameter = commanDoubleParameter;
		this.commandTimeParameter = commandTimeParameter;
		this.parameterType = parameterType;
	}
	
	public CommandParameter(Double commanDoubleParameter) {
		this(commanDoubleParameter, null, CommandParameterType.TIME_PRESSED);
	}
	
	public CommandParameter(LocalTime commandTimeParameter) {
		this(null, commandTimeParameter, CommandParameterType.HOUR_TIME);
	}
	
	
	public String getParameterAsString() {
		if(parameterType == null)
			throw new RuntimeException("The parameter as not been well instanciated");
		
		switch (parameterType) {
			case HOUR_TIME:
				return commandTimeParameter.toString();
			case TIME_PRESSED:
				return commandDoubleParameter.toString();
			default:
				throw new RuntimeException("The parameter as not been well instanciated");
		}
	}
	
	public CommandParameterType getParameterType() {
		return parameterType;
	}
	
	public Double getCommandDoubleParameter() {
		return commandDoubleParameter;
	}
	public LocalTime getCommandTimeParameter() {
		return commandTimeParameter;
	}
	
}
