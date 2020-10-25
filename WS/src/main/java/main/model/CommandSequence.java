package main.model;

import java.util.ArrayList;
import java.util.HashMap;

import main.exceptions.NotImplementedException;
import main.storage.types.IStorageType;

public class CommandSequence implements IStorageType {
	
	private ArrayList<Command> btnSequence;
	private HashMap<String, String> commandMapping;
	
	public CommandSequence() {
		this.btnSequence = new ArrayList<Command>();
		this.commandMapping = new HashMap<>();
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
	public void addToCommandsMapping(String commandFrom, String commandTo) {
		this.commandMapping.put(commandFrom, commandTo);
	}
	
	/*
	 * End Mapping
	 */
	
	
	public ArrayList<Command> getBtnSequence() {
		return btnSequence;
	}
	
	public String toStringRequest() throws NotImplementedException {
		//TODO A coder, et on utilisera le mapping
		throw new NotImplementedException();
	}

	/*
	 * For Conf String encapsulation
	 * 
	 */
	
	@Override
	public String convertToString() {
		return null;
	}

	public static CommandSequence buildFromString(String textValue) throws NotImplementedException {
		throw new NotImplementedException("Need to implement this method !!");
	}
	
	@Override
	public Object getValue() {
		return btnSequence;
	}
	
}
