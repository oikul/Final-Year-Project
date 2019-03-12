package engine;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class GameEngine implements Runnable {

	private Window window;
	private IGameLogic gameLogic;
	//private final Thread gameLoopThread;
	private String title;
	private int width, height;
	private boolean vSync, wireframe;
	private MouseInput mouseInput;

	public GameEngine(String title, int width, int height, boolean vSync, boolean wireframe, IGameLogic gameLogic) throws Exception {
		//gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
		window = new Window();
		this.gameLogic = gameLogic;
		this.title = title;
		this.width = width;
		this.height = height;
		this.vSync = vSync;
		this.wireframe = wireframe;
		mouseInput = new MouseInput();
	}

	public void start() {
		run();
	}

	@Override
	public void run() {
		try {
			init();
			gameLoop();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cleanup();
		}
	}

	private void init() {
		window.createWindow(width, height, title, glfwGetPrimaryMonitor(), 0, vSync, wireframe);
		try {
			gameLogic.init(window);
			mouseInput.init(window);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void gameLoop() {
		double secsPerUpdate = 1.0d / 30.0d;
		double previous = System.currentTimeMillis();
		double steps = 0.0;

		while (!window.shouldClose()) {
			double loopStartTime = System.currentTimeMillis();
			double elapsed = loopStartTime - previous;
			previous = System.currentTimeMillis();
			steps += elapsed;
			handleInput();
			while (steps >= secsPerUpdate) {
				update(0);
				steps -= secsPerUpdate;
			}
			render();
			sync(loopStartTime);
		}
		window.destroy();
	}
	
	public void cleanup(){
		gameLogic.cleanup();
	}

	private void handleInput() {
		mouseInput.input(window, width, height);
		gameLogic.input(window, mouseInput);
		if (window.isKeyPressed(GLFW_KEY_ESCAPE)) {
			window.close();
		}
	}

	private void update(float interval) {
		gameLogic.update(interval, mouseInput);
	}

	private void render() {
		glfwPollEvents();
		glClear(GL_COLOR_BUFFER_BIT);
		gameLogic.render(window);
		window.swapBuffers();
	}

	private void sync(double loopStartTime) {
		float loopSlot = 1f / 30;
		double endTime = loopStartTime + loopSlot;
		while (System.currentTimeMillis() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
