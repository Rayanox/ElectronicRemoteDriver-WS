package main.model.youtube.keyboard;

import main.model.command.old.OldCommand;

public class NextKeyNodeCommand {
	
	public KeyNode nextKeyNode;
	public OldCommand charDirection;
	
	public NextKeyNodeCommand(KeyNode nextKeyNode, OldCommand charDirection) {
		this.nextKeyNode = nextKeyNode;
		this.charDirection = charDirection;
	}
}
