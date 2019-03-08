package engine;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import hud.IHud;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import mesh.Entity;
import mesh.Mesh;

public class Renderer {

	private Matrix4f projection, view;
	private final float FOV = (float) Math.toRadians(60.0f);
	private final float Z_NEAR = 0.01f;
	private final float Z_FAR = 1000.0f;
	private Transformation transformation;
	private float specularPower;
	private Shader shader, hudShader;
    private static final int MAX_POINT_LIGHTS = 5;
    private static final int MAX_SPOT_LIGHTS = 5;
	
	public Renderer(){
		transformation = new Transformation();
		specularPower = 10f;
	}

	public void init(Window window) throws Exception {
		projection = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
		window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		shader = new Shader();
		shader.create("basic");
		shader.createUniform("projectionMatrix");
		shader.createUniform("modelViewMatrix");
		shader.createUniform("texture_sampler");
		shader.createUniform("ambientLight");
		shader.createUniform("specularPower");
		shader.createMaterialUniform("material");
		shader.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
		shader.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
		shader.createDirectionalLightUniform("directionalLight");
		hudShader = new Shader();
		hudShader.create("hud");
		hudShader.createUniform("projModelMatrix");
		hudShader.createUniform("colour");
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void render(Window window, Entity entities[], Camera camera, Vector3f ambientLight, PointLight[] pointLightList, SpotLight[] spotLightList, DirectionalLight directionalLight, IHud hud) {
		clear();
		shader.useShader();	
		projection = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
		shader.setUniform("projectionMatrix", projection);
		shader.setUniform("texture_sampler", 0);
		view = transformation.getViewMatrix(camera);
//		shader.setUniform("ambientLight", ambientLight);
//		shader.setUniform("specularPower", specularPower);
//		PointLight currPointLight = new PointLight(pointLight);
//        Vector3f lightPos = currPointLight.getPosition();
//        Vector4f aux = new Vector4f(lightPos, 1);
//        aux.mul(view);
//        lightPos.x = aux.x;
//        lightPos.y = aux.y;
//        lightPos.z = aux.z;
//        shader.setUniform("pointLight", currPointLight);
//        DirectionalLight currDirLight = new DirectionalLight(directionalLight);
//        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
//        dir.mul(view);
//        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
//        shader.setUniform("directionalLight", currDirLight);

        renderLights(shader, view, ambientLight, pointLightList, spotLightList, directionalLight);
		for (Entity e : entities) {
			if (e != null) {
				Matrix4f modelViewMatrix = transformation.getModelViewMatrix(e, view);
				shader.setUniform("modelViewMatrix", modelViewMatrix);
				shader.setUniform("material", e.getMesh().getMaterial());
				e.getMesh().render();
			}
		}
		shader.unbind();
		hudShader.useShader();
		Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
	    for (Entity entity : hud.getEntities()) {
	        Mesh mesh = entity.getMesh();
	        Matrix4f projModelMatrix = transformation.getOrthoProjModelMatrix(entity, ortho);
	        hudShader.setUniform("projModelMatrix", projModelMatrix);
	        hudShader.setUniform("colour", mesh.getMaterial().getAmbientColour());
	        mesh.render();
	    }
		hudShader.unbind();
	}
	
	private void renderLights(Shader shader, Matrix4f viewMatrix, Vector3f ambientLight,
            PointLight[] pointLightList, SpotLight[] spotLightList, DirectionalLight directionalLight) {

        shader.setUniform("ambientLight", ambientLight);
        shader.setUniform("specularPower", specularPower);

        // Process Point Lights
        int numLights = pointLightList != null ? pointLightList.length : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLightList[i]);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shader.setUniform("pointLights", currPointLight, i);
        }

        // Process Spot Ligths
        numLights = spotLightList != null ? spotLightList.length : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
            SpotLight currSpotLight = new SpotLight(spotLightList[i]);
            Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
            dir.mul(viewMatrix);
            currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
            Vector3f lightPos = currSpotLight.getPointLight().getPosition();

            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            shader.setUniform("spotLights", currSpotLight, i);
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(directionalLight);
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        shader.setUniform("directionalLight", currDirLight);
    }
	
	public void cleanup(){
		shader.destroy();
		hudShader.destroy();
	}

}
