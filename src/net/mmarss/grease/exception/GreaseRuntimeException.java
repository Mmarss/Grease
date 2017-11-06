package net.mmarss.grease.exception;

/**
 * The base class for runtime exceptions thrown by the Grease library.
 */
public class GreaseRuntimeException extends RuntimeException {
	
	/** The randomly generated serial version ID. */
	private static final long serialVersionUID = 4092482941869334112L;
	
	/**
	 * Constructs a new runtime exception with <code>null</code> as its detail
	 * message.
	 */
	public GreaseRuntimeException() {}
	
	/**
	 * Constructs a new runtime exception with the specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public GreaseRuntimeException(String msg) {
		
		super(msg);
	}
}
