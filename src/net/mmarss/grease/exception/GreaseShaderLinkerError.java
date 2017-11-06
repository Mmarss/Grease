package net.mmarss.grease.exception;

public class GreaseShaderLinkerError extends GreaseShaderException {
	
	/** The randomly generated serial version ID. */
	private static final long serialVersionUID = -7988156899336400226L;
	
	/**
	 * Constructs a new shader linker error with <code>null</code> as its
	 * detail message.
	 */
	public GreaseShaderLinkerError() {}
	
	/**
	 * Constructs a new shader linker error with the specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public GreaseShaderLinkerError(String msg) {
		
		super(msg);
	}
}
