package net.mmarss.grease.exception;

public class GreaseFileException extends GreaseException {
	
	/** The randomly generated serial version ID. */
	private static final long serialVersionUID = 227562184725291557L;
	
	/**
	 * Constructs a new exception with <code>null</code> as its detail message.
	 */
	public GreaseFileException() {}
	
	/**
	 * Constructs a new exception with the specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public GreaseFileException(String msg) {
		
		super(msg);
	}
}
