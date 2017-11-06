package net.mmarss.grease.core;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import net.mmarss.grease.exception.GreaseFileException;
import net.mmarss.grease.exception.GreaseInvalidMethodCallException;
import net.mmarss.grease.exception.GreaseShaderCompilationError;
import net.mmarss.grease.exception.GreaseShaderLinkerError;
import net.mmarss.grease.exception.GreaseShaderOpenGLException;
import net.mmarss.grease.exception.GreaseShaderUniformException;

/**
 * A wrapper around an OpenGL shader.
 * 
 * Creating a new shader object is most easily done by calling the
 * <code>Shader(vertexCode, fragmentCode)</code> constructor from a renderer's
 * <code>init</code> method. Alternatively, a new empty shader can be created
 * from any thread, on which the <code>create</code>, <code>addShader</code> and
 * <code>link</code> methods should then be called from the render thread.
 * 
 * To use the shader, call <code>bind</code> in a renderer's
 * <code>preRender</code> method, and <code>unbind</code> from a renderer's
 * <code>postRender</code> method.
 */
public class Shader {
	
	/** The maximum number of characters that may be written to an error string. */
	private static final int ERROR_BUFFER_STRING_LENGTH = 1024;
	
	/** The OpenGL identifier for this shader program. */
	private int	programId;
	/** The OpenGL identifier for the vertex shader program. */
	private int	vertexShaderId;
	/** The OpenGL identifier for the fragment shader program. */
	private int	fragmentShaderId;
	
	/** A map storing the registered uniforms in this shader program. */
	private final Map< String, Uniform > uniformMap = new HashMap<>();
	
	/**
	 * Constructs a new empty shader object.
	 */
	public Shader() {}
	
	/**
	 * Constructs and compiles a shader object, using the supplied source code. Must
	 * be called from the window/render thread.
	 * 
	 * @param vertexCode
	 *            The vertex shader source code.
	 * @param fragmentCode
	 *            The fragment shader source code.
	 * 
	 * @throws GreaseShaderOpenGLException
	 *             if one or more programs cannot be created.
	 * @throws GreaseShaderCompilationError
	 *             if one or more shaders cannot be compiled.
	 * @throws GreaseShaderLinkerError
	 *             if the shader program cannot be linked.
	 */
	public Shader(String vertexCode, String fragmentCode)
			throws GreaseShaderOpenGLException, GreaseShaderCompilationError, GreaseShaderLinkerError {
		
		create();
		addShaders(vertexCode, fragmentCode);
		link();
	}
	
	/**
	 * Registers a new OpenGL shader program.
	 * 
	 * @throws GreaseShaderOpenGLException
	 *             if the shader program cannot be created.
	 */
	public void create() throws GreaseShaderOpenGLException {
		
		if (programId != 0) {
			throw new GreaseInvalidMethodCallException("Shader program has already been created.");
		}
		
		programId = glCreateProgram();
		if (programId == 0) {
			throw new GreaseShaderOpenGLException("OpenGL shader program creation failed");
		}
		
	}
	
	/**
	 * Adds the specified shaders to the created shader program.
	 * 
	 * @param vertexCode
	 *            the code for the vertex shader.
	 * @param fragmentCode
	 *            the code for the fragment shader.
	 * 
	 * @throws GreaseShaderOpenGLException
	 *             if one or both of the shaders cannot be created.
	 * @throws GreaseShaderCompilationError
	 *             if one or both of the shaders cannot be compiled.
	 */
	public void addShaders(String vertexCode, String fragmentCode)
			throws GreaseShaderOpenGLException, GreaseShaderCompilationError {
		
		addVertexShader(vertexCode);
		addFragmentShader(fragmentCode);
	}
	
	/**
	 * Creates a vertex shader with the specified code and adds it to this shader
	 * object.
	 * 
	 * @param code
	 *            the vertex shader code to compile.
	 * 
	 * @throws GreaseShaderOpenGLException
	 *             if the shader cannot be created.
	 * @throws GreaseShaderCompilationError
	 *             if the vertex shader cannot be compiled.
	 */
	public void addVertexShader(String code) throws GreaseShaderOpenGLException, GreaseShaderCompilationError {
		
		if (vertexShaderId != 0) {
			throw new GreaseInvalidMethodCallException("This shader program already has an attached vertex shader.");
		}
		vertexShaderId = createShader(code, GL_VERTEX_SHADER);
	}
	
