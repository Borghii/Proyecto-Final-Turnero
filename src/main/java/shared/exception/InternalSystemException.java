package shared.exception;

public class InternalSystemException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InternalSystemException(String message) {
		super(message);
	}
}
