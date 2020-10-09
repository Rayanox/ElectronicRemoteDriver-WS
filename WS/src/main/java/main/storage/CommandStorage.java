package main.storage;

import java.io.IOException;
import main.exceptions.BadFormatPropertyException;
import main.model.CommandSequence;

public class CommandStorage implements ICachable{

	@Override
	public void refreshCache() throws BadFormatPropertyException, IOException {
		
	}
	
	public CommandSequence getCommands(String programId) {
		return null;
	}
	
}
