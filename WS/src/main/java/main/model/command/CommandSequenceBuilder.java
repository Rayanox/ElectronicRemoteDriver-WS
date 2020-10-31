package main.model.command;

import java.util.List;
import java.util.stream.Collectors;

import main.exceptions.NotImplementedException;
import main.model.Command;
import main.model.youtube.keyboard.IYoutubeKeyboardTextCommandFactory;
import main.model.youtube.keyboard.YoutubeKeyboardTextCommandFactory;

public class CommandSequenceBuilder {
		
	private CommandSequence commandSequence;
	
	private CommandSequenceBuilder() {
		this.commandSequence = new CommandSequence();
	}
	
	/**
	 * 
	 * @param needYoutubeKeyboard if false, it optimize this object creation because the 'youtubeKeyboardCommandManager' costs because of a complex algorithm
	 * @return
	 */
	public static CommandSequenceBuilder createBuilder() {
		return new CommandSequenceBuilder();
	}
	
	public CommandSequenceBuilder addCommand(Command cmd) {
		this.commandSequence.addBtnCommand(cmd);
		return this;
	}
	
	public CommandSequenceBuilder addAllCommands(Command [] commands) {
		for (Command psButton : commands) {
			this.commandSequence.addBtnCommand(psButton);
		}
		return this;
	}
	public CommandSequenceBuilder addAllCommands(CommandSequence commands) {
		for (Command psButton : commands.getBtnSequence()) {
			this.commandSequence.addBtnCommand(psButton);
		}
		return this;
	}
	
	/**
	 *  Add a button and repeat it X consecutive times. 
	 * 
	 * @param cmd
	 * @param times
	 * @return
	 */
	public CommandSequenceBuilder addCommand(Command cmd, int times) {
		for (int i = 0; i < times; i++) {
			this.commandSequence.addBtnCommand(cmd);
		}
		return this;
	}
	
	
	public CommandSequence build() {
		return this.commandSequence;
	}
	
	/*
	 *  Getters
	 */
	
	public CommandSequence getCommandSequence() {
		return commandSequence;
	}
}
