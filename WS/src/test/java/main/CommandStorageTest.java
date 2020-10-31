package main;

import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import beans.AbstractTestCommon;
import beans.CommandStorageConfiguration;
import main.exceptions.BadFormatPropertyException;
import main.exceptions.MissingCommandCaseException;
import main.providers.CommandProvider;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommandStorageConfiguration.class)
public class CommandStorageTest extends AbstractTestCommon {

	@Autowired
	private CommandProvider commandProvider;

	
	
	// ---- Tests Cases ----
	
	@Test
	public void buildCommandTest_ForTime_15h34() throws Exception{
		//---- Arrange ----
		String programId = "programme_3";
		LocalTime time = LocalTime.of(15, 34);
		String resultExpected = readRessourceFile("commands/ResultOut/1_CommandsRequest_Case_15h34.txt");
		
		testCommands(programId, resultExpected, time);
	}

	@Test
	public void buildCommandTest_ForTime_04h12() throws Exception{
		//---- Arrange ----
		String programId = "programme_3";
		LocalTime time = LocalTime.of(4, 12);
		String resultExpected = readRessourceFile("commands/ResultOut/2_CommandsRequest_Case_04h12.txt");
		
		testCommands(programId, resultExpected, time);
	}

	@Test
	public void buildCommandTest_ForTime_17h45() throws Exception {
		//---- Arrange ----
		String programId = "programme_3";
		LocalTime time = LocalTime.of(17, 45);
		String resultExpected = readRessourceFile("commands/ResultOut/3_CommandsRequest_Case_17h45.txt");
		
		testCommands(programId, resultExpected, time);
	}

	@Test
	public void buildCommandTest_ForTime_21h00() throws Exception {
		//---- Arrange ----
		String programId = "programme_3";
		LocalTime time = LocalTime.of(21, 0);
		String resultExpected = readRessourceFile("commands/ResultOut/4_CommandsRequest_Case_21h00.txt");
		
		testCommands(programId, resultExpected, time);
	}

	@Test
	public void buildCommandTest_ForTime_22h00() throws Exception {
		//---- Arrange ----
		String programId = "programme_3";
		LocalTime time = LocalTime.of(22, 0);
		String resultExpected = readRessourceFile("commands/ResultOut/5_CommandsRequest_Case_22h00.txt");
		
		testCommands(programId, resultExpected, time);
	}

	@Test
	public void buildCommandTest_ForTime_22h45() throws Exception {
		//---- Arrange ----
		String programId = "programme_3";
		LocalTime time = LocalTime.of(22, 45);
		String resultExpected = readRessourceFile("commands/ResultOut/6_CommandsRequest_Case_22h45.txt");
		
		testCommands(programId, resultExpected, time);
	}

	@Test
	public void buildCommandTest_ForTime_22h11() throws Exception {
		//---- Arrange ----
		String programId = "programme_3";
		LocalTime time = LocalTime.of(22, 11);
		String resultExpected = readRessourceFile("commands/ResultOut/7_CommandsRequest_Case_22h11.txt");
		
		testCommands(programId, resultExpected, time);
	}
	
	@Test
	public void buildCommandTest_NoDefaultCase_ForTime_22h11() throws Exception {
		//---- Arrange ----
		String programId = "programme_4";
		LocalTime time = LocalTime.of(22, 11);
		String resultExpected = readRessourceFile("commands/ResultOut/8_CommandsRequest_WithNoDefaultCase_Case_22h11.txt");
		
		testCommands(programId, resultExpected, time);
	}

	
	
	@Test(expected = MissingCommandCaseException.class)
	public void throwExceptionMissingCommandCase() throws Exception {
		//---- Arrange ----
		String programId = "progr2";
		LocalTime time = LocalTime.of(22, 11);

		//Action
		commandProvider.GetCommands_ForDevice(programId, time);
	}
	
	@Test(expected = BadFormatPropertyException.class)
	public void throwExceptionOnBadFormatted() throws Exception {
		//---- Arrange ----
		String programId = "program1";
		LocalTime time = LocalTime.of(22, 11);

		//Action
		commandProvider.GetCommands_ForDevice(programId, time);
	}
	
	private void testCommands(String programId, String resultExpected, LocalTime time) throws Exception{
		// ---- Action ----
		String sequenceToSend = null;
		
		sequenceToSend = commandProvider.GetCommands_ForDevice(programId, time);
		
		// ---- Assert ----
		Assert.assertEquals(resultExpected, sequenceToSend);
	}


}
