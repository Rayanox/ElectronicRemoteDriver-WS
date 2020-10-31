package main.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

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
public abstract class AbstractSingleCachableConfStorage<T extends IStorageType, S extends IStorageType> extends AbstractCachableConfStorage<T, S> {

	private final Path fileStoragePath;
		
	public AbstractSingleCachableConfStorage() {
		this.fileStoragePath = Paths.get(getFileStorageLocation());
	}
	
	public Optional<S> getData(T key) throws Exception {
		return super.getData(key, null);
	}

	public void setData(T key, S data) throws Exception {
		super.setData(key, data, null);
	}
	
	
	protected Path getPathFileStorage(String fileKey) {
		return this.fileStoragePath;
	}
	
	@Override
	protected S convertValueFromString(String value, String programId) throws NotImplementedException {
		return convertValueFromString(value);
	}
	
	
	protected abstract String getFileStorageLocation();	
	protected abstract S convertValueFromString(String value) throws NotImplementedException;
}
