package main.model.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import main.exceptions.BadFormatPropertyException;
import main.exceptions.NotImplementedException;
import main.model.Command;
import main.storage.types.IStorageType;

public class CommandSequence implements IStorageType {
	
	private static final char REQUEST_COMMAND_DELIMITER = '-';
	private static final char REQUEST_PARAMETER_DELIMITER = ';';
	private ArrayList<Command> btnSequence;
	private HashMap<String, String> commandMapping;
	
	public CommandSequence() {
		this.btnSequence = new ArrayList<Command>();
		this.commandMapping = new HashMap<>();
		
		SpecialCommandDevice.initCommandMappingWithSpecialCommands(this.commandMapping);
	}
	
	public void addBtnCommand(Command btn) {
		this.btnSequence.add(btn);
	}
	
	public void addBtnCommand(String action) {
		this.btnSequence.add(Command.Create(action));
	}
	
	/*
	 * Mapping
	 */
	public void setCommandsMapping(HashMap<String, String> mapping) {
		commandMapping.putAll(mapping);
	}
		
	/*
	 * End Mapping
	 */
	
	
	public ArrayList<Command> getBtnSequence() {
		return btnSequence;
	}
	
	public String toStringRequest() throws NotImplementedException {
		String requestOut = this.getBtnSequence()
				.stream()
				.map(command -> mapSingleCommandToString(command))
				.collect(Collectors.reducing((command1, command2) -> joinCommands(command1, command2)))
				.orElse("");
		
		return String.format("{%s}", requestOut);
	}
	
	private String joinCommands(String command1, String command2) {
		return String.join(String.valueOf(REQUEST_COMMAND_DELIMITER), command1, command2);
	}
	
	private String mapSingleCommandToString(Command command) {
		String commandValueMapped = commandMapping.getOrDefault(command.getCommandValue(), command.getCommandValue());
		
		return command.isParameterSet() ?
				String.join(String.valueOf(REQUEST_PARAMETER_DELIMITER), commandValueMapped, command.getCommandParameterAsString())
				: String.valueOf(commandValueMapped);
	}

	/*
	 * For Conf String encapsulation
	 * 
	 */
	
	@Override
	public String convertToString() {
		return null;
	}

	public static CommandSequence buildFromString(String textValue) throws Exception {
		try {
			CommandSequence commandSequence = new CommandSequence();
			Arrays.stream(textValue.replace("{", "").replace("}", "").split("-"))
				  .map(commandText -> Command.buildFromConfigString(commandText))
				  .forEachOrdered(command -> commandSequence.addBtnCommand(command));
			return commandSequence;
		}catch (Exception e) {
			throw new BadFormatPropertyException(String.join("",  "The command cannot be parsed from text in the command config file. Text value = ", textValue), e);
		}
	}
	
	@Override
	public Object getValue() {
		return btnSequence;
	}
	
}
