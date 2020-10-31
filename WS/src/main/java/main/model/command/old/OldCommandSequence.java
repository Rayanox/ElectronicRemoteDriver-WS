package main.model.command.old;

import java.util.ArrayList;
import main.exceptions.NotImplementedException;
import main.model.DeviceAction;
import main.storage.types.IStorageType;

public class OldCommandSequence implements IStorageType {
	
	private ArrayList<OldCommand> btnSequence;
	
	public OldCommandSequence() {
		this.btnSequence = new ArrayList<OldCommand>();
	}
	
	public void addBtnCommand(OldCommand btn) {
		this.btnSequence.add(btn);
	}
	
	public void addBtnCommand(DeviceAction action) {
		this.btnSequence.add(OldCommand.Create(action));
	}
	
	public void addBtnCommand(DeviceAction action, Long secondsDuring) {
		this.btnSequence.add(OldCommand.Create(action, secondsDuring));
	}
	
	public void addBtnCommand(DeviceAction action, String stringParam) {
		this.btnSequence.add(OldCommand.Create(action, stringParam));
	}
	
	public ArrayList<OldCommand> getBtnSequence() {
		return btnSequence;
	}
	
	public boolean compare(OldCommandSequence seqCompared) {
		if(btnSequence.size() != seqCompared.getBtnSequence().size())
			return false;
		for(int i = 0; i < btnSequence.size(); i++) {
			if(!btnSequence.get(i).equals(seqCompared.getBtnSequence().get(i)))
				return false;
		}
		return true;
	}
	
	public String toStringRequest() {
		return "";
	}

	/*
	 * For Conf String encapsulation
	 * 
	 */
	
	@Override
	public String convertToString() {
		// TODO Auto-generated method stub
		return null;
	}

	public static OldCommandSequence buildFromString(String textValue) throws NotImplementedException {
		throw new NotImplementedException("Need to implement this method !!");
	}
	
	@Override
	public Object getValue() {
		return btnSequence;
	}
	
}
