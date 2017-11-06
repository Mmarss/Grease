package net.mmarss.grease.exception;

public class GreaseShaderOpenGLException extends GreaseShaderException {
	
	/** The randomly generated serial version ID. */
	private static final long serialVersionUID = -1434892745142733888L;
	
	/**
	 * Constructs a new shader OpenGL exception with <code>null</code> as its detail
	 * message.
	 */
	public GreaseShaderOpenGLException() {}
	
	/**
	 * Constructs a new shader OpenGL exception with the specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public GreaseShaderOpenGLException(String msg) {
		
		super(msg);
	}
}
