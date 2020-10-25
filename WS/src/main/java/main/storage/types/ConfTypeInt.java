package main.storage.types;

import java.util.Optional;

public class ConfTypeInt extends AbstractConfType<Integer> {
	
	private ConfTypeInt(Integer value) {
		super(value);
	}
	
	public static Optional<ConfTypeInt> buildFromString(String textValue) {
		ConfTypeInt instanceBuilt = null;
		
		try {
			Integer value = Integer.parseInt(textValue);
			instanceBuilt = new ConfTypeInt(value);
		} catch(NumberFormatException e) {
		}
		
		return Optional.ofNullable(instanceBuilt);
	}
	
	public static ConfTypeInt buildFrom(Integer value) {
		return new ConfTypeInt(value);
	}
	
	@Override
	public String convertToString() {
		return getValue().toString();
	}
}
