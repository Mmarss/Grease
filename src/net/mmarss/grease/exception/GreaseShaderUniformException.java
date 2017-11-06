package net.mmarss.grease.exception;

public class GreaseShaderUniformException extends GreaseShaderException {
	
	/** The randomly generated serial version ID. */
	private static final long serialVersionUID = -8655711213751307538L;
	
	/**
	 * Constructs a new shader uniform exception with <code>null</code> as its
	 * detail message.
	 */
	public GreaseShaderUniformException() {}
	
	/**
	 * Constructs a new shader uniform exception with the specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public GreaseShaderUniformException(String msg) {
		
		super(msg);
	}
}
