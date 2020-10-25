package main.providers;

import java.io.IOException;
import java.time.LocalTime;

import main.exceptions.BadFormatPropertyException;
import main.exceptions.MissingCommandCaseException;
import main.exceptions.NotImplementedException;
import main.model.OldCommandSequence;
import main.model.ContentType;
import main.model.dto.CommandDto;

public interface ICommandProvider {
	/*
	 *  Content commands creation
	 */
	OldCommandSequence GetContentCommand_SimpleVideo() throws Exception;
	OldCommandSequence GetContentCommand_InfoChannel(InfoChannel channel) throws Exception;
	OldCommandSequence GetContentCommand_CustomDailyYoutubePlaylist(CommandDto wrapper) throws Exception;
	
	/*
	 *  Steps commands creation
	 */
	String GetCommands_ForTimerPeriod() throws Exception;
	String GetCommands_ForWakingUp(OldCommandSequence contentCommands, ContentType contentType) throws Exception;
	String GetCommands_ForClosing(long mediasTotalDurationMinutes) throws Exception;
	
	/*
	 *  New Custom commands for devices
	 */
	String GetCommands_ForDevice(String programId, LocalTime time) throws BadFormatPropertyException, IOException, MissingCommandCaseException, NotImplementedException;
}
