package net.mmarss.grease.exception;

public class GreaseSystemException extends GreaseException {
	
	/** The randomly generated serial version ID. */
	private static final long serialVersionUID = -7627191875581204766L;
	
	/**
	 * Constructs a new system exception with <code>null</code> as its detail
	 * message.
	 */
	public GreaseSystemException() {}
	
	/**
	 * Constructs a new system exception with the specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public GreaseSystemException(String msg) {
		
		super(msg);
	}
}
