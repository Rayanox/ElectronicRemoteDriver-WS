package main.providers;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import main.controllers.InfosController;
import main.controllers.OperationController;
import main.exceptions.BadFormatPropertyException;
import main.exceptions.MissingCommandCaseException;
import main.exceptions.NotImplementedException;
import main.model.command.CommandSequence;
import main.model.command.old.OldCommand;
import main.model.command.old.OldCommandSequence;
import main.model.command.old.OldCommandSequenceBuilder;
import main.model.ContentType;
import main.model.DeviceAction;
import main.model.dto.CommandDto;
import main.providers.youtube.YoutubeManager;
import main.storage.CommandStorage;
import main.storage.DataStorage;
import main.storage.types.ConfTypeInt;
import main.storage.types.ConfTypeString;
import main.storage.types.ConfTypeTime;


/**
 *  Commands sequences building
 */

@Component
public class CommandProvider implements ICommandProvider {


	@Autowired
	DataStorage dataStorage;
	@Autowired
	CommandStorage commandStorage;

	@Autowired
	private YoutubeManager youtubeManager;
	
	
	/*
	 *  New custom command function for devices
	 */
	
	public String GetCommands_ForDevice(String programId, LocalTime time) throws Exception {
		CommandSequence commandSequence = this.commandStorage.getCommandsForTime(programId, time);
		return commandSequence.toStringRequest();
	}
	
	
	
	/*
	 *  Commands by formula
	 */
	
	@Override
	public OldCommandSequence GetContentCommand_SimpleVideo() throws NotImplementedException {
		throw new NotImplementedException("The method simple video has not implemented, and may never be implemented in the future.");
	}

	@Override
	public OldCommandSequence GetContentCommand_InfoChannel(InfoChannel channel) throws Exception {
		OldCommandSequenceBuilder commandBrowserBuilder = OldCommandSequenceBuilder.CreateCommandSequence(true);
		commandBrowserBuilder.browseToYoutube().typeTextInYoutube(channel.getTextToType()).addCommand(DeviceAction.O);		
		return commandBrowserBuilder.getCommandSequence();
	}

	@Override
	public OldCommandSequence GetContentCommand_CustomDailyYoutubePlaylist(CommandDto dto) throws Exception {
		
		// Update the playlist
		this.youtubeManager.UpdateYoutubePlaylist(dto);
		
		OldCommandSequenceBuilder commandBrowserBuilder = OldCommandSequenceBuilder.CreateCommandSequence(true);
		
		//TODO Code the access to the custom channel from Youtube on the PS4
		//throw new NotImplementedException("Youtube custom playlist feature not implemented yet (I have to code the access to the custom channel from Youtube on the PS4)");
		
		
		
		return commandBrowserBuilder.getCommandSequence();
	}
	
	
	
	/*
	 *  Common commands
	 */
	
	
	public String GetCommands_ForTimerPeriod() throws Exception {
		OldCommandSequenceBuilder commandBrowserBuilder = OldCommandSequenceBuilder.CreateCommandSequence(false);
		
		// Sleep during the timer period
		Integer minutesToWait = (Integer) this.dataStorage.getData(ConfTypeString.buildFromString(InfosController.CONF_SHUTDOWN_TIMER_VALUE_PROPERTY_NAME).get())
															.orElse(ConfTypeInt.buildFrom(InfosController.DEFAULT_SHUTDOWN_TIMER_VALUE))
															.getValue();
		commandBrowserBuilder.sleep(minutesToWait * 60);
		
		// Close the TV
		commandBrowserBuilder.addCommand(OldCommand.Create(DeviceAction.TV_Power));
		
		// Close the playstation
		//TODO To code at home
		
		return commandBrowserBuilder.build();
	}
	
