package main.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import main.storage.types.IStorageType;
/**
 * 
 * @author rbenhmidane
 *
 * @param <T> The type of the key in the conf file
 * @param <S> The type of the encapsulation value in the conf file
 */
public abstract class AbstractMultipleCachableConfStorage<T extends IStorageType, S extends IStorageType> extends AbstractCachableConfStorage<T, S> {

	public AbstractMultipleCachableConfStorage() {
	}
	
	
//	public Optional<S> getData(T key, String keyFile) throws BadFormatPropertyException, IOException {
//		return super.getData(key, keyFile);
//	}
//
//	public void setData(T key, S data, String keyFile) throws IOException, BadFormatPropertyException {
//		super.setData(key, data, keyFile);
//	}
	
	
	protected Path getPathFileStorage(String fileKey) {
		return Arrays.stream(super.folder.listFiles())
				.filter(file -> file.getName().contains("-"))
				.filter(file -> file.getName().startsWith(fileKey))
				.map(file -> Paths.get(file.getName()))
				.findFirst()
				.orElse(Paths.get(fileKey));
	}
}
