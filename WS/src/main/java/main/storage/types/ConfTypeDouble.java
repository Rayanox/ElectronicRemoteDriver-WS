package main.storage.types;

import java.util.Optional;

public class ConfTypeDouble extends AbstractConfType<Double> {
	
	private ConfTypeDouble(Double value) {
		super(value);
	}
	
	public static Optional<ConfTypeDouble> buildFromString(String textValue) {
		ConfTypeDouble instanceBuilt = null;
		
		try {
			Double value = Double.parseDouble(textValue);
			instanceBuilt = new ConfTypeDouble(value);
		} catch(NumberFormatException e) {
		}
		
		return Optional.ofNullable(instanceBuilt);
	}

	public static ConfTypeDouble buildFrom(Double value) {
		return new ConfTypeDouble(value);
	}
	
	@Override
	public String convertToString() {
		return getValue().toString();
	}
}
