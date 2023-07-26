package gt.gob.rgm.adm.exception;

public class EntityAlreadyExistsException extends Exception {
	public EntityAlreadyExistsException() {
	}
	
	public EntityAlreadyExistsException(String message) {
		super(message);
	}
}
