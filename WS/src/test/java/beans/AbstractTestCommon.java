package beans;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public abstract class AbstractTestCommon {
	
	protected String readRessourceFile(String path) {
		Resource file = new ClassPathResource(path);
		
		String resultExpected = "";
		try {
			resultExpected = Files.lines(Paths.get(file.getURI())).findFirst().orElse("");
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		return resultExpected;
	}
	
}
