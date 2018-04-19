package init.test;

public class NoURLException extends Exception {
	/**
	 * for test use only
	 */
	private static final long serialVersionUID = 1L;
	public NoURLException(String message) {
		super(message);
	}
	// author: Jin Dai 04102018
	public String getMessage() {
		return super.getMessage();
	}
}
