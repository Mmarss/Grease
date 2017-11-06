package net.mmarss.grease.exception;

public class GreaseException extends Exception {
	
	/** The randomly generated serial version ID. */
	private static final long serialVersionUID = 1200619813695204888L;
	
	/**
	 * Constructs a new exception with <code>null</code> as its detail message.
	 */
	public GreaseException() {}
	
	/**
	 * Constructs a new exception with the specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public GreaseException(String msg) {
		
		super(msg);
	}
}
