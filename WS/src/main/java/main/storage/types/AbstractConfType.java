package main.storage.types;

public abstract class AbstractConfType<T> implements IStorageType {

	private T value;
	
	protected AbstractConfType(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}
