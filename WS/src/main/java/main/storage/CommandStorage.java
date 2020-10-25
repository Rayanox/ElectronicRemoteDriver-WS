package main.storage;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import main.exceptions.BadFormatPropertyException;
import main.exceptions.MissingCommandCaseException;
import main.exceptions.NotImplementedException;
import main.model.CommandSequence;
import main.storage.types.IntervalleTime;

@Repository
public class CommandStorage extends AbstractMultipleCachableConfStorage<IntervalleTime, CommandSequence>{
	
	private static final String CACHE_FOLDER_LOCATION = "commands";
	
	public CommandSequence getCommandsForTime(String programId, LocalTime time) throws BadFormatPropertyException, IOException, MissingCommandCaseException, NotImplementedException {
		
		File file = getFileStorage(programId).orElse(null);
		if(file != null)
			
		CommandMapping
		
		
		HashMap<IntervalleTime, CommandSequence> commands = getAllDataS(programId);
		Optional<CommandSequence> commandSequence = commands.keySet()
				.stream()
				.filter(intervalle -> intervalle.containsTime(time))
				.map(intervalle -> commands.get(intervalle))
				.findFirst();
		
		return commandSequence.orElseThrow(() -> new MissingCommandCaseException(time, programId));
	}

	@Override
	protected IntervalleTime convertKeyFromString(String key) throws NotImplementedException {
		return IntervalleTime.buildFromString(key);
	}

	@Override
	protected CommandSequence convertValueFromString(String value) throws NotImplementedException {
		return CommandSequence.buildFromString(value);
	}

	
	@Override
	protected String getFolderStorageLocation() {
		return CACHE_FOLDER_LOCATION;
	}
	
	

}
