package main.model.youtube.keyboard;

import java.util.HashMap;

import main.model.DeviceAction;
import main.model.command.old.OldCommand;

public class KeyNode {
	
	private Character character;

	private KeyNode nodeUp;
	private KeyNode nodeDown;
	private KeyNode nodeLeft;
	private KeyNode nodeRight;
	
	/**
	 *  Map that tell which Keynode is the closest to reach the expected character.
	 */
	private HashMap<Character, DistanceNode> mapGoToClosest;
	
	private final boolean isBridgeKeyNode;
	private KeyNode nodeKeyBridgeTarget;
	
	/**
	 *  Used only when calculating the distances inside array graph (then no more used)
	 */
	private boolean hasBeenMarqued = false;
	
	/**
	 * Constructor of bridge key node
	 */
	public KeyNode() {
		this.character = null;
		this.isBridgeKeyNode = true;
		this.mapGoToClosest = new HashMap<Character, DistanceNode>();
	}
	
	/**
	 * Constructor of character keyNode
	 * 
	 * @param character
	 */
	public KeyNode(Character character) {
		this.character = character;
		this.mapGoToClosest = new HashMap<Character, DistanceNode>();
		this.isBridgeKeyNode = false;
		this.setNodeKeyBridgeTarget(null);
		this.pushToGoToClosestMap(character, new DistanceNode(0));
	}
	
	public void pushToGoToClosestMap(Character key, DistanceNode value) {
		this.mapGoToClosest.put(key, value);
	}
	
	public DistanceNode getMapDistanceNodeForCharacter(Character character) {
		return this.mapGoToClosest.get(character);
	}
	
	//Celui qui va appeler cette methode devra surement gerer le cas de l'ajout de plusieurs NextKeyNodeCommand à la suite pour faire le lien entre les deux claviers 
	public NextKeyNodeCommand getClosestKeyNodeToReachCharacter(Character character) throws Exception {
		KeyNode nextKeyNode = this.mapGoToClosest.get(character).nextKeyNodeClosestDistance;

		if(nodeUp != null && !nodeUp.isBridgeKeyNode() && nodeUp.getCharacter().equals(nextKeyNode.getCharacter())) {
			return new NextKeyNodeCommand(nextKeyNode, OldCommand.Create(DeviceAction.Up));
		}else if(nodeDown != null && !nodeDown.isBridgeKeyNode() && nodeDown.getCharacter().equals(nextKeyNode.getCharacter())) {
			return new NextKeyNodeCommand(nextKeyNode, OldCommand.Create(DeviceAction.Down));
		}else if(nodeLeft != null && !nodeLeft.isBridgeKeyNode() && nodeLeft.getCharacter().equals(nextKeyNode.getCharacter())) {
			return new NextKeyNodeCommand(nextKeyNode, OldCommand.Create(DeviceAction.Left));
		}else if(nodeRight != null && !nodeRight.isBridgeKeyNode() && nodeRight.getCharacter().equals(nextKeyNode.getCharacter())) {
			return new NextKeyNodeCommand(nextKeyNode, OldCommand.Create(DeviceAction.Right));
		
		// Bridge case
		}else if(this.isBridgeKeyNode() && nextKeyNode == nodeKeyBridgeTarget) {
			return new NextKeyNodeCommand(nextKeyNode, OldCommand.Create(DeviceAction.X));
		}else if(nodeRight != null && nodeRight.isBridgeKeyNode) { // Case of the adjacent bridge node ('N' or ':'), to go to the bridge at the right side
			return new NextKeyNodeCommand(nextKeyNode, OldCommand.Create(DeviceAction.Right));
			
		}else {
			throw new Exception("A development conception mistake is present. No adjacent node is the closest node to go detected.");
		}
	}
	
	
	
	
	/*
	 *  Setters / Getters
	 */
	
	public Character getCharacter() {
		return character;
	}

	public boolean isBridgeKeyNode() {
		return isBridgeKeyNode;
	}
	
	public void setNodeUp(KeyNode nodeUp) {
		this.nodeUp = nodeUp;
	}
	public void setNodeDown(KeyNode nodeDown) {
		this.nodeDown = nodeDown;
	}
	public void setNodeLeft(KeyNode nodeLeft) {
		this.nodeLeft = nodeLeft;
	}
	public void setNodeRight(KeyNode nodeRight) {
		this.nodeRight = nodeRight;
	}
	public KeyNode getNodeUp() {
		return nodeUp;
	}
	public KeyNode getNodeDown() {
		return nodeDown;
	}
	public KeyNode getNodeLeft() {
		return nodeLeft;
	}
	public KeyNode getNodeRight() {
		return nodeRight;
	}

	public KeyNode getNodeKeyBridgeTarget() {
		return nodeKeyBridgeTarget;
	}

	public void setNodeKeyBridgeTarget(KeyNode nodeKeyBridgeTarget) {
		this.nodeKeyBridgeTarget = nodeKeyBridgeTarget;
	}

	public boolean hasBeenMarqued() {
		return hasBeenMarqued;
	}

	public void setHasBeenMarqued(boolean hasBeenMarqued) {
		this.hasBeenMarqued = hasBeenMarqued;
	}
	
}
