package main.storage;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Repository;
import main.exceptions.NotImplementedException;
import main.storage.types.ConfTypeDouble;
import main.storage.types.ConfTypeString;
import main.storage.types.ConfTypeTime;
import main.storage.types.IStorageType;

@Repository
public class DataStorage extends AbstractSingleCachableConfStorage<ConfTypeString, IStorageType>{
	
	private static final String CACHE_LOCATION = "StoredData.conf";
	private static final String FOLDER_LOCATION = "config";

	@Override
	protected String getFileStorageLocation() {
		return CACHE_LOCATION;
	}

	@Override
	protected ConfTypeString convertKeyFromString(String key) throws NotImplementedException {
		return ConfTypeString.buildFromString(key)
								.orElseThrow(() -> new NotImplementedException(
								String.join(" ", "The string value cannot be converted. Need to implement a new conf type for key '", key, "'")));
	}

	@Override
	protected IStorageType convertValueFromString(String value) throws NotImplementedException {
		IStorageType valueData = null;
		
		valueData = ConfTypeDouble.buildFromString(value).orElse(null);
		if(valueData != null)
			return valueData;

		valueData = ConfTypeTime.buildFromString(value).orElse(null);
		if(valueData != null)
			return valueData;
		
		valueData = ConfTypeString.buildFromString(value).orElse(null);
		if(valueData != null)
			return valueData;
		
		throw new NotImplementedException(String.join(" ", "The string value cannot be converted. Need to implement a new conf type for value '", value, "'")); // Ne doit pas rentrer dedans en principe
	}

	@Override
	protected boolean isFirstLineSpecialProcessed() {
		return false;
	}

	@Override
	protected void processFirstLine(String line, String programId) {
	}

	@Override
	protected boolean manageDefaultAloneValue() {
		return false;
	}

	@Override
	protected Path getPathFolderStorageLocation() {
		return Paths.get(FOLDER_LOCATION);
	}

	
	
}
