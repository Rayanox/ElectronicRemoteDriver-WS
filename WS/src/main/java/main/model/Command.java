package main.model;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tomcat.util.http.fileupload.util.Streams;

import main.exceptions.BadFormatPropertyException;
import main.model.command.CommandParameter;
import main.model.command.SpecialCommandDevice;

public class Command {

	private String command;
	
	// Parameter
	private CommandParameter commandParameter;
	
	
	private Command(String confCommand) {
		this.command = confCommand;
	}
	
	public static Command Create(String action) {
		return new Command(action);
	}
	
	
	/*
	 * Set parameter
	 */
	public Command setParameter(Double actionPressTimeSeconds) {
		this.commandParameter = new CommandParameter(actionPressTimeSeconds);
		return this;
	}
	public Command setParameter(LocalTime hourTime) {
		this.commandParameter = new CommandParameter(hourTime);
		return this;
	}
	
	
	/*
	 * End set parameter
	 */
	
	public boolean isParameterSet(){
		return this.commandParameter != null;
	}
	
	public static Command buildFromConfigString(String text) throws BadFormatPropertyException {
		Pattern pattern = Pattern.compile("(\\w+)(\\((.+)\\))?");
		Matcher matcher = pattern.matcher(text);
		
		if(!matcher.find())
			throw new BadFormatPropertyException("The property in command config file is missing or bad formatted. Text value = " + text);
		
		String commandName = matcher.group(1);
		String parameterValue = matcher.groupCount() > 2 ? matcher.group(3) : null;
		
		Command commandResult = Command.Create(commandName);
		
		if(parameterValue == null)
			return commandResult;
		
		// Process Parameter
		
//		processSpecialParameter(parameterValue, commandResult);
//		if(commandResult.getCommandParameter() != null)
//			return commandResult;
		
		Double parameterDouble;
		LocalTime parameterTime;
		if((parameterDouble = tryExtractDoubleFromString(parameterValue)) != null) {
			return SpecialCommandDevice.processParameterForDeviceCommand(commandResult.setParameter(parameterDouble));
		} else if((parameterTime = tryExtractTimeFromString(parameterValue)) != null) {
			return SpecialCommandDevice.processParameterForDeviceCommand(commandResult.setParameter(parameterTime));
		}
		
		throw new BadFormatPropertyException(String.format("Cannot extract property text '%s'", parameterValue));
	}
	
//	/**
//	 * *******************   To use later if needed for special parameters  *********************
//	 * @param parameterValue																	*
//	 * @param commandName																		*
//	 */																							*
//	private static void processSpecialParameter(String parameterValue, Command commandName) {
//		// Use pattern matching with Java 10 ?
//		if(commandName.getCommandValue().equals("SleepTo")) {
//			Pattern pattern = Pattern.compile("");
//			Matcher matcher = pattern.matcher(parameterValue);
//			
//		}
//	}

	private static Double tryExtractDoubleFromString(String textParameterCommand) {
		Pattern pattern = Pattern.compile("^\\d+((\\.|,)\\d+)?$");
		Matcher matcher = pattern.matcher(textParameterCommand);
		
		if(!matcher.find())
			return null;
		
		return Double.parseDouble(textParameterCommand.replace(',', '.'));
	}
	private static LocalTime tryExtractTimeFromString(String textParameterCommand) {
		Pattern pattern = Pattern.compile("^(\\d+)(h|:|H)(\\d+)$");
		Matcher matcher = pattern.matcher(textParameterCommand);
		
		if(!matcher.find())
			return null;
		
		int hour = Integer.parseInt(matcher.group(1));
		int minute = Integer.parseInt(matcher.group(3));
		
		return LocalTime.of(hour, minute);
	}
	
	/*
	 *  Getters
	 */
	
	public String getCommandValue() {
		return command;
	}
	
	public CommandParameter getCommandParameter() {
		return commandParameter;
	}
	
	public String getCommandParameterAsString() {
		return this.commandParameter.getParameterAsString();
	}
}
