package main.model.command.old;

import java.util.List;
import java.util.stream.Collectors;

import main.exceptions.NotImplementedException;
import main.model.DeviceAction;
import main.model.youtube.keyboard.IYoutubeKeyboardTextCommandFactory;
import main.model.youtube.keyboard.YoutubeKeyboardTextCommandFactory;

public class OldCommandSequenceBuilder {
		
	private IYoutubeKeyboardTextCommandFactory youtubeKeyboardCommandManager;
	
	private OldCommandSequence commandSequence;
	
	public static final char delimiter = '-'; 
	
	private OldCommandSequenceBuilder(IYoutubeKeyboardTextCommandFactory youtubeKeyboardCommandManager) {
		this.youtubeKeyboardCommandManager = youtubeKeyboardCommandManager;
		this.commandSequence = new OldCommandSequence();
	}
	
	private OldCommandSequenceBuilder() {
		this(new YoutubeKeyboardTextCommandFactory());
	}
	
	/**
	 * 
	 * @param needYoutubeKeyboard if false, it optimize this object creation because the 'youtubeKeyboardCommandManager' costs because of a complex algorithm
	 * @return
	 */
	public static OldCommandSequenceBuilder CreateCommandSequence(boolean needYoutubeKeyboard) {
		return needYoutubeKeyboard ? new OldCommandSequenceBuilder() : new OldCommandSequenceBuilder(null);
	}
	
	public OldCommandSequenceBuilder addCommand(OldCommand cmd) {
		this.commandSequence.addBtnCommand(cmd);
		return this;
	}
	
	public OldCommandSequenceBuilder addCommand(DeviceAction action) {
		this.commandSequence.addBtnCommand(action);
		return this;
	}
	
	public OldCommandSequenceBuilder addAllCommands(OldCommand [] commands) {
		for (OldCommand psButton : commands) {
			this.commandSequence.addBtnCommand(psButton);
		}
		return this;
	}
	public OldCommandSequenceBuilder addAllCommands(OldCommandSequence commands) {
		for (OldCommand psButton : commands.getBtnSequence()) {
			this.commandSequence.addBtnCommand(psButton);
		}
		return this;
	}
	
	public OldCommandSequenceBuilder addClickCommand() {
		this.commandSequence.addBtnCommand(DeviceAction.X);
		return this;
	}
	
	/**
	 *  Add a button and repeat it X consecutive times. 
	 * 
	 * @param cmd
	 * @param times
	 * @return
	 */
	public OldCommandSequenceBuilder addCommand(OldCommand cmd, int times) {
		for (int i = 0; i < times; i++) {
			this.commandSequence.addBtnCommand(cmd);
		}
		return this;
	}
	
	/**
	 * Here is converted the Command sequence object into a String
	 * @return
	 */
	public String build() {
		String brutSequence = "";
		
		List<String> stringCommandSequence = this.commandSequence.getBtnSequence()
				.stream()
				.map( command -> {
					try {
						return command.isParameterSet() ?
								command.getBrutCharacterForMessage() + "," + command.getParameter()
								: String.valueOf(command.getBrutCharacterForMessage());
					} catch (NotImplementedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					return null;
				})
				.collect(Collectors.toList());
		
		brutSequence += String.join(String.valueOf(delimiter), stringCommandSequence);
		return brutSequence;		
	}
	
	/*
	 *  Predefined command sequences
	 */
	
	public OldCommandSequenceBuilder typeTextInYoutube(String text) throws Exception {
		// Get the youtube commands to type the text
		youtubeKeyboardCommandManager.addCommandsFromText(this, text, 'A');
		
		//Press enter
		return this.addCommand(DeviceAction.Down).addCommand(DeviceAction.Down).addCommand(DeviceAction.O);
	}

	public OldCommandSequenceBuilder browseToYoutube() {
		//Go to the applications folder
		return this.addCommand(DeviceAction.Right).addCommand(DeviceAction.Right).addCommand(DeviceAction.Right).addCommand(DeviceAction.Right).addCommand(DeviceAction.Right).addCommand(DeviceAction.O)
		// Select Youtube
		.addCommand(DeviceAction.Sleep).addCommand(DeviceAction.Right).addCommand(DeviceAction.Down)
		.addCommand(DeviceAction.Down).addCommand(DeviceAction.Down).addCommand(DeviceAction.Down).addCommand(DeviceAction.Down)
		.addCommand(DeviceAction.Down).addCommand(DeviceAction.Down).addCommand(DeviceAction.Down).addCommand(DeviceAction.O).addCommand(DeviceAction.Sleep).addCommand(DeviceAction.O);
	}

	public OldCommandSequenceBuilder sleep(long i) {
		return this.addCommand(OldCommand.Create(DeviceAction.Sleep, i));
	}

	public OldCommandSequenceBuilder sendHttpGetRequest(String url) {
		return this.addCommand(OldCommand.Create(DeviceAction.HTTP_GET, String.format("\"%s\"", url)));
	}

	
	/*
	 *  Getters
	 */
	
	/**
	 *  Shouldn't be used other than in test place
	 *  
	 * @return
	 */
	public IYoutubeKeyboardTextCommandFactory getYoutubeKeyboardCommandManager() {
		return youtubeKeyboardCommandManager;
	}
	
	public OldCommandSequence getCommandSequence() {
		return commandSequence;
	}
}
