package beans;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import main.clients.IYoutubeClient;
import main.clients.YoutubeClient;
import main.providers.CommandProvider;
import main.providers.youtube.YoutubeManager;
import main.providers.youtube.YoutubePlaylistComposer;
import main.storage.CommandStorage;
import main.storage.DataStorage;

@Configuration
public class CommandStorageConfiguration {

	@Bean
	public CommandStorage getCommandStorage() {
		CommandStorage.CACHE_FOLDER_LOCATION = "./src/test/resources/commands";
		return new CommandStorage();
	}
	
	@Bean
	public CommandProvider getCommandProvider() {
		return new CommandProvider();
	}
	
	@Bean
	public DataStorage getDataStorage() {
		return new DataStorage();
	}
	
	@Bean
	public YoutubeManager getYoutubeManager() {
		return new YoutubeManager();
	}
	
	@Bean
	public YoutubePlaylistComposer getYoutubePlaylistComposer() {
		return new YoutubePlaylistComposer();
	}
	
	@Bean
	public IYoutubeClient getYoutubeClient() throws GeneralSecurityException, IOException {
		return new YoutubeClient();
	}
}
