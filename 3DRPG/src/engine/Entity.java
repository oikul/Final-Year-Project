package engine;
import org.joml.Vector3f;

public class Entity {
	
	private final Mesh mesh;
	private Vector3f position, rotation;

	private float scale;
	
	public Entity(Mesh mesh){
		this.mesh = mesh;
		this.position = new Vector3f(0, 0, -5f);
		scale = 1;
		rotation = new Vector3f(0, 0, 0);
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setPosition(float posX, float posY, float posZ) {
		this.position.x = posX;
		this.position.y = posY;
		this.position.z = posZ;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public void setRotation(float rotX, float rotY, float rotZ) {
		this.rotation.x = rotX;
		this.rotation.y = rotY;
		this.rotation.z = rotZ;
	}

}
