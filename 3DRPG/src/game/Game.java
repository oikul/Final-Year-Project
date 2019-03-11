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
import engine.IGameLogic;
import engine.MouseInput;
import engine.Renderer;
import engine.Window;
import generators.TreeGenerator;
import hud.Hud;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import mesh.Entity;
import mesh.Texture;

public class Game implements IGameLogic {

	private Entity[] entities;
	private ArrayList<Entity> entityList;
	private Camera camera;
	private Vector3f cameraInc = new Vector3f();
	private float CAMERA_POS_STEP = 0.0001f, MOUSE_SENSITIVITY = 0.001f;
	private static Texture t = new Texture();
	private Random random;
	private Vector3f ambientLight;
	private PointLight[] pointLightList;
	private SpotLight[] spotLightList;
	private DirectionalLight directionalLight;
	private float lightAngle;
	// private float spotAngle = 0;
	// private float spotInc = 1;
	private Hud hud;

	private final Renderer renderer;

	// terrain variables
	private int blocksPerRow = 1, width = 256, height = 256, textInc = 1, octaves = 4, poctave1 = 4, poctave2 = 5,
			voronoiSize = 4096;
	private float scale = 1f, amplitude = 6f, roughness = 1f;
	private long terrainSeed = 0;
	private boolean perlinOrValue = true;

	// tree variables
	private String rules = "X0.4>F+[[X]-X]-F[-FX]+X,X0.4>F*[[X]/X]/F[/FX]*X,X0.4>F/[[X]*X]*F[*FX]/X,X0.4>F-[[X]+X]+F[+FX]-X,F0.6>FF";
	private long treeSeed = 0;
	private int iterations = 4;
	private float angleIncrementZ = 30f, angleRandZ = 30f, angleIncrementY = 30f, angleRandY = 30f, baseRadius = 0.4f,
			radiusDecrease = 0.95f, baseHeight = 0.8f, heightDecrease = 0.95f, startY = -3;

	public Game() {
		renderer = new Renderer();
		camera = new Camera();
		entityList = new ArrayList<Entity>();
		lightAngle = -90;
	}

