package engine;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lighting.DirectionalLight;
import lighting.PointLight;
import mesh.Entity;

public class Renderer {

	private Matrix4f projection, view;
	private final float FOV = (float) Math.toRadians(60.0f);
	private final float Z_NEAR = 0.01f;
	private final float Z_FAR = 1000.0f;
	private Transformation transformation;
	private float specularPower;
	
	public Renderer(){
		transformation = new Transformation();
		specularPower = 10f;
	}

	public void init(Window window) throws Exception {
		projection = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
		window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void render(Window window, Shader shader, Entity entities[], Camera camera, Vector3f ambientLight, PointLight pointLight, DirectionalLight directionalLight) {
		clear();
		shader.useShader();		
		projection = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
		shader.setUniform("projectionMatrix", projection);
		shader.setUniform("texture_sampler", 0);
		view = transformation.getViewMatrix(camera);
		shader.setUniform("ambientLight", ambientLight);
		shader.setUniform("specularPower", specularPower);
		PointLight currPointLight = new PointLight(pointLight);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(view);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;
        shader.setUniform("pointLight", currPointLight);
        DirectionalLight currDirLight = new DirectionalLight(directionalLight);
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(view);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        shader.setUniform("directionalLight", currDirLight);
		for (Entity e : entities) {
			if (e != null) {
				Matrix4f modelViewMatrix = transformation.getModelViewMatrix(e, view);
				shader.setUniform("modelViewMatrix", modelViewMatrix);
				shader.setUniform("material", e.getMesh().getMaterial());
				e.getMesh().render();
			}
		}
		shader.unbind();
	}

}
