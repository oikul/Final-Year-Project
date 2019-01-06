package game;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.Camera;
import engine.Entity;
import engine.IGameLogic;
import engine.Mesh;
import engine.MouseInput;
import engine.Renderer;
import engine.Shader;
import engine.Texture;
import engine.Window;

public class Game implements IGameLogic {

	private Shader shader;
	private float[] vertices = new float[] {
			// VO
			-0.5f, 0.5f, 0.5f,
			// V1
			-0.5f, -0.5f, 0.5f,
			// V2
			0.5f, -0.5f, 0.5f,
			// V3
			0.5f, 0.5f, 0.5f,
			// V4
			-0.5f, 0.5f, -0.5f,
			// V5
			0.5f, 0.5f, -0.5f,
			// V6
			-0.5f, -0.5f, -0.5f,
			// V7
			0.5f, -0.5f, -0.5f, };
	private int[] indices = new int[] {
			// Front face
			0, 1, 3, 3, 1, 2,
			// Top Face
			4, 0, 3, 5, 4, 3,
			// Right face
			3, 2, 7, 5, 3, 7,
			// Left face
			6, 1, 0, 6, 0, 4,
			// Bottom face
			2, 1, 6, 2, 6, 7,
			// Back face
			7, 6, 4, 7, 4, 5, };
	float[] colours = new float[] { 0.5f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.5f, 0.5f, 0.5f, 0.0f,
			0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.5f, 0.5f, };
	float[] textCoords = new float[] { 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1,
			0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1 };
	float[] normals = new float[] {};
	private Mesh mesh;
	private Texture texture;
	private Entity test;
	private Entity[] entities;
	private Camera camera;
	private Vector3f cameraInc = new Vector3f();
	private float CAMERA_POS_STEP = 0.0001f, MOUSE_SENSITIVITY = 0.001f;
	private static Texture t = new Texture();

	private final Renderer renderer;

	public Game() {
		renderer = new Renderer();
		camera = new Camera();
		entities = new Entity[400];
	}

	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);
		shader = new Shader();
		shader.create("basic");
		shader.createUniform("projectionMatrix");
		shader.createUniform("modelViewMatrix");
		shader.createUniform("texture_sampler");
		shader.createUniform("colour");
		shader.createUniform("useColour");
		texture = new Texture();
		texture.loadTexture(getBufferedImage("grass_jungle"));
		mesh = new Mesh(vertices, textCoords, normals, indices);
		mesh.setColour(0, 1, 1);
		mesh.create();
		test = new Entity(mesh);
		test.setScale(1.5f);
		test.setPosition(0, 0, 0);
		entities[0] = test;
		test = new Entity(mesh);
		test.setScale(2.5f);
		test.setPosition(1, 2, -42);
		entities[1] = test;
		test = new Entity(mesh);
		test.setScale(13f);
		test.setPosition(6, 1, -22);
		entities[2] = test;
		test = new Entity(mesh);
		test.setScale(2.7f);
		test.setPosition(7, 9, -21);
		entities[3] = test;
//		int count = 0;
//		for(int i = 0; i < 20; i++){
//			for(int j = 0; j < 20; j ++){
//				test = new Entity(mesh);
//				test.setScale(1);
//				test.setPosition(i - 10, -10, j - 10);
//				entities[count] = test;
//				count ++;
//			}
//		}
	}

	public static BufferedImage getBufferedImage(String path) {
		try {
			URL url = t.getClass().getClassLoader().getResource("resources/" + path + ".png");
			return ImageIO.read(url);
		} catch (IOException e) {
			System.err.println("failed to load");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void input(Window window, MouseInput mouseInput) {
		// if (window.isKeyPressed(GLFW_KEY_W)) {
		// test.setPosition(test.getPosition().x, test.getPosition().y,
		// test.getPosition().z + 0.5f);
		// }
		// if (window.isKeyPressed(GLFW_KEY_A)) {
		// test.setPosition(test.getPosition().x + 0.5f, test.getPosition().y,
		// test.getPosition().z);
		// }
		// if (window.isKeyPressed(GLFW_KEY_S)) {
		// test.setPosition(test.getPosition().x, test.getPosition().y,
		// test.getPosition().z - 0.5f);
		// }
		// if (window.isKeyPressed(GLFW_KEY_D)) {
		// test.setPosition(test.getPosition().x - 0.5f, test.getPosition().y,
		// test.getPosition().z);
		// }
		cameraInc.set(0, 0, 0);
		if (window.isKeyPressed(GLFW_KEY_W)) {
			cameraInc.z = -1;
		} else if (window.isKeyPressed(GLFW_KEY_S)) {
			cameraInc.z = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_A)) {
			cameraInc.x = -1;
		} else if (window.isKeyPressed(GLFW_KEY_D)) {
			cameraInc.x = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
			cameraInc.y = -1;
		} else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
			cameraInc.y = 1;
		}

	}

	@Override
	public void update(float interval, MouseInput mouseInput) {
		// float rotation = test.getRotation().x + 0.005f;
		// if (rotation > 360) {
		// rotation = 0;
		// }
		// test.setRotation(rotation, rotation, rotation);
		camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP,
				cameraInc.z * CAMERA_POS_STEP);
		Vector2f rotVec = mouseInput.getDisplVec();
		camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);

	}

	@Override
	public void render(Window window) {
		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}
		renderer.clear();
		renderer.render(window, shader, entities, camera);
	}

	@Override
	public void cleanup() {
		shader.destroy();
	}
}