package net.mmarss.grease.exception;

public class GreaseWindowException extends GreaseException {
	
	/** The randomly generated serial version ID. */
	private static final long serialVersionUID = 2817997029243575273L;
	
	/**
	 * Constructs a new window exception with <code>null</code> as its detail
	 * message.
	 */
	public GreaseWindowException() {}
	
	/**
	 * Constructs a new window exception with the specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public GreaseWindowException(String msg) {
		
		super(msg);
	}
}
