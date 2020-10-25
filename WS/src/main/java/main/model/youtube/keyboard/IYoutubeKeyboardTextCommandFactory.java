package main.model.youtube.keyboard;

import main.model.OldCommandSequenceBuilder;

public interface IYoutubeKeyboardTextCommandFactory {
	OldCommandSequenceBuilder addCommandsFromText(OldCommandSequenceBuilder commandBuilder, String text, Character startPosition) throws Exception;
}
