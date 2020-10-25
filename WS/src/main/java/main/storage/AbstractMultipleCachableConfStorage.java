package main.storage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import main.exceptions.BadFormatPropertyException;
import main.storage.types.IStorageType;
/**
 * 
 * @author rbenhmidane
 *
 * @param <T> The type of the key in the conf file
 * @param <S> The type of the encapsulation value in the conf file
 */
public abstract class AbstractMultipleCachableConfStorage<T extends IStorageType, S extends IStorageType> extends AbstractCachableConfStorage<T, S> {

	private final File filefolder;
		
	public AbstractMultipleCachableConfStorage() {
		this.filefolder = new File(getFolderStorageLocation());
	}
	
	
//	public Optional<S> getData(T key, String keyFile) throws BadFormatPropertyException, IOException {
//		return super.getData(key, keyFile);
//	}
//
//	public void setData(T key, S data, String keyFile) throws IOException, BadFormatPropertyException {
//		super.setData(key, data, keyFile);
//	}
	
	
	protected Optional<File> getFileStorage(String fileKey) {
		return Arrays.stream(this.filefolder.listFiles())
				.filter(file -> file.getName().contains("-"))
				.filter(file -> file.getName().startsWith(fileKey))
				.findFirst();
	}
	
	protected abstract String getFolderStorageLocation();	
}
