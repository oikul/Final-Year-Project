package engine;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import mesh.Entity;

public class Transformation {

	private final Matrix4f projectionMatrix, worldMatrix, viewMatrix, modelViewMatrix;

	public Transformation() {
		worldMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();
		modelViewMatrix = new Matrix4f();
	}

	public Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		float aspectRatio = width / height;
		projectionMatrix.identity();
		projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
		return projectionMatrix;
	}

	public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
		worldMatrix.identity().translate(offset).rotateX((float) Math.toRadians(rotation.x))
				.rotateY((float) Math.toRadians(rotation.y)).rotateZ((float) Math.toRadians(rotation.z)).scale(scale);
		return worldMatrix;
	}

	public Matrix4f getViewMatrix(Camera camera) {
		Vector3f position = camera.getPosition();
		Vector3f rotation = camera.getRotation();
		viewMatrix.identity();
		viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
				.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
		viewMatrix.translate(-position.x, -position.y, -position.z);
		return viewMatrix;
	}
	
	public Matrix4f getModelViewMatrix(Entity e, Matrix4f viewMatrix){
		Vector3f rotation = e.getRotation();
		modelViewMatrix.identity().translate(e.getPosition())
		.rotateX((float)Math.toRadians(-rotation.x))
		.rotateY((float)Math.toRadians(-rotation.y))
		.rotateZ((float)Math.toRadians(-rotation.z))
		.scale(e.getScale());
		Matrix4f view = new Matrix4f(viewMatrix);
		return view.mul(modelViewMatrix);
	}

}
