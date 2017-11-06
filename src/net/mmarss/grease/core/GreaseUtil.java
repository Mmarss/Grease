package net.mmarss.grease.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import net.mmarss.grease.exception.GreaseFileException;

/**
 * A class containing utility constants and methods used by the Grease library.
 */
public class GreaseUtil {
	
	/** Whether the program is being executed on a Mac operating system. */
	public static final boolean	SYSTEM_MAC		= System.getProperty("os.name").contains("Mac");
	/** The directory at which resources are stored. */
	private static final String	RESOURCE_DIR	= "../res/";
	
	/**
	 * Reads the specified text file into a single string.
	 * 
	 * @param filename
	 *            the file to read.
	 * @return the file contents, as a string.
	 * @throws GreaseFileException
	 *             if the file could not be found or read.
	 */
	public static String loadResource(String filename) throws GreaseFileException {
		
		try (BufferedReader br = new BufferedReader(new FileReader(RESOURCE_DIR + filename))) {
			
			StringBuilder builder = new StringBuilder();
			String line;
			
			while ((line = br.readLine()) != null) {
				builder.append(line).append("\n");
			}
			
			return builder.toString();
			
		} catch (IOException e) {
			throw new GreaseFileException("Could not read file " + RESOURCE_DIR + filename + ": " + e.getMessage());
		}
	}
}
