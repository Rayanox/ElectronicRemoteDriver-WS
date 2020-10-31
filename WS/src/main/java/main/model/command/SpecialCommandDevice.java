package main.model.command;

import java.util.Arrays;
import java.util.HashMap;

import main.exceptions.BadFormatParameterException;
import main.model.Command;

public enum SpecialCommandDevice {
	
	Sleep("Sleep","S"),
	SleepTo("SleepTo","ST");
	
	private String configText;
	private String outputText;
	
	private SpecialCommandDevice(String configTextIn, String outputRequestText) {
		this.configText = configTextIn;
		this.outputText = outputRequestText;
	}
	
	public static void initCommandMappingWithSpecialCommands(HashMap<String, String> commandMapping) {
		Arrays.stream(SpecialCommandDevice.values())
				.forEach(c -> commandMapping.put(c.configText, c.outputText));
	}

	public static Command processParameterForDeviceCommand(Command command) {
		SpecialCommandDevice specialCommand = detectSpecialCommandFromString(command.getCommandValue());
		
		if(specialCommand == null)
			return command;
		
		CommandParameter param = command.getCommandParameter();
		// Use pattern match with Java 11 ?
		switch(specialCommand) {
			case SleepTo :
				if(param == null || !CommandParameterType.HOUR_TIME.equals(param.getParameterType()))
					throw new BadFormatParameterException("The parameter SleepTo must be an hour formated as 'hh:mm'.");
				// Finalement plus besoin de convertir ici, car il y aura une méthode du WS qui renverra le nombre de secondes en fonction de l'heure envoyée  
				break;
			default:
				break;
		}
		return command;
	}

	private static SpecialCommandDevice detectSpecialCommandFromString(String commandValue) {
		return Arrays.stream(SpecialCommandDevice.values())
		.filter(specials -> commandValue.equals(specials.configText))
		.findFirst()
		.orElse(null);
	}
	
	
}
