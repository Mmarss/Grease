package net.mmarss.grease.exception;

public class GreaseShaderCompilationError extends GreaseShaderException {
	
	/** The randomly generated serial version ID. */
	private static final long serialVersionUID = 5008215822258614353L;
	
	/**
	 * Constructs a new shader compilation error with <code>null</code> as its
	 * detail message.
	 */
	public GreaseShaderCompilationError() {}
	
	/**
	 * Constructs a new shader compilation error with the specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public GreaseShaderCompilationError(String msg) {
		
		super(msg);
	}
}