	/**
	 * Creates a fragment shader with the specified code and adds it to this shader
	 * object.
	 * 
	 * @param code
	 *            the fragment shader code to compile.
	 * 
	 * @throws GreaseShaderOpenGLException
	 *             if the shader cannot be created.
	 * @throws GreaseShaderCompilationError
	 *             if the fragment shader cannot be compiled.
	 */
	public void addFragmentShader(String code) throws GreaseShaderOpenGLException, GreaseShaderCompilationError {
		
		if (fragmentShaderId != 0) {
			throw new GreaseInvalidMethodCallException("This shader program already has an attached fragment shader.");
		}
		fragmentShaderId = createShader(code, GL_FRAGMENT_SHADER);
	}
	
	/**
	 * Loads the specified shader source code from file and adds them to the created
	 * shader program.
	 * 
	 * @param vertexShaderFile
	 *            the file containing the code for the vertex shader.
	 * @param fragmentShaderFile
	 *            the file containing the code for the fragment shader.
	 * 
	 * @throws GreaseFileException
	 *             if one or both of the shader files cannot be read.
	 * @throws GreaseShaderOpenGLException
	 *             if one or both of the shaders cannot be created.
	 * @throws GreaseShaderCompilationError
	 *             if one or both of the shaders cannot be compiled.
	 */
	public void loadShaders(String vertexShaderFile, String fragmentShaderFile)
			throws GreaseShaderOpenGLException, GreaseShaderCompilationError, GreaseFileException {
		
		addVertexShader(GreaseUtil.loadResource(vertexShaderFile));
		addFragmentShader(GreaseUtil.loadResource(fragmentShaderFile));
	}
	
	/**
	 * Links this shader program.
	 * 
	 * @throws GreaseShaderLinkerError
	 *             if an error occurs while linking.
	 */
	public void link() throws GreaseShaderLinkerError {
		
		glLinkProgram(programId);
		if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
			throw new GreaseShaderLinkerError(
					"OpenGL shader linking failed: " + glGetProgramInfoLog(programId, ERROR_BUFFER_STRING_LENGTH));
		}
		
		if (vertexShaderId != 0) {
			glDetachShader(programId, vertexShaderId);
		}
		if (fragmentShaderId != 0) {
			glDetachShader(programId, fragmentShaderId);
		}
		
