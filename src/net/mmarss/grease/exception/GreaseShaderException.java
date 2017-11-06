package net.mmarss.grease.exception;

public class GreaseShaderException extends GreaseException {
	
	/** The randomly generated serial version ID. */
	private static final long serialVersionUID = 3174886916111094921L;
	
	/**
	 * Constructs a new shader exception with <code>null</code> as its detail
	 * message.
	 */
	public GreaseShaderException() {}
	
	/**
	 * Constructs a new shader exception with the specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public GreaseShaderException(String msg) {
		
		super(msg);
	}
}
