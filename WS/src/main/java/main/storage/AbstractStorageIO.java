package main.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractStorageIO {
		
	protected List<String> readFromFile(File file) throws IOException {
		ArrayList<String> resultFile = new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String lineData;
		while((lineData = reader.readLine()) != null) {
			resultFile.add(lineData);
		}
		reader.close();
		
		return resultFile;
	}
	
	protected void writeToFile(List<String> lines, File file) throws IOException {
		file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
		writer.write("");
		for (String line : lines) {
			writer.append(line);
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}
	
}
