package main.model;

import main.exceptions.NotImplementedException;

public class OldCommand {
	private DeviceAction deviceAction;
	
	// Parameter
	private Object commandParameter; // The command parameter, like the time period to maintain a button pressed, or any other parameter relative to the protocol documentation.
	
	private OldCommand(DeviceAction deviceAction, Object actionPressTimeSeconds) {
		this.deviceAction = deviceAction;
		this.commandParameter = actionPressTimeSeconds;
	}
	
	
	public static OldCommand Create(DeviceAction action, Integer actionPressTimeSeconds) {
		return new OldCommand(action, actionPressTimeSeconds);
	}
	public static OldCommand Create(DeviceAction action, Long actionPressTimeSeconds) {
		return new OldCommand(action, actionPressTimeSeconds);
	}
	public static OldCommand Create(DeviceAction action, String stringParameter) {
		return new OldCommand(action, stringParameter);
	}
	public static OldCommand Create(DeviceAction action) {
		return new OldCommand(action, null);
	}
	
	
	
	public boolean isParameterSet() throws NotImplementedException {
		if(this.commandParameter == null)
			return false;
		
		if(this.commandParameter instanceof Integer)
			return ((Integer)this.commandParameter) > 0;
		else if(this.commandParameter instanceof Long)
			return ((Long)this.commandParameter) > 0;
		else if(this.commandParameter instanceof String)
			return !((String)this.commandParameter).isEmpty();
		else 
			throw new NotImplementedException("The command parameter conversion has not been implemented for the type " + this.commandParameter.getClass().getSimpleName());
	}
	
	@Override
	public boolean equals(Object commandObject) {
		if(!commandObject.getClass().equals(this.getClass()))
			return false;
		
		OldCommand command = (OldCommand) commandObject;
		
		if(command.getParameter() != null && this.getParameter() == null
				|| command.getParameter() == null && this.getParameter() != null)
			return false;
		
		if(command.getParameter() != null && !command.getParameter().equals(this.commandParameter))
			return false;
					
		if(!command.getDeviceAction().equals(this.deviceAction))
			return false;
		
		return true;
	}
	
	/*
	 *  Getters
	 */
	
	public DeviceAction getDeviceAction() {
		return this.deviceAction;
	}
	
	public Object getParameter() {
		return this.commandParameter;
	}	
	
	public char getBrutCharacterForMessage() {
		return this.deviceAction.getBrutCharacterForMessage();
	}	
}