	public Game(int width, int height, int octaves, int poctave1, int poctave2, int voronoiSize, float scale,
			float amplitude, float roughness, long terrainSeed, boolean perlinOrValue, String rules, long treeSeed,
			int iterations, float angleIncrementZ, float angleRandZ, float angleIncrementY, float angleRandY,
			float baseRadius, float radiusDecrease, float baseHeight, float heightDecrease, float startY) {
		setWidth(width);
		setHeight(height);
		setOctaves(octaves);
		setPoctave1(poctave1);
		setPoctave2(poctave2);
		setVoronoiSize(voronoiSize);
		setScale(scale);
		setAmplitude(amplitude);
		setRoughness(roughness);
		setTerrainSeed(terrainSeed);
		setPerlinOrValue(perlinOrValue);
		setRules(rules);
		setTreeSeed(treeSeed);
		setIterations(iterations);
		setAngleIncrementZ(angleIncrementZ);
		setAngleRandZ(angleRandZ);
		setAngleIncrementY(angleIncrementY);
		setAngleRandY(angleRandY);
		setBaseRadius(baseRadius);
		setRadiusDecrease(radiusDecrease);
		setBaseHeight(baseHeight);
		setHeightDecrease(heightDecrease);
		setStartY(startY);
		renderer = new Renderer();
		camera = new Camera();
		entityList = new ArrayList<Entity>();
		lightAngle = -90;
	}

	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);
		Terrain terrain = new Terrain(blocksPerRow, scale, width, height, textInc, terrainSeed, amplitude, roughness,
				octaves, poctave1, poctave2, perlinOrValue, voronoiSize);
		entityList.add(terrain.getChunks()[0]);
		random = new Random();
		TreeGenerator treeGen = new TreeGenerator(rules, treeSeed);
		Entity[] tree;
		for (int loop = 0; loop < 3; loop++) {
			tree = treeGen.makeTree(iterations, angleIncrementZ, angleRandZ, angleIncrementY, angleRandY, baseRadius,
					radiusDecrease, baseHeight, heightDecrease, random.nextFloat() * width - (width / 2), startY,
					random.nextFloat() * height - (height / 2));
			for (int i = 0; i < tree.length; i++) {
				entityList.add(tree[i]);
			}
		}
		// VoronoiGenerator voronoi = new VoronoiGenerator(0,
		// System.currentTimeMillis());
		// Entity v = new Entity(voronoi.generateVoronoi(9, -128f, 128f, -128f,
		// 128f, -1, 256f));
		// entityList.add(v);
		// CylinderGenerator cylinder = new CylinderGenerator();
		// Mesh cy = cylinder.makeCylinder(4, 4, 8, 6);
		// cy.setMaterial(new Material(new Vector4f(0.5f, 0f, 0.5f, 1f), 0.5f));
		// Entity c = new Entity(cy);
		// c.setPosition(1, 2, 2);
		// entityList.add(c);
		ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
		Vector3f lightColour = new Vector3f(1, 1, 1);
		Vector3f lightPosition = new Vector3f(0, 5, 0);
		float lightIntensity = 1.0f;
		PointLight pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
		PointLight.Attenuation att = new PointLight.Attenuation(0f, 0f, 0.1f);
		pointLight.setAttenuation(att);
		pointLightList = new PointLight[] { pointLight };
		lightPosition = new Vector3f(0, 0.0f, 10f);
		pointLight = new PointLight(lightColour, lightPosition, 10f);
		att = new PointLight.Attenuation(0.0f, 0.0f, 0.01f);
		pointLight.setAttenuation(att);
		Vector3f coneDir = new Vector3f(0, 0, -1);
		float cutoff = (float) Math.cos(Math.toRadians(140));
		SpotLight spotLight = new SpotLight(pointLight, coneDir, cutoff);
		spotLightList = new SpotLight[] { spotLight };
		lightPosition = new Vector3f(-1, 0, 0);
		directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);
		entities = new Entity[entityList.size()];
		for (Entity e : entityList) {
			entities[entityList.indexOf(e)] = e;
		}
		hud = new Hud("FONT");
		// NameGenerator names = new NameGenerator(System.currentTimeMillis());
		// for(int i = 0; i < 100; i++){
		// System.out.println(names.getName(random.nextInt(3) + 2));
		// }
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
			cameraInc.z = -2;
		} else if (window.isKeyPressed(GLFW_KEY_S)) {
			cameraInc.z = 2;
		}
		if (window.isKeyPressed(GLFW_KEY_A)) {
			cameraInc.x = -2;
		} else if (window.isKeyPressed(GLFW_KEY_D)) {
			cameraInc.x = 2;
		}
		if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
			cameraInc.y = -2;
		} else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
			cameraInc.y = 2;
		}

	}

	@Override
	public void update(float interval, MouseInput mouseInput) {
		// float rotation = test.getRotation().x + 0.005f;
		// if (rotation > 360) {
		// rotation = 0;
		// }
		// test.setRotation(rotation, rotation, rotation);
		// spotAngle += spotInc * 0.005f;
		// if (spotAngle > 2) {
		// spotInc = -1;
		// } else if (spotAngle < -2) {
		// spotInc = 1;
		// }
		// double spotAngleRad = Math.toRadians(spotAngle);
		// Vector3f coneDir = spotLightList[0].getConeDirection();
		// coneDir.y = (float) Math.sin(spotAngleRad);
		lightAngle += 0.0001f;
		if (lightAngle > 90) {
			directionalLight.setIntensity(0);
			if (lightAngle >= 360) {
				lightAngle = -90;
			}
		} else if (lightAngle <= -80 || lightAngle >= 80) {
			float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
			directionalLight.setIntensity(factor);
			directionalLight.getColor().y = Math.max(factor, 0.8f);
			directionalLight.getColor().z = Math.max(factor, 0.4f);
		} else {
			directionalLight.setIntensity(1);
			directionalLight.getColor().x = 1;
			directionalLight.getColor().y = 1;
			directionalLight.getColor().z = 1;
		}
		double angRad = Math.toRadians(lightAngle);
		directionalLight.getDirection().x = (float) Math.sin(angRad);
		directionalLight.getDirection().y = (float) Math.cos(angRad);
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
		hud.updateSize(window);
		renderer.render(window, entities, camera, ambientLight, pointLightList, spotLightList, directionalLight, hud);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		for (Entity e : entities) {
			e.getMesh().cleanUp();
		}
		hud.cleanup();
	}

	public int getBlocksPerRow() {
		return blocksPerRow;
	}

	public void setBlocksPerRow(int blocksPerRow) {
		this.blocksPerRow = blocksPerRow;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getTextInc() {
		return textInc;
	}

	public void setTextInc(int textInc) {
		this.textInc = textInc;
	}

	public int getOctaves() {
		return octaves;
	}

	public void setOctaves(int octaves) {
		this.octaves = octaves;
	}

	public int getPoctave1() {
		return poctave1;
	}

	public void setPoctave1(int poctave1) {
		this.poctave1 = poctave1;
	}

	public int getPoctave2() {
		return poctave2;
	}

	public void setPoctave2(int poctave2) {
		this.poctave2 = poctave2;
	}

	public int getVoronoiSize() {
		return voronoiSize;
	}

	public void setVoronoiSize(int voronoiSize) {
		this.voronoiSize = voronoiSize;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
	}

	public float getRoughness() {
		return roughness;
	}

	public void setRoughness(float roughness) {
		this.roughness = roughness;
	}

	public long getTerrainSeed() {
		return terrainSeed;
	}

	public void setTerrainSeed(long terrainSeed) {
		this.terrainSeed = terrainSeed;
	}

	public boolean isPerlinOrValue() {
		return perlinOrValue;
	}

	public void setPerlinOrValue(boolean perlinOrValue) {
		this.perlinOrValue = perlinOrValue;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public long getTreeSeed() {
		return treeSeed;
	}

	public void setTreeSeed(long treeSeed) {
		this.treeSeed = treeSeed;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public float getAngleIncrementZ() {
		return angleIncrementZ;
	}

	public void setAngleIncrementZ(float angleIncrementZ) {
		this.angleIncrementZ = angleIncrementZ;
	}

	public float getAngleRandZ() {
		return angleRandZ;
	}

	public void setAngleRandZ(float angleRandZ) {
		this.angleRandZ = angleRandZ;
	}

	public float getAngleIncrementY() {
		return angleIncrementY;
	}

	public void setAngleIncrementY(float angleIncrementY) {
		this.angleIncrementY = angleIncrementY;
	}

	public float getAngleRandY() {
		return angleRandY;
	}

	public void setAngleRandY(float angleRandY) {
		this.angleRandY = angleRandY;
	}

	public float getBaseRadius() {
		return baseRadius;
	}

	public void setBaseRadius(float baseRadius) {
		this.baseRadius = baseRadius;
	}

	public float getRadiusDecrease() {
		return radiusDecrease;
	}

	public void setRadiusDecrease(float radiusDecrease) {
		this.radiusDecrease = radiusDecrease;
	}

	public float getBaseHeight() {
		return baseHeight;
	}

	public void setBaseHeight(float baseHeight) {
		this.baseHeight = baseHeight;
	}

	public float getHeightDecrease() {
		return heightDecrease;
	}

	public void setHeightDecrease(float heightDecrease) {
		this.heightDecrease = heightDecrease;
	}

	public float getStartY() {
		return startY;
	}

	public void setStartY(float startY) {
		this.startY = startY;
	}
}