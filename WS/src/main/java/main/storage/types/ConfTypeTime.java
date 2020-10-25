package main.storage.types;

import java.time.LocalTime;
import java.util.Optional;

public class ConfTypeTime extends AbstractConfType<LocalTime> {
	
	private ConfTypeTime(LocalTime value) {
		super(value);
	}
	
	public static Optional<ConfTypeTime> buildFromString(String textValue) {
		ConfTypeTime instanceBuilt = null;
		
		try {
			LocalTime value = LocalTime.parse(textValue);
			instanceBuilt = new ConfTypeTime(value);
		} catch(NumberFormatException e) {
		}
		
		return Optional.ofNullable(instanceBuilt);
	}

	public static ConfTypeTime buildFrom(LocalTime value) {
		return new ConfTypeTime(value);
	}
	
	@Override
	public String convertToString() {
		return getValue().toString();
	}
}
