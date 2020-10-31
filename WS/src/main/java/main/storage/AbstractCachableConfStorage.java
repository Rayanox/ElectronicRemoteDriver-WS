package main.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import main.exceptions.BadFormatPropertyException;
import main.storage.types.IStorageType;
/**
 * 
 * @author rbenhmidane
 *
 * @param <T> The type of the key in the conf file
 * @param <S> The type of the encapsulation value in the conf file
 */
public abstract class AbstractCachableConfStorage<T extends IStorageType, S extends IStorageType> extends AbstractStorageIO {

	private static final String COMMENT_CHAR = "#";
	protected final T KEY_DEFAULT_CASE = null; 
	
	private HashMap<String, LinkedHashMap<T, S>> dataCacheMap;
	private HashMap<String, Long> lastModificationDatesMap;
	
	protected final File folder;
	
	public AbstractCachableConfStorage()  {
		this.dataCacheMap = new HashMap<>();
		this.lastModificationDatesMap = new HashMap<>();
		
		this.folder = getPathFolderStorageLocation().toFile();
		
		if(!this.folder.exists())
			this.folder.mkdirs();
	}
	
	protected Optional<S> getData(T key, String keyFile) throws Exception {
		S data = getAllDataS(keyFile).get(key);
		return Optional.ofNullable(data);				
	}
	
	protected LinkedHashMap<T, S> getAllDataS(String keyFile) throws Exception {
		if(needToRefreshCache(keyFile))
			refreshCache(keyFile);
		
		return dataCacheMap.get(keyFile);
	}
		
	
	protected void setData(T key, S data, String keyFile) throws Exception {
		if(needToRefreshCache(keyFile))
			this.refreshCache(keyFile);
		
		HashMap<T, S> cache = this.dataCacheMap.get(keyFile);
		File file = getFileStorage(keyFile);
		
		// Put in cache map
		cache.put(key, data);
						
		// Put in the file
		writeToFile(cache, file);
		
		// Update the last modified time (for cache)
		this.lastModificationDatesMap.put(keyFile, this.getFileStorage(keyFile).lastModified());
	}
	
	private synchronized void refreshCache(String fileKey) throws Exception {
		File dataFile = getFileStorage(fileKey);
		
		try {
			LinkedHashMap<T, S> mapOfCurrentKey = this.dataCacheMap.get(fileKey);
			if(mapOfCurrentKey == null) {
				mapOfCurrentKey = new LinkedHashMap<>();
				this.dataCacheMap.put(fileKey, mapOfCurrentKey);
			}
			
			List<String> fileLines = readFromFile(dataFile);
			boolean hasAloneValueBeenProcessed = false;
			int currentLine = 0; 
			for(String lineData : fileLines) {	
				currentLine++;
				lineData = removeCommentsAndSpaces(lineData);
				
				if(currentLine == 1 && isFirstLineSpecialProcessed()) {
					processFirstLine(lineData, fileKey);
					continue;
				}
				
				if(lineData.isEmpty())
					continue;
				
				T key = KEY_DEFAULT_CASE;
				S value = null;
				String [] lineSplitted = lineData.split("=");
				if(lineSplitted.length > 2)
					throw new BadFormatPropertyException("The data conf contains a bad format property (should be 'key=value') at line " + currentLine);
				else if(lineSplitted.length == 1) {
					if(!manageDefaultAloneValue())
						throw new BadFormatPropertyException("The data conf contains a bad format property (should be 'key=value') at line " + currentLine);
					
					if(!hasAloneValueBeenProcessed) {
						value = convertValueFromString(lineSplitted[0], fileKey);
						hasAloneValueBeenProcessed = true;
					}
				}else { // Normal case
					key = convertKeyFromString(lineSplitted[0]);
					value = convertValueFromString(lineSplitted[1], fileKey);
				}
				
				mapOfCurrentKey.put(key, value);					
			}
		} catch (FileNotFoundException e) {
			System.out.println("WARN: When refreshing the cache, the file has not been found.");
		}
	}

	
	private String removeCommentsAndSpaces(String lineData) {
		Pattern commentPattern = Pattern.compile("#.*#|#.*");
		Matcher matcher = commentPattern.matcher(lineData);
		return matcher.replaceAll("").trim();
	}

	private void writeToFile(HashMap<T, S> dataCacheMap, File file) throws IOException {
		
		List<String> outputFile = dataCacheMap.keySet().parallelStream() //Parallel not necessary (because low quantity of datas are processed) but just for my own practicing :)
							.map((key) -> writeConfigPropertyInOneLine(key, dataCacheMap))
							.collect(Collectors.toList());
		
		super.writeToFile(outputFile, file);
	}
	
	private String writeConfigPropertyInOneLine(T key, HashMap<T, S> dataCacheMap) {
		String keyString = key.convertToString();
		String valueString = dataCacheMap.get(key).convertToString();
		return keyString.concat("=").concat(valueString);
	}
	
	
	private boolean needToRefreshCache(String keyFile) {
		File storageFile = getFileStorage(keyFile);
		long lastmodificationDateRecorded = this.lastModificationDatesMap.getOrDefault(keyFile, 0L);
		return dataCacheMap.get(keyFile) == null || dataCacheMap.get(keyFile).isEmpty() || storageFile == null || (storageFile.lastModified() > lastmodificationDateRecorded);
	}


	private File getFileStorage(String fileKey) {
		File file = Paths.get(getPathFolderStorageLocation().toString(), getPathFileStorage(fileKey).toString()).toFile();
		
		try {
			if(!file.exists()) {
				if(fileKey == null)
					file.createNewFile();
				else {
					file = Paths.get(getPathFolderStorageLocation().toString(), fileKey+"-newFileAutoCreatedByKey").toFile();
					file.createNewFile();
				}
			}
			return file;
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * Inherited functions
	 */
	
	protected abstract T convertKeyFromString(String key) throws Exception;
	protected abstract S convertValueFromString(String value, String programId) throws Exception;
	protected abstract Path getPathFileStorage(String key);
	protected abstract Path getPathFolderStorageLocation();
	
	protected abstract boolean isFirstLineSpecialProcessed();
	protected abstract void processFirstLine(String line, String programId);
	
	protected abstract boolean manageDefaultAloneValue();
}