		glValidateProgram(programId);
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
			System.err.println(
					"OpenGL shader validation failed: " + glGetProgramInfoLog(programId, ERROR_BUFFER_STRING_LENGTH));
		}
	}
	
	/**
	 * Registers the named uniform within this shader.
	 * 
	 * @param uniform
	 *            the name of the uniform, as used in the shader program.
	 * @param type
	 *            the type of data that should be shared across this uniform.
	 * 
	 * @throws GreaseShaderUniformException
	 *             if no uniform by that name exists, or the type is invalid.
	 */
	public void createUniform(String uniform, Class< ? > type) throws GreaseShaderUniformException {
		
		uniformMap.putIfAbsent(uniform, new Uniform(uniform, type));
	}
	
	/**
	 * Sets the named uniform to the specified value.
	 * 
	 * @param uniformName
	 *            the name of the uniform
	 * @param value
	 *            the value to which to set this uniform.
	 * @throws GreaseShaderUniformException
	 *             if value has the wrong type.
	 */
	public void setUniform(String uniformName, Object value) throws GreaseShaderUniformException {
		
		Uniform uniform = uniformMap.get(uniformName);
		if (uniform == null) {
			throw new GreaseInvalidMethodCallException(
					"Cannot set uniform \"" + uniformName + "\": uniform has not been created");
		}
		
		uniform.setUniform(value);
	}
	
	/**
	 * Binds this shader to the current rendering context. Typically called in a
	 * renderer's <code>preRender</code> method.
	 */
	public void bind() {
		
		glUseProgram(programId);
	}
	
	/**
	 * Unbinds this shader from the current rendering context. Typically called in a
	 * renderer's <code>postRender</code> method.
	 */
	public void unbind() {
		
		glUseProgram(0);
	}
	
	/**
	 * Deletes this shader program.
	 */
	public void cleanup() {
		
		glUseProgram(0);
		if (programId != 0) {
			glDeleteProgram(programId);
		}
		
		for (Uniform u : uniformMap.values()) {
			u.cleanup();
		}
	}
	
	/**
	 * Creates and compiles an OpenGL shader from source code.
	 * 
	 * @param code
	 *            the code to compile.
	 * @param shaderType
	 *            the type of shader to create.
	 * @return the OpenGL identifier for the created shader.
	 * 
	 * @throws GreaseShaderOpenGLException
	 *             if the shader could not be created.
	 * @throws GreaseShaderCompilationError
	 *             if the shader could not be compiled.
	 */
	private int createShader(String code, int shaderType)
			throws GreaseShaderOpenGLException, GreaseShaderCompilationError {
		
		int shaderId = glCreateShader(shaderType);
		if (shaderId == 0) {
			throw new GreaseShaderOpenGLException("OpenGL shader creation failed");
		}
		
		glShaderSource(shaderId, code);
		glCompileShader(shaderId);
		
		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
			throw new GreaseShaderCompilationError(
					"OpenGL shader compilation failed: " + glGetShaderInfoLog(shaderId, ERROR_BUFFER_STRING_LENGTH));
		}
		
		glAttachShader(programId, shaderId);
		
		return shaderId;
	}
	
	/** Represents a uniform shared between the shader and the Java application. */
	private class Uniform {
		
		/** The name of this uniform variable. */
		private String		name;
		/** The location of this uniform variable. */
		private int			location;
		/** The type of this uniform variable. */
		private Class< ? >	type;
		/** The buffer storing the shared variable. */
		private Buffer		buffer;
		
		/**
		 * Constructs a new uniform with a specified name and data type.
		 * 
		 * @param name
		 *            the name of this uniform variable.
		 * @param type
		 *            the type of data stored in this uniform variable.
		 * 
		 * @throws GreaseShaderUniformException
		 *             if no uniform by that name exists, or the type is invalid.
		 */
		public Uniform(String name, Class< ? > type) throws GreaseShaderUniformException {
			
			this.name = name;
			
			location = glGetUniformLocation(programId, name);
			if (location < 0) {
				throw new GreaseShaderUniformException("Could not find uniform: " + name);
			}
			
			this.type = type;
			
			try (MemoryStack stack = MemoryStack.stackPush()) {
				
				createBuffer(stack);
				if (buffer == null) {
					throw new GreaseShaderUniformException("Unrecognized uniform type " + type.getName());
				}
			}
		}
		
		/**
		 * Frees the allocated shared memory space for this uniform.
		 */
		public void cleanup() {
			
			MemoryUtil.memFree(buffer);
		}
		
		/**
		 * Creates the buffer for this uniform in the given stack.
		 * 
		 * @param stack
		 *            the object on which to malloc the buffer.
		 * @return the created buffer.
		 */
		private Buffer createBuffer(MemoryStack stack) {
			
			if (type.equals(boolean.class) || type.equals(Boolean.class)) {
				return stack.mallocInt(1);
			}
			if (type.equals(short.class) || type.equals(Short.class)) {
				return stack.mallocShort(1);
			}
			if (type.equals(int.class) || type.equals(Integer.class)) {
				return stack.mallocInt(1);
			}
			if (type.equals(float.class) || type.equals(Float.class)) {
				return stack.mallocFloat(1);
			}
			if (type.equals(Vector2f.class)) {
				return stack.mallocFloat(2);
			}
			if (type.equals(Vector3f.class)) {
				return stack.mallocFloat(3);
			}
			if (type.equals(Vector4f.class)) {
				return stack.mallocFloat(4);
			}
			if (type.equals(Matrix4f.class)) {
				return stack.mallocFloat(16);
			}
			return null;
		}
		
		/**
		 * Sets the value of this uniform to the specified value.
		 * 
		 * @param value
		 *            the value to set this uniform to.
		 * @throws GreaseShaderUniformException
		 *             if the type of value is invalid.
		 */
		public void setUniform(Object value) throws GreaseShaderUniformException {
			
			if (!type.isAssignableFrom(value.getClass())) {
				throw new GreaseShaderUniformException("Cannot assign value to uniform \"" + name + "\": wrong type");
			}
			
			if (type.equals(boolean.class) || type.equals(Boolean.class)) {
				glUniform1i(location, Boolean.class.cast(value) == true ? 1 : 0);
			}
			if (type.equals(short.class) || type.equals(Short.class)) {
				glUniform1i(location, Short.class.cast(value));
			}
			if (type.equals(int.class) || type.equals(Integer.class)) {
				glUniform1i(location, Integer.class.cast(value));
			}
			if (type.equals(float.class) || type.equals(Float.class)) {
				glUniform1f(location, Float.class.cast(value));
			}
			if (type.equals(Vector2f.class)) {
				glUniform2f(location, Vector2f.class.cast(value).x, Vector2f.class.cast(value).y);
			}
			if (type.equals(Vector3f.class)) {
				glUniform3f(location, Vector3f.class.cast(value).x, Vector3f.class.cast(value).y,
						Vector3f.class.cast(value).z);
			}
			if (type.equals(Vector4f.class)) {
				glUniform4f(location, Vector4f.class.cast(value).x, Vector4f.class.cast(value).y,
						Vector4f.class.cast(value).z, Vector4f.class.cast(value).w);
			}
			if (type.equals(Matrix4f.class)) {
				glUniformMatrix4fv(location, false, Matrix4f.class.cast(value).get(FloatBuffer.allocate(16)));
			}
		}
	}
}
