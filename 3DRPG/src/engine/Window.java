package engine;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonMode;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Window {

	private long window;
	private int width, height;
	private boolean resized;

	public Window() {
		if (!glfwInit()) {
			throw new IllegalStateException();
		}
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
	}

	public void createWindow(int width, int height, String title, long monitor, long fullscreen, boolean vSync,
			boolean wireframe) {
		this.width = width;
		this.height = height;
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		//glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
		window = glfwCreateWindow(width, height, title, monitor, 0);
		if (window == 0) {
			throw new IllegalStateException("Failed to create window");
		}
		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		// GLFWVidMode videoMode = glfwGetVideoMode(monitor);
		// glfwSetWindowPos(window, (videoMode.width() - 640) / 2,
		// (videoMode.height() - 480) / 2);

		glfwSetFramebufferSizeCallback(window, (window, windowWidth, windowHeight) -> {
			Window.this.setWidth(windowWidth);
			Window.this.setHeight(windowHeight);
			Window.this.setResized(true);
		});
		if (vSync) {
			glfwSwapInterval(1);
		}
		// Support for transparencies
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL11.GL_DEPTH_TEST);
		if (wireframe) {
			glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		} else {
			glEnable(GL11.GL_CULL_FACE);
			glCullFace(GL11.GL_FRONT);
		}
		glfwShowWindow(window);
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}

	public void close() {
		glfwSetWindowShouldClose(window, true);
	}

	public void swapBuffers() {
		glfwSwapBuffers(window);
	}

	public void destroy() {
		glfwTerminate();
	}

	public boolean isKeyPressed(int keyCode) {
		return glfwGetKey(window, keyCode) == GLFW_PRESS;
	}

	public void setClearColor(float r, float g, float b, float a) {
		GL11.glClearColor(r, g, b, a);
	}

	public void setResized(boolean resized) {
		this.resized = resized;
	}

	public boolean isResized() {
		return resized;
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

	public long getWindowHandle() {
		return window;
	}

}
