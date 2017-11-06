package net.mmarss.grease.exception;

public class GreaseInvalidMethodCallException extends GreaseRuntimeException {
	
	/** The randomly generated serial version ID. */
	private static final long serialVersionUID = -3890893680928467946L;
	
	/**
	 * Constructs a new invalid method call exception with <code>null</code> as its
	 * detail message.
	 */
	public GreaseInvalidMethodCallException() {}
	
	/**
	 * Constructs a new invalid method call exception with the specified detail
	 * message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public GreaseInvalidMethodCallException(String msg) {
		
		super(msg);
	}
}
