package main.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import main.exceptions.BadFormatPropertyException;
import main.exceptions.NotImplementedException;
import main.storage.types.IStorageType;
/**
 * 
 * @author rbenhmidane
 *
 * @param <T> The type of the key in the conf file
 * @param <S> The type of the encapsulation value in the conf file
 */
public abstract class AbstractCachableConfStorage<T extends IStorageType, S extends IStorageType> extends AbstractStorageIO {

	private HashMap<String, HashMap<T, S>> dataCacheMap;
	private HashMap<String, Long> lastModificationDatesMap;
	
	public AbstractCachableConfStorage()  {
		this.dataCacheMap = new HashMap<>();
		this.lastModificationDatesMap = new HashMap<>();
	}
	
	protected Optional<S> getData(T key, String keyFile) throws BadFormatPropertyException, IOException, NotImplementedException {
		S data = getAllDataS(keyFile).get(key);
		return Optional.ofNullable(data);				
	}
	
	protected HashMap<T, S> getAllDataS(String keyFile) throws BadFormatPropertyException, IOException, NotImplementedException {
		if(needToRefreshCache(keyFile))
			refreshCache(keyFile);
		
		return dataCacheMap.get(keyFile);
	}
		
	
	protected void setData(T key, S data, String keyFile) throws IOException, BadFormatPropertyException, NotImplementedException {
		if(needToRefreshCache(keyFile))
			this.refreshCache(keyFile);
		
		HashMap<T, S> cache = this.dataCacheMap.get(keyFile);
		File file = getFileStorage(keyFile).get();
		
		// Put in cache map
		cache.put(key, data);
						
		// Put in the file
		writeToFile(cache, file);
		
		// Update the last modified time (for cache)
		this.lastModificationDatesMap.put(keyFile, this.getFileStorage(keyFile).get().lastModified());
	}
	
	private void refreshCache(String fileKey) throws BadFormatPropertyException, IOException, NotImplementedException {
		File dataFile = getFileStorage(fileKey).orElse(new File(fileKey+"-newFileAutoCreatedByKey"));
		if(!dataFile.exists())
			dataFile.createNewFile();
		
		try {
			HashMap<T, S> mapOfCurrentKey = this.dataCacheMap.get(fileKey);
			if(mapOfCurrentKey == null) {
				mapOfCurrentKey = new HashMap<>();
				this.dataCacheMap.put(fileKey, mapOfCurrentKey);
			}
			
			List<String> fileLines = readFromFile(dataFile);
			int currentLine = 1;
			for(String lineData : fileLines) {
				String [] lineSplitted = lineData.split("=");
				if(lineSplitted.length != 2)
					throw new BadFormatPropertyException("The data conf contains a bad format property at line " + currentLine);
				
				String key = lineSplitted[0];
				String value = lineSplitted[1];
				
				T keyObj = convertKeyFromString(key);
				S valueObj = convertValueFromString(value);
									
				mapOfCurrentKey.put(keyObj, valueObj);
				
				currentLine++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("WARN: When refreshing the cache, the file has not been found.");
		}
	}
	
	private void writeToFile(HashMap<T, S> dataCacheMap, File file) throws IOException {
		
		List<String> outputFile = dataCacheMap.keySet().parallelStream() //Parallel not necessary (because low quantity of datas are processed) but just for my own practicing :)
							.map((key) -> {
								String keyString = key.convertToString();
								String valueString = dataCacheMap.get(key).convertToString();
								return keyString.concat("=").concat(valueString);
							}).collect(Collectors.toList());
		
		writeToFile(outputFile, file);
	}
	
	private boolean needToRefreshCache(String keyFile) {
		File storageFile = getFileStorage(keyFile).orElse(null);
		long lastmodificationDateRecorded = this.lastModificationDatesMap.getOrDefault(keyFile, 0L);
		return dataCacheMap.get(keyFile) == null || dataCacheMap.get(keyFile).isEmpty() || storageFile == null || (storageFile.lastModified() > lastmodificationDateRecorded);
	}


	/*
	 * Inherited functions
	 */
	
	protected abstract T convertKeyFromString(String key) throws NotImplementedException;
	protected abstract S convertValueFromString(String value) throws NotImplementedException;
	protected abstract Optional<File> getFileStorage(String key);
}