	public String GetCommands_ForWakingUp(OldCommandSequence contentCommands, ContentType contentType) throws Exception {
		OldCommandSequenceBuilder commandBrowserBuilder = OldCommandSequenceBuilder.CreateCommandSequence(false);
		
		// Sleep the time needed to wakeup at defined time
		Integer timerMinutesToWait = (Integer) this.dataStorage.getData(ConfTypeString.buildFromString(InfosController.CONF_SHUTDOWN_TIMER_VALUE_PROPERTY_NAME).get())
																.orElse(ConfTypeInt.buildFrom(InfosController.DEFAULT_SHUTDOWN_TIMER_VALUE))
																.getValue();
		LocalTime timeWakeUp = (LocalTime) this.dataStorage.getData(ConfTypeString.buildFromString(InfosController.CONF_ALARM_CLOCK_PROPERTY_NAME).get())
																.orElse(ConfTypeTime.buildFrom(InfosController.DEFAULT_ALARM_TIME))
																.getValue();
		int nightSleepingDurationSeconds = computeTimeToSleepForNight(timeWakeUp, timerMinutesToWait);
		commandBrowserBuilder.sleep(nightSleepingDurationSeconds);
		
		// Do a specific job, related to the contentType
		processSpecificJob(commandBrowserBuilder, contentType);
		
		// Open the Playstation
		commandBrowserBuilder.addCommand(DeviceAction.PS_Home);
		
		// Go to the main menu (case if I was somewhere else, like already on Youtube or an other app)
		//TODO To code at home
		
		// Add the 'contentCommands' commands to go to Youtube for example and search for the targeted video
		commandBrowserBuilder.addAllCommands(contentCommands);
		
		// Click on the video, then pause it
		//TODO To code at home

		// Enable sound out loud
		Integer volumeLevel = (Integer) this.dataStorage.getData(ConfTypeString.buildFromString(InfosController.CONF_VOLUM_PROPERTY_NAME).get())
														.orElse(ConfTypeInt.buildFrom(InfosController.DEFAULT_VOLUM))
														.getValue();
		setSoundConfiguration(commandBrowserBuilder, volumeLevel);
		
		// Open the TV
		commandBrowserBuilder.addCommand(DeviceAction.TV_Power);
		
		// Wait few seconds (time to wait for the TV to be open)
		commandBrowserBuilder.sleep(10);
		
		// Click Play
		commandBrowserBuilder.addClickCommand();
		
		return commandBrowserBuilder.build();
	}
	

	public String GetCommands_ForClosing(long mediasTotalDurationMinutes) throws Exception {
		OldCommandSequenceBuilder commandBrowserBuilder = OldCommandSequenceBuilder.CreateCommandSequence(false);
		
		// Sleep during the time all the videos will take
		// TODO when I'll have the second PS remote control, I will konw more about the PS4 behaviours switching between the both remote controls, and probably I will enhance the code
		commandBrowserBuilder.sleep(mediasTotalDurationMinutes*60);
		
		// Disable sound out loud
		setSoundConfiguration(commandBrowserBuilder, 0);
		
		// Shutdown the device (Arduino)
		commandBrowserBuilder.addCommand(DeviceAction.Shutdown_Device);
		
		return commandBrowserBuilder.build();
	}
	
	
	
	/*
	 *  Private methods
	 */
	


	private void processSpecificJob(OldCommandSequenceBuilder commandBrowserBuilder, ContentType contentType) {
		switch(contentType) {
			case CUSTOM_DAILY_YOUTUBE_PLAYLIST:
				commandBrowserBuilder.sendHttpGetRequest(OperationController.UPDATE_PLAYLIST_PATH);
				break;
			default:
				return;
		}
	}
	
	private void setSoundConfiguration(OldCommandSequenceBuilder commandBrowserBuilder, int volumeLevel) {
		// Set the sound volume to 0, then increase it to reach the targeted volume level
		//TODO To code at home (check the second count)
		commandBrowserBuilder.addCommand(OldCommand.Create(DeviceAction.Volum_Down, 10));
		commandBrowserBuilder.addCommand(OldCommand.Create(DeviceAction.Volum_Up), 25);
		
		// Set the 'enable sound out of headset' box to checked.
		//TODO To code at home
	}
	
	/**
	 * 
	 * @param timeWakeUp
	 * @param timerMinutesToWait
	 * @return the amount of seconds to sleep
	 */
	private int computeTimeToSleepForNight(LocalTime timeWakeUp, Integer timerMinutesToWait) {
		Calendar cal = Calendar.getInstance();
		Calendar wakeUpTime = Calendar.getInstance(); // Got to remove Calendar and use LocalTime instead (need few time to test the behaviour before commiting so I let it there) 
		wakeUpTime.set(Calendar.HOUR_OF_DAY, timeWakeUp.getHour());
		wakeUpTime.set(Calendar.MINUTE, timeWakeUp.getMinute());
		wakeUpTime.set(Calendar.SECOND, timeWakeUp.getSecond());
		
		if(wakeUpTime.before(cal))
			wakeUpTime.add(Calendar.DAY_OF_MONTH, 1);
		
		Long totalTimeSecondsBeforeWakeup = (wakeUpTime.getTimeInMillis() - cal.getTimeInMillis()) / 1000;
		int shutdownTimerInSeconds = timerMinutesToWait * 60;
		
		return totalTimeSecondsBeforeWakeup.intValue() - shutdownTimerInSeconds;
	}
	
}
