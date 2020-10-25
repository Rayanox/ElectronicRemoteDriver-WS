package main.storage;

import java.io.File;
import java.io.IOException;
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

	private final File fileStorage;
		
	public AbstractSingleCachableConfStorage() {
		this.fileStorage = new File(getFileStorageLocation());
	}
	
	public Optional<S> getData(T key) throws BadFormatPropertyException, IOException, NotImplementedException {
		return super.getData(key, null);
	}

	public void setData(T key, S data) throws IOException, BadFormatPropertyException, NotImplementedException {
		super.setData(key, data, null);
	}
	
	
	protected Optional<File> getFileStorage(String fileKey) {
		return Optional.of(this.fileStorage);
	}
	
	
	protected abstract String getFileStorageLocation();	
}
