package main.storage;

import java.io.IOException;

import main.exceptions.BadFormatPropertyException;

public interface ICachable {
	void refreshCache() throws BadFormatPropertyException, IOException;
}
