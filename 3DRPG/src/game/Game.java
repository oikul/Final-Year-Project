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
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.Camera;
import engine.Entity;
import engine.IGameLogic;
import engine.MouseInput;
import engine.Renderer;
import engine.Shader;
import engine.Texture;
import engine.Window;
import generators.TreeGenerator;
import generators.VoronoiGenerator;

public class Game implements IGameLogic {

	private Shader shader;
	private Entity[] entities;
	private ArrayList<Entity> entityList;
	private Camera camera;
	private Vector3f cameraInc = new Vector3f();
	private float CAMERA_POS_STEP = 0.0001f, MOUSE_SENSITIVITY = 0.001f;
	private static Texture t = new Texture();
	private Random random;

	private final Renderer renderer;

	public Game() {
		renderer = new Renderer();
		camera = new Camera();
		entityList = new ArrayList<Entity>();
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
		Terrain terrain = new Terrain(1, 1f, 256, 256, 1, 0, 4, 1, 8);
		entityList.add(terrain.getChunks()[0]);
		random = new Random();
		TreeGenerator treeGen = new TreeGenerator(
				"X0.4>F+[[X]-X]-F[-FX]+X,X0.4>F-[[X]+X]+F[+FX]-X,X0.4>F*[[X]/X]/F[/FX]*X,X0.4>F/[[X]*X]*F[*FX]/X,F0.6>FF",
				System.currentTimeMillis());
		Entity[] tree;
		for (int loop = 0; loop < 10; loop++) {
			tree = treeGen.makeTree(4, 20f + random.nextFloat() * 20f, 30f + random.nextFloat() * 30f, 0.4f, 0.95f,
					0.8f, 0.95f, random.nextFloat() * 256f - 128f, -4, random.nextFloat() * 256f - 128f);
			for (int i = 0; i < tree.length; i++) {
				entityList.add(tree[i]);
			}
		}
		VoronoiGenerator voronoi = new VoronoiGenerator(0, System.currentTimeMillis());
		Entity v = new Entity(voronoi.generateVoronoi(6, 0f, 256f, 0f, 256f, 0, 128f));
		//entityList.add(v);
		entities = new Entity[entityList.size()];
		for (Entity e : entityList) {
			entities[entityList.indexOf(e)] = e;
		}
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