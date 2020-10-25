package beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import main.storage.CommandStorage;

@Configuration
public class CommandStorageConfiguration {

	@Bean
	public CommandStorage getPersonService() {
		return new CommandStorage();
	}

}
