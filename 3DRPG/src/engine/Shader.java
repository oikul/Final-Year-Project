package engine;
import static org.lwjgl.opengl.GL11.GL_FALSE;
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
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class Shader {

	private int vertexShader, fragmentShader, program;
	private final Map<String, Integer> uniforms;

	public Shader() {
		uniforms = new HashMap<>();
	}

	public boolean create(String shader) {
		int success;

		vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, readSource(shader + ".vs"));
		glCompileShader(vertexShader);

		success = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			System.err.println(glGetShaderInfoLog(vertexShader));
			return false;
		}

		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, readSource(shader + ".fs"));
		glCompileShader(fragmentShader);

		success = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			System.err.println(glGetShaderInfoLog(fragmentShader));
			return false;
		}

		program = glCreateProgram();
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);

		glLinkProgram(program);
		success = glGetProgrami(program, GL_LINK_STATUS);
		if (success == GL_FALSE) {
			System.err.println(glGetProgramInfoLog(program));
			return false;
		}
		glValidateProgram(program);
		success = glGetProgrami(program, GL_VALIDATE_STATUS);
		if (success == GL_FALSE) {
			System.err.println(glGetProgramInfoLog(program));
			return false;
		}
		return true;
	}

	public void destroy() {
		glDetachShader(program, vertexShader);
		glDetachShader(program, fragmentShader);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		glDeleteProgram(program);
	}

	public void useShader() {
		glUseProgram(program);
	}

	public void unbind() {
		glUseProgram(0);
	}

	private String readSource(String file) {
		BufferedReader reader = null;
		StringBuilder sourceBuilder = new StringBuilder();

		try {
			reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/shaders/" + file)));

			String line;

			while ((line = reader.readLine()) != null) {
				sourceBuilder.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sourceBuilder.toString();
	}

	public void createUniform(String uniformName) throws Exception {
		int uniformLocation = glGetUniformLocation(program, uniformName);
		if (uniformLocation < 0) {
			throw new Exception("Could not find uniform: " + uniformName);
		}
		uniforms.put(uniformName, uniformLocation);
	}
	
	public void setUniform(String uniformName, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(16);
	        value.get(fb);
	        glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
	    }
	}
	
	public void setUniform(String uniformName, Vector3f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(3);
	        value.get(fb);
	        glUniform3fv(uniforms.get(uniformName), fb);
	    }
	}
	
	public void setUniform(String name, int value){
		glUniform1i(uniforms.get(name), value);
	}

}
