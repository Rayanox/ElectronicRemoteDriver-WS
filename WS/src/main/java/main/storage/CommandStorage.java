package main.storage;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.springframework.stereotype.Repository;
import main.exceptions.MissingCommandCaseException;
import main.model.command.CommandSequence;
import main.storage.types.IntervalleTime;

@Repository
public class CommandStorage extends AbstractMultipleCachableConfStorage<IntervalleTime, CommandSequence>{
	
	public static String CACHE_FOLDER_LOCATION = "commands";
	
	private HashMap<String, HashMap<String, String>> commandMappingByProgramId; // The mapping between commands in config file and command in the out request
	
	public CommandStorage() {
		this.commandMappingByProgramId = new HashMap<>();
	}
	
	public CommandSequence getCommandsForTime(String programId, LocalTime time) throws Exception {
		LinkedHashMap<IntervalleTime, CommandSequence> commands = getAllDataS(programId);
		
		Integer indexDefaultCaseLimit = getDefaultCaseIndex(commands);
		
		Optional<CommandSequence> commandSequence = commands.keySet()
				.stream()
				.limit(indexDefaultCaseLimit != null ? indexDefaultCaseLimit : Long.MAX_VALUE)
				.filter(intervalle -> intervalle.containsTime(time))
				.map(intervalle -> commands.get(intervalle))
				.findFirst();
		
		if(indexDefaultCaseLimit != null)
			commandSequence = Optional.ofNullable(commandSequence.orElse(commands.get(super.KEY_DEFAULT_CASE))); // Get the default case if needed 
		
		return commandSequence.orElseThrow(() -> new MissingCommandCaseException(time, programId));
	}

	private Integer getDefaultCaseIndex(LinkedHashMap<IntervalleTime, CommandSequence> commands) {
		Integer i = 0;
		for (IntervalleTime key : commands.keySet()) {
			if (key == super.KEY_DEFAULT_CASE)
				return i;
			i++;
		}
		return null;
	}

	@Override
	protected IntervalleTime convertKeyFromString(String key) throws Exception {
		return IntervalleTime.buildFromString(key);
	}

	@Override
	protected CommandSequence convertValueFromString(String value, String programId) throws Exception {
		CommandSequence sequence = CommandSequence.buildFromString(value);
		sequence.setCommandsMapping(commandMappingByProgramId.get(programId));
		return sequence;
	}

	
	@Override
	protected String getFolderStorageLocation() {
		return CACHE_FOLDER_LOCATION;
	}

	@Override
	protected boolean isFirstLineSpecialProcessed() {
		return true;
	}

	@Override
	protected void processFirstLine(String line, String programId) {
		commandMappingByProgramId.put(programId, new HashMap<>());
		
		Pattern mappingPattern = Pattern.compile("\\{((\\d)+=(\\w)+\\s*;?\\s*)+\\}");
		Matcher matcher = mappingPattern.matcher(line);
		
		if(!matcher.find())
			return;
		
		Stream<String> groupsMapping = Arrays.stream(matcher.group()
			   .replace("{", "")
			   .replace("}", "")
			   .split(";"));
		
		groupsMapping.map(String::trim)
					 .map(equality -> equality.split("="))
					 .forEach((equality) -> {
						 commandMappingByProgramId.get(programId).put(equality[1], equality[0]);  
					 });
	}

	@Override
	protected boolean manageDefaultAloneValue() {
		return true;
	}
}
