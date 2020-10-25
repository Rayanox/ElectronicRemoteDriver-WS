package main.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;
import org.springframework.stereotype.Repository;
import main.exceptions.BadFormatPropertyException;
import main.exceptions.NotImplementedException;
import main.storage.types.ConfTypeDouble;
import main.storage.types.ConfTypeString;
import main.storage.types.ConfTypeTime;
import main.storage.types.IStorageType;
import main.utils.Converter;

@Repository
public class DataStorage extends AbstractSingleCachableConfStorage<ConfTypeString, IStorageType>{
	
	private final String cacheLocation = "config" + File.separator + "StoredData.conf";
	

	@Override
	protected String getFileStorageLocation() {
		return this.cacheLocation;
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

	
	
}
