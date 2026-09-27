package shared.exception;

public class InternalSystemException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MESSAGE =
			"Ocurrio un error interno del sistema. Intenta nuevamente mas tarde.";

	public InternalSystemException() {
		super(DEFAULT_MESSAGE);
	}

	public InternalSystemException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}
}
