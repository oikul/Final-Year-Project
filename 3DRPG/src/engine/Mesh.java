package engine;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

public class Mesh {

	private int vertexArrayObject, vertexBufferObject, indexBufferObject, textureBufferObject, normalsBufferObject,
			vertexCount;
	private float[] positions, textCoords, normals;
	private FloatBuffer textureBuffer, normalsBuffer;
	private int[] indices;
	private Texture texture;
	private boolean textured;
	private final Vector3f colour = new Vector3f();

	public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices, Texture texture) {
		this.positions = positions;
		this.textCoords = textCoords;
		this.normals = normals;
		this.indices = indices;
		this.texture = texture;
		textured = true;
	}

	public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
		this.positions = positions;
		this.textCoords = textCoords;
		this.normals = normals;
		this.indices = indices;
		textured = false;
	}

	public boolean create() {
		vertexArrayObject = glGenVertexArrays();
		glBindVertexArray(vertexArrayObject);

		vertexBufferObject = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
		glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

		indexBufferObject = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

		textureBufferObject = glGenBuffers();
		textureBuffer = MemoryUtil.memAllocFloat(textCoords.length);
		textureBuffer.put(textCoords).flip();
		glBindBuffer(GL_ARRAY_BUFFER, textureBufferObject);
		glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

		normalsBufferObject = glGenBuffers();
		normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
		normalsBuffer.put(normals).flip();
		glBindBuffer(GL_ARRAY_BUFFER, normalsBufferObject);
		glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

		glBindVertexArray(0);

		vertexCount = indices.length;

		return true;
	}

	public void destroy() {
		glDeleteBuffers(vertexBufferObject);
		glDeleteBuffers(indexBufferObject);
		glDeleteBuffers(textureBufferObject);
		glDeleteVertexArrays(vertexArrayObject);
	}

	public void draw() {
		if (textured) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture.getID());
		}

		glBindVertexArray(vertexArrayObject);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);

		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public boolean isTextured() {
		return textured;
	}

	public void setColour(float r, float g, float b) {
		colour.x = r;
		colour.y = g;
		colour.z = b;
	}

	public Vector3f getColour() {
		return colour;
	}

}
