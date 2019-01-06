package engine;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import org.joml.Math;
import org.joml.Matrix4f;

public class Renderer {

	private Matrix4f projection, view;
	private final float FOV = (float) Math.toRadians(60.0f);
	private final float Z_NEAR = 0.01f;
	private final float Z_FAR = 1000.0f;
	private Transformation transformation;
	
	public Renderer(){
		transformation = new Transformation();
	}

	public void init(Window window) throws Exception {
		projection = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
		window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void render(Window window, Shader shader, Entity entities[], Camera camera) {
		clear();
		shader.useShader();		
		projection = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
		shader.setUniform("projectionMatrix", projection);
		shader.setUniform("texture_sampler", 0);
		view = transformation.getViewMatrix(camera);
		for (Entity e : entities) {
			if (e != null) {
				Matrix4f modelViewMatrix = transformation.getModelViewMatrix(e, view);
				shader.setUniform("modelViewMatrix", modelViewMatrix);
				shader.setUniform("colour", e.getMesh().getColour());
				shader.setUniform("useColour", e.getMesh().isTextured() ? 0 : 1);
				e.getMesh().draw();
			}
		}
		shader.unbind();
	}

}
