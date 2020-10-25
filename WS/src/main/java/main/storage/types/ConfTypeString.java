package main.storage.types;

import java.util.Optional;

public class ConfTypeString extends AbstractConfType<String> {
	
	private ConfTypeString(String value) {
		super(value);
	}
	
	public static Optional<ConfTypeString> buildFromString(String textValue) {
		ConfTypeString instanceBuilt = null;
				
		instanceBuilt = new ConfTypeString(textValue);
		
		return Optional.ofNullable(instanceBuilt);
	}
	
	@Override
	public String convertToString() {
		return getValue();
	}
}
