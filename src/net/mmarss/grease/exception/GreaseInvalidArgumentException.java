package net.mmarss.grease.exception;

public class GreaseInvalidArgumentException extends GreaseRuntimeException {
	
	/** The randomly generated serial version ID. */
	private static final long serialVersionUID = 1315389685795902471L;
	
	/**
	 * Constructs a new invalid argument exception with <code>null</code> as its
	 * detail message.
	 */
	@Deprecated
	public GreaseInvalidArgumentException() {}
	
	/**
	 * Constructs a new invalid argument exception with the specified detail
	 * message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	@Deprecated
	public GreaseInvalidArgumentException(String msg) {
		
		super(msg);
	}
	
	/**
	 * Constructs a new invalid argument exception with a generated detail message.
	 * 
	 * @param parameterName
	 *            the parameter to which an invalid value was passed.
	 * @param value
	 *            the value passed to the parameter.
	 */
	public GreaseInvalidArgumentException(String parameterName, Object value) {
		
		this(parameterName, value, "");
	}
	
	/**
	 * Constructs a new invalid argument exception with a generated detail message.
	 * 
	 * @param parameterName
	 *            the parameter to which an invalid value was passed.
	 * @param value
	 *            the value passed to the parameter.
	 */
	public GreaseInvalidArgumentException(String parameterName, String value) {
		
		this(parameterName, value, "");
	}
	
	/**
	 * Constructs a new invalid argument exception with a generated detail message.
	 * 
	 * @param parameterName
	 *            the parameter to which an invalid value was passed.
	 * @param value
	 *            the value passed to the parameter.
	 * @param comment
	 *            an additional comment providing any additional details.
	 */
	public GreaseInvalidArgumentException(String parameterName, Object value, String comment) {
		
		this(parameterName, value == null ? "null" : value.toString(), comment);
	}
	
	/**
	 * Constructs a new invalid argument exception with a generated detail message.
	 * 
	 * @param parameterName
	 *            the parameter to which an invalid value was passed.
	 * @param value
	 *            the value passed to the parameter.
	 * @param comment
	 *            an additional comment providing any additional details.
	 */
	public GreaseInvalidArgumentException(String parameterName, String value, String comment) {
		
		super("Invalid value '" + value + "' passed to parameter '" + parameterName + "'." + comment);
	}
}
